package z.xtreamiptv.playerv3.v2api.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.v2api.model.database.VODStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.view.adapter.VodAdapterNewFlow;
import z.xtreamiptv.playerv3.view.activity.LoginActivity;
import java.util.ArrayList;
import java.util.Iterator;

public class VodActivityWithCategory extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener {
    private static final String JSON = "";
    static ProgressBar pbPagingLoader1;
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    ArrayList<LiveStreamCategoryIdDBModel> categoriesList;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.fl_frame)
    FrameLayout frameLayout;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private VodAdapterNewFlow mAdapter;
    private LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.pb_paging_loader)
    ProgressBar pbPagingLoader;
    @BindView(R.id.rl_vod_layout)
    RelativeLayout rl_vod_layout;
    private SearchView searchView;
    ArrayList<LiveStreamCategoryIdDBModel> subCategoryList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    private String userName = "";
    private String userPassword = "";
    private VODStreamsDatabaseHandler vodStreamDatbaseHandler;
    private XMLTVPresenter xmltvPresenter;

    class C15711 implements DialogInterface.OnClickListener {
        C15711() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVodV2Api(VodActivityWithCategory.this.context);
        }
    }

    class C15722 implements DialogInterface.OnClickListener {
        C15722() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15733 implements DialogInterface.OnClickListener {
        C15733() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(VodActivityWithCategory.this.context);
        }
    }

    class C15744 implements DialogInterface.OnClickListener {
        C15744() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15755 implements OnQueryTextListener {
        C15755() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            VodActivityWithCategory.this.tvNoRecordFound.setVisibility(8);
            if (!(VodActivityWithCategory.this.mAdapter == null || VodActivityWithCategory.this.tvNoRecordFound == null || VodActivityWithCategory.this.tvNoRecordFound.getVisibility() == 0)) {
                VodActivityWithCategory.this.mAdapter.filter(newText, VodActivityWithCategory.this.tvNoRecordFound);
            }
            return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_with_category);
        ButterKnife.bind(this);
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            if (this.rl_vod_layout != null) {
                this.rl_vod_layout.setBackground(getResources().getDrawable(R.drawable.layout_background_tv));
            }
        } else if (this.rl_vod_layout != null) {
            this.rl_vod_layout.setBackground(getResources().getDrawable(R.drawable.layout_background));
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
        this.context = this;
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
        intiliaze();
        if (this.tvHeaderTitle != null) {
            this.tvHeaderTitle.setOnClickListener(this);
        }
        this.frameLayout.setVisibility(8);
    }

    private void intiliaze() {
        this.categoriesList = new ArrayList();
        this.subCategoryList = new ArrayList();
        this.vodStreamDatbaseHandler = new VODStreamsDatabaseHandler(this.context);
        this.myRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(this.context);
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            this.mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            this.mLayoutManager = new GridLayoutManager(this, 1);
        }
        this.myRecyclerView.setLayoutManager(this.mLayoutManager);
        this.myRecyclerView.setVisibility(0);
        if (this.context != null) {
            this.liveListDetailUnlcked = new ArrayList();
            this.liveListDetailUnlckedDetail = new ArrayList();
            this.liveListDetailAvailable = new ArrayList();
            this.liveListDetail = new ArrayList();
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            ArrayList<LiveStreamCategoryIdDBModel> moviesCategory = this.vodStreamDatbaseHandler.getAllVODCategories();
            ArrayList<LiveStreamCategoryIdDBModel> moviesCategoryFinal = new ArrayList();
            LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel(null, null, 0);
            int size = moviesCategoryFinal.size();
            LiveStreamCategoryIdDBModel liveStream1 = new LiveStreamCategoryIdDBModel(null, null, 0);
            liveStream1.setLiveStreamCategoryID("-1");
            liveStream1.setLiveStreamCategoryName(getResources().getString(R.string.favourites));
            liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
            liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
            this.liveListDetail = moviesCategory;
            if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || this.liveListDetail == null) {
                this.liveListDetail.add(0, liveStream);
                this.liveListDetail.add(1, liveStream1);
                this.liveListDetailAvailable = this.liveListDetail;
            } else {
                this.listPassword = getPasswordSetCategories();
                this.liveListDetailUnlckedDetail = getUnlockedCategories(moviesCategory, this.listPassword);
                this.liveListDetailUnlcked.add(0, liveStream);
                this.liveListDetailUnlcked.add(1, liveStream1);
                this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
            }
            this.mAdapter = new VodAdapterNewFlow(this.liveListDetailAvailable, this.context);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.myRecyclerView.setAdapter(this.mAdapter);
        }
    }

    private ArrayList<LiveStreamCategoryIdDBModel> getUnlockedCategories(ArrayList<LiveStreamCategoryIdDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamCategoryIdDBModel user1 = (LiveStreamCategoryIdDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getLiveStreamCategoryID().equals((String) it2.next())) {
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
        this.frameLayout.setVisibility(8);
        this.myRecyclerView.setClickable(true);
        this.mAdapter.setVisibiltygone(pbPagingLoader1);
        super.onBackPressed();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivityV2.class));
        } else if (id == R.id.nav_live_tv) {
            startActivity(new Intent(this, LiveActivityNewFlow.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
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
        this.frameLayout.setVisibility(8);
        if (this.mAdapter != null) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
            this.mAdapter.notifyDataSetChanged();
        }
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
            case R.id.iv_bt_up:
                intiliaze();
                return;
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

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_search_v2api);
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
        if (id == R.id.menu_load_channels_vod1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C15711());
            alertDialog.setNegativeButton("NO", new C15722());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide1) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C15733());
            alertDialog.setNegativeButton("NO", new C15744());
            alertDialog.show();
        }
        if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint(getResources().getString(R.string.search_vod));
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C15755());
        }
        return super.onOptionsItemSelected(item);
    }

    public void progressBar(ProgressBar pbPagingLoader) {
        pbPagingLoader1 = pbPagingLoader;
    }
}
