package z.xtreamiptv.playerv3.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.Menu;
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
import z.xtreamiptv.playerv3.model.callback.LiveStreamsEpgCallback;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import z.xtreamiptv.playerv3.presenter.LoginPresenter;
import z.xtreamiptv.playerv3.view.adapter.SubTVArchiveCategoriesAdapter;
import z.xtreamiptv.playerv3.view.interfaces.LiveStreamsEpgInterface;
import z.xtreamiptv.playerv3.view.interfaces.LoginInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SubTVArchiveActivity extends AppCompatActivity implements LoginInterface, LiveStreamsEpgInterface, OnNavigationItemSelectedListener, OnClickListener {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    TextView clientNameTv;
    private Context context;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private LoginPresenter loginPresenter;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.rl_tv_archive_title)
    RelativeLayout rlTvArchiveTitle;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_egp_required)
    TextView tvEpgRequired;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_archive);
        ButterKnife.bind(this);
        changeStatusBarColor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setToggleIconPosition();
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        initialize();
    }

    private void initialize() {
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String opened_num = getIntent().getStringExtra("OPENED_NUM");
        String opened_channel_id = getIntent().getStringExtra("OPENED_CHANNEL_ID");
        String opened_channel_name = getIntent().getStringExtra("OPENED_NAME");
        String opened_channel_icon = getIntent().getStringExtra("OPENED_STREAM_ICON");
        String opened_channel_duration = getIntent().getStringExtra("OPENED_ARCHIVE_DURATION");
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (opened_channel_id != null && !opened_channel_id.equals("")) {
            getEPG(opened_channel_id, opened_stream_id, opened_num, opened_channel_name, opened_channel_icon, opened_channel_duration);
        }
    }

    public void getEPG(String opened_channel_id, int opened_stream_id, String opened_num, String opened_channel_name, String opened_channel_icon, String opened_channel_duration) {
        if (this.liveStreamDBHandler != null) {
            ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(opened_channel_id);
            if (xmltvProgrammePojos != null) {
                int EpgSize = xmltvProgrammePojos.size();
                if (EpgSize != 0) {
                    String currentFormatDateAfter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date());
                    List<String> datesTabs = new ArrayList();
                    int datesTabsIndex = 0;
                    int currentDateIndex = 0;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    for (int k = 0; k < EpgSize; k++) {
                        String currentFormatDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(Long.valueOf(Utils.epgTimeConverter(((XMLTVProgrammePojo) xmltvProgrammePojos.get(k)).getStart())));
                        if (Long.valueOf(getDateDiff(simpleDateFormat, currentFormatDate, currentFormatDateAfter)).longValue() >= 0 && !datesTabs.contains(currentFormatDate)) {
                            datesTabs.add(datesTabsIndex, currentFormatDate);
                            if (currentFormatDateAfter.equals(currentFormatDate)) {
                                currentDateIndex = datesTabsIndex;
                                break;
                            }
                            datesTabsIndex++;
                        }
                    }
                    ViewPager viewPager = this.viewpager;
                    viewPager.setAdapter(new SubTVArchiveCategoriesAdapter(datesTabs, xmltvProgrammePojos, opened_stream_id, opened_num, opened_channel_name, opened_channel_icon, opened_channel_id, opened_channel_duration, getSupportFragmentManager(), this));
                    this.slidingTabs.setupWithViewPager(this.viewpager);
                    this.viewpager.setCurrentItem(currentDateIndex);
                } else {
                    if (this.slidingTabs != null) {
                        this.slidingTabs.setVisibility(8);
                    }
                    if (this.rlTvArchiveTitle != null) {
                        this.rlTvArchiveTitle.setVisibility(0);
                    }
                    if (this.tvNoRecordFound != null) {
                        this.tvNoRecordFound.setVisibility(0);
                    }
                    if (this.tvEpgRequired != null) {
                        this.tvEpgRequired.setVisibility(0);
                    }
                }
                onFinish();
            }
        }
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
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

    public void onResume() {
        super.onResume();
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        headerView();
    }

    public void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback) {
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

    public void onFailed(String errorMessage) {
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
            finish();
        } else if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);
            finish();
        } else if (id == R.id.nav_vod) {
            Utils.set_layout_vod(this.context);
            finish();
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
            finish();

            finish();
        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        } else if (id == R.id.nav_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_dashboard_logout);
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
            default:
                return;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        return super.onOptionsItemSelected(item);
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
        if (loginCallback != null && loginCallback.getUserLoginInfo().getAuth().intValue() == 1) {
            String userStatus = loginCallback.getUserLoginInfo().getStatus();
            if (!userStatus.equals("Active")) {
                Utils.showToast(this.context, AppConst.INVALID_STATUS + userStatus);
                if (this.context != null) {
                    Utils.logoutUser(this.context);
                }
            }
        }
    }

    public void stopLoader() {
    }
}
