package z.xtreamiptv.playerv3.v2api.view.exoplayer2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView.VisibilityListener;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.view.AppPref;
import z.xtreamiptv.playerv3.view.adapter.SearchableAdapter;
import z.xtreamiptv.playerv3.view.app_shell.AppController;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerArchiveActivity extends Activity implements OnClickListener, VisibilityListener {
    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String ACTION_VIEW_LIST = "com.google.android.exoplayer.demo.action.VIEW_LIST";
    public static final String AD_TAG_URI_EXTRA = "ad_tag_uri";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER = new CookieManager();
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_MULTI_SESSION = "drm_multi_session";
    public static final String DRM_SCHEME_EXTRA = "drm_scheme";
    private static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String EXTENSION_EXTRA = "extension";
    public static final String EXTENSION_LIST_EXTRA = "extension_list";
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";
    private static final Map<Integer, Integer> RESIZE_MODE = Collections.unmodifiableMap(new C16421());
    public static final String URI_LIST_EXTRA = "uri_list";
    private int CURRENT_RESIZE_MODE = 0;
    private ViewGroup adUiViewGroup;
    SearchableAdapter adapter;
    private AdsLoader adsLoader;
    private ArrayList<LiveStreamsDBModel> allStreams;
    private AppCompatImageView btnList;
    private AppCompatImageView btn_screen;
    private AppCompatImageView btn_settings;
    public ImageView channelLogo;
    public Context context;
    public TextView currentProgram;
    public TextView currentProgramTime;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private DebugTextViewHelper debugViewHelper;
    public EditText et_search;
    private EventLogger eventLogger;
    private RelativeLayout exo_playback_control_view;
    String extension;
    private TrackGroupArray lastSeenTrackGroupArray;
    public ListView listChannels;
    LiveStreamDBHandler liveStreamDBHandler;
    public LinearLayout ll_seekbar_time;
    private Uri loadedAdTagUri;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    private Handler mainHandler;
    private Factory mediaDataSourceFactory;
    private boolean needRetrySource;
    public TextView nextProgram;
    public TextView nextProgramTime;
    private SimpleExoPlayer player;
    private SimpleDateFormat programTimeFormat = new SimpleDateFormat("HH:mm");
    ProgressBar progressBar;
    ProgressBar progressLoader;
    private long resumePosition;
    private int resumeWindow;
    private Button retryButton;
    private boolean shouldAutoPlay;
    private SimpleExoPlayerView simpleExoPlayerView;
    private TrackSelectionHelper trackSelectionHelper;
    private DefaultTrackSelector trackSelector;
    private String videoTitle;
    private int video_id;
    private int video_num;
    private AppCompatTextView video_title;

    static class C16421 extends HashMap<Integer, Integer> {
        C16421() {
            put(Integer.valueOf(3), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(0), Integer.valueOf(R.drawable.ic_fullscreen_black_24dp));
            put(Integer.valueOf(1), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(2), Integer.valueOf(R.drawable.ic_zoom_out_map_black_24dp));
        }
    }

    class C16432 implements MediaSourceFactory {
        C16432() {
        }

        public MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
            return PlayerArchiveActivity.this.buildMediaSource(uri, null, handler, listener);
        }

        public int[] getSupportedTypes() {
            return new int[]{0, 1, 2, 3};
        }
    }

    private class PlayerEventListener extends DefaultEventListener {
        private PlayerEventListener() {
        }

        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == 2) {
                PlayerArchiveActivity.this.showLoader();
            } else if (playbackState == 4) {
                PlayerArchiveActivity.this.showControls();
            } else if (playbackState == 3) {
                PlayerArchiveActivity.this.hideLoader();
            }
            PlayerArchiveActivity.this.updateButtonVisibilities();
        }

        public void onPositionDiscontinuity(int reason) {
            if (PlayerArchiveActivity.this.needRetrySource) {
                PlayerArchiveActivity.this.updateResumePosition();
            }
        }

        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == 1) {
                Exception cause = e.getRendererException();
                if (cause instanceof DecoderInitializationException) {
                    DecoderInitializationException decoderInitializationException = (DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName != null) {
                        errorString = PlayerArchiveActivity.this.getString(R.string.error_instantiating_decoder, new Object[]{decoderInitializationException.decoderName});
                    } else if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = PlayerArchiveActivity.this.getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = PlayerArchiveActivity.this.getString(R.string.error_no_secure_decoder, new Object[]{decoderInitializationException.mimeType});
                    } else {
                        errorString = PlayerArchiveActivity.this.getString(R.string.error_no_decoder, new Object[]{decoderInitializationException.mimeType});
                    }
                }
            }
            if (errorString != null) {
                PlayerArchiveActivity.this.showToast(errorString);
            }
            PlayerArchiveActivity.this.needRetrySource = true;
            if (PlayerArchiveActivity.isBehindLiveWindow(e)) {
                PlayerArchiveActivity.this.clearResumePosition();
                PlayerArchiveActivity.this.initializePlayer();
                return;
            }
            PlayerArchiveActivity.this.updateResumePosition();
            PlayerArchiveActivity.this.updateButtonVisibilities();
            PlayerArchiveActivity.this.showControls();
            PlayerArchiveActivity.this.hideLoader();
        }

        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            PlayerArchiveActivity.this.updateButtonVisibilities();
            if (trackGroups != PlayerArchiveActivity.this.lastSeenTrackGroupArray) {
                MappedTrackInfo mappedTrackInfo = PlayerArchiveActivity.this.trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(2) == 1) {
                        PlayerArchiveActivity.this.showToast((int) R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(1) == 1) {
                        PlayerArchiveActivity.this.showToast((int) R.string.error_unsupported_audio);
                    }
                }
                PlayerArchiveActivity.this.lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

    static {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.context = this;
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.shouldAutoPlay = true;
        clearResumePosition();
        this.mediaDataSourceFactory = buildDataSourceFactory(true);
        this.mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.player_archive_activity);
        View rootView = findViewById(R.id.root);
        this.debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        this.debugTextView = (TextView) findViewById(R.id.debug_text_view);
        this.retryButton = (Button) findViewById(R.id.retry_button);
        this.simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        this.simpleExoPlayerView.requestFocus();
        this.CURRENT_RESIZE_MODE = AppPref.getInstance(this).getResizeMode();
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.video_title = (AppCompatTextView) findViewById(R.id.title);
        this.btn_settings = (AppCompatImageView) findViewById(R.id.btn_settings);
        AppCompatImageView btn_back = (AppCompatImageView) findViewById(R.id.btn_back);
        AppCompatImageView btn_list = (AppCompatImageView) findViewById(R.id.btn_list);
        this.btn_screen = (AppCompatImageView) findViewById(R.id.btn_screen);
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.exo_playback_control_view = (RelativeLayout) findViewById(R.id.exo_playback_control_view);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressLoader = (ProgressBar) findViewById(R.id.progress_loader);
        this.ll_seekbar_time = (LinearLayout) findViewById(R.id.ll_seekbar_time);
        this.channelLogo = (ImageView) findViewById(R.id.iv_channel_logo);
        this.currentProgram = (TextView) findViewById(R.id.tv_current_program);
        this.currentProgramTime = (TextView) findViewById(R.id.tv_current_time);
        this.nextProgram = (TextView) findViewById(R.id.tv_next_program);
        this.nextProgramTime = (TextView) findViewById(R.id.tv_next_program_time);
        this.btn_screen.setOnClickListener(this);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        btn_back.setOnClickListener(this);
        this.btn_settings.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        this.retryButton.setOnClickListener(this);
        this.simpleExoPlayerView.setControllerVisibilityListener(this);
        rootView.setOnClickListener(this);
    }

    public void onNewIntent(Intent intent) {
        releasePlayer();
        this.shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }

    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || this.player == null) {
            initializePlayer();
        }
    }

    @SuppressLint({"InlinedApi"})
    private void hideSystemUi() {
        this.simpleExoPlayerView.setSystemUiVisibility(4871);
    }

    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        releaseAdsLoader();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            showToast((int) R.string.storage_permission_denied);
            finish();
            return;
        }
        initializePlayer();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        return this.simpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                return;
            case R.id.btn_screen:
                toggleResizeMode();
                return;
            case R.id.btn_settings:
                try {
                    if (this.trackSelector.getCurrentMappedTrackInfo() != null) {
                        this.trackSelectionHelper.showSelectionDialog(this, "Select", this.trackSelector.getCurrentMappedTrackInfo(), 0);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onClick: " + e.getMessage());
                    return;
                }
            case R.id.retry_button:
                initializePlayer();
                return;
            default:
                return;
        }
    }

    public void toggleChannelList() {
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown = event.getRepeatCount() == 0;
        switch (keyCode) {
            case 62:
            case 79:
            case 85:
                return uniqueDown ? true : true;
            case 86:
            case 126:
            case 127:
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public int getIndexOfStreams(ArrayList<LiveStreamsDBModel> allStreams, int num) {
        for (int i = 0; i < allStreams.size(); i++) {
            if (Integer.parseInt(((LiveStreamsDBModel) allStreams.get(i)).getNum()) == num) {
                return i;
            }
        }
        return 0;
    }

    private void previousLive() {
        if (!this.simpleExoPlayerView.getPlayer().getCurrentTimeline().isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            if (currentWindowIndex == 0) {
                this.player.seekTo(this.allStreams.size() - 1, C.TIME_UNSET);
            } else {
                this.player.seekTo(currentWindowIndex - 1, C.TIME_UNSET);
            }
        }
    }

    private void toggleResizeMode() {
        this.CURRENT_RESIZE_MODE++;
        if (this.CURRENT_RESIZE_MODE >= RESIZE_MODE.size()) {
            this.CURRENT_RESIZE_MODE = 0;
        }
        AppPref.getInstance(this).setResizeMode(this.CURRENT_RESIZE_MODE);
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
    }

    private void next() {
        if (!this.simpleExoPlayerView.getPlayer().getCurrentTimeline().isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            if (currentWindowIndex == this.allStreams.size() - 1) {
                this.player.seekTo(0, C.TIME_UNSET);
            } else {
                this.player.seekTo(currentWindowIndex + 1, C.TIME_UNSET);
            }
        }
    }

    public void onVisibilityChange(int visibility) {
        this.debugRootView.setVisibility(visibility);
    }

    private void initializePlayer() {
        Intent intent = getIntent();
        int video_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String stream_start_time = getIntent().getStringExtra("STREAM_START_TIME");
        String stream_stop_time = getIntent().getStringExtra("STREAM_STOP_TIME");
        int currentWindowIndex = this.video_num;
        if (this.video_title != null) {
            String vtitle = getIntent().getStringExtra("VIDEO_TITLE");
            if (!Utils.isEmpty(vtitle)) {
                this.video_title.setText(vtitle);
            }
        }
        boolean needNewPlayer = this.player == null;
        if (needNewPlayer) {
            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            this.trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            this.trackSelectionHelper = new TrackSelectionHelper(this.trackSelector, adaptiveTrackSelectionFactory);
            this.lastSeenTrackGroupArray = null;
            this.eventLogger = new EventLogger(this.trackSelector);
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (intent.hasExtra("drm_scheme") || intent.hasExtra(DRM_SCHEME_UUID_EXTRA)) {
                String drmLicenseUrl = intent.getStringExtra("drm_license_url");
                String[] keyRequestPropertiesArray = intent.getStringArrayExtra("drm_key_request_properties");
                boolean multiSession = intent.getBooleanExtra("drm_multi_session", false);
                int errorStringId = R.string.error_drm_unknown;
                if (Util.SDK_INT < 18) {
                    errorStringId = R.string.error_drm_not_supported;
                } else {
                    try {
                        drmSessionManager = buildDrmSessionManagerV18(DemoUtil.getDrmUuid(intent.getStringExtra(intent.hasExtra("drm_scheme") ? "drm_scheme" : DRM_SCHEME_UUID_EXTRA)), drmLicenseUrl, keyRequestPropertiesArray, multiSession);
                    } catch (UnsupportedDrmException e) {
                        errorStringId = e.reason == 1 ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
                    }
                }
                if (drmSessionManager == null) {
                    showToast(errorStringId);
                    return;
                }
            }
            boolean preferExtensionDecoders = intent.getBooleanExtra("prefer_extension_decoders", false);
            if (AppController.getInstance().useExtensionRenderers()) {
                if (preferExtensionDecoders) {
                }
            }
            this.player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this, drmSessionManager, 2), this.trackSelector);
            this.player.addListener(new PlayerEventListener());
            this.player.addListener(this.eventLogger);
            this.player.addMetadataOutput(this.eventLogger);
            this.player.addAudioDebugListener(this.eventLogger);
            this.player.addVideoDebugListener(this.eventLogger);
            this.simpleExoPlayerView.setPlayer(this.player);
            this.player.setPlayWhenReady(this.shouldAutoPlay);
            this.debugViewHelper = new DebugTextViewHelper(this.player, this.debugTextView);
            this.debugViewHelper.start();
        }
        if (needNewPlayer || this.needRetrySource) {
            Uri[] uris;
            String[] extensions;
            int i;
            String action = intent.getAction();
            if ("com.google.android.exoplayer.demo.action.VIEW".equals(action)) {
                Uri stream_uri = buildURI(video_id, this.extension, stream_stop_time, stream_start_time);
                uris = new Uri[]{stream_uri};
                extensions = new String[uris.length];
            } else if ("com.google.android.exoplayer.demo.action.VIEW_LIST".equals(action)) {
                uris = new Uri[this.allStreams.size()];
                extensions = new String[this.allStreams.size()];
                for (i = 0; i < this.allStreams.size(); i++) {
                    uris[i] = buildURI(Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(i)).getStreamId()), this.extension, stream_stop_time, stream_start_time);
                }
            } else {
                showToast(getString(R.string.unexpected_intent_action, new Object[]{action}));
                return;
            }
            if (!Util.maybeRequestReadExternalStoragePermission(this, uris)) {
                MediaSource mediaSource;
                MediaSource[] mediaSources = new MediaSource[uris.length];
                for (i = 0; i < uris.length; i++) {
                    mediaSources[i] = buildMediaSource(uris[i], extensions[i], this.mainHandler, this.eventLogger);
                }
                if (mediaSources.length == 1) {
                    mediaSource = mediaSources[0];
                } else {
                    mediaSource = new ConcatenatingMediaSource(mediaSources);
                }
                String adTagUriString = intent.getStringExtra("ad_tag_uri");
                if (adTagUriString != null) {
                    Uri adTagUri = Uri.parse(adTagUriString);
                    if (!adTagUri.equals(this.loadedAdTagUri)) {
                        releaseAdsLoader();
                        this.loadedAdTagUri = adTagUri;
                    }
                    try {
                        mediaSource = createAdsMediaSource(mediaSource, Uri.parse(adTagUriString));
                    } catch (Exception e2) {
                        showToast("Ima not loaded");
                    }
                } else {
                    releaseAdsLoader();
                }
                boolean haveResumePosition = this.resumeWindow != -1;
                if (haveResumePosition) {
                    this.player.seekTo(this.resumeWindow, this.resumePosition);
                }
                this.player.seekToDefaultPosition();
                this.player.prepare(mediaSource, !haveResumePosition, false);
                this.needRetrySource = false;
                updateButtonVisibilities();
            } else {
                return;
            }
        }
        this.simpleExoPlayerView.showController();
    }

    private Uri buildURI(int stream_id, String extension, String stream_stop_time, String stream_start_time) {
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        Builder builder = new Builder();
        try {
            builder.scheme("http").encodedAuthority(serverUrl + ":" + serverPort).appendPath("timeshift").appendPath(username).appendPath(password).appendEncodedPath(stream_stop_time).appendEncodedPath(stream_start_time).appendPath(Integer.toString(stream_id) + "." + allowedFormat);
            return builder.build();
        } catch (Exception e) {
            Log.e("DB", "initializePlayer: " + e.getMessage());
            return null;
        }
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
        int type;
        if (TextUtils.isEmpty(overrideExtension)) {
            type = Util.inferContentType(uri);
        } else {
            type = Util.inferContentType("." + overrideExtension);
        }
        switch (type) {
            case 0:
                return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(this.mediaDataSourceFactory), buildDataSourceFactory(false)).createMediaSource(uri, handler, listener);
            case 1:
                return new SsMediaSource.Factory(new DefaultSsChunkSource.Factory(this.mediaDataSourceFactory), buildDataSourceFactory(false)).createMediaSource(uri, handler, listener);
            case 2:
                return new HlsMediaSource.Factory(this.mediaDataSourceFactory).createMediaSource(uri, handler, listener);
            case 3:
                return new ExtractorMediaSource.Factory(this.mediaDataSourceFactory).createMediaSource(uri, handler, listener);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession) throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null, this.mainHandler, this.eventLogger, multiSession);
    }

    private void releasePlayer() {
        if (this.player != null) {
            this.debugViewHelper.stop();
            this.debugViewHelper = null;
            this.shouldAutoPlay = this.player.getPlayWhenReady();
            updateResumePosition();
            this.player.release();
            this.player = null;
            this.trackSelector = null;
            this.trackSelectionHelper = null;
            this.eventLogger = null;
        }
    }

    private void updateResumePosition() {
        this.resumeWindow = this.player.getCurrentWindowIndex();
        this.resumePosition = Math.max(0, this.player.getContentPosition());
    }

    private void clearResumePosition() {
        this.resumeWindow = -1;
        this.resumePosition = C.TIME_UNSET;
    }

    private Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return AppController.getInstance().buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return AppController.getInstance().buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private MediaSource createAdsMediaSource(MediaSource mediaSource, Uri adTagUri) throws Exception {
        Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
        if (this.adsLoader == null) {
            this.adsLoader = (AdsLoader) loaderClass.getConstructor(new Class[]{Context.class, Uri.class}).newInstance(new Object[]{this, adTagUri});
            this.adUiViewGroup = new FrameLayout(this);
            this.simpleExoPlayerView.getOverlayFrameLayout().addView(this.adUiViewGroup);
        }
        return new AdsMediaSource(mediaSource, new C16432(), this.adsLoader, this.adUiViewGroup, this.mainHandler, this.eventLogger);
    }

    private void releaseAdsLoader() {
        if (this.adsLoader != null) {
            this.adsLoader.release();
            this.adsLoader = null;
            this.loadedAdTagUri = null;
            this.simpleExoPlayerView.getOverlayFrameLayout().removeAllViews();
        }
    }

    private void updateButtonVisibilities() {
        this.debugRootView.removeAllViews();
        this.retryButton.setVisibility(this.needRetrySource ? 0 : 8);
        this.debugRootView.addView(this.retryButton);
        if (this.player != null) {
            MappedTrackInfo mappedTrackInfo = this.trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                for (int i = 0; i < mappedTrackInfo.length; i++) {
                    if (mappedTrackInfo.getTrackGroups(i).length != 0) {
                        int label = 0;
                        Button button = new Button(this);
                        switch (this.player.getRendererType(i)) {
                            case 1:
                                label = R.string.audio;
                                break;
                            case 2:
                                label = R.string.video;
                                break;
                            case 3:
                                label = R.string.text;
                                break;
                            default:
                                break;
                        }
                        button.setText(label);
                        button.setTag(Integer.valueOf(i));
                        button.setOnClickListener(this);
                    }
                }
            }
        }
    }

    private void showControls() {
        this.debugRootView.setVisibility(0);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, 1).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != 0) {
            return false;
        }
        for (Throwable cause = e.getSourceException(); cause != null; cause = cause.getCause()) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
        }
        return false;
    }

    private void showLoader() {
        if (this.progressLoader != null) {
            this.progressLoader.setVisibility(0);
        }
    }

    private void hideLoader() {
        if (this.progressLoader != null) {
            this.progressLoader.setVisibility(8);
        }
    }
}
