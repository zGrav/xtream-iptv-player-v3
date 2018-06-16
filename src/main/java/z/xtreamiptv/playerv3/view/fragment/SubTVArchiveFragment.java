package z.xtreamiptv.playerv3.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.callback.LiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.model.callback.LiveStreamsCallback;
import z.xtreamiptv.playerv3.model.callback.LiveStreamsEpgCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.pojo.EpgListingPojo;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import z.xtreamiptv.playerv3.view.activity.ImportEPGActivity;
import z.xtreamiptv.playerv3.view.adapter.SubTVArchiveAdapter;
import z.xtreamiptv.playerv3.view.interfaces.LiveStreamsInterface;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubTVArchiveFragment extends Fragment implements LiveStreamsInterface, OnNavigationItemSelectedListener {
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_ID = "";
    public static final String ACTIVE_LIVE_STREAM_CHANNEL_ID = "";
    public static final String ACTIVE_LIVE_STREAM_ICON = "";
    public static final String ACTIVE_LIVE_STREAM_NAME = "";
    public static final String ACTIVE_LIVE_STREAM_NUM = "";
    public static final String LIVE_STREAMS_EPG = "";
    private SubTVArchiveAdapter LiveStreamsEpgAdapter;
    int actionBarHeight;
    public Context context;
    private List<EpgListingPojo> dataSet;
    DatabaseHandler database;
    private ArrayList<LiveStreamsCallback> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId;
    private String getActiveLiveStreamChannelDuration;
    private String getActiveLiveStreamChannelId;
    private String getActiveLiveStreamIcon;
    private int getActiveLiveStreamId;
    private String getActiveLiveStreamName;
    private String getActiveLiveStreamNum;
    private LayoutManager layoutManager;
    private SharedPreferences loginPreferencesSharedPref;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    SearchView searchView;
    private Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    Unbinder unbinder;

    class C20421 implements OnClickListener {
        C20421() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsVodandSeries(SubTVArchiveFragment.this.context);
        }
    }

    class C20432 implements OnClickListener {
        C20432() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C20443 implements OnClickListener {
        C20443() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SubTVArchiveFragment.this.loadTvGuid();
        }
    }

    class C20454 implements OnClickListener {
        C20454() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public static SubTVArchiveFragment newInstance(String category_id, ArrayList<XMLTVProgrammePojo> liveStreamEpgCallback, int opened_stream_id, String opened_num, String opened_channel_name, String opened_channel_icon, String opened_channel_id, String opened_channel_duration) {
        Bundle args = new Bundle();
        args.putString("ACTIVE_LIVE_STREAM_CATEGORY_ID", category_id);
        args.putInt("ACTIVE_LIVE_STREAM_ID", opened_stream_id);
        args.putString("ACTIVE_LIVE_STREAM_NUM", opened_num);
        args.putString("ACTIVE_LIVE_STREAM_NAME", opened_channel_name);
        args.putString("ACTIVE_LIVE_STREAM_ICON", opened_channel_icon);
        args.putString("ACTIVE_LIVE_STREAM_CHANNEL_ID", opened_channel_id);
        args.putString("ACTIVE_LIVE_STREAM_CHANNEL_DURATION", opened_channel_duration);
        args.putSerializable("LIVE_STREAMS_EPG", liveStreamEpgCallback);
        SubTVArchiveFragment fragment = new SubTVArchiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setEpgtoAdapter() {
        this.getActiveLiveStreamCategoryId = getArguments().getString("ACTIVE_LIVE_STREAM_CATEGORY_ID");
        this.getActiveLiveStreamId = getArguments().getInt("ACTIVE_LIVE_STREAM_ID");
        this.getActiveLiveStreamNum = getArguments().getString("ACTIVE_LIVE_STREAM_NUM");
        this.getActiveLiveStreamName = getArguments().getString("ACTIVE_LIVE_STREAM_NAME");
        this.getActiveLiveStreamIcon = getArguments().getString("ACTIVE_LIVE_STREAM_ICON");
        this.getActiveLiveStreamChannelId = getArguments().getString("ACTIVE_LIVE_STREAM_CHANNEL_ID");
        this.getActiveLiveStreamChannelDuration = getArguments().getString("ACTIVE_LIVE_STREAM_CHANNEL_DURATION");
        Serializable getAllLiveStreamsEpg = getArguments().getSerializable("LIVE_STREAMS_EPG");
        if (this.getActiveLiveStreamCategoryId != null && getAllLiveStreamsEpg != null) {
            ArrayList<XMLTVProgrammePojo> liveStreamEpgCallback = (ArrayList) getAllLiveStreamsEpg;
            String Title = "";
            int nowPlayingPosition = 0;
            boolean nowPlayingFlag = false;
            int j = 0;
            ArrayList sortedArray = new ArrayList();
            for (int i = 0; i < liveStreamEpgCallback.size(); i++) {
                String startDateTime = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getStart();
                String stopDateTime = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getStop();
                Title = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getTitle();
                String Desc = ((XMLTVProgrammePojo) liveStreamEpgCallback.get(i)).getDesc();
                Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                String currentFormatDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(epgStartDateToTimestamp);
                if (currentFormatDate != null) {
                    if (currentFormatDate.equals(this.getActiveLiveStreamCategoryId)) {
                        sortedArray.add(liveStreamEpgCallback.get(i));
                        j++;
                    }
                }
                if (Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context)) {
                    nowPlayingFlag = true;
                    nowPlayingPosition = j - 1;
                    break;
                }
            }
            this.LiveStreamsEpgAdapter = new SubTVArchiveAdapter(sortedArray, nowPlayingPosition, nowPlayingFlag, this.getActiveLiveStreamCategoryId, this.getActiveLiveStreamId, this.getActiveLiveStreamNum, this.getActiveLiveStreamName, this.getActiveLiveStreamIcon, this.getActiveLiveStreamChannelId, this.getActiveLiveStreamChannelDuration, getContext());
            this.myRecyclerView.setAdapter(this.LiveStreamsEpgAdapter);
            initialize(nowPlayingPosition + 1);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_epg, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        ActivityCompat.invalidateOptionsMenu(getActivity());
        this.context = getContext();
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        setEpgtoAdapter();
        return view;
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
        this.toolbar.inflateMenu(R.menu.menu_dashboard_logout);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        Builder alertDialog;
        if (id == R.id.menu_load_channels_vod) {
            alertDialog = new Builder(this.context);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C20421());
            alertDialog.setNegativeButton("NO", new C20432());
            alertDialog.show();
            return true;
        } else if (id != R.id.menu_load_tv_guide) {
            return false;
        } else {
            alertDialog = new Builder(this.context);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C20443());
            alertDialog.setNegativeButton("NO", new C20454());
            alertDialog.show();
            return true;
        }
    }

    private void loadTvGuid() {
        if (this.context != null) {
            SharedPreferences loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            Editor loginPrefsEditor = loginPreferencesAfterLogin.edit();
            if (loginPrefsEditor != null) {
                loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "autoLoad");
                loginPrefsEditor.commit();
                String skipButton = loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
                new LiveStreamDBHandler(this.context).makeEmptyEPG();
                startActivity(new Intent(this.context, ImportEPGActivity.class));
            }
        }
    }

    private void initialize(int nowPlayingPosition) {
        this.context = getContext();
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(getContext());
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.smoothScrollToPosition(nowPlayingPosition);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public void liveStreamCategories(List<LiveStreamCategoriesCallback> list) {
    }

    public void liveStreams(List<LiveStreamsCallback> list, ArrayList<FavouriteDBModel> arrayList) {
    }

    public void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback, TextView tvActiveChannel, TextView tvNextChannel) {
    }

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String message) {
        Utils.showToast(this.context, AppConst.NETWORK_ERROR_OCCURED);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
