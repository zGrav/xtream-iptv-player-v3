package z.xtreamiptv.playerv3.v2api.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import butterknife.BindView;
import butterknife.ButterKnife;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.presenter.LoginPresenter;
import z.xtreamiptv.playerv3.view.activity.LoginActivity;
import z.xtreamiptv.playerv3.view.utility.LoadingSpinner;

public class ExoPlayerVodActivity extends Activity implements OnClickListener {
    private Context context;
    private SharedPreferences loginPreferencesSharedPref;
    private LoginPresenter loginPresenter;
    private String mFilePath;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private ProgressDialog progressDialog;
    private Button retryButton;
    VideoView videoView = null;
    public LoadingSpinner video_loader;

    class C14591 implements OnPreparedListener {

        class C14581 implements OnVideoSizeChangedListener {
            C14581() {
            }

            public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                ExoPlayerVodActivity.this.video_loader.setVisibility(8);
                mp.start();
            }
        }

        C14591() {
        }

        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mp.setOnVideoSizeChangedListener(new C14581());
        }
    }

    class C14602 implements OnErrorListener {
        C14602() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            ExoPlayerVodActivity.this.video_loader.setVisibility(8);
            ExoPlayerVodActivity.this.retryButton.setVisibility(0);
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_exoplayer_vod);
        ButterKnife.bind(this);
        changeStatusBarColor();
        initialize();
    }

    private void initialize() {
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String containerExtension = getIntent().getStringExtra("CONTAINER_EXTENSION");
        String streamType = getIntent().getStringExtra("STREAM_TYPE");
        this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/movie/" + username + "/" + password + "/" + opened_stream_id + "." + containerExtension;
        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.retryButton = (Button) findViewById(R.id.retry_button);
        this.retryButton.setOnClickListener(this);
        this.video_loader = (LoadingSpinner) findViewById(R.id.iv_video_loader);
        preparePlayer();
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

    public void preparePlayer() {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.videoView);
        Uri uri = Uri.parse(this.mFilePath);
        this.videoView.setMediaController(mediaController);
        this.videoView.setVideoURI(uri);
        this.videoView.requestFocus();
        this.retryButton.setVisibility(8);
        this.video_loader.setVisibility(0);
        this.videoView.setOnPreparedListener(new C14591());
        this.videoView.setOnErrorListener(new C14602());
    }

    public void onClick(View view) {
        if (view == this.retryButton) {
            preparePlayer();
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

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onResume() {
        super.onResume();
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesSharedPref.getString("username", "").equals("") && this.loginPreferencesSharedPref.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context != null) {
            onFinish();
        }
    }
}
