package z.xtreamiptv.playerv3.v2api.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import z.xtreamiptv.playerv3.view.utility.LoadingGearSpinner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImportStreamsActivity extends AppCompatActivity implements PlayerApiInterface {
    Context context;
    @BindView(R.id.iv_gear_loader)
    LoadingGearSpinner ivGearLoader;
    LiveStreamDBHandler liveStreamDBHandler;
    LiveStreamsDatabaseHandler liveStreamsDatabaseHandler;
    private SharedPreferences loginPrefXtreamVersion;
    private Editor loginPrefXtreamVersionEditor;
    private SharedPreferences loginPreferencesAfterLogin;
    private PlayerApiPresenter playerApiPresenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rl_import_layout)
    RelativeLayout rlImportLayout;
    @BindView(R.id.rl_import_process)
    RelativeLayout rlImportProcess;
    SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    @BindView(R.id.tv_countings)
    TextView tvCountings;
    @BindView(R.id.tv_importing_streams)
    TextView tvImportingStreams;
    @BindView(R.id.tv_percentage)
    TextView tvPercentage;
    @BindView(R.id.tv_settings)
    TextView tvSettingStreams;
    VODStreamsDatabaseHandler vodStreamsDatabaseHandler;
    private XMLTVPresenter xmlTvPresenter;
    private XtreamPanelAPIPresenter xtreamPanelAPIPresenter;

    class AnonymousClass1LiveStream extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getLiveStreamCallback;

        AnonymousClass1LiveStream(Context context, List list) {
            this.val$getLiveStreamCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportStreamsActivity.this.liveStreamsDatabaseHandler != null) {
                ImportStreamsActivity.this.liveStreamsDatabaseHandler.addAllliveStreams((ArrayList) this.val$getLiveStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.liveStreamsDatabaseHandler == null)) {
                ImportStreamsActivity.this.liveStreamsDatabaseHandler.updateLiveStreamsDBStatusAndDate(AppConst.DB_LIVE_STREAMS, AppConst.DB_LIVE_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.loginPreferencesAfterLogin = ImportStreamsActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            ImportStreamsActivity.this.playerApiPresenter.getVODStreamCat(ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("username", ""), ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("password", ""));
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
            if (ImportStreamsActivity.this.liveStreamsDatabaseHandler != null) {
                ImportStreamsActivity.this.liveStreamsDatabaseHandler.addLiveCategories((ArrayList) this.val$getLiveStreamCategoriesCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.liveStreamsDatabaseHandler == null)) {
                ImportStreamsActivity.this.liveStreamsDatabaseHandler.updateLiveStreamsCatDBStatusAndDate(AppConst.DB_LIVE_STREAMS_CAT, AppConst.DB_LIVE_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.loginPreferencesAfterLogin = ImportStreamsActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            ImportStreamsActivity.this.playerApiPresenter.getLiveStreams(ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("username", ""), ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("password", ""));
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
            if (ImportStreamsActivity.this.context != null) {
                ImportStreamsActivity.this.startActivity(new Intent(ImportStreamsActivity.this.context, DashboardActivityV2.class));
                ImportStreamsActivity.this.finish();
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

    class AnonymousClass1VODStream extends AsyncTask<String, Integer, Boolean> {
        Context mcontext = null;
        final /* synthetic */ List val$getVODStreamCallback;

        AnonymousClass1VODStream(Context context, List list) {
            this.val$getVODStreamCallback = list;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportStreamsActivity.this.vodStreamsDatabaseHandler != null) {
                ImportStreamsActivity.this.vodStreamsDatabaseHandler.addAllVODStreams((ArrayList) this.val$getVODStreamCallback);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.vodStreamsDatabaseHandler == null)) {
                ImportStreamsActivity.this.vodStreamsDatabaseHandler.updateVODStreamsDBStatusAndDate(AppConst.DB_VOD_STREAMS, AppConst.DB_VOD_STREAMS_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.loginPreferencesAfterLogin = ImportStreamsActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            ImportStreamsActivity.this.playerApiPresenter.getSeriesStreamCat(ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("username", ""), ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("password", ""));
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
            if (ImportStreamsActivity.this.vodStreamsDatabaseHandler != null) {
                ImportStreamsActivity.this.vodStreamsDatabaseHandler.addVODCategories((ArrayList) this.val$getVODStreamCategoriesCallback);
            }
            int count2 = ImportStreamsActivity.this.vodStreamsDatabaseHandler.getAllVODCountCount();
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            String currentDate = ImportStreamsActivity.this.currentDateValue();
            if (!(currentDate == null || ImportStreamsActivity.this.vodStreamsDatabaseHandler == null)) {
                ImportStreamsActivity.this.vodStreamsDatabaseHandler.updateVODStreamsCatDBStatusAndDate(AppConst.DB_VOD_STREAMS_CAT, AppConst.DB_VOD_STREAMS_CAT_ID, AppConst.DB_UPDATED_STATUS_FINISH, currentDate);
            }
            ImportStreamsActivity.this.loginPreferencesAfterLogin = ImportStreamsActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            ImportStreamsActivity.this.playerApiPresenter.getVODStream(ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("username", ""), ImportStreamsActivity.this.loginPreferencesAfterLogin.getString("password", ""));
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_import_streams_new_v2api);
        ButterKnife.bind(this);
        changeStatusBarColor();
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.liveStreamsDatabaseHandler = new LiveStreamsDatabaseHandler(this.context);
        this.vodStreamsDatabaseHandler = new VODStreamsDatabaseHandler(this.context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
        this.playerApiPresenter = new PlayerApiPresenter(this.context, this);
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
        addLiveStreamCatStatus(currentDate);
        addLiveStreamStatus(currentDate);
        addVODStreamCatStatus(currentDate);
        addVODStreamStatus(currentDate);
        addSeriesStreamCatStatus(currentDate);
        addSeriesStreamStatus(currentDate);
    }

    private void addLiveStreamCatStatus(String currentDate) {
        if (this.liveStreamsDatabaseHandler != null) {
            int count = this.liveStreamsDatabaseHandler.getLiveStreamsCatDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addLiveStreamCatDBStatus(this.liveStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addLiveStreamStatus(String currentDate) {
        if (this.liveStreamsDatabaseHandler != null) {
            int count = this.liveStreamsDatabaseHandler.getLiveStreamsDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addLiveStreamDBStatus(this.liveStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addVODStreamCatStatus(String currentDate) {
        if (this.vodStreamsDatabaseHandler != null) {
            int count = this.vodStreamsDatabaseHandler.getVODStreamsCatDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addVODStreamCatDBStatus(this.vodStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addVODStreamStatus(String currentDate) {
        if (this.vodStreamsDatabaseHandler != null) {
            int count = this.vodStreamsDatabaseHandler.getVODStreamsDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addVODStreamDBStatus(this.vodStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addSeriesStreamCatStatus(String currentDate) {
        if (this.seriesStreamsDatabaseHandler != null) {
            int count = this.seriesStreamsDatabaseHandler.getSeriesStreamsCatDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addSeriesStreamCatDBStatus(this.seriesStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addSeriesStreamStatus(String currentDate) {
        if (this.seriesStreamsDatabaseHandler != null) {
            int count = this.seriesStreamsDatabaseHandler.getSeriesStreamsDBStatusCount();
            if (count != -1 && count == 0) {
                if (currentDate != null) {
                    addSeriesStreamDBStatus(this.seriesStreamsDatabaseHandler, currentDate);
                } else {
                    Utils.showToast(this.context, "Invalid current date");
                }
            }
        }
    }

    private void addLiveStreamCatDBStatus(LiveStreamsDatabaseHandler liveStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_LIVE_STREAMS_CAT);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_LIVE_STREAMS_CAT_ID);
        liveStreamsDatabaseHandler.addLiveStreamsCatStatus(updatedStatusDBModel);
    }

    private void addLiveStreamDBStatus(LiveStreamsDatabaseHandler liveStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_LIVE_STREAMS);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_LIVE_STREAMS_ID);
        liveStreamsDatabaseHandler.addLiveStreamsCatStatus(updatedStatusDBModel);
    }

    private void addVODStreamCatDBStatus(VODStreamsDatabaseHandler vodStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_VOD_STREAMS_CAT);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_VOD_STREAMS_CAT_ID);
        vodStreamsDatabaseHandler.addLiveStreamsCatStatus(updatedStatusDBModel);
    }

    private void addVODStreamDBStatus(VODStreamsDatabaseHandler vodStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_VOD_STREAMS);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_VOD_STREAMS_ID);
        vodStreamsDatabaseHandler.addLiveStreamsCatStatus(updatedStatusDBModel);
    }

    private void addSeriesStreamCatDBStatus(SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_SERIES_STREAMS_CAT);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_SERIES_STREAMS_CAT_ID);
        seriesStreamsDatabaseHandler.addSeriesStreamsCatStatus(updatedStatusDBModel);
    }

    private void addSeriesStreamDBStatus(SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_SERIES_STREAMS);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_SERIES_STREAMS_ID);
        seriesStreamsDatabaseHandler.addSeriesStreamsCatStatus(updatedStatusDBModel);
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
            this.playerApiPresenter.getLiveStreamCat(username, password);
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
    }

    public void atProcess() {
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
        if (message != null && !message.isEmpty() && message.equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            if (this.liveStreamsDatabaseHandler != null) {
                this.liveStreamsDatabaseHandler.deleteAndRecreateAllLiveTables();
            }
            if (this.vodStreamsDatabaseHandler != null) {
                this.vodStreamsDatabaseHandler.deleteAndRecreateAllVODTables();
            }
            setXtreamVersion2_7();
            startActivity(new Intent(this, z.xtreamiptv.playerv3.view.activity.ImportStreamsActivity.class));
        }
    }

    private void setXtreamVersion2_7() {
        this.loginPrefXtreamVersion = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_XTREAM_VERSION, 0);
        this.loginPrefXtreamVersionEditor = this.loginPrefXtreamVersion.edit();
        this.loginPrefXtreamVersionEditor.putString(AppConst.XTREAM_SELCETED_VERSION_CODE, AppConst.XTREAM_2_7);
        this.loginPrefXtreamVersionEditor.commit();
    }

    public void getLiveStreamFailed(String message) {
    }

    public void getVODStreamsFailed(String message) {
    }

    public void getSeriesStreamsFailed(String message) {
    }
}
