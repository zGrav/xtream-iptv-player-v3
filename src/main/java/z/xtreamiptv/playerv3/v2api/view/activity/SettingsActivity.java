package z.xtreamiptv.playerv3.v2api.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
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
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordDBModel;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.view.activity.LoginActivity;
import java.util.ArrayList;
import java.util.Iterator;

public class SettingsActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener {
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

    class C15081 implements DialogInterface.OnClickListener {
        C15081() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVodV2Api(SettingsActivity.this.context);
        }
    }

    class C15092 implements DialogInterface.OnClickListener {
        C15092() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15103 implements DialogInterface.OnClickListener {
        C15103() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(SettingsActivity.this.context);
        }
    }

    class C15114 implements DialogInterface.OnClickListener {
        C15114() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C15125 implements OnClickListener {
        C15125() {
        }

        public void onClick(View view) {
            SettingsActivity.this.changeSortPopUp.dismiss();
        }
    }

    class C15147 implements OnClickListener {
        C15147() {
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
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
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
        startActivity(new Intent(this, DashboardActivityV2.class));
        finish();
        super.onBackPressed();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivityV2.class));
        } else if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);
        } else if (id == R.id.nav_live_tv_guide) {
            if (this.context != null) {
                Utils.startDashboardV2LoadTVGuid(this.context);
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
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
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
            alertDialog.setPositiveButton("YES", new C15081());
            alertDialog.setNegativeButton("NO", new C15092());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C15103());
            alertDialog.setNegativeButton("NO", new C15114());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
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
                startActivity(new Intent(this, DashboardActivityV2.class));
                return;
            default:
                return;
        }
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
        closeBT.setOnClickListener(new C15125());
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
        ((Button) layout.findViewById(R.id.bt_close)).setOnClickListener(new C15147());
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

    @OnClick({R.id.btn_back_settings})
    public void onViewClicked() {
        startActivity(new Intent(this.context, DashboardActivityV2.class));
        finish();
    }
}
