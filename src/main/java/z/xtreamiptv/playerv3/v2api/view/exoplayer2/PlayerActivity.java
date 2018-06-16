package z.xtreamiptv.playerv3.v2api.view.exoplayer2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
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
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import z.xtreamiptv.playerv3.view.AppPref;
import z.xtreamiptv.playerv3.view.adapter.SearchableAdapter;
import z.xtreamiptv.playerv3.view.app_shell.AppController;
import com.squareup.picasso.Picasso;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class PlayerActivity extends Activity implements OnClickListener, VisibilityListener {
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
    private static final Map<Integer, Integer> RESIZE_MODE = Collections.unmodifiableMap(new C16381());
    public static final String URI_LIST_EXTRA = "uri_list";
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private int CURRENT_RESIZE_MODE = 0;
    private ViewGroup adUiViewGroup;
    SearchableAdapter adapter;
    private AdsLoader adsLoader;
    private ArrayList<LiveStreamCategoryIdDBModel> allLiveCategories;
    private ArrayList<LiveStreamsDBModel> allStreams;
    private AppCompatImageView btn_cat_back;
    private AppCompatImageView btn_cat_forward;
    private AppCompatImageView btn_list;
    private AppCompatImageView btn_screen;
    private AppCompatImageView btn_settings;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    public ImageView channelLogo;
    public Context context;
    private int currentCategoryIndex = 0;
    public TextView currentProgram;
    public TextView currentProgramTime;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private DebugTextViewHelper debugViewHelper;
    public EditText et_search;
    private EventLogger eventLogger;
    private RelativeLayout exo_playback_control_view;
    public boolean hideEPGData = true;
    private TrackGroupArray lastSeenTrackGroupArray;
    public ListView listChannels;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableChannels;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableNewChannels;
    private ArrayList<LiveStreamsDBModel> liveListDetailChannels;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedChannels;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetailChannels;
    LiveStreamDBHandler liveStreamDBHandler;
    public LinearLayout ll_categories_view;
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
    private SimpleDateFormat programTimeFormat;
    ProgressBar progressBar;
    ProgressBar progressLoader;
    private long resumePosition;
    private int resumeWindow;
    private Button retryButton;
    private RelativeLayout rlChannelList;
    private boolean shouldAutoPlay;
    private SimpleExoPlayerView simpleExoPlayerView;
    String stream_type;
    private TrackSelectionHelper trackSelectionHelper;
    private DefaultTrackSelector trackSelector;
    public TextView tv_categories_view;
    private String videoTitle;
    private int video_id;
    private int video_num;
    private AppCompatTextView video_title;

    static class C16381 extends HashMap<Integer, Integer> {
        C16381() {
            put(Integer.valueOf(3), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(0), Integer.valueOf(R.drawable.ic_fullscreen_black_24dp));
            put(Integer.valueOf(1), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(2), Integer.valueOf(R.drawable.ic_zoom_out_map_black_24dp));
        }
    }

    class C16403 implements TextWatcher {
        C16403() {
        }

        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            if (PlayerActivity.this.adapter != null) {
                PlayerActivity.this.adapter.getFilter().filter(cs.toString());
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }

    class C16414 implements MediaSourceFactory {
        C16414() {
        }

        public MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
            return PlayerActivity.this.buildMediaSource(uri, null, handler, listener);
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
                PlayerActivity.this.showLoader();
            } else if (playbackState == 4) {
                PlayerActivity.this.showControls();
            } else if (playbackState == 3) {
                PlayerActivity.this.hideLoader();
            }
            PlayerActivity.this.updateButtonVisibilities();
        }

        public void onPositionDiscontinuity(int reason) {
            if (PlayerActivity.this.needRetrySource) {
                PlayerActivity.this.updateResumePosition();
            }
        }

        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == 1) {
                Exception cause = e.getRendererException();
                if (cause instanceof DecoderInitializationException) {
                    DecoderInitializationException decoderInitializationException = (DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName != null) {
                        errorString = PlayerActivity.this.getString(R.string.error_instantiating_decoder, new Object[]{decoderInitializationException.decoderName});
                    } else if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = PlayerActivity.this.getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = PlayerActivity.this.getString(R.string.error_no_secure_decoder, new Object[]{decoderInitializationException.mimeType});
                    } else {
                        errorString = PlayerActivity.this.getString(R.string.error_no_decoder, new Object[]{decoderInitializationException.mimeType});
                    }
                }
            }
            if (errorString != null) {
                PlayerActivity.this.showToast(errorString);
            }
            PlayerActivity.this.needRetrySource = true;
            if (PlayerActivity.isBehindLiveWindow(e)) {
                PlayerActivity.this.clearResumePosition();
                PlayerActivity.this.initializePlayer();
                return;
            }
            PlayerActivity.this.updateResumePosition();
            PlayerActivity.this.updateButtonVisibilities();
            PlayerActivity.this.showControls();
            PlayerActivity.this.hideLoader();
        }

        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            PlayerActivity.this.updateButtonVisibilities();
            if (trackGroups != PlayerActivity.this.lastSeenTrackGroupArray) {
                MappedTrackInfo mappedTrackInfo = PlayerActivity.this.trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(2) == 1) {
                        PlayerActivity.this.showToast((int) R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(1) == 1) {
                        PlayerActivity.this.showToast((int) R.string.error_unsupported_audio);
                    }
                }
                PlayerActivity.this.lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

    static {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "live");
        this.context = this;
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.shouldAutoPlay = true;
        clearResumePosition();
        this.mediaDataSourceFactory = buildDataSourceFactory(true);
        this.mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.player_activity);
        findViewById(R.id.root).setOnClickListener(this);
        this.debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        this.debugTextView = (TextView) findViewById(R.id.debug_text_view);
        this.rlChannelList = (RelativeLayout) findViewById(R.id.rl_channel_list);
        this.retryButton = (Button) findViewById(R.id.retry_button);
        this.retryButton.setOnClickListener(this);
        this.simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        this.simpleExoPlayerView.setControllerVisibilityListener(this);
        this.simpleExoPlayerView.requestFocus();
        this.CURRENT_RESIZE_MODE = AppPref.getInstance(this).getResizeMode();
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.video_title = (AppCompatTextView) findViewById(R.id.title);
        this.btn_settings = (AppCompatImageView) findViewById(R.id.btn_settings);
        ((AppCompatImageView) findViewById(R.id.btn_back)).setOnClickListener(this);
        this.btn_settings.setOnClickListener(this);
        this.btn_list = (AppCompatImageView) findViewById(R.id.btn_list);
        this.btn_list.setOnClickListener(this);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        this.btn_screen = (AppCompatImageView) findViewById(R.id.btn_screen);
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
        this.btn_screen.setOnClickListener(this);
        this.listChannels = (ListView) findViewById(R.id.lv_ch);
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.ll_categories_view = (LinearLayout) findViewById(R.id.ll_categories_view);
        this.exo_playback_control_view = (RelativeLayout) findViewById(R.id.exo_playback_control_view);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressLoader = (ProgressBar) findViewById(R.id.progress_loader);
        this.ll_seekbar_time = (LinearLayout) findViewById(R.id.ll_seekbar_time);
        this.channelLogo = (ImageView) findViewById(R.id.iv_channel_logo);
        this.currentProgram = (TextView) findViewById(R.id.tv_current_program);
        this.currentProgramTime = (TextView) findViewById(R.id.tv_current_time);
        this.nextProgram = (TextView) findViewById(R.id.tv_next_program);
        this.nextProgramTime = (TextView) findViewById(R.id.tv_next_program_time);
        this.btn_cat_back = (AppCompatImageView) findViewById(R.id.btn_category_back);
        this.btn_cat_forward = (AppCompatImageView) findViewById(R.id.btn_category_forward);
        this.tv_categories_view = (TextView) findViewById(R.id.tv_categories_view);
        this.btn_cat_back.setOnClickListener(this);
        this.btn_cat_forward.setOnClickListener(this);
        this.video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        getIntent().putExtra("VIDEO_NUM", getIndexOfStreams(this.allStreams, this.video_num));
        this.tv_categories_view.setText(getResources().getString(R.string.all));
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        this.liveListDetailUnlckedChannels = new ArrayList();
        this.liveListDetailUnlckedDetailChannels = new ArrayList();
        this.liveListDetailAvailableChannels = new ArrayList();
        this.liveListDetailAvailableNewChannels = new ArrayList();
        this.liveListDetailChannels = new ArrayList();
        this.allLiveCategories = this.liveStreamDBHandler.getAllliveCategories();
        LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel(null, null, 0);
        liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
        liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
        int parentalStatusCount = this.liveStreamDBHandler.getParentalStatusCount();
        this.listPassword = getPasswordSetCategories();
        if (parentalStatusCount > 0 && this.allLiveCategories != null) {
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(this.allLiveCategories, this.listPassword);
            }
            this.liveListDetailUnlcked.add(0, liveStream);
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        } else if (this.allLiveCategories != null) {
            this.allLiveCategories.add(0, liveStream);
            this.liveListDetailAvailable = this.allLiveCategories;
        }
        if (parentalStatusCount <= 0 || this.allStreams == null) {
            this.liveListDetailAvailableChannels = this.allStreams;
        } else {
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetailChannels = getUnlockedChannels(this.allStreams, this.listPassword);
            }
            this.liveListDetailAvailableChannels = this.liveListDetailUnlckedDetailChannels;
        }
        if (this.liveListDetailAvailableChannels != null) {
            setChannelListAdapter(this.liveListDetailAvailableChannels);
        }
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }

    private ArrayList<LiveStreamCategoryIdDBModel> getUnlockedCategories(ArrayList<LiveStreamCategoryIdDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamCategoryIdDBModel user1 = (LiveStreamCategoryIdDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getLiveStreamCategoryID().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedChannels(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlckedChannels.add(user1);
            }
        }
        return this.liveListDetailUnlckedChannels;
    }

    public void setChannelListAdapter(final ArrayList<LiveStreamsDBModel> allStreams) {
        this.video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        int positionToSelect = this.video_num;
        this.adapter = new SearchableAdapter(this, allStreams);
        if (this.listChannels != null) {
            this.listChannels.setAdapter(this.adapter);
            this.listChannels.setSelection(positionToSelect);
            this.listChannels.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    view.setSelected(true);
                    ArrayList<LiveStreamsDBModel> filteredData = PlayerActivity.this.adapter.getFilteredData();
                    if (filteredData != null) {
                        int num = Integer.parseInt(((LiveStreamsDBModel) filteredData.get(position)).getNum());
                        String epgChannelID = ((LiveStreamsDBModel) filteredData.get(position)).getEpgChannelId();
                        String channelLogo = ((LiveStreamsDBModel) filteredData.get(position)).getStreamIcon();
                        String title = ((LiveStreamsDBModel) filteredData.get(position)).getEpgTitle();
                        num = PlayerActivity.this.getIndexOfStreams(allStreams, num);
                        PlayerActivity.this.player.seekTo(num, C.TIME_UNSET);
                        PlayerActivity.this.clearResumePosition();
                        PlayerActivity.this.getIntent().putExtra("VIDEO_ID", Integer.parseInt(((LiveStreamsDBModel) allStreams.get(num)).getStreamId()));
                        PlayerActivity.this.getIntent().putExtra("VIDEO_NUM", num);
                        PlayerActivity.this.getIntent().putExtra("VIDEO_TITLE", title);
                        PlayerActivity.this.initializePlayer();
                        PlayerActivity.this.video_title.setText(((LiveStreamsDBModel) filteredData.get(position)).getName());
                        PlayerActivity.this.updateEPGData(epgChannelID, channelLogo);
                        return;
                    }
                    int num = Integer.parseInt(((LiveStreamsDBModel) allStreams.get(position)).getNum());
                    String epgChannelID = ((LiveStreamsDBModel) allStreams.get(position)).getEpgChannelId();
                    String channelLogo = ((LiveStreamsDBModel) allStreams.get(position)).getStreamIcon();
                    String title = ((LiveStreamsDBModel) allStreams.get(position)).getEpgTitle();
                    PlayerActivity.this.player.seekTo(num, C.TIME_UNSET);
                    PlayerActivity.this.clearResumePosition();
                    PlayerActivity.this.getIntent().putExtra("VIDEO_ID", Integer.parseInt(((LiveStreamsDBModel) allStreams.get(num)).getStreamId()));
                    PlayerActivity.this.getIntent().putExtra("VIDEO_NUM", num);
                    PlayerActivity.this.getIntent().putExtra("VIDEO_TITLE", title);
                    PlayerActivity.this.initializePlayer();
                    PlayerActivity.this.video_title.setText(((LiveStreamsDBModel) allStreams.get(position)).getName());
                    PlayerActivity.this.updateEPGData(epgChannelID, channelLogo);
                }
            });
            this.et_search.addTextChangedListener(new C16403());
        }
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
        boolean z = false;
        int keyCode = event.getKeyCode();
        if (event.getAction() == 0) {
            if (keyCode == 23 || keyCode == 66) {
                if (this.listChannels.getVisibility() == 0) {
                    this.listChannels.performClick();
                } else {
                    this.listChannels.setVisibility(0);
                    this.et_search.setVisibility(0);
                    this.ll_categories_view.setVisibility(0);
                    this.listChannels.setFocusable(true);
                    this.listChannels.requestFocus();
                    return true;
                }
            }
            if (keyCode == 21 && this.listChannels.getVisibility() == 0) {
                if (this.et_search != null) {
                    this.et_search.setText("");
                }
                backbutton();
            }
            if (keyCode == 22 && this.listChannels.getVisibility() == 0) {
                if (this.et_search != null) {
                    this.et_search.setText("");
                }
                nextbutton();
            }
        }
        if (this.simpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event)) {
            z = true;
        }
        return z;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                return;
            case R.id.btn_category_back:
                backbutton();
                return;
            case R.id.btn_category_forward:
                nextbutton();
                return;
            case R.id.btn_list:
                toggleChannelList();
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
            case R.id.exo_next:
                if (this.player != null) {
                    next();
                    int indexNext = this.player.getCurrentWindowIndex();
                    Log.e("TAG", "indexNext: " + indexNext);
                    if (indexNext <= this.allStreams.size() - 1) {
                        clearResumePosition();
                        getIntent().putExtra("VIDEO_ID", Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getStreamId()));
                        initializePlayer();
                        this.video_title.setText(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getName());
                        updateEPGData(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getEpgChannelId(), ((LiveStreamsDBModel) this.allStreams.get(indexNext)).getStreamIcon());
                        return;
                    }
                    return;
                }
                return;
            case R.id.exo_prev:
                if (this.player != null) {
                    previousLive();
                    int indexPrev = this.player.getCurrentWindowIndex();
                    Log.e("TAG", "indexPrev: " + indexPrev);
                    if (indexPrev <= this.allStreams.size() - 1) {
                        clearResumePosition();
                        getIntent().putExtra("VIDEO_ID", Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getStreamId()));
                        initializePlayer();
                        this.video_title.setText(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getName());
                        updateEPGData(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getEpgChannelId(), ((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getStreamIcon());
                        return;
                    }
                    return;
                }
                return;
            case R.id.retry_button:
                initializePlayer();
                return;
            default:
                return;
        }
    }

    public void backbutton() {
        if (this.currentCategoryIndex != 0) {
            this.currentCategoryIndex--;
        }
        if (this.currentCategoryIndex == 0 && this.liveListDetailAvailableChannels != null) {
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.all));
            }
            setChannelListAdapter(this.liveListDetailAvailableChannels);
        } else if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryID();
            String currentCatName = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryName();
            if (this.liveStreamDBHandler != null) {
                this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
            }
            setChannelListAdapter(this.allStreams);
        }
    }

    public void nextbutton() {
        if (this.currentCategoryIndex != this.liveListDetailAvailable.size() - 1) {
            this.currentCategoryIndex++;
        }
        if (this.currentCategoryIndex == 0 && this.liveListDetailAvailableChannels != null) {
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.all));
            }
            setChannelListAdapter(this.liveListDetailAvailableChannels);
        } else if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryID();
            String currentCatName = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryName();
            if (this.liveStreamDBHandler != null) {
                this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
            }
            setChannelListAdapter(this.allStreams);
        }
    }

    public void toggleChannelList() {
        if (this.listChannels != null && this.et_search != null) {
            if (this.listChannels.getVisibility() == 0) {
                this.listChannels.setVisibility(8);
                this.et_search.setVisibility(8);
                this.ll_categories_view.setVisibility(8);
                return;
            }
            this.listChannels.setVisibility(0);
            this.et_search.setVisibility(0);
            this.ll_categories_view.setVisibility(0);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 19:
                if (this.listChannels != null && this.listChannels.getVisibility() == 0) {
                    return true;
                }
                findViewById(R.id.exo_next).performClick();
                return true;
            case 20:
                if (this.listChannels != null && this.listChannels.getVisibility() == 0) {
                    return true;
                }
                findViewById(R.id.exo_prev).performClick();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void onBackPressed() {
        if (this.listChannels != null) {
            this.listChannels.setFocusable(true);
            this.listChannels.requestFocus();
        }
        if (this.listChannels == null || this.listChannels.getVisibility() != 0) {
            super.onBackPressed();
            return;
        }
        this.listChannels.setVisibility(8);
        if (this.et_search != null && this.et_search.getVisibility() == 0) {
            this.et_search.setVisibility(8);
        }
        if (this.ll_categories_view != null && this.ll_categories_view.getVisibility() == 0) {
            this.ll_categories_view.setVisibility(8);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown;
        if (event.getRepeatCount() == 0) {
            uniqueDown = true;
        } else {
            uniqueDown = false;
        }
        switch (keyCode) {
            case 23:
                this.listChannels.setVisibility(0);
                this.et_search.setVisibility(0);
                this.ll_categories_view.setVisibility(0);
                this.listChannels.setFocusable(true);
                this.listChannels.requestFocus();
                return true;
            case 62:
            case 79:
            case 85:
                return uniqueDown ? true : true;
            case 66:
                this.listChannels.setVisibility(0);
                this.et_search.setVisibility(0);
                this.ll_categories_view.setVisibility(0);
                this.listChannels.setFocusable(true);
                this.listChannels.requestFocus();
                return true;
            case 86:
            case 126:
            case 127:
                return true;
            case 166:
                findViewById(R.id.exo_next).performClick();
                return true;
            case 167:
                findViewById(R.id.exo_prev).performClick();
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

    public void updateEPGData(String epgChannelID, String channel_logo) {
        this.hideEPGData = true;
        if (!(this.liveStreamDBHandler == null || this.loginPreferencesAfterLogin == null)) {
            String savedEPGShift = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_SELECTED_EPG_SHIFT, "");
            if (epgChannelID != null) {
                if (!epgChannelID.equals("")) {
                    ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(epgChannelID);
                    String Title = "";
                    if (xmltvProgrammePojos != null) {
                        for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                            String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                            String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                            Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                            Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                            Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                            if (Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context)) {
                                int epgPercentage = Utils.getPercentageLeft(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context);
                                if (epgPercentage != 0) {
                                    epgPercentage = 100 - epgPercentage;
                                    if (Title.equals("")) {
                                        this.hideEPGData = true;
                                    } else {
                                        this.hideEPGData = false;
                                        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                                            this.rlChannelList.setPadding(0, 0, 0, PsExtractor.AUDIO_STREAM);
                                        } else {
                                            this.rlChannelList.setPadding(0, 0, 0, 245);
                                        }
                                        this.ll_seekbar_time.setVisibility(0);
                                        this.progressBar.setVisibility(0);
                                        this.progressBar.setProgress(epgPercentage);
                                        this.currentProgram.setVisibility(0);
                                        this.currentProgram.setText(Title);
                                        this.currentProgramTime.setVisibility(0);
                                        loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
                                        this.programTimeFormat = new SimpleDateFormat(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
                                        this.currentProgramTime.setText(this.programTimeFormat.format(epgStartDateToTimestamp) + " - " + this.programTimeFormat.format(epgStopDateToTimestamp));
                                        this.channelLogo.setVisibility(0);
                                        if (channel_logo != null) {
                                            if (!channel_logo.equals("")) {
                                                Picasso.with(this).load(channel_logo).placeholder((int) R.drawable.iptv_placeholder).into(this.channelLogo);
                                            }
                                        }
                                        if (VERSION.SDK_INT >= 21) {
                                            this.channelLogo.setImageDrawable(getResources().getDrawable(R.drawable.iptv_placeholder, null));
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        this.hideEPGData = true;
                    }
                }
            }
            this.hideEPGData = true;
        }
        if (this.hideEPGData) {
            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.rlChannelList.setPadding(0, 0, 0, 96);
            } else {
                this.rlChannelList.setPadding(0, 0, 0, 122);
            }
            this.ll_seekbar_time.setVisibility(8);
            this.progressBar.setVisibility(8);
            this.progressBar.setProgress(0);
            this.currentProgram.setVisibility(8);
            this.currentProgram.setText("");
            this.currentProgramTime.setVisibility(8);
            this.currentProgramTime.setText("");
            this.channelLogo.setVisibility(8);
        }
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
        hideSystemUi();
        if (visibility == 1 || visibility == 0) {
            if (this.hideEPGData) {
                if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                    this.rlChannelList.setPadding(0, 0, 0, 96);
                } else {
                    this.rlChannelList.setPadding(0, 0, 0, 122);
                }
            } else if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.rlChannelList.setPadding(0, 0, 0, PsExtractor.AUDIO_STREAM);
            } else {
                this.rlChannelList.setPadding(0, 0, 0, 245);
            }
        }
        if (visibility == 8 && this.rlChannelList != null) {
            this.rlChannelList.setPadding(0, 0, 0, 0);
        }
    }

    private void initializePlayer() {
        Intent intent = getIntent();
        this.video_id = intent.getIntExtra("VIDEO_ID", 0);
        this.stream_type = "live";
        this.video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        String epgChannelID = getIntent().getStringExtra("EPG_CHANNEL_ID");
        String epgChannelLogo = getIntent().getStringExtra("EPG_CHANNEL_LOGO");
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
            this.player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this, drmSessionManager, 1), this.trackSelector);
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
                Uri stream_uri = buildURI(this.stream_type, this.video_id);
                uris = new Uri[]{stream_uri};
                extensions = new String[uris.length];
            } else if ("com.google.android.exoplayer.demo.action.VIEW_LIST".equals(action)) {
                uris = new Uri[this.allStreams.size()];
                extensions = new String[this.allStreams.size()];
                for (i = 0; i < this.allStreams.size(); i++) {
                    uris[i] = buildURI(this.stream_type, Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(i)).getStreamId()));
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
                this.player.seekTo(currentWindowIndex, C.TIME_UNSET);
                this.player.prepare(mediaSource, !haveResumePosition, false);
                this.needRetrySource = false;
                updateEPGData(epgChannelID, epgChannelLogo);
                updateButtonVisibilities();
            } else {
                return;
            }
        }
        this.simpleExoPlayerView.showController();
    }

    private Uri buildURI(String stream_type, int stream_id) {
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        Builder builder = new Builder();
        try {
            builder.scheme("http").encodedAuthority(serverUrl + ":" + serverPort).appendPath(stream_type).appendPath(username).appendPath(password).appendPath(Integer.toString(stream_id) + "." + allowedFormat);
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
        return new AdsMediaSource(mediaSource, new C16414(), this.adsLoader, this.adUiViewGroup, this.mainHandler, this.eventLogger);
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
