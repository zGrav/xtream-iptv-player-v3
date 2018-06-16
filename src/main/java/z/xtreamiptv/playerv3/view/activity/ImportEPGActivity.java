package z.xtreamiptv.playerv3.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.view.interfaces.XMLTVInterface;
import z.xtreamiptv.playerv3.view.utility.LoadingGearSpinner;
import java.util.Calendar;

public class ImportEPGActivity extends AppCompatActivity implements XMLTVInterface {
    Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.iv_gear_loader)
    LoadingGearSpinner ivGearLoader;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private Editor loginPrefsEditor;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rl_import_layout)
    RelativeLayout rlImportLayout;
    @BindView(R.id.rl_import_process)
    RelativeLayout rlImportProcess;
    @BindView(R.id.rl_skip)
    RelativeLayout rlSkip;
    @BindView(R.id.tv_countings)
    TextView tvCountings;
    @BindView(R.id.tv_importing_epg)
    TextView tvImportingEpg;
    @BindView(R.id.tv_percentage)
    TextView tvPercentage;
    @BindView(R.id.tv_setting_streams)
    TextView tvSettingStreams;
    private XMLTVPresenter xmlTvPresenter;

    class AnonymousClass1NewAsyncTask extends AsyncTask<String, Integer, Boolean> {
        //int ITERATIONS = this.val$xmltvCallback.programmePojos.size();
        Context mcontext = null;
        private volatile boolean running = true;
        private XMLTVCallback val$xmltvCallback = null;

        AnonymousClass1NewAsyncTask(Context context, XMLTVCallback xMLTVCallback) {
            this.val$xmltvCallback = xMLTVCallback;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (ImportEPGActivity.this.liveStreamDBHandler != null) {
                ImportEPGActivity.this.liveStreamDBHandler.addEPG(this.val$xmltvCallback.programmePojos);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            int totalEPGFound = this.val$xmltvCallback.programmePojos.size();
            ImportEPGActivity.this.loginPreferencesAfterLogin = ImportEPGActivity.this.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String skipButton = ImportEPGActivity.this.loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
            Utils.showToast(ImportEPGActivity.this.context, ImportEPGActivity.this.getResources().getString(R.string.epg_imported) + " (" + totalEPGFound + ")");
            if (ImportEPGActivity.this.liveStreamDBHandler != null) {
                ImportEPGActivity.this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
            }
            if (!skipButton.equals("pressed") && ImportEPGActivity.this.context != null) {
                ImportEPGActivity.this.startActivity(new Intent(ImportEPGActivity.this.context, NewEPGActivity.class));
                ImportEPGActivity.this.finish();
            }
        }

        protected void onCancelled() {
            this.running = false;
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_import_epg_new);
        ButterKnife.bind(this);
        changeStatusBarColor();
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        initialize();
    }

    private void initialize() {
        if (this.context != null) {
            this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            String username = this.loginPreferencesAfterLogin.getString("username", "");
            String password = this.loginPreferencesAfterLogin.getString("password", "");
            int epgCounts = this.liveStreamDBHandler.getEPGCount();
            this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
            String status = "";
            if (this.databaseUpdatedStatusDBModelEPG != null) {
                addDatabaseStatusOnSetup(this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState());
            }
            if (epgCounts == 0) {
                this.xmlTvPresenter = new XMLTVPresenter(this, this.context);
                this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_PROCESSING);
                this.xmlTvPresenter.epgXMLTV(username, password);
                return;
            }
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }

    private void addDatabaseStatusOnSetup(String status) {
        String str = "";
        str = currentDateValue();
        if (status != null && !status.equals("")) {
            return;
        }
        if (str != null) {
            addDBStatus(this.liveStreamDBHandler, str);
        } else {
            Utils.showToast(this.context, "Invalid current date");
        }
    }

    private String currentDateValue() {
        return Utils.parseDateToddMMyyyy(Calendar.getInstance().getTime().toString());
    }

    private void addDBStatus(LiveStreamDBHandler liveStreamDBHandler, String currentDate) {
        DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
        updatedStatusDBModel.setDbUpadatedStatusState("");
        updatedStatusDBModel.setDbLastUpdatedDate(currentDate);
        updatedStatusDBModel.setDbCategory(AppConst.DB_EPG);
        updatedStatusDBModel.setDbCategoryID(AppConst.DB_EPG_ID);
        liveStreamDBHandler.addDBUpdatedStatus(updatedStatusDBModel);
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

    public void epgXMLTV(XMLTVCallback xmltvCallback) {
        if (xmltvCallback == null || this.context == null || xmltvCallback.programmePojos == null) {
            if (this.context != null) {
                if (this.liveStreamDBHandler != null) {
                    this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
                }
                startActivity(new Intent(this.context, NewEPGActivity.class));
                finish();
            }
        } else if (VERSION.SDK_INT >= 11) {
            new AnonymousClass1NewAsyncTask(this.context, xmltvCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else {
            new AnonymousClass1NewAsyncTask(this.context, xmltvCallback).execute(new String[0]);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String errorMessage) {
        Utils.showToast(this.context, getResources().getString(R.string.network_error));
    }

    @OnClick({R.id.bt_skip})
    public void onViewClicked() {
        if (this.context != null) {
            this.loginPrefsEditor = this.loginPreferencesAfterLogin.edit();
            if (this.loginPrefsEditor != null) {
                this.loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "pressed");
                this.loginPrefsEditor.commit();
            }
            startActivity(new Intent(this.context, DashboardActivity.class));
            finish();
        }
    }

    public void epgXMLTVUpdateFailed(String failedUpdate) {
        if (failedUpdate.equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FAILED);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
