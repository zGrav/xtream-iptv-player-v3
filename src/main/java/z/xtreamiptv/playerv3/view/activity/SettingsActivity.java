package z.xtreamiptv.playerv3.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordDBModel;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.view.interfaces.XMLTVInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class SettingsActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener, XMLTVInterface {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    int actionBarHeight;
    @BindView(R.id.btn_back_settings)
    Button btnBackSettings;
    private PopupWindow changeSortPopUp;
    private TextView clientNameTv;
    private Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private Editor loginPrefsEditor;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.rl_setting_parental)
    RelativeLayout rlAutoUpdate;
    @BindView(R.id.rl_epg_channel_update)
    RelativeLayout rlEpgChannelUpdate;
    @BindView(R.id.rl_live_vod_layout)
    RelativeLayout rlLiveVodLayout;
    @BindView(R.id.rl_player)
    RelativeLayout rlPlayer;
    @BindView(R.id.rl_time_format)
    RelativeLayout rlTimeFormat;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_time_format)
    TextView tvTimeFormat;
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C18391 implements DialogInterface.OnClickListener {
        C18391() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsVodandSeries(SettingsActivity.this.context);
        }
    }

    class AnonymousClass1NewAsyncTask extends AsyncTask<String, Integer, Boolean> {
        int ITERATIONS = this.val$xmltvCallback.programmePojos.size();
        Context mcontext = null;
         /* synthetic */ XMLTVCallback val$xmltvCallback;

        AnonymousClass1NewAsyncTask(Context context, XMLTVCallback xMLTVCallback) {
            this.val$xmltvCallback = xMLTVCallback;
            this.mcontext = context;
        }

        protected Boolean doInBackground(String... params) {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            if (SettingsActivity.this.liveStreamDBHandler != null) {
                SettingsActivity.this.liveStreamDBHandler.addEPG(this.val$xmltvCallback.programmePojos);
            }
            return Boolean.valueOf(true);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Boolean result) {
            SettingsActivity.this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FINISH);
            if (SettingsActivity.this.context == null) {
            }
        }
    }

    class C18402 implements DialogInterface.OnClickListener {
        C18402() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C18413 implements DialogInterface.OnClickListener {
        C18413() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SettingsActivity.this.loadTvGuid();
        }
    }

    class C18424 implements DialogInterface.OnClickListener {
        C18424() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C18435 implements OnClickListener {
        C18435() {
        }

        public void onClick(View view) {
            SettingsActivity.this.changeSortPopUp.dismiss();
        }
    }

    class C18457 implements OnClickListener {
        C18457() {
        }

        public void onClick(View view) {
            SettingsActivity.this.changeSortPopUp.dismiss();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        loginPreferencesSharedPref_time_format = getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        if (loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "").equals("hh:mm a")) {
            this.tvTimeFormat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time_12hr, 0, 0, 0);
        } else {
            this.tvTimeFormat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time_24hr, 0, 0, 0);
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
        navigationView.getMenu().findItem(R.id.nav_settings).setChecked(true);
        initialize();
        if (this.tvHeaderTitle != null) {
            this.tvHeaderTitle.setOnClickListener(this);
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

    private void initialize() {
        this.context = this;
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
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
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
        super.onBackPressed();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
        } else if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);
        } else if (id == R.id.nav_live_tv_guide) {
            if (!(this.context == null || this.databaseUpdatedStatusDBModelLive == null || this.liveStreamDBHandler == null)) {
                this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
                if (this.databaseUpdatedStatusDBModelLive == null || !this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) {
                    Utils.showToast(this.context, getResources().getString(R.string.udpating_channels_please_wait));
                } else {
                    startDashboardActivty();
                }
            }
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_vod) {
            Utils.set_layout_vod(this.context);

        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
        } else if (id == R.id.nav_logout) {
            logoutUser();
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
    }

    private void startDashboardActivty() {
        Intent dashboardActitvityIntent = new Intent(this, DashboardActivity.class);
        dashboardActitvityIntent.putExtra(AppConst.LAUNCH_TV_GUIDE, AppConst.LAUNCH_TV_GUIDE);
        startActivity(dashboardActitvityIntent);
    }

    private void launchTvGuide() {
        String str = "";
        startXMLTV(getUserName(), getUserPassword(), currentDateValue());
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
            this.xmltvPresenter.epgXMLTV(userName, userPassword);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logoutUser();
        }
        if (id == R.id.menu_load_channels_vod) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C18391());
            alertDialog.setNegativeButton("NO", new C18402());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C18413());
            alertDialog.setNegativeButton("NO", new C18424());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadChannelsAndVod() {
        if (this.context == null) {
            return;
        }
        if (getChannelVODUpdateStatus()) {
            new LiveStreamDBHandler(this.context).makeEmptyAllChannelsVODTablesRecords();
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

    public void onResume() {
        super.onResume();
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context != null) {
        }
        loginPreferencesSharedPref_time_format = getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        if (loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "").equals("hh:mm a")) {
            this.tvTimeFormat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time_12hr, 0, 0, 0);
        } else {
            this.tvTimeFormat.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time_24hr, 0, 0, 0);
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

    private void logoutUser() {
        Toast.makeText(this, getResources().getString(R.string.logged_out), 0).show();
        Intent intentLogout = new Intent(this, LoginActivity.class);
        Editor loginPreferencesEditor = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).edit();
        loginPreferencesEditor.clear();
        loginPreferencesEditor.commit();
        startActivity(intentLogout);
    }

    @OnClick({R.id.rl_epg_channel_update, R.id.rl_epg_shift, R.id.rl_live_vod_daily_update, R.id.rl_live_vod_layout, R.id.rl_parental, R.id.rl_player, R.id.rl_stream_format, R.id.rl_time_format})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_epg_channel_update:
                startActivity(new Intent(this, EPGChannelUpdateActivity.class));
                return;
            case R.id.rl_epg_shift:
                startActivity(new Intent(this, EPGTimeShiftActivity.class));
                return;
            case R.id.rl_live_vod_daily_update:
                startActivity(new Intent(this, AutoUpdateChannelsandVODActivity.class));
                return;
            case R.id.rl_live_vod_layout:
                startActivity(new Intent(this, LiveVodLayoutActivity.class));
                return;
            case R.id.rl_parental:
                String username = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).getString("username", "");
                this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
                ArrayList<PasswordDBModel> list = this.liveStreamDBHandler.getAllPassword();
                String usernameDB = "";
                String userPasswordDB = "";
                if (list != null) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        PasswordDBModel listItem = (PasswordDBModel) it.next();
                        if (listItem.getUserDetail().equals(username) && !listItem.getUserPassword().isEmpty()) {
                            usernameDB = listItem.getUserDetail();
                            userPasswordDB = listItem.getUserPassword();
                        }
                    }
                }
                if (usernameDB != null && !usernameDB.equals("") && !usernameDB.isEmpty()) {
                    passwordConfirmationPopUp(this, 100, username, usernameDB, userPasswordDB);
                    return;
                } else if (username != null && !username.isEmpty() && !username.equals("")) {
                    showSortPopup(this, 100, username);
                    return;
                } else {
                    return;
                }
            case R.id.rl_player:
                startActivity(new Intent(this, PlayerSelectionActivity.class));
                return;
            case R.id.rl_stream_format:
                startActivity(new Intent(this, StreamFormatActivity.class));
                return;
            case R.id.rl_time_format:
                startActivity(new Intent(this, TimeFormatActivity.class));
                return;
            default:
                return;
        }
    }

    private void passwordConfirmationPopUp(SettingsActivity context, int i, String username, String usernameDB, String passwordDB) {
        View layout = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.view_password_verification, (RelativeLayout) context.findViewById(R.id.rl_password_verification));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        Button savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        Button closeBT = (Button) layout.findViewById(R.id.bt_close);
        final EditText passwordET = (EditText) layout.findViewById(R.id.et_password);
        final String[] passowrd = new String[1];
        passwordET.requestFocus();
        closeBT.setOnClickListener(new C18435());
        final String str = passwordDB;
        final SettingsActivity settingsActivity = context;
        savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (fieldsCheck()) {
                    passwordValidation(comparePassword(str));
                }
            }

            private void passwordValidation(boolean comPassword) {
                if (comPassword) {
                    SettingsActivity.this.changeSortPopUp.dismiss();
                    startActity();
                    return;
                }
                if (settingsActivity != null) {
                    Toast.makeText(settingsActivity, SettingsActivity.this.getResources().getString(R.string.parental_invalid_password), 1).show();
                }
                passwordET.getText().clear();
            }

            private boolean fieldsCheck() {
                passowrd[0] = String.valueOf(passwordET.getText());
                if (passowrd != null && passowrd[0].equals("")) {
                    Toast.makeText(settingsActivity, SettingsActivity.this.getResources().getString(R.string.enter_password_error), 1).show();
                    return false;
                } else if (passowrd == null || passowrd[0].equals("")) {
                    return false;
                } else {
                    return true;
                }
            }

            private boolean comparePassword(String passwordDB) {
                passowrd[0] = String.valueOf(passwordET.getText());
                if (passowrd[0] == null || passowrd[0].equals("") || passowrd[0].isEmpty() || passwordDB == null || passwordDB.isEmpty() || passwordDB.equals("") || !passowrd[0].equals(passwordDB)) {
                    return false;
                }
                return true;
            }

            private void startActity() {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ParentalControlActivitity.class));
            }
        });
    }

    private void showSortPopup(Activity context, int p, String username) {
        View layout = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.view_password_prompt, (RelativeLayout) context.findViewById(R.id.rl_password_prompt));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        Button savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        final EditText passwordET = (EditText) layout.findViewById(R.id.tv_password);
        final EditText confirmPasswordET = (EditText) layout.findViewById(R.id.tv_confirm_password);
        final String[] passowrd = new String[1];
        final String[] confirmPassword = new String[1];
        ((Button) layout.findViewById(R.id.bt_close)).setOnClickListener(new C18457());
        final Activity activity = context;
        final String str = username;
        savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (fieldsCheck()) {
                    setPassword(comparePassword());
                }
            }

            private boolean fieldsCheck() {
                passowrd[0] = String.valueOf(passwordET.getText());
                confirmPassword[0] = String.valueOf(confirmPasswordET.getText());
                if (passowrd != null && passowrd[0].equals("")) {
                    Toast.makeText(activity, SettingsActivity.this.getResources().getString(R.string.enter_password_error), 1).show();
                    return false;
                } else if (passowrd != null && !passowrd[0].equals("") && confirmPassword != null && confirmPassword[0].equals("")) {
                    Toast.makeText(activity, SettingsActivity.this.getResources().getString(R.string.parental_confirm_password), 1).show();
                    return false;
                } else if (passowrd == null || confirmPassword == null || passowrd[0].equals("") || confirmPassword[0].equals("")) {
                    return false;
                } else {
                    return true;
                }
            }

            private void setPassword(boolean comPassword) {
                if (comPassword) {
                    PasswordDBModel passwordDBModel = new PasswordDBModel(null, null);
                    passwordDBModel.setUserPassword(String.valueOf(passowrd[0]));
                    passwordDBModel.setUserDetail(str);
                    if (SettingsActivity.this.liveStreamDBHandler != null) {
                        SettingsActivity.this.liveStreamDBHandler.addPassword(passwordDBModel);
                        SettingsActivity.this.changeSortPopUp.dismiss();
                        startActity();
                        return;
                    }
                    return;
                }
                if (activity != null) {
                    Toast.makeText(activity, SettingsActivity.this.getResources().getString(R.string.parental_password_confirm_password_match_error), 1).show();
                }
                passwordET.getText().clear();
                confirmPasswordET.getText().clear();
            }

            private boolean comparePassword() {
                passowrd[0] = String.valueOf(passwordET.getText());
                confirmPassword[0] = String.valueOf(confirmPasswordET.getText());
                if (passowrd == null || passowrd[0].equals("") || confirmPassword == null || confirmPassword[0].equals("") || !passowrd[0].equals(confirmPassword[0])) {
                    return false;
                }
                return true;
            }

            private void startActity() {
                SettingsActivity.this.startActivity(new Intent(activity, ParentalControlActivitity.class));
            }
        });
    }

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String errorMessage) {
    }

    public void epgXMLTV(XMLTVCallback xmltvCallback) {
        if (xmltvCallback != null && this.context != null && xmltvCallback.programmePojos != null) {
            this.liveStreamDBHandler.makeEmptyEPG();
            if (VERSION.SDK_INT >= 11) {
                new AnonymousClass1NewAsyncTask(this.context, xmltvCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else {
                new AnonymousClass1NewAsyncTask(this.context, xmltvCallback).execute(new String[0]);
            }
        }
    }

    public void epgXMLTVUpdateFailed(String failedUpdate) {
        if (failedUpdate.equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.liveStreamDBHandler != null) {
            this.liveStreamDBHandler.updateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID, AppConst.DB_UPDATED_STATUS_FAILED);
        }
    }

    @OnClick({R.id.btn_back_settings})
    public void onViewClicked() {
        startActivity(new Intent(this.context, DashboardActivity.class));
        finish();
    }
}
