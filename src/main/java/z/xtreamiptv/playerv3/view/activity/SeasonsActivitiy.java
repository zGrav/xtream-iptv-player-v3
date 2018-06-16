package z.xtreamiptv.playerv3.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
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
import com.google.gson.JsonElement;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.callback.SeasonsDetailCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.presenter.XMLTVPresenter;
import z.xtreamiptv.playerv3.v2api.model.GetEpisdoeDetailsCallback;
import z.xtreamiptv.playerv3.v2api.model.database.SeriesStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.v2api.presenter.SeriesPresenter;
import z.xtreamiptv.playerv3.v2api.view.interfaces.SeriesInterface;
import z.xtreamiptv.playerv3.view.adapter.EpisodeDetailAdapter;
import z.xtreamiptv.playerv3.view.adapter.SeasonsAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeasonsActivitiy extends AppCompatActivity implements SeriesInterface, OnNavigationItemSelectedListener, OnClickListener {
    static ProgressBar pbPagingLoader1;
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    ArrayList<LiveStreamCategoryIdDBModel> categoriesList;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    TextView clientNameTv;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ArrayList<GetEpisdoeDetailsCallback> episdoeDetailsCallbacksList = new ArrayList();
    private EpisodeDetailAdapter episodeDetailAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesAfterLogin;
    private SeasonsAdapter mAdapter;
    private LayoutManager mLayoutManager;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.rl_settings)
    RelativeLayout rlSettings;
    @BindView(R.id.rl_vod_layout)
    RelativeLayout rl_vod_layout;
    SearchView searchView;
    private ArrayList<SeasonsDetailCallback> seasonsDetailCallbacks = new ArrayList();
    @BindView(R.id.tv_settings)
    TextView seasonsName;
    private String seriesCover = "";
    private String seriesId = "";
    private String seriesName = "";
    private SeriesPresenter seriesPresenter;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    ArrayList<LiveStreamCategoryIdDBModel> subCategoryList;
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
    private String userName = "";
    private String userPassword = "";
    private XMLTVPresenter xmltvPresenter;

    class C18271 implements DialogInterface.OnClickListener {
        C18271() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsVodandSeries(SeasonsActivitiy.this.context);
        }
    }

    class C18282 implements DialogInterface.OnClickListener {
        C18282() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C18293 implements DialogInterface.OnClickListener {
        C18293() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuidV2Api(SeasonsActivitiy.this.context);
        }
    }

    class C18304 implements DialogInterface.OnClickListener {
        C18304() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons_activitiy);
        ButterKnife.bind(this);
        changeStatusBarColor();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            if (this.rl_vod_layout != null) {
                this.rl_vod_layout.setBackground(getResources().getDrawable(R.drawable.layout_background_tv));
            }
        } else if (this.rl_vod_layout != null) {
            this.rl_vod_layout.setBackground(getResources().getDrawable(R.drawable.layout_background));
        }
        getWindow().setFlags(1024, 1024);
        changeStatusBarColor();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setToggleIconPosition();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_series).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_series).setVisible(true);
        if (this.tvHeaderTitle != null) {
            this.tvHeaderTitle.setOnClickListener(this);
        }
        initializeV();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(8388611)) {
            drawer.closeDrawer(8388611);
        } else {
            super.onBackPressed();
        }
        if (this.mAdapter != null && pbPagingLoader1 != null) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
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
            if (this.context != null) {
                Utils.startDashboardV2LoadTVGuid(this.context);
            }
        } else if (id == R.id.nav_logout && this.context != null) {
            Utils.logoutUser(this.context);
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(8388611);
        return true;
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

    private void initializeV() {
        this.context = this;
        this.seriesPresenter = new SeriesPresenter(this.context, this);
        Intent intent = getIntent();
        if (intent != null) {
            this.seriesId = intent.getStringExtra(AppConst.SERIES_SERIES_ID);
            this.seriesCover = intent.getStringExtra(AppConst.SERIES_COVER);
            this.seriesName = intent.getStringExtra(AppConst.SERIES_NAME);
            if (!(this.seriesName == null || this.seriesName.isEmpty())) {
                this.seasonsName.setText(this.seriesName);
            }
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String password = this.loginPreferencesAfterLogin.getString("password", "");
        if (this.seriesId != null && !this.seriesId.isEmpty() && this.seriesPresenter != null && username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            this.seriesPresenter.getSeriesEpisode(username, password, this.seriesId);
        }
    }

    public void getSeriesEpisodeInfo(JsonElement jsonElement) {
        if (jsonElement != null) {
            JSONObject jsonObject;
            JSONObject jsonArrayEpisodes;
            String key;
            SeasonsDetailCallback seasonsDetailCallback;
            Iterator<String> iterator;
            JSONArray jsonArrayEpisode;
            int i;
            JSONObject jsonObjectEpisodeDetails;
            GetEpisdoeDetailsCallback getEpisdoeDetailsCallback;
            try {
                jsonObject = new JSONObject(jsonElement.toString());
                jsonArrayEpisodes = null;
                JSONObject jsonArraySeasons = null;
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArraySeasons = jsonObject.getJSONObject("seasons");
                }
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                }
                this.episdoeDetailsCallbacksList.clear();
                Iterator<?> keys = jsonArraySeasons.keys();
                while (keys.hasNext()) {
                    key = (String) keys.next();
                    if (jsonArraySeasons.get(key) instanceof JSONObject) {
                        seasonsDetailCallback = new SeasonsDetailCallback();
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date")).isEmpty()) {
                            seasonsDetailCallback.setAirDate("");
                        } else {
                            seasonsDetailCallback.setAirDate((String) ((JSONObject) jsonArraySeasons.get(key)).get("air_date"));
                        }
                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count")).intValue() == 0) {
                            seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
                        } else {
                            seasonsDetailCallback.setEpisodeCount((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("episode_count"));
                        }
                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id")).intValue() == 0) {
                            seasonsDetailCallback.setId(Integer.valueOf(-1));
                        } else {
                            seasonsDetailCallback.setId((Integer) ((JSONObject) jsonArraySeasons.get(key)).get("id"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("name")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("name")).isEmpty()) {
                            seasonsDetailCallback.setName("");
                        } else {
                            seasonsDetailCallback.setName((String) ((JSONObject) jsonArraySeasons.get(key)).get("name"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview")).isEmpty()) {
                            seasonsDetailCallback.setOverview("");
                        } else {
                            seasonsDetailCallback.setOverview((String) ((JSONObject) jsonArraySeasons.get(key)).get("overview"));
                        }
                        if (((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER)) == null || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER)).intValue() == -1 || ((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER)).intValue() == 0) {
                            seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
                        } else {
                            seasonsDetailCallback.setSeasonNumber((Integer) ((JSONObject) jsonArraySeasons.get(key)).get(AppConst.SEASON_NUMBER));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover")).isEmpty()) {
                            seasonsDetailCallback.setCover("");
                        } else {
                            seasonsDetailCallback.setCover((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover"));
                        }
                        if (((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big")) == null || ((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big")).isEmpty()) {
                            seasonsDetailCallback.setCoverBig("");
                        } else {
                            seasonsDetailCallback.setCoverBig((String) ((JSONObject) jsonArraySeasons.get(key)).get("cover_big"));
                        }
                        this.seasonsDetailCallbacks.add(seasonsDetailCallback);
                    }
                }
                if (jsonArrayEpisodes != null) {
                    iterator = jsonArrayEpisodes.keys();
                    while (iterator.hasNext()) {
                        key = (String) iterator.next();
                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                            jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                            for (i = 0; i < jsonArrayEpisode.length(); i++) {
                                jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
                                }
                                if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
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
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                jsonObject = new JSONObject(jsonElement.toString());
                jsonArrayEpisodes = null;
                JSONArray jsonArraySeasons2 = jsonObject.getJSONArray("seasons");
                if (jsonObject.getJSONObject("episodes") != null) {
                    jsonArrayEpisodes = jsonObject.getJSONObject("episodes");
                }
                this.episdoeDetailsCallbacksList.clear();
                int k = 0;
                while (k < jsonArraySeasons2.length()) {
                    seasonsDetailCallback = new SeasonsDetailCallback();
                    String airDate = ((JSONObject) jsonArraySeasons2.get(k)).getString("air_date");
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("air_date") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("air_date").isEmpty()) {
                        seasonsDetailCallback.setAirDate("");
                    } else {
                        seasonsDetailCallback.setAirDate(((JSONObject) jsonArraySeasons2.get(k)).getString("air_date"));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("episode_count") == null || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("episode_count")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("episode_count")).intValue() == 0) {
                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(-1));
                    } else {
                        seasonsDetailCallback.setEpisodeCount(Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("episode_count")));
                    }
                    if (Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")) == null || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")).intValue() == 0) {
                        seasonsDetailCallback.setId(Integer.valueOf(-1));
                    } else {
                        seasonsDetailCallback.setId(Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt("id")));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("name") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("name").isEmpty()) {
                        seasonsDetailCallback.setName("");
                    } else {
                        seasonsDetailCallback.setName(((JSONObject) jsonArraySeasons2.get(k)).getString("name"));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("overview") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("overview").isEmpty()) {
                        seasonsDetailCallback.setOverview("");
                    } else {
                        seasonsDetailCallback.setOverview(((JSONObject) jsonArraySeasons2.get(k)).getString("overview"));
                    }
                    if (Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt(AppConst.SEASON_NUMBER)) == null || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt(AppConst.SEASON_NUMBER)).intValue() == -1 || Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt(AppConst.SEASON_NUMBER)).intValue() == 0) {
                        seasonsDetailCallback.setSeasonNumber(Integer.valueOf(-1));
                    } else {
                        seasonsDetailCallback.setSeasonNumber(Integer.valueOf(((JSONObject) jsonArraySeasons2.get(k)).getInt(AppConst.SEASON_NUMBER)));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("cover") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("cover").isEmpty()) {
                        seasonsDetailCallback.setCover("");
                    } else {
                        seasonsDetailCallback.setCover(((JSONObject) jsonArraySeasons2.get(k)).getString("cover"));
                    }
                    if (((JSONObject) jsonArraySeasons2.get(k)).getString("cover_big") == null || ((JSONObject) jsonArraySeasons2.get(k)).getString("cover_big").isEmpty()) {
                        seasonsDetailCallback.setCoverBig("");
                    } else {
                        seasonsDetailCallback.setCoverBig(((JSONObject) jsonArraySeasons2.get(k)).getString("cover_big"));
                    }
                    this.seasonsDetailCallbacks.add(seasonsDetailCallback);
                    k++;
                }
                if (jsonArrayEpisodes != null) {
                    iterator = jsonArrayEpisodes.keys();
                    while (iterator.hasNext()) {
                        key = (String) iterator.next();
                        if (jsonArrayEpisodes.get(key) instanceof JSONArray) {
                            jsonArrayEpisode = new JSONArray(jsonArrayEpisodes.get(key).toString());
                            for (i = 0; i < jsonArrayEpisode.length(); i++) {
                                jsonObjectEpisodeDetails = jsonArrayEpisode.getJSONObject(i);
                                getEpisdoeDetailsCallback = new GetEpisdoeDetailsCallback();
                                if (jsonObjectEpisodeDetails.getString("id") == null || jsonObjectEpisodeDetails.getString("id").isEmpty()) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setId(jsonObjectEpisodeDetails.getString("id"));
                                }
                                if (jsonObjectEpisodeDetails.getInt("season") == -1 || jsonObjectEpisodeDetails.getInt("season") == 0) {
                                    getEpisdoeDetailsCallback.setId("");
                                } else {
                                    getEpisdoeDetailsCallback.setSeasonNumber(Integer.valueOf(jsonObjectEpisodeDetails.getInt("season")));
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
                        }
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        if (this.context != null) {
            onFinish();
            this.myRecyclerView.setHasFixedSize(true);
            this.mLayoutManager = new LinearLayoutManager(this.context);
            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.mLayoutManager = new GridLayoutManager(this, 2);
            } else {
                this.mLayoutManager = new GridLayoutManager(this, 1);
            }
            this.myRecyclerView.setLayoutManager(this.mLayoutManager);
            this.myRecyclerView.setVisibility(0);
            if (this.episdoeDetailsCallbacksList == null || this.seasonsDetailCallbacks == null || this.seasonsDetailCallbacks.size() <= 0) {
                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
                this.myRecyclerView.setAdapter(this.mAdapter);
                this.tvNoRecordFound.setVisibility(0);
                this.tvNoRecordFound.setText(getResources().getString(R.string.no_season_dound));
                return;
            }
            this.mAdapter = new SeasonsAdapter(this.episdoeDetailsCallbacksList, this.seasonsDetailCallbacks, this.context);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            this.myRecyclerView.setAdapter(this.mAdapter);
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

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
        if (this.mAdapter != null) {
            this.mAdapter.setVisibiltygone(pbPagingLoader1);
        }
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context != null) {
        }
        headerView();
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
            alertDialog.setPositiveButton("YES", new C18271());
            alertDialog.setNegativeButton("NO", new C18282());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C18293());
            alertDialog.setNegativeButton("NO", new C18304());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
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
}
