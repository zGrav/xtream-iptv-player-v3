package z.xtreamiptv.playerv3.view.nstplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.view.adapter.SearchableAdapter;
import java.util.ArrayList;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class NSTPlayerArchiveActivity extends Activity implements OnClickListener {
    public Activity activity;
    SearchableAdapter adapter;
    public ArrayList<LiveStreamsDBModel> allMovies;
    public Context context;
    public long defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    private ArrayList<LiveStreamsDBModel> filterList;
    public View forwardButton;
    public boolean fullScreenOnly = true;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    public String mFilePath;
    SearchableAdapter mSearchableAdapter;
    public View nextButton;
    public View pauseButton;
    public View playButton;
    NSTPlayerArchive player;
    public View prevButton;
    public View rewindButton;
    public RelativeLayout rl_middle;
    public String scaleType;
    public boolean showNavIcon = true;
    public String title;
    public String url;

    public static class Config implements Parcelable {
        public static final Creator<Config> CREATOR = new C20901();
        private static boolean debug = true;
        private Activity activity;
        private long defaultRetryTime;
        private boolean fullScreenOnly;
        private String scaleType;
        private boolean showNavIcon;
        private String title;
        private String url;

        static class C20901 implements Creator<Config> {
            C20901() {
            }

            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            public Config[] newArray(int size) {
                return new Config[size];
            }
        }

        public Config(Activity activity) {
            this.fullScreenOnly = true;
            this.defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
            this.showNavIcon = true;
            this.activity = activity;
        }

        private Config(Parcel in) {
            boolean z = true;
            this.fullScreenOnly = true;
            this.defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
            this.showNavIcon = true;
            this.scaleType = in.readString();
            this.fullScreenOnly = in.readByte() != (byte) 0;
            this.defaultRetryTime = in.readLong();
            this.title = in.readString();
            this.url = in.readString();
            if (in.readByte() == (byte) 0) {
                z = false;
            }
            this.showNavIcon = z;
        }

        public static boolean isDebug() {
            return debug;
        }

        public Config debug(boolean debug) {
            debug = debug;
            return this;
        }

        public Config setTitle(String title) {
            this.title = title;
            return this;
        }

        public Config setDefaultRetryTime(long defaultRetryTime) {
            this.defaultRetryTime = defaultRetryTime;
            return this;
        }

        public void play(String url) {
            this.url = url;
            Intent intent = new Intent(this.activity, NSTPlayerArchiveActivity.class);
            intent.putExtra("config", this);
            this.activity.startActivity(intent);
        }

        public Config setScaleType(String scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Config setFullScreenOnly(boolean fullScreenOnly) {
            this.fullScreenOnly = fullScreenOnly;
            return this;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 1;
            dest.writeString(this.scaleType);
            dest.writeByte((byte) (this.fullScreenOnly ? 1 : 0));
            dest.writeLong(this.defaultRetryTime);
            dest.writeString(this.title);
            dest.writeString(this.url);
            if (!this.showNavIcon) {
                i = 0;
            }
            dest.writeByte((byte) i);
        }
    }

    public static void play(Activity context, String... url) {
        Intent intent = new Intent(context, NSTPlayerArchiveActivity.class);
        intent.putExtra(OnNativeInvokeListener.ARG_URL, url[0]);
        if (url.length > 1) {
            intent.putExtra("title", url[1]);
        }
        context.startActivity(intent);
    }

    public static Config configPlayer(Activity activity) {
        return new Config(activity);
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nst_player_archive_activity);
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String streamType = getIntent().getStringExtra("STREAM_TYPE");
        String videoTitle = getIntent().getStringExtra("VIDEO_TITLE");
        String epgChannelID = getIntent().getStringExtra("EPG_CHANNEL_ID");
        String epgChannelLogo = getIntent().getStringExtra("EPG_CHANNEL_LOGO");
        String stream_start_time = getIntent().getStringExtra("STREAM_START_TIME");
        String stream_stop_time = getIntent().getStringExtra("STREAM_STOP_TIME");
        String stream_duration = getIntent().getStringExtra("STREAM_DURATION");
        this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/timeshift/" + username + "/" + password + "/";
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.player = new NSTPlayerArchive(this);
        this.player.hideSystemUi();
        this.player.setTitle(videoTitle);
        this.player.setDefaultRetryTime(this.defaultRetryTime);
        this.player.setFullScreenOnly(this.fullScreenOnly);
        this.player.setScaleType(TextUtils.isEmpty(this.scaleType) ? "fitParent" : this.scaleType);
        this.player.setShowNavIcon(this.showNavIcon);
        this.player.showAll();
        this.player.play(this.mFilePath, opened_stream_id, allowedFormat, stream_start_time, stream_duration, stream_stop_time);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        this.playButton = findViewById(R.id.exo_play);
        if (this.playButton != null) {
            this.playButton.setOnClickListener(this);
        }
        this.pauseButton = findViewById(R.id.exo_pause);
        if (this.pauseButton != null) {
            this.pauseButton.setOnClickListener(this);
        }
        this.forwardButton = findViewById(R.id.exo_ffwd);
        if (this.forwardButton != null) {
            this.forwardButton.setOnClickListener(this);
        }
        this.rewindButton = findViewById(R.id.exo_rew);
        if (this.rewindButton != null) {
            this.rewindButton.setOnClickListener(this);
        }
        this.prevButton = findViewById(R.id.exo_prev);
        if (this.prevButton != null) {
            this.prevButton.setOnClickListener(this);
        }
        this.nextButton = findViewById(R.id.exo_next);
        if (this.nextButton != null) {
            this.nextButton.setOnClickListener(this);
        }
        this.rl_middle = (RelativeLayout) findViewById(R.id.middle);
    }

    public int getIndexOfMovies(ArrayList<LiveStreamsDBModel> allMovies, int num) {
        for (int i = 0; i < allMovies.size(); i++) {
            if (Integer.parseInt(((LiveStreamsDBModel) allMovies.get(i)).getNum()) == num) {
                return i;
            }
        }
        return 0;
    }

    protected void onPause() {
        super.onPause();
        if (this.player != null) {
            this.player.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.player != null) {
            this.player.hideSystemUi();
            this.player.onResume();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.player != null) {
            this.player.onDestroy();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.player != null) {
            this.player.onConfigurationChanged(newConfig);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown = event.getRepeatCount() == 0;
        switch (keyCode) {
            case 21:
            case 89:
            case 275:
                showTitleBarAndFooter();
                findViewById(R.id.exo_rew).performClick();
                return true;
            case 22:
            case 90:
            case 274:
                showTitleBarAndFooter();
                findViewById(R.id.exo_ffwd).performClick();
                return true;
            case 23:
                showTitleBarAndFooter();
                return true;
            case 62:
            case 79:
            case 85:
                if (!uniqueDown) {
                    return true;
                }
                showTitleBarAndFooter();
                this.player.doPauseResume();
                return true;
            case 66:
                showTitleBarAndFooter();
                return true;
            case 86:
            case 127:
                if (!uniqueDown || !this.player.isPlaying()) {
                    return true;
                }
                showTitleBarAndFooter();
                this.player.pause();
                playerPauseIconsUpdate();
                return true;
            case 126:
                if (!uniqueDown || this.player.isPlaying()) {
                    return true;
                }
                showTitleBarAndFooter();
                this.player.start();
                playerStartIconsUpdate();
                return true;
            case 166:
                showTitleBarAndFooter();
                return true;
            case 167:
                showTitleBarAndFooter();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void showTitleBarAndFooter() {
        findViewById(R.id.app_video_top_box).setVisibility(0);
        findViewById(R.id.app_video_bottom_box).setVisibility(0);
        findViewById(R.id.app_video_currentTime).setVisibility(0);
        findViewById(R.id.app_video_endTime).setVisibility(0);
        findViewById(R.id.app_video_seekBar).setVisibility(0);
        findViewById(R.id.controls).setVisibility(0);
    }

    public void hideTitleBarAndFooter() {
        findViewById(R.id.app_video_bottom_box).setVisibility(8);
        findViewById(R.id.app_video_currentTime).setVisibility(8);
        findViewById(R.id.app_video_endTime).setVisibility(8);
        findViewById(R.id.app_video_seekBar).setVisibility(8);
        findViewById(R.id.app_video_top_box).setVisibility(8);
        findViewById(R.id.controls).setVisibility(8);
    }

    private void playerStartIconsUpdate() {
        this.playButton.setVisibility(8);
        this.pauseButton.setVisibility(0);
    }

    private void playerPauseIconsUpdate() {
        this.pauseButton.setVisibility(8);
        this.playButton.setVisibility(0);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exo_ffwd:
                if (this.player != null) {
                    this.player.forward(0.2f);
                    return;
                }
                return;
            case R.id.exo_pause:
                if (this.player != null && this.pauseButton != null) {
                    this.player.pause();
                    playerPauseIconsUpdate();
                    return;
                }
                return;
            case R.id.exo_play:
                if (this.player != null && this.playButton != null) {
                    this.player.start();
                    playerStartIconsUpdate();
                    return;
                }
                return;
            case R.id.exo_rew:
                if (this.player != null) {
                    this.player.forward(-0.2f);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        if (findViewById(R.id.app_video_top_box).getVisibility() == 0) {
            hideTitleBarAndFooter();
        } else if (this.player == null || !this.player.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
