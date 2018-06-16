package z.xtreamiptv.playerv3.view.nstplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.ads.dynamite.ModuleDescriptor;
import z.xtreamiptv.playerv3.R;
import org.joda.time.DateTimeConstants;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnInfoListener;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class NSTPlayerSeries {
    private static final int MESSAGE_FADE_OUT = 2;
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;
    private static final int MESSAGE_RESTART_PLAY = 5;
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;
    private static final int MESSAGE_SHOW_PROGRESS = 1;
    public static final String SCALETYPE_16_9 = "16:9";
    public static final String SCALETYPE_4_3 = "4:3";
    public static final String SCALETYPE_FILLPARENT = "fillParent";
    public static final String SCALETYPE_FITPARENT = "fitParent";
    public static final String SCALETYPE_FITXY = "fitXY";
    public static final String SCALETYPE_WRAPCONTENT = "wrapContent";
    private Query f48$;
    private int STATUS_COMPLETED = 4;
    private int STATUS_ERROR = -1;
    private int STATUS_IDLE = 0;
    private int STATUS_LOADING = 1;
    private int STATUS_PAUSE = 3;
    private int STATUS_PLAYING = 2;
    private final Activity activity;
    private final AudioManager audioManager;
    private float brightness = -1.0f;
    private int currentPosition;
    private int currentWindowIndex = 0;
    private long defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    private int defaultTimeout = 7000;
    private long duration;
    private boolean fullScreenOnly;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    NSTPlayerSeries.this.setProgress();
                    if (!NSTPlayerSeries.this.isDragging && NSTPlayerSeries.this.isShowing) {
                        sendMessageDelayed(obtainMessage(1), 1000);
                        NSTPlayerSeries.this.updatePausePlay();
                        return;
                    }
                    return;
                case 2:
                    NSTPlayerSeries.this.hide(false);
                    return;
                case 3:
                    if (!NSTPlayerSeries.this.isLive && NSTPlayerSeries.this.newPosition >= 0) {
                        NSTPlayerSeries.this.videoView.seekTo((int) NSTPlayerSeries.this.newPosition);
                        NSTPlayerSeries.this.newPosition = -1;
                        return;
                    }
                    return;
                case 4:
                    NSTPlayerSeries.this.f48$.id(R.id.app_video_volume_box).gone();
                    NSTPlayerSeries.this.f48$.id(R.id.app_video_brightness_box).gone();
                    NSTPlayerSeries.this.f48$.id(R.id.app_video_fastForward_box).gone();
                    return;
                default:
                    return;
            }
        }
    };
    private final int initHeight;
    private boolean instantSeeking;
    private boolean isDragging;
    private boolean isLive = false;
    private boolean isShowing;
    private final int mMaxVolume;
    private final OnSeekBarChangeListener mSeekListener = new C20966();
    private long newPosition = -1;
    private final OnClickListener onClickListener = new C20911();
    public OnControlPanelVisibilityChangeListener onControlPanelVisibilityChangeListener = new C20955();
    private z.xtreamiptv.playerv3.view.nstplayer.NSTPlayerArchive.OnErrorListener onErrorListener = new C20922();
    private z.xtreamiptv.playerv3.view.nstplayer.NSTPlayerArchive.OnInfoListener onInfoListener = new C20944();
    private Runnable oncomplete = new C20933();
    private OrientationEventListener orientationEventListener;
    private long pauseTime;
    private boolean playerSupport;
    private boolean portrait;
    private int screenWidthPixels;
    private final SeekBar seekBar;
    private int status = this.STATUS_IDLE;
    private String url;
    private final IjkVideoView videoView;
    private int volume = -1;

    class C20911 implements OnClickListener {
        C20911() {
        }

        public void onClick(View v) {
            if (v.getId() == R.id.app_video_fullscreen) {
                NSTPlayerSeries.this.toggleFullScreen();
            } else if (v.getId() == R.id.app_video_play) {
                NSTPlayerSeries.this.doPauseResume();
                NSTPlayerSeries.this.show(NSTPlayerSeries.this.defaultTimeout);
            } else if (v.getId() == R.id.app_video_replay_icon) {
                NSTPlayerSeries.this.videoView.seekTo(0);
                NSTPlayerSeries.this.videoView.start();
                NSTPlayerSeries.this.doPauseResume();
            } else if (v.getId() != R.id.app_video_finish) {
            } else {
                if (NSTPlayerSeries.this.fullScreenOnly || NSTPlayerSeries.this.portrait) {
                    NSTPlayerSeries.this.activity.finish();
                } else {
                    NSTPlayerSeries.this.activity.setRequestedOrientation(1);
                }
            }
        }
    }

    class C20922 implements z.xtreamiptv.playerv3.view.nstplayer.NSTPlayerArchive.OnErrorListener {
        C20922() {
        }

        public void onError(int what, int extra) {
        }
    }

    class C20933 implements Runnable {
        C20933() {
        }

        public void run() {
        }
    }

    class C20944 implements z.xtreamiptv.playerv3.view.nstplayer.NSTPlayerArchive.OnInfoListener {
        C20944() {
        }

        public void onInfo(int what, int extra) {
        }
    }

    public interface OnControlPanelVisibilityChangeListener {
        void change(boolean z);
    }

    class C20955 implements OnControlPanelVisibilityChangeListener {
        C20955() {
        }

        public void change(boolean isShowing) {
        }
    }

    class C20966 implements OnSeekBarChangeListener {
        C20966() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                NSTPlayerSeries.this.f48$.id(R.id.app_video_status).gone();
                int newPosition = (int) ((((double) (NSTPlayerSeries.this.duration * ((long) progress))) * 1.0d) / 1000.0d);
                String time = NSTPlayerSeries.this.generateTime((long) newPosition);
                if (NSTPlayerSeries.this.instantSeeking) {
                    NSTPlayerSeries.this.videoView.seekTo(newPosition);
                }
                NSTPlayerSeries.this.f48$.id(R.id.app_video_currentTime).text(time);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            NSTPlayerSeries.this.isDragging = true;
            NSTPlayerSeries.this.show(DateTimeConstants.MILLIS_PER_HOUR);
            NSTPlayerSeries.this.handler.removeMessages(1);
            if (NSTPlayerSeries.this.instantSeeking) {
                NSTPlayerSeries.this.audioManager.setStreamMute(3, true);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!NSTPlayerSeries.this.instantSeeking) {
                NSTPlayerSeries.this.videoView.seekTo((int) ((((double) (NSTPlayerSeries.this.duration * ((long) seekBar.getProgress()))) * 1.0d) / 1000.0d));
            }
            NSTPlayerSeries.this.show(NSTPlayerSeries.this.defaultTimeout);
            NSTPlayerSeries.this.handler.removeMessages(1);
            NSTPlayerSeries.this.audioManager.setStreamMute(3, false);
            NSTPlayerSeries.this.isDragging = false;
            NSTPlayerSeries.this.handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    class C20988 implements IMediaPlayer$OnCompletionListener {
        C20988() {
        }

        public void onCompletion(IMediaPlayer mp) {
            NSTPlayerSeries.this.statusChange(NSTPlayerSeries.this.STATUS_COMPLETED);
            NSTPlayerSeries.this.oncomplete.run();
        }
    }

    class C20999 implements IMediaPlayer$OnErrorListener {
        C20999() {
        }

        public boolean onError(IMediaPlayer mp, int what, int extra) {
            NSTPlayerSeries.this.statusChange(NSTPlayerSeries.this.STATUS_ERROR);
            NSTPlayerSeries.this.onErrorListener.onError(what, extra);
            return true;
        }
    }

    public interface OnErrorListener {
        void onError(int i, int i2);
    }

    public interface OnInfoListener {
        void onInfo(int i, int i2);
    }

    public class PlayerGestureListener extends SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean toSeek;
        private boolean volumeControl;

        public boolean onDoubleTap(MotionEvent e) {
            NSTPlayerSeries.this.videoView.toggleAspectRatio();
            return true;
        }

        public boolean onDown(MotionEvent e) {
            this.firstTouch = true;
            return super.onDown(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean z = true;
            if (!(e1 == null || e2 == null)) {
                float mOldX = e1.getX();
                float deltaY = e1.getY() - e2.getY();
                float deltaX = mOldX - e2.getX();
                if (this.firstTouch) {
                    boolean z2;
                    if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    this.toSeek = z2;
                    if (mOldX <= ((float) NSTPlayerSeries.this.screenWidthPixels) * 0.5f) {
                        z = false;
                    }
                    this.volumeControl = z;
                    this.firstTouch = false;
                }
                if (!this.toSeek) {
                    float percent = deltaY / ((float) NSTPlayerSeries.this.videoView.getHeight());
                    if (this.volumeControl) {
                        NSTPlayerSeries.this.onVolumeSlide(percent);
                    } else {
                        NSTPlayerSeries.this.onBrightnessSlide(percent);
                    }
                } else if (!NSTPlayerSeries.this.isLive) {
                    NSTPlayerSeries.this.onProgressSlide((-deltaX) / ((float) NSTPlayerSeries.this.videoView.getWidth()));
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onSingleTapUp(MotionEvent e) {
            if (NSTPlayerSeries.this.isShowing) {
                NSTPlayerSeries.this.hide(false);
            } else {
                LinearLayout topBoxLinearLayout = (LinearLayout) NSTPlayerSeries.this.activity.findViewById(R.id.app_video_top_box);
                LinearLayout controlsLinearLayout = (LinearLayout) NSTPlayerSeries.this.activity.findViewById(R.id.controls);
                RelativeLayout bottomBoxRelativeLayout = (RelativeLayout) NSTPlayerSeries.this.activity.findViewById(R.id.app_video_bottom_box);
                if (topBoxLinearLayout.getVisibility() == 0) {
                    topBoxLinearLayout.setVisibility(8);
                    controlsLinearLayout.setVisibility(8);
                    bottomBoxRelativeLayout.setVisibility(8);
                } else {
                    NSTPlayerSeries.this.show(NSTPlayerSeries.this.defaultTimeout);
                }
            }
            return true;
        }
    }

    class Query {
        private final Activity activity;
        private View view;

        public Query(Activity activity) {
            this.activity = activity;
        }

        public Query id(int id) {
            this.view = this.activity.findViewById(id);
            return this;
        }

        public Query image(int resId) {
            if (this.view instanceof ImageView) {
                ((ImageView) this.view).setImageResource(resId);
            }
            return this;
        }

        public Query visible() {
            if (this.view != null) {
                this.view.setVisibility(0);
            }
            return this;
        }

        public Query gone() {
            if (this.view != null) {
                this.view.setVisibility(8);
            }
            return this;
        }

        public Query invisible() {
            if (this.view != null) {
                this.view.setVisibility(4);
            }
            return this;
        }

        public Query clicked(OnClickListener handler) {
            if (this.view != null) {
                this.view.setOnClickListener(handler);
            }
            return this;
        }

        public Query text(CharSequence text) {
            if (this.view != null && (this.view instanceof TextView)) {
                ((TextView) this.view).setText(text);
            }
            return this;
        }

        public Query visibility(int visible) {
            if (this.view != null) {
                this.view.setVisibility(visible);
            }
            return this;
        }

        private void size(boolean width, int n, boolean dip) {
            if (this.view != null) {
                LayoutParams lp = this.view.getLayoutParams();
                if (n > 0 && dip) {
                    n = dip2pixel(this.activity, (float) n);
                }
                if (width) {
                    lp.width = n;
                } else {
                    lp.height = n;
                }
                this.view.setLayoutParams(lp);
            }
        }

        public void height(int height, boolean dip) {
            size(false, height, dip);
        }

        public int dip2pixel(Context context, float n) {
            return (int) TypedValue.applyDimension(1, n, context.getResources().getDisplayMetrics());
        }

        public float pixel2dip(Context context, float n) {
            return n / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
        }
    }

    public void setCurrentWindowIndex(int index) {
        this.currentWindowIndex = index;
    }

    public void setDefaultRetryTime(long defaultRetryTime) {
        this.defaultRetryTime = defaultRetryTime;
    }

    public void setTitle(CharSequence title) {
        this.f48$.id(R.id.app_video_title).text(title);
    }

    public void doPauseResume() {
        if (this.status == this.STATUS_COMPLETED) {
            this.f48$.id(R.id.app_video_replay).gone();
            this.videoView.seekTo(0);
            this.videoView.start();
        } else if (this.videoView.isPlaying()) {
            statusChange(this.STATUS_PAUSE);
            this.videoView.pause();
        } else {
            statusChange(this.STATUS_PLAYING);
            this.videoView.start();
        }
    }

    private void updatePausePlay() {
        if (this.videoView.isPlaying()) {
            this.f48$.id(R.id.exo_play).gone();
            this.f48$.id(R.id.exo_pause).visible();
            return;
        }
        this.f48$.id(R.id.exo_pause).gone();
        this.f48$.id(R.id.exo_play).visible();
    }

    @SuppressLint({"InlinedApi"})
    public void hideSystemUi() {
        this.videoView.setSystemUiVisibility(4871);
    }

    public void show(int timeout) {
        if (!this.isShowing) {
            this.f48$.id(R.id.app_video_top_box).visible();
            this.f48$.id(R.id.app_video_bottom_box).visible();
            this.f48$.id(R.id.controls).visible();
            if (!this.isLive) {
                showBottomControl(true);
            }
            if (!this.fullScreenOnly) {
                this.f48$.id(R.id.app_video_fullscreen).visible();
            }
            this.isShowing = true;
            this.onControlPanelVisibilityChangeListener.change(true);
        }
        updatePausePlay();
        this.handler.sendEmptyMessage(1);
        this.handler.removeMessages(2);
        if (timeout != 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(2), (long) timeout);
        }
    }

    public void showBottomControl(boolean show) {
        int i;
        int i2 = 0;
        Query id = this.f48$.id(R.id.app_video_currentTime);
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        id.visibility(i);
        id = this.f48$.id(R.id.app_video_endTime);
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        id.visibility(i);
        Query id2 = this.f48$.id(R.id.app_video_seekBar);
        if (!show) {
            i2 = 8;
        }
        id2.visibility(i2);
    }

    public NSTPlayerSeries(final Activity activity) {
        boolean z;
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.playerSupport = true;
        } catch (Throwable e) {
            Log.e("NSTPlayerArchive", "loadLibraries error", e);
        }
        this.activity = activity;
        this.screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels;
        this.f48$ = new Query(activity);
        this.videoView = (IjkVideoView) activity.findViewById(R.id.video_view);
        this.videoView.setOnCompletionListener(new C20988());
        this.videoView.setOnErrorListener(new C20999());
        this.videoView.setOnInfoListener(new IMediaPlayer$OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case 3:
                        NSTPlayerSeries.this.statusChange(NSTPlayerSeries.this.STATUS_PLAYING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START /*701*/:
                        NSTPlayerSeries.this.statusChange(NSTPlayerSeries.this.STATUS_LOADING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END /*702*/:
                        NSTPlayerSeries.this.statusChange(NSTPlayerSeries.this.STATUS_PLAYING);
                        break;
                }
                NSTPlayerSeries.this.onInfoListener.onInfo(what, extra);
                return false;
            }
        });
        this.seekBar = (SeekBar) activity.findViewById(R.id.app_video_seekBar);
        this.seekBar.setMax(1000);
        this.seekBar.setOnSeekBarChangeListener(this.mSeekListener);
        this.f48$.id(R.id.app_video_fullscreen).clicked(this.onClickListener);
        this.f48$.id(R.id.app_video_finish).clicked(this.onClickListener);
        this.f48$.id(R.id.app_video_replay_icon).clicked(this.onClickListener);
        this.audioManager = (AudioManager) activity.getSystemService("audio");
        this.mMaxVolume = this.audioManager.getStreamMaxVolume(3);
        final GestureDetector gestureDetector = new GestureDetector(activity, new PlayerGestureListener());
        View liveBox = activity.findViewById(R.id.app_video_box);
        liveBox.setClickable(true);
        liveBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent != null) {
                    if (!gestureDetector.onTouchEvent(motionEvent)) {
                        switch (motionEvent.getAction() & 255) {
                            case 1:
                                NSTPlayerSeries.this.endGesture();
                                break;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        this.orientationEventListener = new OrientationEventListener(activity) {
            public void onOrientationChanged(int orientation) {
                if ((orientation < 0 || orientation > 30) && orientation < 330 && (orientation < ModuleDescriptor.MODULE_VERSION || orientation > 210)) {
                    if (((orientation >= 90 && orientation <= 120) || (orientation >= PsExtractor.VIDEO_STREAM_MASK && orientation <= IjkMediaCodecInfo.RANK_SECURE)) && !NSTPlayerSeries.this.portrait) {
                        activity.setRequestedOrientation(4);
                        NSTPlayerSeries.this.orientationEventListener.disable();
                    }
                } else if (NSTPlayerSeries.this.portrait) {
                    activity.setRequestedOrientation(4);
                    NSTPlayerSeries.this.orientationEventListener.disable();
                }
            }
        };
        if (this.fullScreenOnly) {
            activity.setRequestedOrientation(0);
        }
        if (getScreenOrientation() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.portrait = z;
        this.initHeight = activity.findViewById(R.id.app_video_box).getLayoutParams().height;
        hideAll();
        if (!this.playerSupport) {
            showStatus(activity.getResources().getString(R.string.not_support));
        }
    }

    private void endGesture() {
        this.volume = -1;
        this.brightness = -1.0f;
        if (this.newPosition >= 0) {
            this.handler.removeMessages(3);
            this.handler.sendEmptyMessage(3);
        }
        this.handler.removeMessages(4);
        this.handler.sendEmptyMessageDelayed(4, 500);
    }

    private void statusChange(int newStatus) {
        this.status = newStatus;
        if (!this.isLive && newStatus == this.STATUS_COMPLETED) {
            this.handler.removeMessages(1);
            hideAll();
            this.f48$.id(R.id.app_video_replay).visible();
        } else if (newStatus == this.STATUS_ERROR) {
            this.handler.removeMessages(1);
            hideAll();
            if (this.isLive) {
                showStatus(this.activity.getResources().getString(R.string.small_problem));
                if (this.defaultRetryTime > 0) {
                    this.handler.sendEmptyMessageDelayed(5, this.defaultRetryTime);
                    return;
                }
                return;
            }
            showStatus(this.activity.getResources().getString(R.string.small_problem));
        } else if (newStatus == this.STATUS_LOADING) {
            hideAll();
            this.f48$.id(R.id.app_video_loading).visible();
        } else if (newStatus == this.STATUS_PLAYING) {
            this.f48$.id(R.id.exo_play).gone();
            this.f48$.id(R.id.exo_pause).visible();
            hideAll();
        } else if (newStatus == this.STATUS_PAUSE) {
            this.f48$.id(R.id.exo_play).visible();
            this.f48$.id(R.id.exo_pause).gone();
            show(this.defaultTimeout);
        }
    }

    private void hideAll() {
        this.f48$.id(R.id.app_video_replay).gone();
        this.f48$.id(R.id.app_video_top_box).gone();
        this.f48$.id(R.id.app_video_bottom_box).gone();
        this.f48$.id(R.id.controls).gone();
        this.f48$.id(R.id.app_video_loading).gone();
        this.f48$.id(R.id.app_video_fullscreen).invisible();
        this.f48$.id(R.id.app_video_status).gone();
        showBottomControl(false);
        this.onControlPanelVisibilityChangeListener.change(false);
    }

    public void showAll() {
        this.isShowing = true;
        this.f48$.id(R.id.app_video_top_box).visible();
        this.f48$.id(R.id.app_video_bottom_box).visible();
        this.f48$.id(R.id.controls).visible();
        showBottomControl(true);
        show(this.defaultTimeout);
    }

    public void onPause() {
        this.pauseTime = System.currentTimeMillis();
        show(0);
        if (this.status == this.STATUS_PLAYING) {
            this.videoView.pause();
            if (!this.isLive) {
                this.currentPosition = this.videoView.getCurrentPosition();
            }
        }
    }

    public void onResume() {
        this.pauseTime = 0;
        if (this.status == this.STATUS_PLAYING) {
            if (this.isLive) {
                this.videoView.seekTo(0);
            } else if (this.currentPosition > 0) {
                this.videoView.seekTo(this.currentPosition);
            }
            this.videoView.start();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        boolean z = true;
        if (newConfig.orientation != 1) {
            z = false;
        }
        this.portrait = z;
        doOnConfigurationChanged(this.portrait);
    }

    private void doOnConfigurationChanged(final boolean portrait) {
        if (this.videoView != null && !this.fullScreenOnly) {
            this.handler.post(new Runnable() {
                public void run() {
                    NSTPlayerSeries.this.tryFullScreen(!portrait);
                    if (portrait) {
                        NSTPlayerSeries.this.f48$.id(R.id.app_video_box).height(NSTPlayerSeries.this.initHeight, false);
                    } else {
                        NSTPlayerSeries.this.f48$.id(R.id.app_video_box).height(Math.min(NSTPlayerSeries.this.activity.getResources().getDisplayMetrics().heightPixels, NSTPlayerSeries.this.activity.getResources().getDisplayMetrics().widthPixels), false);
                    }
                    NSTPlayerSeries.this.updateFullScreenButton();
                }
            });
            this.orientationEventListener.enable();
        }
    }

    public void tryFullScreen(boolean fullScreen) {
        if (this.activity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) this.activity).getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();
                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(fullScreen);
    }

    private void setFullScreen(boolean fullScreen) {
        if (this.activity != null) {
            WindowManager.LayoutParams attrs = this.activity.getWindow().getAttributes();
            if (fullScreen) {
                attrs.flags |= 1024;
                this.activity.getWindow().setAttributes(attrs);
                this.activity.getWindow().addFlags(512);
                return;
            }
            attrs.flags &= -1025;
            this.activity.getWindow().setAttributes(attrs);
            this.activity.getWindow().clearFlags(512);
        }
    }

    public void onDestroy() {
        this.orientationEventListener.disable();
        this.handler.removeCallbacksAndMessages(null);
        this.videoView.stopPlayback();
    }

    private void showStatus(String statusText) {
        this.f48$.id(R.id.app_video_status).visible();
        this.f48$.id(R.id.app_video_status_text).text(statusText);
    }

    public void play(String url, int opened_stream_id, String videoExtension, String stream_start_time, String stream_duration, String stream_stop_time) {
        this.url = url;
        if (this.playerSupport) {
            this.f48$.id(R.id.app_video_loading).visible();
            this.videoView.setVideoPath(url + opened_stream_id + "." + videoExtension);
            this.videoView.start();
        }
    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        if (totalSeconds / DateTimeConstants.SECONDS_PER_HOUR > 0) {
            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(totalSeconds / DateTimeConstants.SECONDS_PER_HOUR), Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    private int getScreenOrientation() {
        int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (((rotation == 0 || rotation == 2) && height > width) || ((rotation == 1 || rotation == 3) && width > height)) {
            switch (rotation) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                case 2:
                    return 9;
                case 3:
                    return 8;
                default:
                    return 1;
            }
        }
        switch (rotation) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 8;
            case 3:
                return 9;
            default:
                return 0;
        }
    }

    private void onVolumeSlide(float percent) {
        if (this.volume == -1) {
            this.volume = this.audioManager.getStreamVolume(3);
            if (this.volume < 0) {
                this.volume = 0;
            }
        }
        hide(true);
        int index = ((int) (((float) this.mMaxVolume) * percent)) + this.volume;
        if (index > this.mMaxVolume) {
            index = this.mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        this.audioManager.setStreamVolume(3, index, 0);
        int i = (int) (((((double) index) * 1.0d) / ((double) this.mMaxVolume)) * 100.0d);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
        this.f48$.id(R.id.app_video_volume_icon).image(i == 0 ? R.drawable.ic_volume_off_white_36dp : R.drawable.ic_volume_up_white_36dp);
        this.f48$.id(R.id.app_video_brightness_box).gone();
        this.f48$.id(R.id.app_video_volume_box).visible();
        this.f48$.id(R.id.app_video_volume_box).visible();
        this.f48$.id(R.id.app_video_volume).text(s).visible();
    }

    private void onProgressSlide(float percent) {
        long position = (long) this.videoView.getCurrentPosition();
        long duration = (long) this.videoView.getDuration();
        long delta = (long) (((float) Math.min(100000, duration - position)) * percent);
        this.newPosition = delta + position;
        if (this.newPosition > duration) {
            this.newPosition = duration;
        } else if (this.newPosition <= 0) {
            this.newPosition = 0;
            delta = -position;
        }
        int showDelta = ((int) delta) / 1000;
        if (showDelta != 0) {
            this.f48$.id(R.id.app_video_fastForward_box).visible();
            this.f48$.id(R.id.app_video_fastForward).text((showDelta > 0 ? "+" + showDelta : "" + showDelta) + "s");
            this.f48$.id(R.id.app_video_fastForward_target).text(generateTime(this.newPosition) + "/");
            this.f48$.id(R.id.app_video_fastForward_all).text(generateTime(duration));
        }
    }

    private void onBrightnessSlide(float percent) {
        if (this.brightness < 0.0f) {
            this.brightness = this.activity.getWindow().getAttributes().screenBrightness;
            if (this.brightness <= 0.0f) {
                this.brightness = 0.5f;
            } else if (this.brightness < 0.01f) {
                this.brightness = 0.01f;
            }
        }
        Log.d(getClass().getSimpleName(), "brightness:" + this.brightness + ",percent:" + percent);
        this.f48$.id(R.id.app_video_brightness_box).visible();
        WindowManager.LayoutParams lpa = this.activity.getWindow().getAttributes();
        lpa.screenBrightness = this.brightness + percent;
        if (lpa.screenBrightness > TextTrackStyle.DEFAULT_FONT_SCALE) {
            lpa.screenBrightness = TextTrackStyle.DEFAULT_FONT_SCALE;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        this.f48$.id(R.id.app_video_brightness).text(((int) (lpa.screenBrightness * 100.0f)) + "%");
        this.activity.getWindow().setAttributes(lpa);
    }

    private long setProgress() {
        if (this.isDragging) {
            return 0;
        }
        long position = (long) this.videoView.getCurrentPosition();
        long duration = (long) this.videoView.getDuration();
        if (this.seekBar != null) {
            if (duration > 0) {
                this.seekBar.setProgress((int) ((1000 * position) / duration));
            }
            this.seekBar.setSecondaryProgress(this.videoView.getBufferPercentage() * 10);
        }
        this.duration = duration;
        this.f48$.id(R.id.app_video_currentTime).text(generateTime(position));
        this.f48$.id(R.id.app_video_endTime).text(generateTime(this.duration));
        return position;
    }

    public void hide(boolean force) {
        if (force || this.isShowing) {
            this.handler.removeMessages(1);
            showBottomControl(false);
            this.f48$.id(R.id.app_video_top_box).gone();
            this.f48$.id(R.id.app_video_bottom_box).gone();
            this.f48$.id(R.id.controls).gone();
            this.f48$.id(R.id.app_video_fullscreen).invisible();
            this.isShowing = false;
            this.onControlPanelVisibilityChangeListener.change(false);
        }
    }

    private void updateFullScreenButton() {
        if (getScreenOrientation() == 0) {
            this.f48$.id(R.id.app_video_fullscreen).image(R.drawable.ic_fullscreen_exit_white_36dp);
        } else {
            this.f48$.id(R.id.app_video_fullscreen).image(R.drawable.ic_fullscreen_white_24dp);
        }
    }

    public void setFullScreenOnly(boolean fullScreenOnly) {
        this.fullScreenOnly = fullScreenOnly;
        tryFullScreen(fullScreenOnly);
        if (fullScreenOnly) {
            this.activity.setRequestedOrientation(0);
        } else {
            this.activity.setRequestedOrientation(4);
        }
    }

    public void setScaleType(String scaleType) {
        if ("fitParent".equals(scaleType)) {
            this.videoView.setAspectRatio(0);
        } else if ("fillParent".equals(scaleType)) {
            this.videoView.setAspectRatio(1);
        } else if ("wrapContent".equals(scaleType)) {
            this.videoView.setAspectRatio(2);
        } else if ("fitXY".equals(scaleType)) {
            this.videoView.setAspectRatio(3);
        } else if ("16:9".equals(scaleType)) {
            this.videoView.setAspectRatio(4);
        } else if ("4:3".equals(scaleType)) {
            this.videoView.setAspectRatio(5);
        }
    }

    public void setShowNavIcon(boolean show) {
        this.f48$.id(R.id.app_video_finish).visibility(show ? 0 : 8);
    }

    public void start() {
        this.videoView.start();
    }

    public void pause() {
        this.videoView.pause();
    }

    public boolean onBackPressed() {
        if (this.fullScreenOnly || getScreenOrientation() != 0) {
            return false;
        }
        this.activity.setRequestedOrientation(1);
        return true;
    }

    public boolean isPlayerSupport() {
        return this.playerSupport;
    }

    public boolean isPlaying() {
        return this.videoView != null ? this.videoView.isPlaying() : false;
    }

    public void stop() {
        this.videoView.stopPlayback();
    }

    public NSTPlayerSeries seekTo(int msec, boolean showControlPanle) {
        this.videoView.seekTo(msec);
        if (showControlPanle) {
            show(this.defaultTimeout);
        }
        return this;
    }

    public NSTPlayerSeries forward(float percent) {
        if (!this.isLive && percent <= TextTrackStyle.DEFAULT_FONT_SCALE && percent >= -1.0f) {
            onProgressSlide(percent);
            showBottomControl(true);
            this.handler.sendEmptyMessage(1);
            endGesture();
        }
        return this;
    }

    public int getCurrentPosition() {
        return this.videoView.getCurrentPosition();
    }

    public int getCurrentWindowIndex() {
        return this.currentWindowIndex;
    }

    public int getDuration() {
        return this.videoView.getDuration();
    }

    public NSTPlayerSeries playInFullScreen(boolean fullScreen) {
        if (fullScreen) {
            this.activity.setRequestedOrientation(0);
            updateFullScreenButton();
        }
        return this;
    }

    public void toggleFullScreen() {
        if (getScreenOrientation() == 0) {
            this.activity.setRequestedOrientation(1);
        } else {
            this.activity.setRequestedOrientation(0);
        }
        updateFullScreenButton();
    }

    public NSTPlayerSeries onError(z.xtreamiptv.playerv3.view.nstplayer.NSTPlayerArchive.OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
        return this;
    }

    public NSTPlayerSeries onComplete(Runnable complete) {
        this.oncomplete = complete;
        return this;
    }

    public NSTPlayerSeries onInfo(z.xtreamiptv.playerv3.view.nstplayer.NSTPlayerArchive.OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
        return this;
    }

    public NSTPlayerSeries onControlPanelVisibilityChang(OnControlPanelVisibilityChangeListener listener) {
        this.onControlPanelVisibilityChangeListener = listener;
        return this;
    }

    public NSTPlayerSeries live(boolean isLive) {
        this.isLive = isLive;
        return this;
    }

    public NSTPlayerSeries toggleAspectRatio() {
        if (this.videoView != null) {
            this.videoView.toggleAspectRatio();
        }
        return this;
    }

    public NSTPlayerSeries onControlPanelVisibilityChange(OnControlPanelVisibilityChangeListener listener) {
        this.onControlPanelVisibilityChangeListener = listener;
        return this;
    }
}
