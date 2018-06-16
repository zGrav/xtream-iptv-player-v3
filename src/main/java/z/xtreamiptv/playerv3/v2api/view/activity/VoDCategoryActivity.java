package z.xtreamiptv.playerv3.v2api.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.AdapterSectionRecycler;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.v2api.model.database.VODStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.view.adapter.VodAdapter;
import z.xtreamiptv.playerv3.v2api.view.adapter.VodAdapterNewFlow;
import z.xtreamiptv.playerv3.view.activity.LoginActivity;
import z.xtreamiptv.playerv3.view.adapter.VodSubCatAdpaterNew;
import java.util.ArrayList;
import java.util.Iterator;

public class VoDCategoryActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener {
    private static final String JSON = "";
    static ProgressBar pbPagingLoader1;
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryList = new ArrayList();
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal = new ArrayList();
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal_menu = new ArrayList();
    private SharedPreferences SharedPreferencesSort;
    private Editor SharedPreferencesSortEditor;
    int actionBarHeight;
    private AdapterSectionRecycler adapterRecycler;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    ArrayList<LiveStreamCategoryIdDBModel> categoriesList;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private PopupWindow changeSortPopUp;
    TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private Editor editor;
    private ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
    private String getActiveLiveStreamCategoryId = "";
    private String getActiveLiveStreamCategoryName = "";
    GridLayoutManager gridLayoutManager;
    private boolean isSubcaetgroy = false;
    boolean isSubcaetgroyAvail = false;
    private LayoutManager layoutManager;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private VodAdapterNewFlow mAdapter;
    private LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;
    @BindView(R.id.rl_vod_layout)
    RelativeLayout rl_vod_layout;
    SearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    @BindView(R.id.tv_view_provider)
    TextView tvViewProvider;
    private String userName = "";
    private String userPassword = "";
    private VodAdapter vodAdapter;
//    @BindView(2131362613)
//    TextView vodCategoryName;
    private VodSubCatAdpaterNew vodSubCatAdpaterNew;

    class C15621 implements OnQueryTextListener {
        C15621() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            VoDCategoryActivity.this.tvNoRecordFound.setVisibility(8);
            if (!(VoDCategoryActivity.this.vodSubCatAdpaterNew == null || VoDCategoryActivity.this.tvNoStream == null || VoDCategoryActivity.this.tvNoStream.getVisibility() == 0)) {
                VoDCategoryActivity.this.vodSubCatAdpaterNew.filter(newText, VoDCategoryActivity.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C15632 implements OnQueryTextListener {
        C15632() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            VoDCategoryActivity.this.tvNoRecordFound.setVisibility(8);
            if (!(VoDCategoryActivity.this.vodAdapter == null || VoDCategoryActivity.this.tvNoStream == null || VoDCategoryActivity.this.tvNoStream.getVisibility() == 0)) {
                VoDCategoryActivity.this.vodAdapter.filter(newText, VoDCategoryActivity.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C15643 implements DialogInterface.OnClickListener {
        C15643() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVodV2Api(VoDCategoryActivity.this.context);
        }
    }

    class C15654 implements DialogInterface.OnClickListener {
        C15654() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15665 implements DialogInterface.OnClickListener {
        C15665() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(VoDCategoryActivity.this.context);
        }
    }

    class C15676 implements DialogInterface.OnClickListener {
        C15676() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15687 implements OnClickListener {
        C15687() {
        }

        public void onClick(View view) {
            VoDCategoryActivity.this.changeSortPopUp.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        int i = 0;
        super.onCreate(savedInstanceState);
        this.SharedPreferencesSort = getSharedPreferences(AppConst.LOGIN_PREF_SORT_VOD, 0);
        this.SharedPreferencesSortEditor = this.SharedPreferencesSort.edit();
        if (this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "").equals("")) {
            this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
            this.SharedPreferencesSortEditor.commit();
        }
        Intent intent = getIntent();
        if (intent != null) {
            this.getActiveLiveStreamCategoryId = intent.getStringExtra(AppConst.CATEGORY_ID);
            this.getActiveLiveStreamCategoryName = intent.getStringExtra(AppConst.CATEGORY_NAME);
        }
        this.context = this;
        this.mAdapter = new VodAdapterNewFlow(subCategoryList, this.context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        String str = this.getActiveLiveStreamCategoryId;
        switch (str.hashCode()) {
            case 48:
                if (str.equals(AppConst.PASSWORD_UNSET)) {
                    i = 1;
                    break;
                }
            case 1444:
                if (str.equals("-1")) {
                    break;
                }
            default:
                i = -1;
                break;
        }
        switch (i) {
            case 0:
                setContentView(R.layout.acitivitity_vod_category_layout);
                ButterKnife.bind(this);
                atStart();
                setLayout();
                getFavourites();
                break;
            case 1:
                setContentView(R.layout.acitivitity_vod_category_layout);
                ButterKnife.bind(this);
                atStart();
                setLayout();
                break;
            default:
                setContentView(R.layout.acitivitity_vod_category_layout);
                ButterKnife.bind(this);
                atStart();
                setLayout();
                if ((getResources().getConfiguration().screenLayout & 15) != 3) {
                    if (this.rl_vod_layout != null) {
                        this.rl_vod_layout.setBackground(getResources().getDrawable(R.drawable.layout_background));
                        break;
                    }
                } else if (this.rl_vod_layout != null) {
                    this.rl_vod_layout.setBackground(getResources().getDrawable(R.drawable.layout_background_tv));
                    break;
                }
                break;
        }
        changeStatusBarColor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setToggleIconPosition();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_vod).setChecked(true);
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
        this.context = this;
//        if (!this.getActiveLiveStreamCategoryName.isEmpty()) {
//            this.vodCategoryName.setText(this.getActiveLiveStreamCategoryName);
//        }
        if (this.tvHeaderTitle != null) {
            this.tvHeaderTitle.setOnClickListener(this);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (this.isSubcaetgroyAvail) {
            this.toolbar.inflateMenu(R.menu.menu_search_v2api);
        } else {
            this.toolbar.inflateMenu(R.menu.menu_search_text_icon_v2api);
        }
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(16843499, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout1 && this.context != null) {
            Utils.logoutUser(this.context);
        }
        if (this.isSubcaetgroyAvail) {
            if (id == R.id.action_search) {
                this.searchView = (SearchView) MenuItemCompat.getActionView(item);
                this.searchView.setQueryHint("Search Sub Categories");
                this.searchView.setIconifiedByDefault(false);
                this.searchView.setOnQueryTextListener(new C15621());
                return true;
            }
        } else if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint(getResources().getString(R.string.search_vod));
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C15632());
            return true;
        }
        if (id == R.id.menu_load_channels_vod1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C15643());
            alertDialog.setNegativeButton("NO", new C15654());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C15665());
            alertDialog.setNegativeButton("NO", new C15676());
            alertDialog.show();
        }
        if (id == R.id.layout_view_grid) {
            if (this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                this.editor.putInt(AppConst.VOD, 0);
                this.editor.commit();
                initialize();
                getAllMovies();
            } else if (this.getActiveLiveStreamCategoryId.equals("-1")) {
                this.editor.putInt(AppConst.VOD, 0);
                this.editor.commit();
                initialize();
            } else {
                this.editor.putInt(AppConst.VOD, 0);
                this.editor.commit();
                initialize();
            }
        }
        if (id == R.id.layout_view_linear) {
            if (this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                this.editor.putInt(AppConst.VOD, 1);
                this.editor.commit();
                getAllMovies();
                initialize1();
            } else if (this.getActiveLiveStreamCategoryId.equals("-1")) {
                this.editor.putInt(AppConst.VOD, 1);
                this.editor.commit();
                initialize1();
            } else {
                this.editor.putInt(AppConst.VOD, 1);
                this.editor.commit();
                initialize1();
            }
        }
        if (id == R.id.menu_sort) {
            showSortPopup(this);
        }
        return super.onOptionsItemSelected(item);
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
        closedBT.setOnClickListener(new C15687());
        savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RadioButton selectedPlayer1 = (RadioButton) layout.findViewById(rgRadio.getCheckedRadioButtonId());
                if (selectedPlayer1.getText().toString().equals(VoDCategoryActivity.this.getResources().getString(R.string.sort_last_added))) {
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "1");
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(VoDCategoryActivity.this.getResources().getString(R.string.sort_atoz))) {
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_EPG_ID);
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(VoDCategoryActivity.this.getResources().getString(R.string.sort_ztoa))) {
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_LIVE_STREAMS_CAT_ID);
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.commit();
                } else {
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
                    VoDCategoryActivity.this.SharedPreferencesSortEditor.commit();
                }
                VoDCategoryActivity.this.pref = VoDCategoryActivity.this.getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
                VoDCategoryActivity.this.editor = VoDCategoryActivity.this.pref.edit();
                AppConst.LIVE_FLAG_VOD = VoDCategoryActivity.this.pref.getInt(AppConst.VOD, 0);
                if (AppConst.LIVE_FLAG_VOD == 1) {
                    VoDCategoryActivity.this.initialize1();
                } else {
                    VoDCategoryActivity.this.initialize();
                }
                VoDCategoryActivity.this.changeSortPopUp.dismiss();
            }
        });
    }

    private void setSubCategoryLayout(ArrayList<LiveStreamCategoryIdDBModel> subCategoryList) {
        initializeSubCat(subCategoryList);
    }

    private void initializeSubCat(ArrayList<LiveStreamCategoryIdDBModel> subCategoryList) {
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.gridLayoutManager = new GridLayoutManager(this, 2);
            } else {
                this.gridLayoutManager = new GridLayoutManager(this, 1);
            }
            this.myRecyclerView.setLayoutManager(this.gridLayoutManager);
            this.myRecyclerView.setHasFixedSize(true);
            onFinish();
            this.vodSubCatAdpaterNew = new VodSubCatAdpaterNew(subCategoryList, this.context, this.liveStreamDBHandler);
            this.myRecyclerView.setAdapter(this.vodSubCatAdpaterNew);
        }
    }

    private void setLayout() {
        this.pref = getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG_VOD = this.pref.getInt(AppConst.VOD, 0);
        if (AppConst.LIVE_FLAG_VOD == 1) {
            initialize1();
        } else {
            initialize();
        }
    }

    public void getFavourites() {
        this.favouriteStreams.clear();
        if (this.myRecyclerView != null) {
            this.myRecyclerView.setAdapter(this.vodAdapter);
        }
        if (this.context != null) {
            this.database = new DatabaseHandler(this.context);
            Iterator it = this.database.getAllFavourites(AppConst.VOD).iterator();
            while (it.hasNext()) {
                FavouriteDBModel favListItem = (FavouriteDBModel) it.next();
                LiveStreamsDBModel channelAvailable = new VODStreamsDatabaseHandler(this.context).getLiveStreamFavouriteRow(favListItem.getCategoryID(), String.valueOf(favListItem.getStreamID()));
                if (channelAvailable != null) {
                    this.favouriteStreams.add(channelAvailable);
                }
            }
            onFinish();
            if (!(this.myRecyclerView == null || this.favouriteStreams == null || this.favouriteStreams.size() == 0)) {
                this.vodAdapter = new VodAdapter(this.favouriteStreams, this.context);
                this.myRecyclerView.setAdapter(this.vodAdapter);
                this.vodAdapter.notifyDataSetChanged();
                this.tvNoStream.setVisibility(4);
            }
            if (this.tvNoStream != null && this.favouriteStreams != null && this.favouriteStreams.size() == 0) {
                if (this.myRecyclerView != null) {
                    this.myRecyclerView.setAdapter(this.vodAdapter);
                }
                this.tvNoStream.setText(getResources().getString(R.string.no_fav_vod_found));
                this.tvNoStream.setVisibility(0);
            }
        }
    }

    public void getAllMovies() {
        atStart();
        this.pref = getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG_VOD = this.pref.getInt(AppConst.VOD, 0);
        if (AppConst.LIVE_FLAG_VOD == 1) {
            this.context = this;
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            if (!(this.myRecyclerView == null || this.context == null)) {
                this.myRecyclerView.setHasFixedSize(true);
                this.layoutManager = new LinearLayoutManager(this.context);
                this.myRecyclerView.setLayoutManager(this.layoutManager);
                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        } else {
            this.context = this;
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            if (!(this.myRecyclerView == null || this.context == null)) {
                this.myRecyclerView.setHasFixedSize(true);
                this.layoutManager = new GridLayoutManager(this.context, Utils.getNumberOfColumns(this.context) + 1);
                this.myRecyclerView.setLayoutManager(this.layoutManager);
                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        }
        if (this.context != null) {
            ArrayList<LiveStreamsDBModel> channelAvailable = new VODStreamsDatabaseHandler(this.context).getAllVODStreasWithCategoryId(AppConst.PASSWORD_UNSET);
            onFinish();
            if (channelAvailable == null || this.myRecyclerView == null || channelAvailable.size() == 0) {
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
                if (this.tvNoStream != null) {
                    this.tvNoStream.setVisibility(0);
                }
            } else {
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                }
                this.vodAdapter = new VodAdapter(channelAvailable, this.context);
                this.myRecyclerView.setAdapter(this.vodAdapter);
            }
        }
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    private void initialize() {
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(this.context, Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void initialize1() {
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(this.context);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void setUpdatabaseResult() {
        if (this.context != null) {
            LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            VODStreamsDatabaseHandler vodStreamsDatabaseHandler = new VODStreamsDatabaseHandler(this.context);
            if (!this.getActiveLiveStreamCategoryId.equals("-1")) {
                ArrayList<LiveStreamsDBModel> channelAvailable;
                if (this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                    this.categoryWithPasword = new ArrayList();
                    this.liveListDetailUnlcked = new ArrayList();
                    this.liveListDetailUnlckedDetail = new ArrayList();
                    this.liveListDetailAvailable = new ArrayList();
                    channelAvailable = vodStreamsDatabaseHandler.getAllVODStreasWithCategoryId(this.getActiveLiveStreamCategoryId);
                    if (liveStreamDBHandler.getParentalStatusCount() <= 0 || channelAvailable == null) {
                        this.liveListDetailAvailable = channelAvailable;
                    } else {
                        this.listPassword = getPasswordSetCategories();
                        if (this.listPassword != null) {
                            this.liveListDetailUnlckedDetail = getUnlockedCategories(channelAvailable, this.listPassword);
                        }
                        this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
                    }
                    onFinish();
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                    if (this.liveListDetailAvailable != null && this.myRecyclerView != null && this.liveListDetailAvailable.size() != 0) {
                        this.vodAdapter = new VodAdapter(this.liveListDetailAvailable, this.context);
                        this.myRecyclerView.setAdapter(this.vodAdapter);
                    } else if (this.tvNoStream != null) {
                        this.tvNoStream.setVisibility(0);
                    }
                } else {
                    channelAvailable = vodStreamsDatabaseHandler.getAllVODStreasWithCategoryId(this.getActiveLiveStreamCategoryId);
                    onFinish();
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                    if (channelAvailable != null && this.myRecyclerView != null && channelAvailable.size() != 0) {
                        this.vodAdapter = new VodAdapter(channelAvailable, this.context);
                        this.myRecyclerView.setAdapter(this.vodAdapter);
                    } else if (this.tvNoStream != null) {
                        this.tvNoStream.setVisibility(0);
                    }
                }
            }
        }
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
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

    private void setToggleIconPosition() {
        this.tv = new TypedValue();
        if (getTheme().resolveAttribute(16843499, this.tv, true)) {
            this.actionBarHeight = TypedValue.complexToDimensionPixelSize(this.tv.data, getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ImageButton) {
                ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
            }
        }
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        if (VERSION.SDK_INT >= 19) {
            window.clearFlags(67108864);
        }
        if (VERSION.SDK_INT >= 21) {
            window.addFlags(Integer.MIN_VALUE);
        }
        if (VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(8388611)) {
            drawer.closeDrawer(8388611);
            return;
        }
        this.mAdapter.setVisibiltygone(pbPagingLoader1);
        this.myRecyclerView.setClickable(true);
        super.onBackPressed();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivityV2.class));
        } else if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_vod) {
            Utils.set_layout_vod(this.context);
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_live_tv_guide) {
            if (this.context != null) {
                Utils.startDashboardV2LoadTVGuid(this.context);
            }
        } else if (id == R.id.nav_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
    }

    public void onResume() {
        super.onResume();
        this.mAdapter.setVisibiltygone(pbPagingLoader1);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context != null) {
            onFinish();
        }
        headerView();
    }

    private void headerView() {
        View header = this.navView.getHeaderView(0);
        ImageView closeDrawerIV = (ImageView) header.findViewById(R.id.iv_close_drawer);
        this.clientNameTv = (TextView) header.findViewById(R.id.tv_client_name);
        this.clientNameTv.setText(this.loginPreferencesAfterLogin.getString("username", ""));
        closeDrawerIV.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_drawer:
                this.drawerLayout.closeDrawers();
                return;
            case R.id.tv_header_title:
                startActivity(new Intent(this, DashboardActivityV2.class));
                return;
            default:
                return;
        }
    }

    public void atStart() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void progressBar(ProgressBar pbPagingLoader) {
        pbPagingLoader1 = pbPagingLoader;
    }
}
