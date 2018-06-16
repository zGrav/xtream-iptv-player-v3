package z.xtreamiptv.playerv3.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.callback.XtreamPanelAPICallback;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.pojo.PanelAvailableChannelsPojo;
import z.xtreamiptv.playerv3.model.pojo.PanelLivePojo;
import z.xtreamiptv.playerv3.model.pojo.PanelMoviePojo;
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
import z.xtreamiptv.playerv3.view.interfaces.XtreamPanelAPIInterface;
import z.xtreamiptv.playerv3.view.utility.LoadingGearSpinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ImportStreamsActivity extends AppCompatActivity implements XtreamPanelAPIInterface, PlayerApiInterface {
    Context context;
    @BindView(R.id.iv_gear_loader)
    LoadingGearSpinner ivGearLoader;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private PlayerApiPresenter playerApiPresenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rl_import_layout)
    RelativeLayout rlImportLayout;
    @BindView(R.id.rl_import_process)
    RelativeLayout rlImportProcess;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    @BindView(R.id.tv_countings)
    TextView tvCountings;
    @BindView(R.id.tv_importing_streams)
    TextView tvImportingStreams;
    @BindView(R.id.tv_percentage)
    TextView tvPercentage;
    @BindView(R.id.tv_setting_streams)
    TextView tvSettingStreams;
    private XMLTVPresenter xmlTvPresenter;
    private XtreamPanelAPIPresenter xtreamPanelAPIPresenter;

    class AnonymousClass1AllChannelsAndVodAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$finalTotalLiveAndVod;
        Context mcontext = null;
         /* synthetic */ Map val$finalAvailableChanelsList;
         /* synthetic */ int val$finalTotalLiveAndVod;

        AnonymousClass1AllChannelsAndVodAsyncTask(Context context, int i, Map map) {
            this.val$finalTotalLiveAndVod = i;
            this.val$finalAvailableChanelsList = map;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportStreamsActivity.this.liveStreamDBHandler != null) {
                ImportStreamsActivity.this.liveStreamDBHandler.addAllAvailableChannel(this.val$finalAvailableChanelsList);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.liveStreamDBHandler == null)) {
                ImportStreamsActivity.this.liveStreamDBHandler.updateDBStatusAndDate(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.loginPreferencesAfterLogin = ImportStreamsActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("username", "");
            String password = ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("password", "");
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                ImportStreamsActivity.this.playerApiPresenter.getSeriesStreamCat(username, password);
            }
        }
    }

    class AnonymousClass1LiveAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$finalTotalLive;
        Context mcontext = null;
         /* synthetic */ Map val$finalAvailableChanelsList;
         /* synthetic */ ArrayList val$finalLiveList;
         /* synthetic */ int val$finalTotalLive;
         /* synthetic */ int val$finalTotalLiveAndVod;

        AnonymousClass1LiveAsyncTask(Context context, int i, ArrayList arrayList, Map map, int i2) {
            this.val$finalTotalLive = i;
            this.val$finalLiveList = arrayList;
            this.val$finalAvailableChanelsList = map;
            this.val$finalTotalLiveAndVod = i2;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportStreamsActivity.this.liveStreamDBHandler != null) {
                ImportStreamsActivity.this.liveStreamDBHandler.addLiveCategories(this.val$finalLiveList);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            if (ImportStreamsActivity.this.context == null) {
                return;
            }
            if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1AllChannelsAndVodAsyncTask(ImportStreamsActivity.this.context, this.val$finalTotalLiveAndVod, this.val$finalAvailableChanelsList).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1AllChannelsAndVodAsyncTask(ImportStreamsActivity.this.context, this.val$finalTotalLiveAndVod, this.val$finalAvailableChanelsList).execute(new String[0]);
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
            if (ImportStreamsActivity.this.seriesStreamsDatabaseHandler != null) {
                ImportStreamsActivity.this.seriesStreamsDatabaseHandler.addAllSeriesStreams((ArrayList) this.val$getSeriesStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.seriesStreamsDatabaseHandler == null)) {
                ImportStreamsActivity.this.seriesStreamsDatabaseHandler.updateseriesStreamsDBStatusAndDate(AppConst.DB_SERIES_STREAMS, AppConst.DB_SERIES_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.startDashboardActvity();
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
            if (ImportStreamsActivity.this.seriesStreamsDatabaseHandler != null) {
                ImportStreamsActivity.this.seriesStreamsDatabaseHandler.addSeriesCategories((ArrayList) this.val$getSeriesStreamCategoriesCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.seriesStreamsDatabaseHandler == null)) {
                ImportStreamsActivity.this.seriesStreamsDatabaseHandler.updateSeriesStreamsCatDBStatusAndDate(AppConst.DB_SERIES_STREAMS_CAT, AppConst.DB_SERIES_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.loginPreferencesAfterLogin = ImportStreamsActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            ImportStreamsActivity.this.playerApiPresenter.getSeriesStream(ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("username", ""), ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    class AnonymousClass1VodAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$finalTotalVod;
        Context mcontext = null;
         /* synthetic */ Map val$finalAvailableChanelsList;
         /* synthetic */ ArrayList val$finalLiveList;
         /* synthetic */ ArrayList val$finalMovieList;
         /* synthetic */ int val$finalTotalLive;
         /* synthetic */ int val$finalTotalLiveAndVod;
         /* synthetic */ int val$finalTotalVod;

        AnonymousClass1VodAsyncTask(Context context, int i, ArrayList arrayList, int i2, Map map, ArrayList arrayList2, int i3) {
            this.val$finalTotalVod = i;
            this.val$finalMovieList = arrayList;
            this.val$finalTotalLiveAndVod = i2;
            this.val$finalAvailableChanelsList = map;
            this.val$finalLiveList = arrayList2;
            this.val$finalTotalLive = i3;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportStreamsActivity.this.liveStreamDBHandler != null) {
                ImportStreamsActivity.this.liveStreamDBHandler.addMovieCategories(this.val$finalMovieList);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            if (ImportStreamsActivity.this.context == null) {
                return;
            }
            if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1LiveAsyncTask(ImportStreamsActivity.this.context, this.val$finalTotalLive, this.val$finalLiveList, this.val$finalAvailableChanelsList, this.val$finalTotalLiveAndVod).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1LiveAsyncTask(ImportStreamsActivity.this.context, this.val$finalTotalLive, this.val$finalLiveList, this.val$finalAvailableChanelsList, this.val$finalTotalLiveAndVod).execute(new String[0]);
            }
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_import_streams_new);
        ButterKnife.bind(this);
        changeStatusBarColor();
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
        this.playerApiPresenter = new PlayerApiPresenter(this.context, this);
        this.xtreamPanelAPIPresenter = new XtreamPanelAPIPresenter(this, this.context);
        initialize();
    }

    public void onResume() {
        super.onResume();
    }

    private void initialize() {
        if (this.context != null) {
            this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesAfterLogin.getString("username", "");
            String password = this.loginPreferencesAfterLogin.getString("password", "");
            addDatabaseStatusOnSetup();
            getChannelsCategories(this.context, this.liveStreamDBHandler, username, password);
        }
    }

    private void addDatabaseStatusOnSetup() {
        String currentDate = "";
        currentDate = currentDateValue();
        if (this.liveStreamDBHandler != null) {
            int count = this.liveStreamDBHandler.getDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addDBStatus(this.liveStreamDBHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    private void addDBStatus(LiveStreamDBHandler liveStreamDBHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_CHANNELS);
        updatedStatusDBModel.setDbCategoryID("1");
        liveStreamDBHandler.addDBUpdatedStatus(updatedStatusDBModel);
    }

    private void getChannelsCategories(Context context, LiveStreamDBHandler liveStreamDBHandler, String username, String password) {
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty() && !username.equals("") && !password.equals("")) {
            liveStreamDBHandler.updateDBStatus(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_PROCESSING);
            this.xtreamPanelAPIPresenter.panelAPI(username, password);
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

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String errorMessage) {
        Utils.showToast(this.context, getResources().getString(R.string.network_error));
    }

    public void atProcess() {
    }

    public void panelAPI(XtreamPanelAPICallback xtreamPanelAPICallback, String username) {
        if (xtreamPanelAPICallback != null && this.context != null) {
            ArrayList<PanelMoviePojo> movieList = new ArrayList();
            ArrayList<PanelLivePojo> liveList = new ArrayList();
            Map<String, PanelAvailableChannelsPojo> availableChanelsList = null;
            if (xtreamPanelAPICallback.getCategories() != null) {
                movieList = xtreamPanelAPICallback.getCategories().getMovie();
                liveList = xtreamPanelAPICallback.getCategories().getLive();
            }
            if (xtreamPanelAPICallback.getAvailableChannels() != null) {
                availableChanelsList = xtreamPanelAPICallback.getAvailableChannels();
            }
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
            Map<String, PanelAvailableChannelsPojo> finalAvailableChanelsList = availableChanelsList;
            int finalTotalLive = totalLive;
            ArrayList<PanelLivePojo> finalLiveList = liveList;
            int finalTotalVod = totalVod;
            ArrayList<PanelMoviePojo> finalMovieList = movieList;
            if (finalTotalVod != 0) {
                if (VERSION.SDK_INT >= 11) {
                    new AnonymousClass1VodAsyncTask(this.context, finalTotalVod, finalMovieList, finalTotalLiveAndVod, finalAvailableChanelsList, finalLiveList, finalTotalLive).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                } else {
                    new AnonymousClass1VodAsyncTask(this.context, finalTotalVod, finalMovieList, finalTotalLiveAndVod, finalAvailableChanelsList, finalLiveList, finalTotalLive).execute(new String[0]);
                }
            } else if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1LiveAsyncTask(this.context, finalTotalLive, finalLiveList, finalAvailableChanelsList, finalTotalLiveAndVod).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1LiveAsyncTask(this.context, finalTotalLive, finalLiveList, finalAvailableChanelsList, finalTotalLiveAndVod).execute(new String[0]);
            }
        }
    }

    private void startDashboardActvity() {
        if (this.context != null) {
            startActivity(new Intent(this.context, DashboardActivity.class));
            finish();
        }
    }

    public void panelApiFailed(String updateDBFailed) {
        if (updateDBFailed.equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_CHANNELS, "1", AppConst.DB_UPDATED_STATUS_FAILED);
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
        startDashboardActvity();
    }

    public void getLiveStreamFailed(String message) {
    }

    public void getVODStreamsFailed(String message) {
    }

    public void getSeriesStreamsFailed(String message) {
        startDashboardActvity();
    }
}
