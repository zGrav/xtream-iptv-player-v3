package z.xtreamiptv.playerv3.view.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.presenter.LoginPresenter;
import z.xtreamiptv.playerv3.v2api.model.database.LiveStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.model.database.SeriesStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.model.database.VODStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.view.interfaces.LoginInterface;
import java.util.List;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity implements LoginInterface, OnItemSelectedListener {
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;
    @BindView(R.id.cb_remember_me)
    CheckBox cbRememberMe;
    private Context context;
    private Dialog dialog;
    @BindView(R.id.et_email)
    EditText emailIdET;
    @BindView(R.id.et_sever_url)
    EditText etServerUrl;
    private DatabaseHandler favDBHandler;
    private LiveStreamDBHandler liveStreamDBHandler;
    private LiveStreamsDatabaseHandler liveStreamsDatabaseHandler;
    @BindView(R.id.bt_submit)
    Button loginBT;
    private SharedPreferences loginPrefXtream;
    private Editor loginPrefXtreamEditor;
    private SharedPreferences loginPrefXtreamVersion;
    private Editor loginPrefXtreamVersionEditor;
    private SharedPreferences loginPreferences;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesServerURl;
    private Editor loginPreferencesServerURlPut;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    private SharedPreferences loginPreferencesSharedPref_epg_channel_auto_update;
    private SharedPreferences loginPreferencesSharedPref_epg_channel_update;
    private SharedPreferences loginPreferencesSharedPref_time_format;
    private Editor loginPrefsEditorBeforeLogin;
    private Editor loginPrefsEditor_epgchannelautoupdate;
    private Editor loginPrefsEditor_epgchannelupdate;
    private Editor loginPrefsEditor_fomat;
    private Editor loginPrefsEditor_timefomat;
    private LoginPresenter loginPresenter;
    @BindView(R.id.tv_enter_credentials)
    TextView loginTV;
    private String loginWith;
    private String password;
    @BindView(R.id.et_password)
    EditText passwordET;
    private ProgressDialog progressDialog;
    private Boolean saveLogin;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    private String server_url;
    @BindView(R.id.sp_xtream_version_selection)
    Spinner spXtreamVersionCode;
    private String username;
    private VODStreamsDatabaseHandler vodStreamsDatabaseHandler;
    private String xtreamVersoin = "";
    @BindView(R.id.iv_logo)
    ImageView yourLogioTV;

    class C18061 implements OnClickListener {
        C18061() {
        }

        @SuppressLint({"ApplySharedPref"})
        public void onClick(View view) {
            LoginActivity.this.getSharedPreferences(AppConst.ACCEPTCLICKED, 0).edit().putString(AppConst.ACCEPTCLICKED, "true").commit();
            LoginActivity.this.dialog.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initialize();
        changeStatusBarColor();
        if (!getSharedPreferences(AppConst.ACCEPTCLICKED, 0).getString(AppConst.ACCEPTCLICKED, "").equals("true")) {
            getDialog();
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
        moveTaskToBack(true);
        super.onBackPressed();
    }

    private void initialize() {
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.favDBHandler = new DatabaseHandler(this.context);
        this.spXtreamVersionCode.setOnItemSelectedListener(this);
        this.liveStreamsDatabaseHandler = new LiveStreamsDatabaseHandler(this.context);
        this.vodStreamsDatabaseHandler = new VODStreamsDatabaseHandler(this.context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(this.context);
        if (this.context != null) {
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setMessage(getResources().getString(R.string.please_wait));
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.setCancelable(false);
            this.progressDialog.setProgressStyle(0);
        }
        this.emailIdET.requestFocus();
        this.username = this.emailIdET.getText().toString();
        this.password = this.passwordET.getText().toString();
        this.server_url = this.etServerUrl.getText().toString();
        this.loginPresenter = new LoginPresenter(this, this.context);
        this.loginPreferences = getSharedPreferences(AppConst.SHARED_PREFERENCE, 0);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesServerURl = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_SERVER_URL, 0);
        this.loginPrefsEditorBeforeLogin = this.loginPreferences.edit();
        this.saveLogin = Boolean.valueOf(this.loginPreferences.getBoolean(AppConst.PREF_SAVE_LOGIN, false));
        loginCheck();
    }

    private void initializeSpinnerItem() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.xtream_server_array, 17367048);
        adapter.setDropDownViewResource(17367049);
        this.spXtreamVersionCode.setAdapter(adapter);
    }

    void loginCheck() {
        this.loginWith = this.loginPreferences.getString(AppConst.PREF_LOGIN_WITH, "");
        if (!this.loginWith.equals(AppConst.LOGIN_WITH_DETAILS)) {
            return;
        }
        if (!this.saveLogin.booleanValue()) {
            this.emailIdET.setText(this.loginPreferences.getString("username", ""));
            this.passwordET.setText(this.loginPreferences.getString("password", ""));
            this.etServerUrl.setText(this.loginPreferences.getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, ""));
            this.cbRememberMe.setChecked(false);
        } else if (this.loginPreferencesAfterLogin.getString("username", "").equals("") || this.loginPreferencesAfterLogin.getString("password", "").equals("") || this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, "").equals("")) {
            this.emailIdET.setText(this.loginPreferences.getString("username", ""));
            this.passwordET.setText(this.loginPreferences.getString("password", ""));
            this.etServerUrl.setText(this.loginPreferences.getString(AppConst.LOGIN_PREF_SERVER_URL_MAG, ""));
            this.cbRememberMe.setChecked(true);
        } else {
            this.xtreamVersoin = getXtreamVersion();
            if (this.context != null && this.liveStreamDBHandler != null && this.liveStreamDBHandler.getAvailableChannelsCount() > 0) {
                startActivity(new Intent(this, DashboardActivity.class));
            } else if (this.context != null) {
                startActivity(new Intent(this, ImportStreamsActivity.class));
            }
        }
    }

    public void onResume() {
        super.onResume();
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
        if (this.context != null && !message.isEmpty()) {
            Utils.showToast(this.context, message);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void getDialog() {
        this.dialog = new Dialog(this, 16973840);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.activity_terms_condition_page);
        Window window = this.dialog.getWindow();
        LayoutParams wlp = window.getAttributes();
        wlp.gravity = 17;
        wlp.flags &= -5;
        window.setAttributes(wlp);
        this.dialog.getWindow().setLayout(-2, -1);
        this.dialog.show();
        this.dialog.setCanceledOnTouchOutside(false);
        WebView mWebView = (WebView) this.dialog.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/terms.html");
        ((Button) this.dialog.findViewById(R.id.accept)).setOnClickListener(new C18061());
        this.dialog.show();
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        if (loginCallback == null || loginCallback.getUserLoginInfo() == null) {
            onFailed(getResources().getString(R.string.invalid_server_response));
        } else if (loginCallback.getUserLoginInfo().getAuth().intValue() == 1) {
            String userStatus = loginCallback.getUserLoginInfo().getStatus();
            if (userStatus.equals("Active")) {
                String username = loginCallback.getUserLoginInfo().getUsername();
                String password = loginCallback.getUserLoginInfo().getPassword();
                String serverPort = loginCallback.getServerInfo().getPort();
                String serverUrl = loginCallback.getServerInfo().getUrl();
                String expDate = loginCallback.getUserLoginInfo().getExpDate();
                String isTrial = loginCallback.getUserLoginInfo().getIsTrial();
                String activeCons = loginCallback.getUserLoginInfo().getActiveCons();
                String createdAt = loginCallback.getUserLoginInfo().getCreatedAt();
                String maxConnections = loginCallback.getUserLoginInfo().getMaxConnections();
                List<String> allowedFormatList = loginCallback.getUserLoginInfo().getAllowedOutputFormats();
                if (allowedFormatList.size() != 0) {
                    String allowedFormat = (String) allowedFormatList.get(0);
                }
                Editor editor1 = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).edit();
                editor1.putString("username", username);
                editor1.putString("password", password);
                editor1.putString(AppConst.LOGIN_PREF_SERVER_PORT, serverPort);
                editor1.putString(AppConst.LOGIN_PREF_SERVER_URL, serverUrl);
                editor1.putString(AppConst.LOGIN_PREF_EXP_DATE, expDate);
                editor1.putString(AppConst.LOGIN_PREF_IS_TRIAL, isTrial);
                editor1.putString(AppConst.LOGIN_PREF_ACTIVE_CONS, activeCons);
                editor1.putString(AppConst.LOGIN_PREF_CREATE_AT, createdAt);
                editor1.putString(AppConst.LOGIN_PREF_MAX_CONNECTIONS, maxConnections);
                editor1.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, this.server_url);
                editor1.commit();
                this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
                this.loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
                this.loginPreferencesSharedPref_epg_channel_update = this.context.getSharedPreferences(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, 0);
                this.loginPreferencesSharedPref_epg_channel_auto_update = this.context.getSharedPreferences(AppConst.LOGIN_PREF_AUTOUPDATE, 0);
                this.loginPrefsEditor_fomat = this.loginPreferencesSharedPref_allowed_format.edit();
                this.loginPrefsEditor_timefomat = this.loginPreferencesSharedPref_time_format.edit();
                this.loginPrefsEditor_epgchannelupdate = this.loginPreferencesSharedPref_epg_channel_update.edit();
                this.loginPrefsEditor_epgchannelautoupdate = this.loginPreferencesSharedPref_epg_channel_auto_update.edit();
                String allowedFormat1 = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
                if (allowedFormat1 != null && allowedFormat1.equals("")) {
                    this.loginPrefsEditor_fomat.putString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "ts");
                    this.loginPrefsEditor_fomat.commit();
                }
                String timeFormat = this.loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "");
                if (timeFormat != null && timeFormat.equals("")) {
                    this.loginPrefsEditor_timefomat.putString(AppConst.LOGIN_PREF_TIME_FORMAT, "HH:mm");
                    this.loginPrefsEditor_timefomat.commit();
                }
                String channelupdate = this.loginPreferencesSharedPref_epg_channel_update.getString(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, "");
                if (channelupdate != null && channelupdate.equals("")) {
                    this.loginPrefsEditor_epgchannelupdate.putString(AppConst.LOGIN_PREF_EPG_CHANNEL_UPDATE, "withepg");
                    this.loginPrefsEditor_epgchannelupdate.commit();
                }
                String channelVodAutoupdate = this.loginPreferencesSharedPref_epg_channel_auto_update.getString(AppConst.LOGIN_PREF_AUTOUPDATE, "");
                if (channelVodAutoupdate != null && channelVodAutoupdate.equals("")) {
                    this.loginPrefsEditor_epgchannelautoupdate.putString(AppConst.LOGIN_PREF_AUTOUPDATE, getResources().getString(R.string.disable));
                    this.loginPrefsEditor_epgchannelautoupdate.commit();
                }
                Toast.makeText(this, getResources().getString(R.string.logged_in), 0).show();
                this.xtreamVersoin = getXtreamVersion();
                if (this.liveStreamDBHandler != null && this.liveStreamDBHandler.getMagportal(serverUrl) == 0) {
                    this.liveStreamDBHandler.deleteAndRecreateAllTables();
                    this.seriesStreamsDatabaseHandler.deleteAndRecreateAllVSeriesTables();
                    if (this.favDBHandler != null) {
                        this.favDBHandler.deleteAndRecreateAllTables();
                    }
                    this.liveStreamDBHandler.addMagPortal(serverUrl);
                }
                if (this.context != null && this.liveStreamDBHandler != null && this.liveStreamDBHandler.getAvailableChannelsCount() > 0) {
                    startActivity(new Intent(this, DashboardActivity.class));
                    return;
                } else if (this.context != null) {
                    startActivity(new Intent(this, ImportStreamsActivity.class));
                    return;
                } else {
                    return;
                }
            }
            Toast.makeText(this, getResources().getString(R.string.invalid_status) + userStatus, 0).show();
        } else if (validateLogin == AppConst.VALIDATE_LOGIN) {
            Toast.makeText(this, getResources().getString(R.string.invalid_details), 0).show();
        }
    }

    public void stopLoader() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            Toast.makeText(this, getResources().getString(R.string.network_error), 0).show();
        }
    }

    @OnClick({R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                this.progressDialog.show();
                this.username = this.emailIdET.getText().toString();
                this.password = this.passwordET.getText().toString();
                this.server_url = this.etServerUrl.getText().toString();
                this.xtreamVersoin = getXtreamVersion();
                this.loginPreferencesServerURlPut = this.loginPreferencesServerURl.edit();
                if (this.username.isEmpty()) {
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                    Toast.makeText(this, getResources().getString(R.string.enter_username_error), 0).show();
                }
                if (!this.username.isEmpty() && this.password.isEmpty()) {
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                    Toast.makeText(this, getResources().getString(R.string.enter_password_error), 0).show();
                }
                if (!(this.username.isEmpty() || this.password.isEmpty() || !this.server_url.isEmpty())) {
                    if (this.progressDialog != null) {
                        this.progressDialog.dismiss();
                    }
                    Toast.makeText(this, AppConst.ENTER_SERVER_URL_ERROR, 0).show();
                }
                if (this.username != null && !this.username.isEmpty() && this.password != null && !this.password.isEmpty() && this.server_url != null && !this.server_url.isEmpty()) {
                    if (this.cbRememberMe.isChecked()) {
                        this.loginPrefsEditorBeforeLogin.putBoolean(AppConst.PREF_SAVE_LOGIN, true);
                        this.loginPrefsEditorBeforeLogin.putString("username", this.username);
                        this.loginPrefsEditorBeforeLogin.putString("password", this.password);
                        this.loginPrefsEditorBeforeLogin.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, this.server_url);
                        this.loginPrefsEditorBeforeLogin.putString("activationCode", "");
                        this.loginPrefsEditorBeforeLogin.putString(AppConst.PREF_LOGIN_WITH, AppConst.LOGIN_WITH_DETAILS);
                        this.loginPrefsEditorBeforeLogin.commit();
                    } else {
                        this.loginPrefsEditorBeforeLogin.clear();
                        this.loginPrefsEditorBeforeLogin.putBoolean(AppConst.PREF_SAVE_LOGIN, false);
                        this.loginPrefsEditorBeforeLogin.putString(AppConst.PREF_LOGIN_WITH, AppConst.LOGIN_WITH_DETAILS);
                        this.loginPrefsEditorBeforeLogin.commit();
                    }
                    this.loginPreferencesServerURlPut.putString(AppConst.LOGIN_PREF_SERVER_URL_MAG, this.server_url);
                    this.loginPreferencesServerURlPut.commit();
                    this.loginPresenter.validateLogin(this.username, this.password);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setXtream1_06() {
        this.loginPrefXtream = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IS_XTREAM_1_06, 0);
        this.loginPrefXtreamEditor = this.loginPrefXtream.edit();
        this.loginPrefXtreamEditor.putBoolean(AppConst.IS_XTREAM_1_06, false);
        this.loginPrefXtreamEditor.commit();
    }

    private boolean getIsXtream1_06() {
        this.loginPrefXtream = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IS_XTREAM_1_06, 0);
        return this.loginPrefXtream.getBoolean(AppConst.IS_XTREAM_1_06, false);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getChildAt(0) != null) {
            ((TextView) parent.getChildAt(0)).setTextColor(-1);
            ((TextView) parent.getChildAt(0)).setPadding(50, 0, 50, 0);
        }
        String selcetedVersion = String.valueOf(parent.getItemAtPosition(position));
        if (selcetedVersion.equals(AppConst.XTREAM_1_0_6)) {
            setXtreamVersion1_0_6();
        } else if (selcetedVersion.equals(AppConst.XTREAM_2_7)) {
            setXtreamVersion2_7();
        } else {
            setXtreamVersion2_8();
        }
    }

    private void setXtreamVersion2_8() {
        this.loginPrefXtreamVersion = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_XTREAM_VERSION, 0);
        this.loginPrefXtreamVersionEditor = this.loginPrefXtreamVersion.edit();
        this.loginPrefXtreamVersionEditor.putString(AppConst.XTREAM_SELCETED_VERSION_CODE, AppConst.XTREAM_2_8);
        this.loginPrefXtreamVersionEditor.commit();
    }

    private void setXtreamVersion2_7() {
        this.loginPrefXtreamVersion = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_XTREAM_VERSION, 0);
        this.loginPrefXtreamVersionEditor = this.loginPrefXtreamVersion.edit();
        this.loginPrefXtreamVersionEditor.putString(AppConst.XTREAM_SELCETED_VERSION_CODE, AppConst.XTREAM_2_7);
        this.loginPrefXtreamVersionEditor.commit();
    }

    private void setXtreamVersion1_0_6() {
        this.loginPrefXtreamVersion = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_XTREAM_VERSION, 0);
        this.loginPrefXtreamVersionEditor = this.loginPrefXtreamVersion.edit();
        this.loginPrefXtreamVersionEditor.putString(AppConst.XTREAM_SELCETED_VERSION_CODE, AppConst.XTREAM_1_0_6);
        this.loginPrefXtreamVersionEditor.commit();
    }

    private void setXtreamVersion_null() {
        if (!this.saveLogin.booleanValue()) {
            this.loginPrefXtreamVersion = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_XTREAM_VERSION, 0);
            this.loginPrefXtreamVersionEditor = this.loginPrefXtreamVersion.edit();
            this.loginPrefXtreamVersionEditor.putString(AppConst.XTREAM_SELCETED_VERSION_CODE, "null");
            this.loginPrefXtreamVersionEditor.commit();
        }
    }

    private String getXtreamVersion() {
        this.loginPrefXtreamVersion = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_XTREAM_VERSION, 0);
        return this.loginPrefXtreamVersion.getString(AppConst.XTREAM_SELCETED_VERSION_CODE, "");
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
