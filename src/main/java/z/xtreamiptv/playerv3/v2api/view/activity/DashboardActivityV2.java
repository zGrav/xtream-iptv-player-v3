package z.xtreamiptv.playerv3.v2api.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.Log;
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
import butterknife.OnClick;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.presenter.XtreamPanelAPIPresenter;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.database.LiveStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.model.database.SeriesStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.model.database.VODStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.presenter.PlayerApiPresenter;
import z.xtreamiptv.playerv3.v2api.view.interfaces.PlayerApiInterface;
import z.xtreamiptv.playerv3.view.activity.LoginActivity;
import z.xtreamiptv.playerv3.view.interfaces.LoginInterface;
import z.xtreamiptv.playerv3.view.interfaces.XMLTVInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DashboardActivityV2 extends AppCompatActivity implements LoginInterface, XMLTVInterface, OnNavigationItemSelectedListener, OnClickListener, PlayerApiInterface {
    int actionBarHeight;
    @BindView(R.id.activity_dashboard)
    RelativeLayout activityDashboard;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    String currentDate = "";
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.rl_dashboard)
    RelativeLayout detail;
    @BindView(R.id.detail_view_epg)
    RelativeLayout detailViewEpg;
    @BindView(R.id.detail_vod)
    RelativeLayout detailVod;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_live_tv_forward_arrow)
    ImageView ivLivetvForwardArrow;
    @BindView(R.id.iv_livetv_icon)
    ImageView ivLivetvIcon;
    @BindView(R.id.iv_vod_forward_arrow)
    ImageView ivVodForwardArrow;
    @BindView(R.id.iv_vod_icon)
    ImageView ivVodIcon;
    private LiveStreamDBHandler liveStreamDBHandler;
    private DatabaseUpdatedStatusDBModel liveStreamsCatStatus = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private LiveStreamsDatabaseHandler liveStreamsDatabaseHandler;
    private DatabaseUpdatedStatusDBModel liveStreamsStatus = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private SharedPreferences loginPreferencesAfterLogin;
    @BindView(2131362316)
    NavigationView navView;
    private PlayerApiPresenter playerApiPresenter;
    @BindView(2131362351)
    ProgressBar progressBar;
    @BindView(2131362390)
    RelativeLayout rlImportProcess;
    @BindView(2131362396)
    RelativeLayout rlLivetv;
    @BindView(2131362419)
    RelativeLayout rlVod;
    private DatabaseUpdatedStatusDBModel seriesStreamsCatStatus = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    private DatabaseUpdatedStatusDBModel seriesStreamsStatus = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(2131362517)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(2131362545)
    TextView tvCountings;
    @BindView(2131362556)
    ImageView tvDivider;
    @BindView(2131362560)
    TextView tvEPGCount;
    @BindView(2131362565)
    ImageView tvHeaderTitle;
    @BindView(2131362567)
    TextView tvImportingStreams;
    @BindView(2131362572)
    TextView tvLiveCount;
    @BindView(2131362575)
    TextView tvLivetv;
    @BindView(2131362600)
    TextView tvPercentage;
    @BindView(2131362610)
    TextView tvSeries;
    @BindView(2131362611)
    TextView tvSeriesCount;
    @BindView(2131362631)
    TextView tvVod;
    @BindView(2131362632)
    TextView tvVodCount;
    private String userName = "";
    private String userPassword = "";
    private DatabaseUpdatedStatusDBModel vodStreamsCatStatus = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private VODStreamsDatabaseHandler vodStreamsDatabaseHandler;
    private DatabaseUpdatedStatusDBModel vodStreamsStatus = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private XMLTVPresenter xmlTvPresenter;
    private XtreamPanelAPIPresenter xtreamPanelAPIPresenter;

    class C14311 implements DialogInterface.OnClickListener {
        C14311() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVodV2Api(DashboardActivityV2.this.context);
        }
    }

    class AnonymousClass1LiveStream extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getLiveStreamCallback;

        AnonymousClass1LiveStream(Context context, List list) {
            this.val$getLiveStreamCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                DashboardActivityV2.this.liveStreamsDatabaseHandler.emptyLiveStreamRecords();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (DashboardActivityV2.this.liveStreamsDatabaseHandler != null) {
                DashboardActivityV2.this.liveStreamsDatabaseHandler.addAllliveStreams((ArrayList) this.val$getLiveStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivityV2.this.currentDateValue();
            if (!(currentDate == null || DashboardActivityV2.this.liveStreamsDatabaseHandler == null)) {
                DashboardActivityV2.this.liveStreamsDatabaseHandler.updateLiveStreamsDBStatusAndDate(AppConst.DB_LIVE_STREAMS, AppConst.DB_LIVE_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            DashboardActivityV2.this.loginPreferencesAfterLogin = DashboardActivityV2.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            DashboardActivityV2.this.playerApiPresenter.getVODStreamCat(DashboardActivityV2.this.loginPreferencesAfterLogin.getString("username", ""), DashboardActivityV2.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    class AnonymousClass1LivestreamCat extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getLiveStreamCategoriesCallback;

        AnonymousClass1LivestreamCat(Context context, List list) {
            this.val$getLiveStreamCategoriesCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                DashboardActivityV2.this.liveStreamsDatabaseHandler.emptyLiveStreamCatRecords();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (DashboardActivityV2.this.liveStreamsDatabaseHandler != null) {
                DashboardActivityV2.this.liveStreamsDatabaseHandler.addLiveCategories((ArrayList) this.val$getLiveStreamCategoriesCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivityV2.this.currentDateValue();
            if (!(currentDate == null || DashboardActivityV2.this.liveStreamsDatabaseHandler == null)) {
                DashboardActivityV2.this.liveStreamsDatabaseHandler.updateLiveStreamsCatDBStatusAndDate(AppConst.DB_LIVE_STREAMS_CAT, AppConst.DB_LIVE_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            DashboardActivityV2.this.loginPreferencesAfterLogin = DashboardActivityV2.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            DashboardActivityV2.this.playerApiPresenter.getLiveStreams(DashboardActivityV2.this.loginPreferencesAfterLogin.getString("username", ""), DashboardActivityV2.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    class AnonymousClass1NewAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$xmltvCallback.programmePojos.size();
        Context mcontext = null;
        final /* synthetic */ int val$totalEPGSize;
         /* synthetic */ XMLTVCallback val$xmltvCallback;

        AnonymousClass1NewAsyncTask(Context context, XMLTVCallback xMLTVCallback, int i) {
            this.val$xmltvCallback = xMLTVCallback;
            this.val$totalEPGSize = i;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                if (DashboardActivityV2.this.liveStreamDBHandler != null) {
                    DashboardActivityV2.this.liveStreamDBHandler.addEPG(this.val$xmltvCallback.programmePojos);
                }
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            if (DashboardActivityV2.this.rlImportProcess != null) {
                DashboardActivityV2.this.rlImportProcess.setVisibility(8);
            }
            DashboardActivityV2.this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
            if (DashboardActivityV2.this.context != null) {
                Utils.showToast(DashboardActivityV2.this.context, DashboardActivityV2.this.getResources().getString(R.string.update_tv_guide_success) + " (" + this.val$totalEPGSize + ")");
            }
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
            try {
                DashboardActivityV2.this.seriesStreamsDatabaseHandler.emptySeriesStreamRecords();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (DashboardActivityV2.this.seriesStreamsDatabaseHandler != null) {
                DashboardActivityV2.this.seriesStreamsDatabaseHandler.addAllSeriesStreams((ArrayList) this.val$getSeriesStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivityV2.this.currentDateValue();
            if (!(currentDate == null || DashboardActivityV2.this.seriesStreamsDatabaseHandler == null)) {
                DashboardActivityV2.this.seriesStreamsDatabaseHandler.updateseriesStreamsDBStatusAndDate(AppConst.DB_SERIES_STREAMS, AppConst.DB_SERIES_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            if (DashboardActivityV2.this.context != null) {
                DashboardActivityV2.this.startActivity(new Intent(DashboardActivityV2.this.context, DashboardActivityV2.class));
                DashboardActivityV2.this.finish();
            }
            if (DashboardActivityV2.this.rlImportProcess != null) {
                DashboardActivityV2.this.rlImportProcess.setVisibility(8);
            }
            if (!(DashboardActivityV2.this.liveStreamsDatabaseHandler == null || DashboardActivityV2.this.liveStreamsDatabaseHandler.getLiveStreamsCount() <= 0 || DashboardActivityV2.this.tvLiveCount == null)) {
                DashboardActivityV2.this.tvLiveCount.setText(DashboardActivityV2.this.getResources().getString(R.string.total));
                DashboardActivityV2.this.tvLiveCount.append(" : " + DashboardActivityV2.this.liveStreamsDatabaseHandler.getLiveStreamsCount());
                DashboardActivityV2.this.tvLiveCount.setVisibility(0);
            }
            if (!(DashboardActivityV2.this.vodStreamsDatabaseHandler == null || DashboardActivityV2.this.vodStreamsDatabaseHandler.getAllVODCountCount() <= 0 || DashboardActivityV2.this.tvVodCount == null)) {
                DashboardActivityV2.this.tvVodCount.setText(DashboardActivityV2.this.getResources().getString(R.string.total));
                DashboardActivityV2.this.tvVodCount.append(" : " + DashboardActivityV2.this.vodStreamsDatabaseHandler.getAllVODCountCount());
                DashboardActivityV2.this.tvVodCount.setVisibility(0);
            }
            if (!(DashboardActivityV2.this.seriesStreamsDatabaseHandler == null || DashboardActivityV2.this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount() <= 0 || DashboardActivityV2.this.tvSeriesCount == null)) {
                DashboardActivityV2.this.tvSeriesCount.setText(DashboardActivityV2.this.getResources().getString(R.string.total));
                DashboardActivityV2.this.tvSeriesCount.append(" : " + DashboardActivityV2.this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount());
                DashboardActivityV2.this.tvSeriesCount.setVisibility(0);
            }
            if (DashboardActivityV2.this.context != null) {
                Utils.showToast(DashboardActivityV2.this.context, DashboardActivityV2.this.getResources().getString(R.string.update_livestreams_vod_success));
            }
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
            try {
                DashboardActivityV2.this.seriesStreamsDatabaseHandler.emptySeriesStreamCatRecords();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (DashboardActivityV2.this.seriesStreamsDatabaseHandler != null) {
                DashboardActivityV2.this.seriesStreamsDatabaseHandler.addSeriesCategories((ArrayList) this.val$getSeriesStreamCategoriesCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivityV2.this.currentDateValue();
            if (!(currentDate == null || DashboardActivityV2.this.seriesStreamsDatabaseHandler == null)) {
                DashboardActivityV2.this.seriesStreamsDatabaseHandler.updateSeriesStreamsCatDBStatusAndDate(AppConst.DB_SERIES_STREAMS_CAT, AppConst.DB_SERIES_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            DashboardActivityV2.this.loginPreferencesAfterLogin = DashboardActivityV2.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            DashboardActivityV2.this.playerApiPresenter.getSeriesStream(DashboardActivityV2.this.loginPreferencesAfterLogin.getString("username", ""), DashboardActivityV2.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    class AnonymousClass1VODStream extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getVODStreamCallback;

        AnonymousClass1VODStream(Context context, List list) {
            this.val$getVODStreamCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                DashboardActivityV2.this.vodStreamsDatabaseHandler.emptyVODStreamRecords();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (DashboardActivityV2.this.vodStreamsDatabaseHandler != null) {
                DashboardActivityV2.this.vodStreamsDatabaseHandler.addAllVODStreams((ArrayList) this.val$getVODStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivityV2.this.currentDateValue();
            if (!(currentDate == null || DashboardActivityV2.this.vodStreamsDatabaseHandler == null)) {
                DashboardActivityV2.this.vodStreamsDatabaseHandler.updateVODStreamsDBStatusAndDate(AppConst.DB_VOD_STREAMS, AppConst.DB_VOD_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            DashboardActivityV2.this.loginPreferencesAfterLogin = DashboardActivityV2.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            DashboardActivityV2.this.playerApiPresenter.getSeriesStreamCat(DashboardActivityV2.this.loginPreferencesAfterLogin.getString("username", ""), DashboardActivityV2.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    class AnonymousClass1VODtreamCat extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getVODStreamCategoriesCallback;

        AnonymousClass1VODtreamCat(Context context, List list) {
            this.val$getVODStreamCategoriesCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                DashboardActivityV2.this.vodStreamsDatabaseHandler.emptyVODStreamCatRecords();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (DashboardActivityV2.this.vodStreamsDatabaseHandler != null) {
                DashboardActivityV2.this.vodStreamsDatabaseHandler.addVODCategories((ArrayList) this.val$getVODStreamCategoriesCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivityV2.this.currentDateValue();
            if (!(currentDate == null || DashboardActivityV2.this.vodStreamsDatabaseHandler == null)) {
                DashboardActivityV2.this.vodStreamsDatabaseHandler.updateVODStreamsCatDBStatusAndDate(AppConst.DB_VOD_STREAMS_CAT, AppConst.DB_VOD_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            DashboardActivityV2.this.loginPreferencesAfterLogin = DashboardActivityV2.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            DashboardActivityV2.this.playerApiPresenter.getVODStream(DashboardActivityV2.this.loginPreferencesAfterLogin.getString("username", ""), DashboardActivityV2.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    class C14322 implements DialogInterface.OnClickListener {
        C14322() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C14333 implements DialogInterface.OnClickListener {
        C14333() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(DashboardActivityV2.this.context);
        }
    }

    class C14344 implements DialogInterface.OnClickListener {
        C14344() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_v2);
        ButterKnife.bind(this);
        setSupportActionBar(this.toolbar);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setToggleIconPosition();
        changeStatusBarColor();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        this.context = this;
        intialize(this.context);
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
        launchTVGuideFromDifferentActivity();
        updateChannelsandEPG(this.context);
        lsitGridViewDefaultSetup();
    }

    private void lsitGridViewDefaultSetup() {
        Editor editor = getSharedPreferences(AppConst.LIST_GRID_VIEW, 0).edit();
        editor.putInt(AppConst.LIVE_STREAM, 0);
        editor.putInt(AppConst.VOD, 0);
    }

    private void intialize(Context context) {
        if (context != null) {
            this.liveStreamDBHandler = new LiveStreamDBHandler(context);
            this.liveStreamsDatabaseHandler = new LiveStreamsDatabaseHandler(context);
            this.vodStreamsDatabaseHandler = new VODStreamsDatabaseHandler(context);
            this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(context);
            this.playerApiPresenter = new PlayerApiPresenter(context, this);
            this.xmlTvPresenter = new XMLTVPresenter(this, context);
            if (this.liveStreamsDatabaseHandler.getLiveStreamsCount() > 0 && this.tvLiveCount != null) {
                this.tvLiveCount.append(" : " + this.liveStreamsDatabaseHandler.getLiveStreamsCount());
                this.tvLiveCount.setVisibility(0);
            }
            if (this.vodStreamsDatabaseHandler.getAllVODCountCount() > 0 && this.tvVodCount != null) {
                this.tvVodCount.append(" : " + this.vodStreamsDatabaseHandler.getAllVODCountCount());
                this.tvVodCount.setVisibility(0);
            }
            if (this.liveStreamDBHandler.getEPGCount() > 0 && this.tvEPGCount != null) {
                this.tvEPGCount.append(" : " + this.liveStreamDBHandler.getEPGCount());
                this.tvEPGCount.setVisibility(0);
            }
            if (this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount() > 0 && this.tvSeriesCount != null) {
                this.tvSeriesCount.append(" : " + this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount());
                this.tvSeriesCount.setVisibility(0);
            }
        }
    }

    private void launchTVGuideFromDifferentActivity() {
        Intent intentOtherActiviy = getIntent();
        String launchTvGuid = "";
        if (intentOtherActiviy != null) {
            launchTvGuid = intentOtherActiviy.getStringExtra(AppConst.LAUNCH_TV_GUIDE);
            if (launchTvGuid != null && !launchTvGuid.equals("") && launchTvGuid.equals(AppConst.LAUNCH_TV_GUIDE)) {
                launchTvGuide();
            }
        }
    }

    public void updateChannelsandEPG(Context context) {
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_PREF_AUTOUPDATE, 0);
        if (!this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_AUTOUPDATE, "").equals(getResources().getString(R.string.disable))) {
            this.currentDate = currentDateValue();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (context != null && this.liveStreamsDatabaseHandler != null && this.rlImportProcess != null) {
                this.rlImportProcess.setVisibility(8);
                this.loginPreferencesAfterLogin = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
                String username = this.loginPreferencesAfterLogin.getString("username", "");
                String password = this.loginPreferencesAfterLogin.getString("password", "");
                if (this.liveStreamsDatabaseHandler.getLiveStreamsCatDBStatusCount() != 0 && this.liveStreamsDatabaseHandler != null && this.rlImportProcess != null && this.playerApiPresenter != null) {
                    String lastUpdatedStatusdate = "";
                    String status = "";
                    this.liveStreamsCatStatus = this.liveStreamsDatabaseHandler.getdateLiveStreamsCatDBStatus(AppConst.DB_LIVE_STREAMS_CAT, AppConst.DB_LIVE_STREAMS_CAT_ID);
                    if (this.liveStreamsCatStatus != null) {
                        status = this.liveStreamsCatStatus.getDbUpadatedStatusState();
                        lastUpdatedStatusdate = this.liveStreamsCatStatus.getDbLastUpdatedDate();
                    }
                    if (getDateDiff(simpleDateFormat, lastUpdatedStatusdate, this.currentDate) >= 1) {
                        this.rlImportProcess.setVisibility(0);
                        if (this.tvImportingStreams != null) {
                            this.tvImportingStreams.setText(getResources().getString(R.string.refreshing_all_channels));
                        }
                        if ((!username.equals("") && !password.equals("") && !status.equals("") && !status.isEmpty() && status.equals(AppConst.DB_UPDATED_STATUS_FINISH)) || (!username.equals("") && !password.equals("") && status != null && !status.equals("") && !status.isEmpty() && status.equals(AppConst.DB_UPDATED_STATUS_FAILED))) {
                            this.liveStreamsDatabaseHandler.updateLiveStreamsCatDBStatusAndDate(AppConst.DB_LIVE_STREAMS_CAT, AppConst.DB_LIVE_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_PROCESSING, this.currentDate);
                            this.playerApiPresenter.getLiveStreamCat(username, password);
                        }
                    }
                }
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

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(8388611)) {
            drawer.closeDrawer(8388611);
        } else {
            moveTaskToBack(true);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_text_icon_v2api);
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

    private void seticon(int itemid, int iconid) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        if (id == R.id.menu_load_channels_vod) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C14311());
            alertDialog.setNegativeButton("NO", new C14322());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C14333());
            alertDialog.setNegativeButton("NO", new C14344());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);
        } else if (id == R.id.nav_vod) {
            Utils.set_layout_vod(this.context);

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_live_tv_guide) {
            launchTvGuide();
        } else if (id == R.id.nav_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
    }

    public String getUserName() {
        if (this.context == null) {
            return this.userName;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("username", "");
    }

    public String getUserPassword() {
        if (this.context == null) {
            return this.userPassword;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("password", "");
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

    @OnClick({R.id.account_info, R.id.detail_series, R.id.detail_settings, R.id.detail_tv_archive, R.id.detail_view_epg, R.id.detail_vod, R.id.live_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.account_info:
                startActivity(new Intent(this, AccountInfoActivity.class));
                return;
            case R.id.detail_series:
                startActivity(new Intent(this, SeriesTabActivity.class));
                return;
            case R.id.detail_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return;
            case R.id.detail_tv_archive:
                startActivity(new Intent(this, TVArchiveActivity.class));
                return;
            case R.id.detail_view_epg:
                launchTvGuide();
                return;
            case R.id.detail_vod:
                Utils.set_layout_vod(this.context);
                return;
            case R.id.live_tv:
                Utils.set_layout_live(this.context);
                return;
            default:
                return;
        }
    }

    private void launchTvGuide() {
        this.currentDate = currentDateValue();
        startXMLTV(getUserName(), getUserPassword(), this.currentDate);
    }

    public void startXMLTV(String userName, String userPassword, String currentDate) {
        String status = "";
        String lastUpdatedStatusdate = "";
        int epgCount = this.liveStreamDBHandler.getEPGCount();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelEPG != null) {
            status = this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState();
            lastUpdatedStatusdate = this.databaseUpdatedStatusDBModelEPG.getDbLastUpdatedDate();
        }
        long dateDifference = getDateDiff(simpleDateFormat, lastUpdatedStatusdate, currentDate);
        if (epgCount == 0) {
            startImportTvGuideActivity();
        } else if (dateDifference >= 0 && dateDifference < 2) {
            startTvGuideActivity();
        } else if (dateDifference >= 2) {
            executeXMLTV(userName, userPassword, status, currentDate);
        }
    }

    private void executeXMLTV(String userName, String userPassword, String status, String currentDate) {
        if ((!userName.equals("") && !userPassword.equals("") && status != null && !status.equals("") && !status.isEmpty() && status.equals(AppConst.DB_UPDATED_STATUS_FINISH)) || (!userName.equals("") && !userPassword.equals("") && status != null && !status.equals("") && !status.isEmpty() && status.equals(AppConst.DB_UPDATED_STATUS_FAILED))) {
            this.liveStreamDBHandler.updateDBStatusAndDate(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_PROCESSING, currentDate);
            if (this.rlImportProcess != null) {
                this.rlImportProcess.setVisibility(0);
            }
            if (this.tvImportingStreams != null) {
                this.tvImportingStreams.setText(getResources().getString(R.string.refreshing_epg));
            }
            this.xmlTvPresenter.epgXMLTV(userName, userPassword);
        }
    }

    public void startTvGuideActivity() {
        startActivity(new Intent(this, NewEPGActivity.class));
        finish();
    }

    public void startImportTvGuideActivity() {
        startActivity(new Intent(this, ImportEPGActivity.class));
        finish();
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
            default:
                return;
        }
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
        if (loginCallback != null && loginCallback.getUserLoginInfo().getAuth().intValue() == 1 && !loginCallback.getUserLoginInfo().getStatus().equals("Active") && this.context != null) {
            Utils.logoutUser(this.context);
        }
    }

    public void stopLoader() {
    }

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String message) {
        Utils.showToast(this.context, getResources().getString(R.string.network_error));
    }

    public void atProcess() {
    }

    public void epgXMLTVUpdateFailed(String failedUpdate) {
        if (this.rlImportProcess != null) {
            this.rlImportProcess.setVisibility(4);
        }
        if (failedUpdate.equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FAILED);
        }
    }

    public void epgXMLTV(XMLTVCallback xmltvCallback) {
        if (xmltvCallback != null && this.context != null && xmltvCallback.programmePojos != null) {
            if (!(this.rlImportProcess == null || this.tvImportingStreams == null)) {
                this.rlImportProcess.setVisibility(0);
            }
            int totalEPGSize = xmltvCallback.programmePojos.size();
            if (totalEPGSize > 0 && this.tvEPGCount != null) {
                this.tvEPGCount.setText(getResources().getString(R.string.total));
                this.tvEPGCount.append(" : " + totalEPGSize);
                this.tvEPGCount.setVisibility(0);
            }
            this.liveStreamDBHandler.makeEmptyEPG();
            if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1NewAsyncTask(this.context, xmltvCallback, totalEPGSize).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1NewAsyncTask(this.context, xmltvCallback, totalEPGSize).execute(new String[0]);
            }
        }
    }

    public void getLiveStreamCategories(List<GetLiveStreamCategoriesCallback> getLiveStreamCategoriesCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1LivestreamCat(this.context, getLiveStreamCategoriesCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1LivestreamCat(this.context, getLiveStreamCategoriesCallback).execute(new String[0]);
        }
    }

    public void getLiveStreams(List<GetLiveStreamCallback> getLiveStreamCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1LiveStream(this.context, getLiveStreamCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1LiveStream(this.context, getLiveStreamCallback).execute(new String[0]);
        }
    }

    public void getVODStreamCategories(List<GetVODStreamCategoriesCallback> getVODStreamCategoriesCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1VODtreamCat(this.context, getVODStreamCategoriesCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1VODtreamCat(this.context, getVODStreamCategoriesCallback).execute(new String[0]);
        }
    }

    public void getVODStreams(List<GetVODStreamCallback> getVODStreamCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1VODStream(this.context, getVODStreamCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1VODStream(this.context, getVODStreamCallback).execute(new String[0]);
        }
    }

    public void getSeriesCategories(List<GetSeriesStreamCategoriesCallback> getSeriesStreamCategoriesCallback) {
        if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1SeriesStreamCat(this.context, getSeriesStreamCategoriesCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1SeriesStreamCat(this.context, getSeriesStreamCategoriesCallback).execute(new String[0]);
        }
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
    }

    public void getLiveStreamFailed(String message) {
    }

    public void getVODStreamsFailed(String message) {
    }

    public void getSeriesStreamsFailed(String message) {
    }
}
