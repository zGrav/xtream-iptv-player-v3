package z.xtreamiptv.playerv3.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.callback.VodInfoCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.presenter.VodPresenter;
import z.xtreamiptv.playerv3.view.interfaces.VodInterface;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ViewDetailsActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, OnClickListener, VodInterface {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    private String categoryId = "";
    private TextView clientNameTv;
    private String containerExtension = "";
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;
    @BindView(R.id.iv_movie_image)
    ImageView ivMovieImage;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private String movieName = "";
    @BindView(R.id.nav_view)
    NavigationView navView;
    private String num = "";
    private ProgressDialog progressDialog;
    @BindView(R.id.rl_account_info)
    RelativeLayout rlAccountInfo;
    @BindView(R.id.rl_movie_image)
    RelativeLayout rlMovieImage;
    private String selectedPlayer = "";
    private int streamId = -1;
    private String streamType = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @BindView(R.id.tv_add_to_fav)
    TextView tvAddToFav;
    @BindView(R.id.tv_cast)
    TextView tvCast;
    @BindView(R.id.tv_cast_info)
    TextView tvCastInfo;
    @BindView(R.id.tv_director)
    TextView tvDirector;
    @BindView(R.id.tv_director_info)
    TextView tvDirectorInfo;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_movie_genere)
    TextView tvMovieGenere;
    @BindView(R.id.tv_movie_info)
    TextView tvMovieInfo;
    @BindView(R.id.tv_movie_name)
    TextView tvMovieName;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_rating_label)
    TextView tvRatingLabel;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_release_date_info)
    TextView tvReleaseDateInfo;
    @BindView(R.id.tv_detail_back_btn)
    TextView tvdetailbackbutton;
    @BindView(R.id.tv_detail_ProgressBar)
    ProgressBar tvdetailprogressbar;
    private String userName = "";
    private String userPassword = "";
    private VodPresenter vodPresenter;

    class C18861 implements DialogInterface.OnClickListener {
        C18861() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsVodandSeries(ViewDetailsActivity.this.context);
        }
    }

    class C18872 implements DialogInterface.OnClickListener {
        C18872() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C18883 implements DialogInterface.OnClickListener {
        C18883() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ViewDetailsActivity.this.loadTvGuid();
        }
    }

    class C18894 implements DialogInterface.OnClickListener {
        C18894() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
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
        navigationView.getMenu().findItem(R.id.nav_vod).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
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
        this.database = new DatabaseHandler(this.context);
        this.tvPlay.requestFocus();
        this.tvPlay.setFocusable(true);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String password = this.loginPreferencesAfterLogin.getString("password", "");
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startViewingDetails(this.context, username, password);
        }
    }

    private void startViewingDetails(Context context, String username, String password) {
        this.vodPresenter = new VodPresenter(this, context);
        Intent intetGetIntent = getIntent();
        if (intetGetIntent != null) {
            this.streamId = Integer.parseInt(intetGetIntent.getStringExtra(AppConst.STREAM_ID));
            this.movieName = intetGetIntent.getStringExtra("movie");
            this.selectedPlayer = intetGetIntent.getStringExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER);
            this.streamType = intetGetIntent.getStringExtra("streamType");
            this.containerExtension = intetGetIntent.getStringExtra("containerExtension");
            this.categoryId = intetGetIntent.getStringExtra("categoryID");
            this.num = intetGetIntent.getStringExtra("num");
            if (this.database.checkFavourite(this.streamId, this.categoryId, AppConst.VOD).size() > 0) {
                this.tvAddToFav.setText("Remove from Favourite");
                this.ivFavourite.setVisibility(0);
            } else {
                this.tvAddToFav.setText("Add To Favourite");
                this.ivFavourite.setVisibility(4);
            }
            if (this.streamId != -1 && this.streamId != 0) {
                this.vodPresenter.vodInfo(username, password, this.streamId);
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

        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_live_tv_guide) {
            if (!(this.context == null || this.databaseUpdatedStatusDBModelLive == null || this.liveStreamDBHandler == null)) {
                this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
                if (this.databaseUpdatedStatusDBModelLive == null || !this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) {
                    Utils.showToast(this.context, getResources().getString(R.string.udpating_channels_please_wait));
                } else {
                    startDashboardActivty();
                }
            }
        } else if (id == R.id.nav_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
    }

    private void startDashboardActivty() {
        Intent dashboardActitvityIntent = new Intent(this, DashboardActivity.class);
        dashboardActitvityIntent.putExtra(AppConst.LAUNCH_TV_GUIDE, AppConst.LAUNCH_TV_GUIDE);
        startActivity(dashboardActitvityIntent);
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
        if (id == R.id.action_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        if (id == R.id.menu_load_channels_vod) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C18861());
            alertDialog.setNegativeButton("NO", new C18872());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C18883());
            alertDialog.setNegativeButton("NO", new C18894());
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
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public void onFailed(String errorMessage) {
        this.tvdetailprogressbar.setVisibility(8);
    }

    public void vodInfo(VodInfoCallback vodInfoCallback) {
        this.tvdetailprogressbar.setVisibility(8);
        if (vodInfoCallback != null && vodInfoCallback.getInfo() != null) {
            String movieImage = vodInfoCallback.getInfo().getMovieImage();
            String movieDirector = vodInfoCallback.getInfo().getDirector();
            String cast = vodInfoCallback.getInfo().getCast();
            String releaseDate = vodInfoCallback.getInfo().getReleasedate();
            String rating = vodInfoCallback.getInfo().getRating();
            String description = vodInfoCallback.getInfo().getPlot();
            String gnere = vodInfoCallback.getInfo().getGenre();
            this.tvdetailprogressbar.setVisibility(8);
            if (!(this.context == null || movieImage == null || movieImage.isEmpty())) {
                Picasso.with(this.context).load(movieImage).placeholder((int) R.drawable.noposter).into(this.ivMovieImage);
            }
            if (this.movieName != null) {
                this.tvMovieName.setText(this.movieName);
            }
            if (this.tvReleaseDateInfo == null || releaseDate == null || releaseDate.isEmpty() || releaseDate.equals("n/A") || releaseDate == "n/A") {
                this.tvReleaseDateInfo.setText("");
            } else {
                this.tvReleaseDate.setVisibility(0);
                this.tvReleaseDateInfo.setText(releaseDate);
            }
            if (this.tvDirectorInfo == null || movieDirector == null || movieDirector.isEmpty() || movieDirector.equals("n/A")) {
                this.tvDirectorInfo.setText("");
            } else {
                this.tvDirector.setVisibility(0);
                this.tvDirectorInfo.setText(movieDirector);
            }
            if (this.tvRatingLabel == null || rating == null || rating.isEmpty() || rating.equals("n/A")) {
                this.tvRatingLabel.setText("");
            } else {
                this.tvRating.setVisibility(0);
                this.tvRatingLabel.setText(rating);
            }
            if (!(this.tvMovieInfo == null || description == null || description.isEmpty() || description.equals("n/A"))) {
                this.tvMovieInfo.setText(description);
            }
            if (this.tvMovieGenere != null && gnere != null && !gnere.isEmpty()) {
                this.tvMovieGenere.setVisibility(8);
                this.tvMovieGenere.setText(gnere);
            }
        }
    }

    @OnClick({R.id.tv_add_to_fav, R.id.tv_detail_back_btn, R.id.tv_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_to_fav:
                if (this.database.checkFavourite(this.streamId, this.categoryId, AppConst.VOD).size() > 0) {
                    removeFromFavourite();
                    return;
                } else {
                    addToFavourite();
                    return;
                }
            case R.id.tv_detail_back_btn:
                finish();
                return;
            case R.id.tv_play:
                Context context = this.context;
                String str = AppConst.LOGIN_PREF_SELECTED_PLAYER;
                Context context2 = this.context;
                this.loginPreferencesSharedPref = context.getSharedPreferences(str, 0);
                Utils.playWithPlayerVOD(this.context, this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, ""), this.streamId, this.streamType, this.containerExtension, this.num, this.movieName);
                return;
            default:
                return;
        }
    }

    private void addToFavourite() {
        FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel(0, null, null);
        LiveStreamsFavourite.setCategoryID(this.categoryId);
        LiveStreamsFavourite.setStreamID(this.streamId);
        this.database.addToFavourite(LiveStreamsFavourite, AppConst.VOD);
        this.tvAddToFav.setText("Remove To Favourite");
        this.ivFavourite.setVisibility(0);
    }

    private void removeFromFavourite() {
        this.database.deleteFavourite(this.streamId, this.categoryId, AppConst.VOD);
        this.tvAddToFav.setText("Add To Favourite");
        this.ivFavourite.setVisibility(4);
    }
}
