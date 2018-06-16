package z.xtreamiptv.playerv3.view.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;
import z.xtreamiptv.playerv3.model.callback.XtreamPanelAPICallback;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.pojo.PanelAvailableChannelsPojo;
import z.xtreamiptv.playerv3.model.pojo.PanelLivePojo;
import z.xtreamiptv.playerv3.model.pojo.PanelMoviePojo;
import z.xtreamiptv.playerv3.presenter.LoginPresenter;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.presenter.XtreamPanelAPIPresenter;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.database.SeriesStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.presenter.PlayerApiPresenter;
import z.xtreamiptv.playerv3.v2api.view.interfaces.PlayerApiInterface;
import z.xtreamiptv.playerv3.view.interfaces.LoginInterface;
import z.xtreamiptv.playerv3.view.interfaces.XMLTVInterface;
import z.xtreamiptv.playerv3.view.interfaces.XtreamPanelAPIInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends AppCompatActivity implements LoginInterface, XtreamPanelAPIInterface, XMLTVInterface, OnNavigationItemSelectedListener, OnClickListener, PlayerApiInterface {
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
    @BindView(R.id.iv_livetv_forward_arrow)
    ImageView ivLivetvForwardArrow;
    @BindView(R.id.iv_livetv_icon)
    ImageView ivLivetvIcon;
    @BindView(R.id.iv_vod_forward_arrow)
    ImageView ivVodForwardArrow;
    @BindView(R.id.iv_vod_icon)
    ImageView ivVodIcon;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private LoginPresenter loginPresenter;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private PlayerApiPresenter playerApiPresenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private ProgressDialog progressDialog;
    @BindView(R.id.rl_import_process)
    RelativeLayout rlImportProcess;
    @BindView(R.id.rl_livetv)
    RelativeLayout rlLivetv;
    @BindView(R.id.rl_vod)
    RelativeLayout rlVod;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_countings)
    TextView tvCountings;
    @BindView(R.id.tv_divider)
    ImageView tvDivider;
    @BindView(R.id.tv_epg_count)
    TextView tvEPGCount;
    private Editor tvGuidesetUpEditor;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_importing_streams)
    TextView tvImportingStreams;
    @BindView(R.id.tv_live_count)
    TextView tvLiveCount;
    @BindView(R.id.tv_livetv)
    TextView tvLivetv;
    @BindView(R.id.tv_percentage)
    TextView tvPercentage;
    @BindView(R.id.tv_series_count)
    TextView tvSeriesCount;
    @BindView(R.id.tv_vod)
    TextView tvVod;
    @BindView(R.id.tv_vod_count)
    TextView tvVodCount;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmlTvPresenter;
    private XtreamPanelAPIPresenter xtreamPanelAPIPresenter;

    class C17541 implements DialogInterface.OnClickListener {
        C17541() {
        }

        public void onClick(DialogInterface dialog, int which) {
            DashboardActivity.this.loadChannelsAndVod();
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    class AnonymousClass1AllChannelsAndVodAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$finalTotalLiveAndVod;
        Context mcontext = null;
         /* synthetic */ Map val$availableChanelsList;
         /* synthetic */ int val$finalTotalLiveAndVod;

        AnonymousClass1AllChannelsAndVodAsyncTask(Context context, int i, Map map) {
            this.val$finalTotalLiveAndVod = i;
            this.val$availableChanelsList = map;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                if (DashboardActivity.this.liveStreamDBHandler != null) {
                    DashboardActivity.this.liveStreamDBHandler.makeEmptyChanelsRecord();
                }
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            try {
                if (DashboardActivity.this.liveStreamDBHandler != null) {
                    DashboardActivity.this.liveStreamDBHandler.addAllAvailableChannel(this.val$availableChanelsList);
                }
            } catch (IllegalStateException e2) {
                Log.e("Exception", "IllegalState Exception:", e2);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            DashboardActivity.this.currentDate = DashboardActivity.this.currentDateValue();
            if (!(DashboardActivity.this.currentDate == null || DashboardActivity.this.liveStreamDBHandler == null)) {
                try {
                    DashboardActivity.this.liveStreamDBHandler.updateDBStatus(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_FINISH);
                } catch (IllegalStateException e) {
                    Log.e("Exception", "IllegalState Exception:", e);
                }
            }
            if (DashboardActivity.this.rlImportProcess != null) {
                DashboardActivity.this.rlImportProcess.setVisibility(8);
            }
            if (!(DashboardActivity.this.liveStreamDBHandler == null || DashboardActivity.this.liveStreamDBHandler.getStreamsCount("live") <= 0 || DashboardActivity.this.tvLiveCount == null)) {
                DashboardActivity.this.tvLiveCount.setText(DashboardActivity.this.getResources().getString(R.string.total));
                DashboardActivity.this.tvLiveCount.append(" : " + DashboardActivity.this.liveStreamDBHandler.getStreamsCount("live"));
                DashboardActivity.this.tvLiveCount.setVisibility(0);
            }
            if (!(DashboardActivity.this.liveStreamDBHandler == null || DashboardActivity.this.liveStreamDBHandler.getStreamsCount("movie") <= 0 || DashboardActivity.this.tvVodCount == null)) {
                DashboardActivity.this.tvVodCount.setText(DashboardActivity.this.getResources().getString(R.string.total));
                DashboardActivity.this.tvVodCount.append(" : " + DashboardActivity.this.liveStreamDBHandler.getStreamsCount("movie"));
                DashboardActivity.this.tvVodCount.setVisibility(0);
            }
            if (DashboardActivity.this.getUserName() != null && !DashboardActivity.this.getUserName().isEmpty() && DashboardActivity.this.getUserPassword() != null && !DashboardActivity.this.getUserPassword().isEmpty()) {
                DashboardActivity.this.playerApiPresenter.getSeriesStreamCat(DashboardActivity.this.getUserName(), DashboardActivity.this.getUserPassword());
            }
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    class AnonymousClass1LiveAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$finalTotalLive;
        Context mcontext = null;
         /* synthetic */ Map val$availableChanelsList;
         /* synthetic */ int val$finalTotalLive;
         /* synthetic */ int val$finalTotalLiveAndVod;
         /* synthetic */ ArrayList val$liveList;

        AnonymousClass1LiveAsyncTask(Context context, int i, ArrayList arrayList, Map map, int i2) {
            this.val$finalTotalLive = i;
            this.val$liveList = arrayList;
            this.val$availableChanelsList = map;
            this.val$finalTotalLiveAndVod = i2;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                DashboardActivity.this.liveStreamDBHandler.makeEmptyLiveCategory();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            try {
                if (DashboardActivity.this.liveStreamDBHandler != null) {
                    DashboardActivity.this.liveStreamDBHandler.addLiveCategories(this.val$liveList);
                }
            } catch (IllegalStateException e2) {
                Log.e("Exception", "IllegalState Exception:", e2);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            if (DashboardActivity.this.context != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (VERSION.SDK_INT >= 11) {
                    new AnonymousClass1AllChannelsAndVodAsyncTask(DashboardActivity.this.context, this.val$finalTotalLiveAndVod, this.val$availableChanelsList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                } else {
                    new AnonymousClass1AllChannelsAndVodAsyncTask(DashboardActivity.this.context, this.val$finalTotalLiveAndVod, this.val$availableChanelsList).execute(new String[0]);
                }
            }
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    class AnonymousClass1NewAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$xmltvCallback.programmePojos.size();
        Context mcontext = null;
         /* synthetic */ int val$totalEPGSize;
         /* synthetic */ XMLTVCallback val$xmltvCallback;

        AnonymousClass1NewAsyncTask(Context context, XMLTVCallback xMLTVCallback, int i) {
            this.val$xmltvCallback = xMLTVCallback;
            this.val$totalEPGSize = i;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                if (DashboardActivity.this.liveStreamDBHandler != null) {
                    DashboardActivity.this.liveStreamDBHandler.addEPG(this.val$xmltvCallback.programmePojos);
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
            if (DashboardActivity.this.rlImportProcess != null) {
                DashboardActivity.this.rlImportProcess.setVisibility(8);
            }
            DashboardActivity.this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
            if (DashboardActivity.this.context != null) {
                Utils.showToast(DashboardActivity.this.context, DashboardActivity.this.getResources().getString(R.string.update_tv_guide_success) + " (" + this.val$totalEPGSize + ")");
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
                if (DashboardActivity.this.seriesStreamsDatabaseHandler != null) {
                    DashboardActivity.this.seriesStreamsDatabaseHandler.addAllSeriesStreams((ArrayList) this.val$getSeriesStreamCallback);
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
            String currentDate = DashboardActivity.this.currentDateValue();
            if (!(currentDate == null || DashboardActivity.this.seriesStreamsDatabaseHandler == null)) {
                DashboardActivity.this.seriesStreamsDatabaseHandler.updateseriesStreamsDBStatusAndDate(AppConst.DB_SERIES_STREAMS, AppConst.DB_SERIES_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            if (!(DashboardActivity.this.seriesStreamsDatabaseHandler == null || DashboardActivity.this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount() <= 0 || DashboardActivity.this.tvSeriesCount == null)) {
                DashboardActivity.this.tvSeriesCount.setText(DashboardActivity.this.getResources().getString(R.string.total));
                DashboardActivity.this.tvSeriesCount.append(" : " + DashboardActivity.this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount());
                DashboardActivity.this.tvSeriesCount.setVisibility(0);
            }
            if (DashboardActivity.this.context != null) {
                Utils.showToast(DashboardActivity.this.context, DashboardActivity.this.getResources().getString(R.string.update_livestreams_vod_series_success));
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
                if (DashboardActivity.this.seriesStreamsDatabaseHandler != null) {
                    DashboardActivity.this.seriesStreamsDatabaseHandler.deleteAndRecreateAllVSeriesTables();
                }
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            try {
                if (DashboardActivity.this.seriesStreamsDatabaseHandler != null) {
                    DashboardActivity.this.seriesStreamsDatabaseHandler.addSeriesCategories((ArrayList) this.val$getSeriesStreamCategoriesCallback);
                }
            } catch (IllegalStateException e2) {
                Log.e("Exception", "IllegalState Exception:", e2);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = DashboardActivity.this.currentDateValue();
            if (!(currentDate == null || DashboardActivity.this.seriesStreamsDatabaseHandler == null)) {
                DashboardActivity.this.seriesStreamsDatabaseHandler.updateSeriesStreamsCatDBStatusAndDate(AppConst.DB_SERIES_STREAMS_CAT, AppConst.DB_SERIES_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            DashboardActivity.this.loginPreferencesAfterLogin = DashboardActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            DashboardActivity.this.playerApiPresenter.getSeriesStream(DashboardActivity.this.loginPreferencesAfterLogin.getString("username", ""), DashboardActivity.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    class AnonymousClass1VodAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$finalTotalVod;
        Context mcontext = null;
         /* synthetic */ Map val$availableChanelsList;
         /* synthetic */ int val$finalTotalLive;
         /* synthetic */ int val$finalTotalLiveAndVod;
         /* synthetic */ int val$finalTotalVod;
         /* synthetic */ ArrayList val$liveList;
         /* synthetic */ ArrayList val$movieList;

        AnonymousClass1VodAsyncTask(Context context, int i, ArrayList arrayList, int i2, Map map, ArrayList arrayList2, int i3) {
            this.val$finalTotalVod = i;
            this.val$movieList = arrayList;
            this.val$finalTotalLiveAndVod = i2;
            this.val$availableChanelsList = map;
            this.val$liveList = arrayList2;
            this.val$finalTotalLive = i3;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            try {
                DashboardActivity.this.liveStreamDBHandler.makeEmptyMovieCategory();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            try {
                if (DashboardActivity.this.liveStreamDBHandler != null) {
                    DashboardActivity.this.liveStreamDBHandler.addMovieCategories(this.val$movieList);
                }
            } catch (IllegalStateException e2) {
                Log.e("Exception", "IllegalState Exception:", e2);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            if (DashboardActivity.this.context != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (VERSION.SDK_INT >= 11) {
                    new AnonymousClass1LiveAsyncTask(DashboardActivity.this.context, this.val$finalTotalLive, this.val$liveList, this.val$availableChanelsList, this.val$finalTotalLiveAndVod).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                } else {
                    new AnonymousClass1LiveAsyncTask(DashboardActivity.this.context, this.val$finalTotalLive, this.val$liveList, this.val$availableChanelsList, this.val$finalTotalLiveAndVod).execute(new String[0]);
                }
            }
        }
    }

    class C17552 implements DialogInterface.OnClickListener {
        C17552() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C17563 implements DialogInterface.OnClickListener {
        C17563() {
        }

        public void onClick(DialogInterface dialog, int which) {
            DashboardActivity.this.loadTvGuid();
        }
    }

    class C17574 implements DialogInterface.OnClickListener {
        C17574() {
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
        launchTVGuideFromDifferentActivity();
        updateChannelsandEPG(this.context);
        lsitGridViewDefaultSetup();
    }

    private void lsitGridViewDefaultSetup() {
        Editor editor = getSharedPreferences(AppConst.LIST_GRID_VIEW, 0).edit();
        editor.putInt(AppConst.LIVE_STREAM, 0);
        editor.putInt(AppConst.VOD, 0);
    }

    private void setUpTVGuideStatus(Context context) {
        this.loginPreferencesAfterLogin = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (!this.loginPreferencesAfterLogin.getBoolean("firstTime", false)) {
            addDatabaseStatusOnSetup();
            this.tvGuidesetUpEditor = this.loginPreferencesAfterLogin.edit();
            this.tvGuidesetUpEditor.putBoolean("firstTime", true);
            this.tvGuidesetUpEditor.commit();
        }
    }

    private void addDatabaseStatusOnSetup() {
        String currentDate = "";
        int count = -1;
        currentDate = currentDateValue();
        if (this.liveStreamDBHandler != null) {
            try {
                count = this.liveStreamDBHandler.getDBStatusCount();
            } catch (IllegalStateException e) {
                Log.e("Exception", "IllegalState Exception:", e);
            }
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addDBStatus(this.liveStreamDBHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addDBStatus(LiveStreamDBHandler liveStreamDBHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_EPG);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_EPG_ID);
        liveStreamDBHandler.addDBUpdatedStatus(updatedStatusDBModel);
    }

    private void intialize(Context context) {
        if (context != null) {
            this.liveStreamDBHandler = new LiveStreamDBHandler(context);
            this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(context);
            if (this.liveStreamDBHandler.getStreamsCount("live") > 0 && this.tvLiveCount != null) {
                this.tvLiveCount.append(" : " + this.liveStreamDBHandler.getStreamsCount("live"));
                this.tvLiveCount.setVisibility(0);
            }
            if (this.liveStreamDBHandler.getStreamsCount("movie") > 0 && this.tvVodCount != null) {
                this.tvVodCount.append(" : " + this.liveStreamDBHandler.getStreamsCount("movie"));
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
            this.xtreamPanelAPIPresenter = new XtreamPanelAPIPresenter(this, context);
            this.xmlTvPresenter = new XMLTVPresenter(this, context);
            this.playerApiPresenter = new PlayerApiPresenter(context, this);
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
            if (context != null && this.liveStreamDBHandler != null && this.rlImportProcess != null) {
                this.rlImportProcess.setVisibility(8);
                this.loginPreferencesAfterLogin = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
                String username = this.loginPreferencesAfterLogin.getString("username", "");
                String password = this.loginPreferencesAfterLogin.getString("password", "");
                if (this.liveStreamDBHandler.getDBStatusCount() != 0 && this.liveStreamDBHandler != null && this.rlImportProcess != null && this.xtreamPanelAPIPresenter != null && this.xmlTvPresenter != null) {
                    String lastUpdatedStatusdate = "";
                    String status = "";
                    this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
                    if (this.databaseUpdatedStatusDBModelLive != null) {
                        status = this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState();
                        lastUpdatedStatusdate = this.databaseUpdatedStatusDBModelLive.getDbLastUpdatedDate();
                    }
                    if (getDateDiff(simpleDateFormat, lastUpdatedStatusdate, this.currentDate) >= 1) {
                        this.rlImportProcess.setVisibility(0);
                        if (this.tvImportingStreams != null) {
                            this.tvImportingStreams.setText(getResources().getString(R.string.refreshing_all_channels_series));
                        }
                        if ((!username.equals("") && !password.equals("") && !status.equals("") && !status.isEmpty() && status.equals(AppConst.DB_UPDATED_STATUS_FINISH)) || (!username.equals("") && !password.equals("") && status != null && !status.equals("") && !status.isEmpty() && status.equals(AppConst.DB_UPDATED_STATUS_FAILED))) {
                            this.liveStreamDBHandler.updateDBStatusAndDate(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_PROCESSING, this.currentDate);
                            this.xtreamPanelAPIPresenter.panelAPI(username, password);
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
        this.toolbar.inflateMenu(R.menu.menu_text_icon);
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
            alertDialog.setPositiveButton("YES", new C17541());
            alertDialog.setNegativeButton("NO", new C17552());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C17563());
            alertDialog.setNegativeButton("NO", new C17574());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadChannelsAndVod() {
        if (this.context == null) {
            return;
        }
        if (getChannelVODUpdateStatus()) {
            LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
            liveStreamDBHandler.makeEmptyAllChannelsVODTablesRecords();
            seriesStreamsDatabaseHandler.emptySeriesStreamCatandSeriesStreamRecords();
            startActivity(new Intent(this.context, ImportStreamsActivity.class));
        } else if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.upadating_channels_vod));
        }
    }

    private boolean getChannelVODUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        if (this.databaseUpdatedStatusDBModelLive == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null) {
            return true;
        }
        if (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            return true;
        }
        return false;
    }

    private void loadTvGuid() {
        if (this.context == null) {
            return;
        }
        if (getEPGUpdateStatus()) {
            SharedPreferences loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            Editor loginPrefsEditor = loginPreferencesAfterLogin.edit();
            if (loginPrefsEditor != null) {
                loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "autoLoad");
                loginPrefsEditor.commit();
                String skipButton = loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
                new LiveStreamDBHandler(this.context).makeEmptyEPG();
                startActivity(new Intent(this.context, ImportEPGActivity.class));
            }
        } else if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.upadating_tv_guide));
        }
    }

    private boolean getEPGUpdateStatus() {
        intialize(this.context);
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            return true;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals("")) {
            return true;
        }
        return false;
    }

    private boolean getChannelEPGUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null) {
            return true;
        }
        if ((this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) || ((this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) || ((this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) || (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH))))) {
            return true;
        }
        return false;
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
            this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
            if (this.databaseUpdatedStatusDBModelLive == null || !this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) {
                Utils.showToast(this.context, getResources().getString(R.string.udpating_channels_please_wait));
            } else {
                launchTvGuide();
            }
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
                this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
                if (this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null || !this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) {
                    Utils.showToast(this.context, getResources().getString(R.string.udpating_channels_please_wait));
                    return;
                } else {
                    launchTvGuide();
                    return;
                }
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
        } else if (dateDifference >= 0 && dateDifference <= 2) {
            startTvGuideActivity();
        } else if (dateDifference > 2) {
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
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        if (loginCallback != null && loginCallback.getUserLoginInfo().getAuth().intValue() == 1 && !loginCallback.getUserLoginInfo().getStatus().equals("Active") && this.context != null) {
            Utils.logoutUser(this.context);
        }
    }

    public void stopLoader() {
    }

    public void atStart() {
        if (this.progressDialog != null) {
            this.progressDialog.show();
        }
    }

    public void onFinish() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
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

    public void panelApiFailed(String updateDBFailed) {
        if (updateDBFailed.equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_FAILED);
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

    public void panelAPI(XtreamPanelAPICallback xtreamPanelAPICallback, String username) {
        if (xtreamPanelAPICallback != null && this.context != null && xtreamPanelAPICallback.getCategories() != null && xtreamPanelAPICallback.getAvailableChannels() != null) {
            if (this.rlImportProcess != null) {
                this.rlImportProcess.setVisibility(0);
            }
            ArrayList<PanelMoviePojo> movieList = xtreamPanelAPICallback.getCategories().getMovie();
            ArrayList<PanelLivePojo> liveList = xtreamPanelAPICallback.getCategories().getLive();
            Map<String, PanelAvailableChannelsPojo> availableChanelsList = xtreamPanelAPICallback.getAvailableChannels();
            int totalVod = 0;
            int totalLive = 0;
            int totalLiveAndVod = 0;
            if (movieList != null) {
                totalVod = movieList.size();
            }
            if (liveList != null) {
                totalLive = liveList.size();
            }
            if (availableChanelsList != null) {
                totalLiveAndVod = availableChanelsList.size();
            }
            LiveStreamCategoryIdDBModel movieCategoryIdDBModel = new LiveStreamCategoryIdDBModel(null, null, 0);
            LiveStreamCategoryIdDBModel liveStreamCategoryIdDBModel = new LiveStreamCategoryIdDBModel(null, null, 0);
            LiveStreamsDBModel availableChannel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
            int finalTotalLiveAndVod = totalLiveAndVod;
            int finalTotalLive = totalLive;
            int finalTotalVod = totalVod;
            if (finalTotalVod != 0) {
                if (VERSION.SDK_INT >= 11) {
                    new AnonymousClass1VodAsyncTask(this.context, finalTotalVod, movieList, finalTotalLiveAndVod, availableChanelsList, liveList, finalTotalLive).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                } else {
                    new AnonymousClass1VodAsyncTask(this.context, finalTotalVod, movieList, finalTotalLiveAndVod, availableChanelsList, liveList, finalTotalLive).execute(new String[0]);
                }
            } else if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1LiveAsyncTask(this.context, finalTotalLive, liveList, availableChanelsList, finalTotalLiveAndVod).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1LiveAsyncTask(this.context, finalTotalLive, liveList, availableChanelsList, finalTotalLiveAndVod).execute(new String[0]);
            }
        }
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
        if (!(this.seriesStreamsDatabaseHandler == null || this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount() <= 0 || this.tvSeriesCount == null)) {
            this.tvSeriesCount.setText(getResources().getString(R.string.total));
            this.tvSeriesCount.append(" : " + this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount());
            this.tvSeriesCount.setVisibility(0);
        }
        if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.update_livestreams_vod_series_success));
        }
    }

    public void getLiveStreamFailed(String message) {
    }

    public void getVODStreamsFailed(String message) {
    }

    public void getSeriesStreamsFailed(String message) {
        if (!(this.seriesStreamsDatabaseHandler == null || this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount() <= 0 || this.tvSeriesCount == null)) {
            this.tvSeriesCount.setText(getResources().getString(R.string.total));
            this.tvSeriesCount.append(" : " + this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount());
            this.tvSeriesCount.setVisibility(0);
        }
        if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.update_livestreams_vod_series_success));
        }
    }
}
