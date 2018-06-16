package z.xtreamiptv.playerv3.v2api.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.JsonElement;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.v2api.model.GetEpisdoeDetailsCallback;
import z.xtreamiptv.playerv3.v2api.presenter.SeriesPresenter;
import z.xtreamiptv.playerv3.v2api.view.adapter.EpisodeDetailAdapter;
import z.xtreamiptv.playerv3.v2api.view.interfaces.SeriesInterface;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EpisodeDetailActivity extends AppCompatActivity implements OnNavigationItemSelectedListener, SeriesInterface {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ArrayList<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksList = new ArrayList();
    private EpisodeDetailAdapter episodeDetailAdapter;
    private LayoutManager layoutManager;
    private SharedPreferences loginPreferencesAfterLogin;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private String seriesCover = "";
    private String seriesId = "";
    private SeriesPresenter seriesPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    TypedValue tv;
    @BindView(R.id.tv_header_title)
    ImageView tvHeaderTitle;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    @BindView(R.id.tv_view_provider)
    TextView tvViewProvider;
    @BindView(R.id.tv_movie_category_name)
    TextView vodCategoryName;

    class C14541 implements OnClickListener {
        C14541() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVodV2Api(EpisodeDetailActivity.this.context);
        }
    }

    class C14552 implements OnClickListener {
        C14552() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C14563 implements OnClickListener {
        C14563() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(EpisodeDetailActivity.this.context);
        }
    }

    class C14574 implements OnClickListener {
        C14574() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);
        ButterKnife.bind(this);
        setGridView();
        initializeSideDrawer();
        initializeV();
    }

    private void setGridView() {
        if (this.myRecyclerView != null) {
            this.context = this;
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(this.context, Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void initializeSideDrawer() {
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
        navigationView.getMenu().findItem(R.id.nav_series).setChecked(true);
        this.context = this;
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
    }

    private void initializeV() {
        this.context = this;
        this.seriesPresenter = new SeriesPresenter(this.context, this);
        Intent intent = getIntent();
        if (intent != null) {
            this.seriesId = intent.getStringExtra(AppConst.SERIES_SERIES_ID);
            this.seriesCover = intent.getStringExtra(AppConst.SERIES_COVER);
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String password = this.loginPreferencesAfterLogin.getString("password", "");
        if (this.seriesId != null && !this.seriesId.isEmpty() && this.seriesPresenter != null && username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            this.seriesPresenter.getSeriesEpisode(username, password, this.seriesId);
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

    public void atStart() {
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onFailed(String errorMessage) {
    }

    public void getSeriesEpisodeInfo(JsonElement jsonElement) {
        if (jsonElement != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                JSONObject jsonArrayEpisodes = null;
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                }
                this.episdoeDetailsCallbacksList.clear();
                if (jsonArrayEpisodes != null) {
                    Iterator<String> iterator = jsonArrayEpisodes.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                            JSONArray jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                            for (int i = 0; i < jsonArrayEpisode.length(); i++) {
                                JSONObject jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                GetEpisdoeDetailsCallback getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
                                }
                                if (jsonObjectEpisodeDetails.getString("title") == null || jsonObjectEpisodeDetails.getString("title").isEmpty()) {
                                    getEpisdoeDetailsCallback.setTitle("");
                                } else {
                                    getEpisdoeDetailsCallback.setTitle(jsonObjectEpisodeDetails.getString("title"));
                                }
                                if (jsonObjectEpisodeDetails.getString("direct_source") == null || jsonObjectEpisodeDetails.getString("direct_source").isEmpty()) {
                                    getEpisdoeDetailsCallback.setDirectSource("");
                                } else {
                                    getEpisdoeDetailsCallback.setDirectSource(jsonObjectEpisodeDetails.getString("direct_source"));
                                }
                                if (jsonObjectEpisodeDetails.getString("added") == null || jsonObjectEpisodeDetails.getString("added").isEmpty()) {
                                    getEpisdoeDetailsCallback.setAdded("");
                                } else {
                                    getEpisdoeDetailsCallback.setAdded(jsonObjectEpisodeDetails.getString("added"));
                                }
                                if (jsonObjectEpisodeDetails.getString("custom_sid") == null || jsonObjectEpisodeDetails.getString("custom_sid").isEmpty()) {
                                    getEpisdoeDetailsCallback.setCustomSid("");
                                } else {
                                    getEpisdoeDetailsCallback.setCustomSid(jsonObjectEpisodeDetails.getString("custom_sid"));
                                }
                                if (jsonObjectEpisodeDetails.getString("container_extension") == null || jsonObjectEpisodeDetails.getString("container_extension").isEmpty()) {
                                    getEpisdoeDetailsCallback.setContainerExtension("");
                                } else {
                                    getEpisdoeDetailsCallback.setContainerExtension(jsonObjectEpisodeDetails.getString("container_extension"));
                                }
                                this.episdoeDetailsCallbacksList.add(getEpisdoeDetailsCallback);
                            }
                            continue;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (this.episdoeDetailsCallbacksList == null || this.myRecyclerView == null || this.episdoeDetailsCallbacksList.size() == 0) {
            onFinish();
            if (this.tvNoStream != null) {
                this.tvNoStream.setVisibility(0);
                return;
            }
            return;
        }
        onFinish();
        this.episodeDetailAdapter = new EpisodeDetailAdapter(this.episdoeDetailsCallbacksList, this.context, this.seriesCover);
        this.myRecyclerView.setAdapter(this.episodeDetailAdapter);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivityV2.class));
        } else if (id == R.id.nav_live_tv) {
            Utils.set_layout_live(this.context);

        } else if (id == R.id.nav_archive) {
            startActivity(new Intent(this, TVArchiveActivity.class));
        } else if (id == R.id.nav_series) {
            startActivity(new Intent(this, SeriesTabActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_vod) {
            Utils.set_layout_vod(this.context);
        } else if (id == R.id.nav_account_info) {
            startActivity(new Intent(this, AccountInfoActivity.class));
        } else if (id == R.id.nav_live_tv_guide) {
            if (this.context != null) {
                Utils.startDashboardV2LoadTVGuid(this.context);
            }
        } else if (id == R.id.nav_logout && this.context != null) {
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
            alertDialog.setPositiveButton("YES", new C14541());
            alertDialog.setNegativeButton("NO", new C14552());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C14563());
            alertDialog.setNegativeButton("NO", new C14574());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
