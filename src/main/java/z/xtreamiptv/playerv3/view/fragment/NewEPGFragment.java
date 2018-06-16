package z.xtreamiptv.playerv3.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.EpgChannelModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.view.activity.DashboardActivity;
import z.xtreamiptv.playerv3.view.utility.epg.EPG;
import z.xtreamiptv.playerv3.view.utility.epg.EPGClickListener;
import z.xtreamiptv.playerv3.view.utility.epg.EPGData;
import z.xtreamiptv.playerv3.view.utility.epg.domain.EPGChannel;
import z.xtreamiptv.playerv3.view.utility.epg.domain.EPGEvent;
import z.xtreamiptv.playerv3.view.utility.epg.misc.EPGDataListener;
import z.xtreamiptv.playerv3.view.utility.epg.service.EPGService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class NewEPGFragment extends Fragment implements OnNavigationItemSelectedListener {
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_ID = "";
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    int actionBarHeight;
    public Context context;
    @BindView(R.id.current_event)
    TextView currentEvent;
    @BindView(R.id.current_event_description)
    TextView currentEventDescription;
    private TextView currentEventDescriptionTextView;
    private TextView currentEventTextView;
    @BindView(R.id.current_event_time)
    TextView currentEventTime;
    private TextView currentEventTimeTextView;
    @BindView(R.id.current_time)
    TextView currentTime;
    private SimpleDateFormat currentTimeFormat;
    private TextView currentTimeTextView;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private int devEnterCounter = 0;
    private boolean devMode = false;
    @BindView(R.id.epg)
    EPG epg;
    ArrayList<EpgChannelModel> epgChannelModelList = new ArrayList();
    @BindView(R.id.rl_newepg_fragment)
    RelativeLayout epgFragment;
    @BindView(R.id.ll_epg_view)
    RelativeLayout epgView;
    LiveStreamsDBModel favouriteStream = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
    private String getActiveLiveStreamCategoryId;
    private ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
    private LayoutManager layoutManager;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private Handler periodicTaskHandler;
    SearchView searchView;
    private Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    @BindView(R.id.tv_view_provider)
    TextView tvViewProvider;
    Unbinder unbinder;

    class C20211 implements OnKeyListener {
        C20211() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 1) {
                if ((keyCode == 20 || keyCode == 19 || keyCode == 22 || keyCode == 21 || keyCode == 23 || keyCode == 66) && NewEPGFragment.this.epg != null) {
                    return NewEPGFragment.this.epg.onKeyDown(keyCode, event);
                }
                if (keyCode == 4) {
                    NewEPGFragment.this.startActivity(new Intent(NewEPGFragment.this.context, DashboardActivity.class));
                }
            }
            return false;
        }
    }

    class AnonymousClass1AsyncLoadEPGData extends AsyncTask<Void, Void, EPGData> {
        EPG epg;
        final /* synthetic */ String val$categoryID;
        final /* synthetic */ RelativeLayout val$epgFragment;

        public AnonymousClass1AsyncLoadEPGData(EPG epg, String str, RelativeLayout relativeLayout) {
            this.val$categoryID = str;
            this.val$epgFragment = relativeLayout;
            this.epg = epg;
        }

        protected EPGData doInBackground(Void... voids) {
            return new EPGService(NewEPGFragment.this.context).getData(new EPGDataListener(this.epg), 0, this.val$categoryID);
        }

        protected void onPostExecute(EPGData epgData) {
            int totalChannels = 0;
            if (epgData != null) {
                totalChannels = epgData.getChannelCount();
            }
            this.epg.setEPGData(epgData);
            this.epg.recalculateAndRedraw(null, false, this.val$epgFragment, this.epg);
            if (NewEPGFragment.this.epgView != null && totalChannels > 0) {
                NewEPGFragment.this.epgView.setVisibility(0);
            } else if (NewEPGFragment.this.epgView != null) {
                NewEPGFragment.this.epgView.setVisibility(8);
                NewEPGFragment.this.tvNoRecordFound.setVisibility(0);
                NewEPGFragment.this.tvNoRecordFound.setText(NewEPGFragment.this.getResources().getString(R.string.no_epg_guide_found));
            }
            if (NewEPGFragment.this.pbLoader != null) {
                NewEPGFragment.this.pbLoader.setVisibility(4);
            }
        }
    }

    public static NewEPGFragment newInstance(String category_id) {
        Bundle args = new Bundle();
        args.putString("", category_id);
        NewEPGFragment fragment = new NewEPGFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActiveLiveStreamCategoryId = getArguments().getString("");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_epg_streams, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        initialize();
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getView() != null) {
            getView().setOnKeyListener(new C20211());
        }
    }

    private void updateTime() {
        Date now = new Date();
        loginPreferencesSharedPref_time_format = getActivity().getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        this.currentTimeFormat = new SimpleDateFormat(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
        this.currentTimeTextView.setText(this.currentTimeFormat.format(now));
    }

    void onCreateEPG() {
        Context context = this.context;
        String str = AppConst.LOGIN_PREF_SELECTED_PLAYER;
        Context context2 = this.context;
        this.loginPreferencesSharedPref = context.getSharedPreferences(str, 0);
        final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
        this.currentTimeTextView = this.currentTime;
        this.currentEventTextView = this.currentEvent;
        this.currentEventTimeTextView = this.currentEventTime;
        this.currentEventDescriptionTextView = this.currentEventDescription;
        this.epg.setCurrentEventTextView(this.currentEventTextView);
        this.epg.setCurrentEventTimeTextView(this.currentEventTimeTextView);
        this.epg.setCurrentEventDescriptionTextView(this.currentEventDescriptionTextView);
        this.periodicTaskHandler = new Handler();
        this.epg.setEPGClickListener(new EPGClickListener() {
            public void onChannelClicked(int channelPosition, EPGChannel epgChannel) {
                int streamID = Integer.parseInt(epgChannel.getStreamID());
                String name = epgChannel.getName();
                Utils.playWithPlayer(NewEPGFragment.this.context, selectedPlayer, streamID, "live", epgChannel.getNum(), name, epgChannel.getEpgChannelID(), epgChannel.getImageURL());
            }

            public void onEventClicked(int channelPosition, int programPosition, EPGEvent epgEvent) {
                int streamID = Integer.parseInt(epgEvent.getChannel().getStreamID());
                String name = epgEvent.getChannel().getName();
                String num = epgEvent.getChannel().getNum();
                String epgChannelId = epgEvent.getChannel().getEpgChannelID();
                String channelLogo = epgEvent.getChannel().getImageURL();
                NewEPGFragment.this.epg.selectEvent(epgEvent, true);
                Utils.playWithPlayer(NewEPGFragment.this.context, selectedPlayer, streamID, "live", num, name, epgChannelId, channelLogo);
            }

            public void onResetButtonClicked() {
                NewEPGFragment.this.epg.recalculateAndRedraw(null, true, NewEPGFragment.this.epgFragment, NewEPGFragment.this.epg);
            }
        });
    }

    private void setToolbarLogoImagewithSearchView() {
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.context != null && this.toolbar != null) {
            TypedValue tv = new TypedValue();
            if (this.context.getTheme().resolveAttribute(16843499, tv, true)) {
                TypedValue.complexToDimensionPixelSize(tv.data, this.context.getResources().getDisplayMetrics());
            }
            for (int i = 0; i < this.toolbar.getChildCount(); i++) {
                if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                    ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
                }
            }
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout1 && this.context != null) {
            Utils.logoutUser(this.context);
        }
        return false;
    }

    private boolean getChannelEPGUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null || !this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || !this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) {
            return false;
        }
        return true;
    }

    private void initialize() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.context != null) {
            onCreateEPG();
            if (this.liveStreamDBHandler.getLiveStreamsCount(this.getActiveLiveStreamCategoryId) != 0 || this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                onWindowFocusChanged(this.getActiveLiveStreamCategoryId, this.epgFragment, R.id.epg);
                return;
            }
            if (this.pbLoader != null) {
                this.pbLoader.setVisibility(4);
            }
            if (this.tvNoStream != null) {
                this.tvNoStream.setVisibility(0);
            }
        }
    }

    public void onDestroyView() {
        if (this.epg != null) {
            this.epg.clearEPGImageCache();
        }
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void updateImageCropping(ImageView imageView) {
        Matrix matrix = imageView.getImageMatrix();
        float imageHeight = (float) imageView.getDrawable().getIntrinsicHeight();
        float scaleRatio = ((float) getResources().getDisplayMetrics().widthPixels) / ((float) imageView.getDrawable().getIntrinsicWidth());
        matrix.postScale(scaleRatio, scaleRatio);
        matrix.postTranslate(0.0f, (-1.0f * imageHeight) * 0.3f);
        imageView.setImageMatrix(matrix);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        this.periodicTaskHandler.removeCallbacksAndMessages(null);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void onWindowFocusChanged(String categoryID, RelativeLayout epgFragment, int epg) {
        int hrs = (((TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings()) / 1000) / 60) / 60;
        new AnonymousClass1AsyncLoadEPGData(this.epg, categoryID, epgFragment).execute(new Void[0]);
        updateTime();
    }
}
