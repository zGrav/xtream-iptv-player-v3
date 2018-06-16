package z.xtreamiptv.playerv3.v2api.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.EpgChannelModel;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.v2api.model.database.LiveStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.view.adapter.LiveStreamListViewAdapter;
import z.xtreamiptv.playerv3.v2api.view.adapter.LiveStreamsAdapter;
import java.util.ArrayList;
import java.util.Iterator;

public class LiveStreamsFragment extends Fragment implements OnNavigationItemSelectedListener {
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_ID = "";
    private SharedPreferences SharedPreferencesSort;
    private Editor SharedPreferencesSortEditor;
    int actionBarHeight;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private PopupWindow changeSortPopUp;
    public Context context;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private Editor editor;
    ArrayList<EpgChannelModel> epgChannelModelList = new ArrayList();
    LiveStreamsDBModel favouriteStream = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
    private ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId;
    private LayoutManager layoutManager;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableNew;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;
    private LiveStreamsAdapter liveStreamsAdapter;
    private LiveStreamListViewAdapter liveStreamsListViewAdapter;
    private SharedPreferences loginPreferencesSharedPref;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private SharedPreferences pref;
    SearchView searchView;
    private Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    Unbinder unbinder;

    class C16481 implements OnQueryTextListener {
        C16481() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            LiveStreamsFragment.this.tvNoRecordFound.setVisibility(8);
            if (!(LiveStreamsFragment.this.liveStreamsAdapter == null || LiveStreamsFragment.this.tvNoStream == null || LiveStreamsFragment.this.tvNoStream.getVisibility() == 0)) {
                LiveStreamsFragment.this.liveStreamsAdapter.filter(newText, LiveStreamsFragment.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C16492 implements OnClickListener {
        C16492() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVodV2Api(LiveStreamsFragment.this.context);
        }
    }

    class C16503 implements OnClickListener {
        C16503() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C16514 implements OnClickListener {
        C16514() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(LiveStreamsFragment.this.context);
        }
    }

    class C16525 implements OnClickListener {
        C16525() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C16536 implements View.OnClickListener {
        C16536() {
        }

        public void onClick(View view) {
            LiveStreamsFragment.this.changeSortPopUp.dismiss();
        }
    }

    public static LiveStreamsFragment newInstance(String category_id) {
        Bundle args = new Bundle();
        args.putString("", category_id);
        LiveStreamsFragment fragment = new LiveStreamsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActiveLiveStreamCategoryId = getArguments().getString("");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_streams, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.SharedPreferencesSort = getActivity().getSharedPreferences(AppConst.LOGIN_PREF_SORT, 0);
        this.SharedPreferencesSortEditor = this.SharedPreferencesSort.edit();
        if (this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "").equals("")) {
            this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
            this.SharedPreferencesSortEditor.commit();
        }
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        this.pref = getActivity().getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG = this.pref.getInt(AppConst.LIVE_STREAM, 0);
        if (AppConst.LIVE_FLAG == 1) {
            initialize();
        } else {
            initialize1();
        }
        if (this.getActiveLiveStreamCategoryId.equals("-1")) {
            getFavourites();
        }
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
        this.toolbar.inflateMenu(R.menu.menu_search_text_icon_v2api);
        MenuItem item = menu.findItem(R.id.layout_view_grid);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout1 && this.context != null) {
            Utils.logoutUser(this.context);
        }
        if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint(getResources().getString(R.string.search_channel));
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C16481());
            return true;
        } else if (id == R.id.menu_load_channels_vod1) {
            Builder alertDialog = new Builder(this.context);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C16492());
            alertDialog.setNegativeButton("NO", new C16503());
            alertDialog.show();
            return true;
        } else if (id == R.id.menu_load_tv_guide1) {
            Builder alertDialog = new Builder(this.context);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C16514());
            alertDialog.setNegativeButton("NO", new C16525());
            alertDialog.show();
            return true;
        } else {
            if (id == R.id.layout_view_grid) {
                this.editor.putInt(AppConst.LIVE_STREAM, 1);
                this.editor.commit();
                initialize();
            }
            if (id == R.id.layout_view_linear) {
                this.editor.putInt(AppConst.LIVE_STREAM, 0);
                this.editor.commit();
                initialize1();
            }
            if (id == R.id.menu_sort) {
                showSortPopup(getActivity());
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private void showSortPopup(Activity context) {
        final View layout = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.sort_layout, (RelativeLayout) context.findViewById(R.id.rl_password_prompt));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        Button savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        Button closedBT = (Button) layout.findViewById(R.id.bt_close);
        final RadioGroup rgRadio = (RadioGroup) layout.findViewById(R.id.rg_radio);
        RadioButton normal = (RadioButton) layout.findViewById(R.id.rb_normal);
        RadioButton last_added = (RadioButton) layout.findViewById(R.id.rb_lastadded);
        RadioButton atoz = (RadioButton) layout.findViewById(R.id.rb_atoz);
        RadioButton ztoa = (RadioButton) layout.findViewById(R.id.rb_ztoa);
        String sort = this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "");
        if (sort.equals("1")) {
            last_added.setChecked(true);
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            atoz.setChecked(true);
        } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
            ztoa.setChecked(true);
        } else {
            normal.setChecked(true);
        }
        closedBT.setOnClickListener(new C16536());
        savePasswordBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton selectedPlayer1 = (RadioButton) layout.findViewById(rgRadio.getCheckedRadioButtonId());
                if (selectedPlayer1.getText().toString().equals(LiveStreamsFragment.this.getResources().getString(R.string.sort_last_added))) {
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "1");
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(LiveStreamsFragment.this.getResources().getString(R.string.sort_atoz))) {
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_EPG_ID);
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(LiveStreamsFragment.this.getResources().getString(R.string.sort_ztoa))) {
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_LIVE_STREAMS_CAT_ID);
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.commit();
                } else {
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
                    LiveStreamsFragment.this.SharedPreferencesSortEditor.commit();
                }
                LiveStreamsFragment.this.pref = LiveStreamsFragment.this.getActivity().getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
                LiveStreamsFragment.this.editor = LiveStreamsFragment.this.pref.edit();
                AppConst.LIVE_FLAG = LiveStreamsFragment.this.pref.getInt(AppConst.LIVE_STREAM, 0);
                if (AppConst.LIVE_FLAG == 1) {
                    LiveStreamsFragment.this.initialize();
                } else {
                    LiveStreamsFragment.this.initialize1();
                }
                LiveStreamsFragment.this.changeSortPopUp.dismiss();
            }
        });
    }

    private void initialize() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(getContext(), Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void initialize1() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(getContext());
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void setUpdatabaseResult() {
        atStart();
        if (this.context != null) {
            LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            LiveStreamsDatabaseHandler liveStreamsDatabaseHandler = new LiveStreamsDatabaseHandler(this.context);
            this.categoryWithPasword = new ArrayList();
            this.liveListDetailUnlcked = new ArrayList();
            this.liveListDetailUnlckedDetail = new ArrayList();
            this.liveListDetailAvailable = new ArrayList();
            this.liveListDetailAvailableNew = new ArrayList();
            this.liveListDetail = new ArrayList();
            ArrayList<LiveStreamsDBModel> channelAvailable = liveStreamsDatabaseHandler.getAllLiveStreasWithCategoryId(this.getActiveLiveStreamCategoryId);
            if (liveStreamDBHandler.getParentalStatusCount() <= 0 || channelAvailable == null) {
                this.liveListDetailAvailable = channelAvailable;
            } else {
                this.listPassword = getPasswordSetCategories();
                if (this.listPassword != null) {
                    this.liveListDetailUnlckedDetail = getUnlockedCategories(channelAvailable, this.listPassword);
                }
                this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
            }
            if (this.getActiveLiveStreamCategoryId.equals("-1")) {
                onFinish();
            } else if (this.liveListDetailAvailable == null || this.myRecyclerView == null || this.liveListDetailAvailable.size() == 0) {
                onFinish();
                if (this.tvNoStream != null) {
                    this.tvNoStream.setVisibility(0);
                }
            } else {
                onFinish();
                this.liveStreamsAdapter = new LiveStreamsAdapter(this.liveListDetailAvailable, getContext());
                this.myRecyclerView.setAdapter(this.liveStreamsAdapter);
                this.liveStreamsAdapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public void getFavourites() {
        this.favouriteStreams.clear();
        if (this.myRecyclerView != null) {
            this.myRecyclerView.setAdapter(this.liveStreamsAdapter);
        }
        if (this.context != null) {
            this.database = new DatabaseHandler(this.context);
            Iterator it = this.database.getAllFavourites("live").iterator();
            while (it.hasNext()) {
                FavouriteDBModel favListItem = (FavouriteDBModel) it.next();
                LiveStreamsDBModel channelAvailable = new LiveStreamDBHandler(this.context).getLiveStreamFavouriteRow(favListItem.getCategoryID(), String.valueOf(favListItem.getStreamID()));
                if (channelAvailable != null) {
                    this.favouriteStreams.add(channelAvailable);
                }
            }
            if (!(this.myRecyclerView == null || this.favouriteStreams == null || this.favouriteStreams.size() == 0)) {
                onFinish();
                this.liveStreamsAdapter = new LiveStreamsAdapter(this.favouriteStreams, getContext());
                this.myRecyclerView.setAdapter(this.liveStreamsAdapter);
                this.liveStreamsAdapter.notifyDataSetChanged();
                this.tvNoStream.setVisibility(4);
            }
            if (this.tvNoStream != null && this.favouriteStreams != null && this.favouriteStreams.size() == 0) {
                onFinish();
                if (this.myRecyclerView != null) {
                    this.myRecyclerView.setAdapter(this.liveStreamsAdapter);
                }
                this.tvNoStream.setText(getResources().getString(R.string.no_fav_channel_found));
                this.tvNoStream.setVisibility(0);
            }
        }
    }

    public void atStart() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(0);
        }
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
