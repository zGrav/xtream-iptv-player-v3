package z.xtreamiptv.playerv3.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.database.LiveStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.model.database.SeriesStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.presenter.PlayerApiPresenter;
import z.xtreamiptv.playerv3.v2api.view.interfaces.PlayerApiInterface;
import z.xtreamiptv.playerv3.view.adapter.SeriesTabCategoryAdapter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SeriesTabActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener, PlayerApiInterface {
    private static final String JSON = "";
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private LiveStreamDBHandler liveStreamDBHandler;
    private LiveStreamsDatabaseHandler liveStreamsDatabaseHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private PlayerApiPresenter playerApiPresenter;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    class C18381 implements OnPageChangeListener {
        C18381() {
        }

        public void onPageScrolled(int i, float v, int i2) {
        }

        public void onPageSelected(int i) {
        }

        public void onPageScrollStateChanged(int i) {
        }
    }

    class AnonymousClass1SeriesStream extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getSeriesStreamCallback;

        AnonymousClass1SeriesStream(Context context, List list) {
            this.val$getSeriesStreamCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (SeriesTabActivity.this.seriesStreamsDatabaseHandler != null) {
                SeriesTabActivity.this.seriesStreamsDatabaseHandler.addAllSeriesStreams((ArrayList) this.val$getSeriesStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = SeriesTabActivity.this.currentDateValue();
            if (!(currentDate == null || SeriesTabActivity.this.seriesStreamsDatabaseHandler == null)) {
                SeriesTabActivity.this.seriesStreamsDatabaseHandler.updateseriesStreamsDBStatusAndDate(AppConst.DB_SERIES_STREAMS, AppConst.DB_SERIES_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            SeriesTabActivity.this.setUpDatabaseResults();
        }
    }

    class AnonymousClass1SeriesStreamCat extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getSeriesStreamCategoriesCallback;

        AnonymousClass1SeriesStreamCat(Context context, List list) {
            this.val$getSeriesStreamCategoriesCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (SeriesTabActivity.this.seriesStreamsDatabaseHandler != null) {
                SeriesTabActivity.this.seriesStreamsDatabaseHandler.addSeriesCategories((ArrayList) this.val$getSeriesStreamCategoriesCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = SeriesTabActivity.this.currentDateValue();
            if (!(currentDate == null || SeriesTabActivity.this.seriesStreamsDatabaseHandler == null)) {
                SeriesTabActivity.this.seriesStreamsDatabaseHandler.updateSeriesStreamsCatDBStatusAndDate(AppConst.DB_SERIES_STREAMS_CAT, AppConst.DB_SERIES_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            SeriesTabActivity.this.loginPreferencesAfterLogin = SeriesTabActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            SeriesTabActivity.this.playerApiPresenter.getSeriesStream(SeriesTabActivity.this.loginPreferencesAfterLogin.getString("username", ""), SeriesTabActivity.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_tab);
        ButterKnife.bind(this);
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
        navigationView.getMenu().findItem(R.id.nav_series).setChecked(true);
        this.context = this;
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
        if (this.tvHeaderTitle != null) {
            this.tvHeaderTitle.setOnClickListener(this);
        }
        setUpDatabaseResults();
    }

    private int getSeriesCatCount() {
        return this.seriesStreamsDatabaseHandler.getAllSeriesCategories().size();
    }

    private int getSeriesCount() {
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
        return this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount();
    }

    private void saveDatafirstTime() {
        this.context = this;
        this.playerApiPresenter = new PlayerApiPresenter(this.context, this);
        if (this.playerApiPresenter != null) {
            this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesAfterLogin.getString("username", "");
            String password = this.loginPreferencesAfterLogin.getString("password", "");
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                this.playerApiPresenter.getSeriesStreamCat(username, password);
            }
        }
    }

    private void setUpDatabaseResults() {
        if (this.context != null) {
            this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            this.categoryWithPasword = new ArrayList();
            this.liveListDetailAvailable = new ArrayList();
            this.liveListDetail = new ArrayList();
            this.liveListDetail = this.seriesStreamsDatabaseHandler.getAllSeriesCategories();
            LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel(null, null, 0);
            LiveStreamCategoryIdDBModel liveStream1 = new LiveStreamCategoryIdDBModel(null, null, 0);
            LiveStreamCategoryIdDBModel liveStream2 = new LiveStreamCategoryIdDBModel(null, null, 0);
            liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
            liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
            liveStream1.setLiveStreamCategoryID("-1");
            liveStream1.setLiveStreamCategoryName(getResources().getString(R.string.favourites));
            this.liveListDetail.add(0, liveStream);
            this.liveListDetailAvailable = this.liveListDetail;
            if (this.liveListDetailAvailable != null && this.viewpager != null && this.slidingTabs != null) {
                this.viewpager.setAdapter(new SeriesTabCategoryAdapter(this.liveListDetailAvailable, getSupportFragmentManager(), this));
                this.slidingTabs.setupWithViewPager(this.viewpager);
                this.viewpager.setOnPageChangeListener(new C18381());
            }
        }
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
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
        } else if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);
        } else if (id == R.id.nav_vod) {
            Utils.set_layout_vod(this.context);

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_live_tv_guide) {
            if (this.context != null) {
                Utils.startDashboardV2LoadTVGuid(this.context);
            }
        } else if (id == R.id.nav_logout) {
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
    }

    public void onResume() {
        super.onResume();
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
                startActivity(new Intent(this, DashboardActivity.class));
                return;
            default:
                return;
        }
    }

    public void atStart() {
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFailed(String messsage) {
    }

    public void atProcess() {
    }

    public void getLiveStreamCategories(List<GetLiveStreamCategoriesCallback> list) {
    }

    public void getVODStreamCategories(List<GetVODStreamCategoriesCallback> list) {
    }

    public void getSeriesCategories(List<GetSeriesStreamCategoriesCallback> getSeriesStreamCategoriesCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1SeriesStreamCat(this.context, getSeriesStreamCategoriesCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1SeriesStreamCat(this.context, getSeriesStreamCategoriesCallback).execute(new String[0]);
        }
    }

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    public void getLiveStreams(List<GetLiveStreamCallback> list) {
    }

    public void getVODStreams(List<GetVODStreamCallback> list) {
    }

    public void getSeriesStreams(List<GetSeriesStreamCallback> getSeriesStreamCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1SeriesStream(this.context, getSeriesStreamCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1SeriesStream(this.context, getSeriesStreamCallback).execute(new String[0]);
        }
    }

    public void getLiveStreamCatFailed(String message) {
    }

    public void getVODStreamCatFailed(String message) {
    }

    public void getSeriesStreamCatFailed(String message) {
        setUpDatabaseResults();
    }

    public void getLiveStreamFailed(String message) {
    }

    public void getVODStreamsFailed(String message) {
    }

    public void getSeriesStreamsFailed(String message) {
        setUpDatabaseResults();
    }
}
