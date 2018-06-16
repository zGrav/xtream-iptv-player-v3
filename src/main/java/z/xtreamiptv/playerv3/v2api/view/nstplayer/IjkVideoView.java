package z.xtreamiptv.playerv3.v2api.view.nstplayer;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v7.media.*;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import z.xtreamiptv.playerv3.v2api.view.nstplayer.IRenderView.IRenderCallback;
import z.xtreamiptv.playerv3.v2api.view.nstplayer.IRenderView.ISurfaceHolder;
import z.xtreamiptv.playerv3.v2api.view.nstplayer.NSTPlayerActivity.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnPreparedListener;
import tv.danmaku.ijk.media.player.IMediaPlayer$OnVideoSizeChangedListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

public class IjkVideoView extends FrameLayout implements MediaPlayerControl {
    public static final int RENDER_NONE = 0;
    public static final int RENDER_SURFACE_VIEW = 1;
    public static final int RENDER_TEXTURE_VIEW = 2;
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PREPARING = 1;
    private static final int[] s_allAspectRatio = new int[]{0, 1, 2, 3, 4, 5};
    private String TAG = "IjkVideoView";
    private boolean enableBackgroundPlay = false;
    private boolean enableNoView = false;
    private boolean enableSurfaceView = true;
    private boolean enableTextureView = false;
    private List<Integer> mAllRenders = new ArrayList();
    private Context mAppContext;
    private IMediaPlayer$OnBufferingUpdateListener mBufferingUpdateListener = new C17006();
    private boolean mCanPause = true;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private IMediaPlayer$OnCompletionListener mCompletionListener = new C16963();
    private int mCurrentAspectRatio = s_allAspectRatio[this.mCurrentAspectRatioIndex];
    private int mCurrentAspectRatioIndex = 0;
    private int mCurrentBufferPercentage;
    private int mCurrentRender = 0;
    private int mCurrentRenderIndex = 0;
    private int mCurrentState = 0;
    private IMediaPlayer$OnErrorListener mErrorListener = new C16995();
    private Map<String, String> mHeaders;
    private IMediaPlayer$OnInfoListener mInfoListener = new C16974();
    private IMediaController mMediaController;
    private IMediaPlayer mMediaPlayer = null;
    private IMediaPlayer$OnCompletionListener mOnCompletionListener;
    private IMediaPlayer$OnErrorListener mOnErrorListener;
    private IMediaPlayer$OnInfoListener mOnInfoListener;
    private IMediaPlayer$OnPreparedListener mOnPreparedListener;
    IMediaPlayer$OnPreparedListener mPreparedListener = new C16952();
    private IRenderView mRenderView;
    IRenderCallback mSHCallback = new C17017();
    private long mSeekWhenPrepared;
    IMediaPlayer$OnVideoSizeChangedListener mSizeChangedListener = new C16941();
    private int mSurfaceHeight;
    private ISurfaceHolder mSurfaceHolder = null;
    private int mSurfaceWidth;
    private int mTargetState = 0;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoRotationDegree;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private String pixelFormat = "";
    private TextView subtitleDisplay;
    private boolean usingAndroidPlayer = false;
    private boolean usingMediaCodec = true;
    private boolean usingMediaCodecAutoRotate = true;
    private boolean usingOpenSLES = false;

    class C16941 implements IMediaPlayer$OnVideoSizeChangedListener {
        C16941() {
        }

        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
            IjkVideoView.this.mVideoWidth = mp.getVideoWidth();
            IjkVideoView.this.mVideoHeight = mp.getVideoHeight();
            IjkVideoView.this.mVideoSarNum = mp.getVideoSarNum();
            IjkVideoView.this.mVideoSarDen = mp.getVideoSarDen();
            if (IjkVideoView.this.mVideoWidth != 0 && IjkVideoView.this.mVideoHeight != 0) {
                if (IjkVideoView.this.mRenderView != null) {
                    IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                    IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                }
                IjkVideoView.this.requestLayout();
            }
        }
    }

    class C16952 implements IMediaPlayer$OnPreparedListener {
        C16952() {
        }

        public void onPrepared(IMediaPlayer mp) {
            IjkVideoView.this.mCurrentState = 2;
            if (IjkVideoView.this.mOnPreparedListener != null) {
                IjkVideoView.this.mOnPreparedListener.onPrepared(IjkVideoView.this.mMediaPlayer);
            }
            if (IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.setEnabled(true);
            }
            IjkVideoView.this.mVideoWidth = mp.getVideoWidth();
            IjkVideoView.this.mVideoHeight = mp.getVideoHeight();
            long seekToPosition = IjkVideoView.this.mSeekWhenPrepared;
            if (seekToPosition != 0) {
                IjkVideoView.this.seekTo((int) seekToPosition);
            }
            if (IjkVideoView.this.mVideoWidth == 0 || IjkVideoView.this.mVideoHeight == 0) {
                if (IjkVideoView.this.mTargetState == 3) {
                    IjkVideoView.this.start();
                }
            } else if (IjkVideoView.this.mRenderView != null) {
                IjkVideoView.this.mRenderView.setVideoSize(IjkVideoView.this.mVideoWidth, IjkVideoView.this.mVideoHeight);
                IjkVideoView.this.mRenderView.setVideoSampleAspectRatio(IjkVideoView.this.mVideoSarNum, IjkVideoView.this.mVideoSarDen);
                if (IjkVideoView.this.mRenderView.shouldWaitForResize() && (IjkVideoView.this.mSurfaceWidth != IjkVideoView.this.mVideoWidth || IjkVideoView.this.mSurfaceHeight != IjkVideoView.this.mVideoHeight)) {
                    return;
                }
                if (IjkVideoView.this.mTargetState == 3) {
                    IjkVideoView.this.start();
                    if (IjkVideoView.this.mMediaController != null) {
                        IjkVideoView.this.mMediaController.show();
                    }
                } else if (!IjkVideoView.this.isPlaying()) {
                    if ((seekToPosition != 0 || IjkVideoView.this.getCurrentPosition() > 0) && IjkVideoView.this.mMediaController != null) {
                        IjkVideoView.this.mMediaController.show(0);
                    }
                }
            }
        }
    }

    class C16963 implements IMediaPlayer$OnCompletionListener {
        C16963() {
        }

        public void onCompletion(IMediaPlayer mp) {
            IjkVideoView.this.mCurrentState = 5;
            IjkVideoView.this.mTargetState = 5;
            if (IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.hide();
            }
            if (IjkVideoView.this.mOnCompletionListener != null) {
                IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer);
            }
        }
    }

    class C16974 implements IMediaPlayer$OnInfoListener {
        C16974() {
        }

        public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
            if (IjkVideoView.this.mOnInfoListener != null) {
                IjkVideoView.this.mOnInfoListener.onInfo(mp, arg1, arg2);
            }
            switch (arg1) {
                case 10001:
                    IjkVideoView.this.mVideoRotationDegree = arg2;
                    Log.d(IjkVideoView.this.TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                    if (IjkVideoView.this.mRenderView != null) {
                        IjkVideoView.this.mRenderView.setVideoRotation(arg2);
                        break;
                    }
                    break;
            }
            return true;
        }
    }

    class C16995 implements IMediaPlayer$OnErrorListener {

        class C16981 implements DialogInterface.OnClickListener {
            C16981() {
            }

            public void onClick(DialogInterface dialog, int whichButton) {
                if (IjkVideoView.this.mOnCompletionListener != null) {
                    IjkVideoView.this.mOnCompletionListener.onCompletion(IjkVideoView.this.mMediaPlayer);
                }
            }
        }

        C16995() {
        }

        public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
            Log.d(IjkVideoView.this.TAG, "Error: " + framework_err + "," + impl_err);
            IjkVideoView.this.mCurrentState = -1;
            IjkVideoView.this.mTargetState = -1;
            if (IjkVideoView.this.mMediaController != null) {
                IjkVideoView.this.mMediaController.hide();
            }
            if ((IjkVideoView.this.mOnErrorListener == null || !IjkVideoView.this.mOnErrorListener.onError(IjkVideoView.this.mMediaPlayer, framework_err, impl_err)) && IjkVideoView.this.getWindowToken() != null) {
                Resources r = IjkVideoView.this.mAppContext.getResources();
                String message = "Unknown error";
                if (framework_err == 200) {
                    message = "Invalid progressive playback";
                }
                new Builder(IjkVideoView.this.getContext()).setMessage(message).setPositiveButton("MediaRouteProviderProtocol.SERVICE_DATA_ERROR", new C16981()).setCancelable(false).show();
            }
            return true;
        }
    }

    class C17006 implements IMediaPlayer$OnBufferingUpdateListener {
        C17006() {
        }

        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            IjkVideoView.this.mCurrentBufferPercentage = percent;
        }
    }

    class C17017 implements IRenderCallback {
        C17017() {
        }

        public void onSurfaceChanged(@NonNull ISurfaceHolder holder, int format, int w, int h) {
            if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                Log.e(IjkVideoView.this.TAG, "onSurfaceChanged: unmatched render callback\n");
                return;
            }
            IjkVideoView.this.mSurfaceWidth = w;
            IjkVideoView.this.mSurfaceHeight = h;
            boolean isValidState;
            if (IjkVideoView.this.mTargetState == 3) {
                isValidState = true;
            } else {
                isValidState = false;
            }
            boolean hasValidSize;
            if (!IjkVideoView.this.mRenderView.shouldWaitForResize() || (IjkVideoView.this.mVideoWidth == w && IjkVideoView.this.mVideoHeight == h)) {
                hasValidSize = true;
            } else {
                hasValidSize = false;
            }
            if (IjkVideoView.this.mMediaPlayer != null && isValidState && hasValidSize) {
                if (IjkVideoView.this.mSeekWhenPrepared != 0) {
                    IjkVideoView.this.seekTo((int) IjkVideoView.this.mSeekWhenPrepared);
                }
                IjkVideoView.this.start();
            }
        }

        public void onSurfaceCreated(@NonNull ISurfaceHolder holder, int width, int height) {
            if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                Log.e(IjkVideoView.this.TAG, "onSurfaceCreated: unmatched render callback\n");
                return;
            }
            IjkVideoView.this.mSurfaceHolder = holder;
            if (IjkVideoView.this.mMediaPlayer != null) {
                IjkVideoView.this.bindSurfaceHolder(IjkVideoView.this.mMediaPlayer, holder);
            } else {
                IjkVideoView.this.openVideo();
            }
        }

        public void onSurfaceDestroyed(@NonNull ISurfaceHolder holder) {
            if (holder.getRenderView() != IjkVideoView.this.mRenderView) {
                Log.e(IjkVideoView.this.TAG, "onSurfaceDestroyed: unmatched render callback\n");
                return;
            }
            IjkVideoView.this.mSurfaceHolder = null;
            IjkVideoView.this.releaseWithoutStop();
        }
    }

    public int getCurrentState() {
        return this.mCurrentState;
    }

    public IjkVideoView(Context context) {
        super(context);
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    @TargetApi(21)
    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVideoView(context);
    }

    private void initVideoView(Context context) {
        this.mAppContext = context.getApplicationContext();
        initBackground();
        initRenders();
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.subtitleDisplay = new TextView(context);
        this.subtitleDisplay.setTextSize(24.0f);
        this.subtitleDisplay.setGravity(17);
        addView(this.subtitleDisplay, new LayoutParams(-1, -2, 80));
    }

    public void setRenderView(IRenderView renderView) {
        if (this.mRenderView != null) {
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.setDisplay(null);
            }
            View renderUIView = this.mRenderView.getView();
            this.mRenderView.removeRenderCallback(this.mSHCallback);
            this.mRenderView = null;
            removeView(renderUIView);
        }
        if (renderView != null) {
            this.mRenderView = renderView;
            renderView.setAspectRatio(this.mCurrentAspectRatio);
            if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
                renderView.setVideoSize(this.mVideoWidth, this.mVideoHeight);
            }
            if (this.mVideoSarNum > 0 && this.mVideoSarDen > 0) {
                renderView.setVideoSampleAspectRatio(this.mVideoSarNum, this.mVideoSarDen);
            }
            View renderUIView = this.mRenderView.getView();
            renderUIView.setLayoutParams(new LayoutParams(-2, -2, 17));
            addView(renderUIView);
            this.mRenderView.addRenderCallback(this.mSHCallback);
            this.mRenderView.setVideoRotation(this.mVideoRotationDegree);
        }
    }

    public void setRender(int render) {
        switch (render) {
            case 0:
                setRenderView(null);
                return;
            case 1:
                setRenderView(new SurfaceRenderView(getContext()));
                return;
            case 2:
                TextureRenderView renderView = new TextureRenderView(getContext());
                if (this.mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(this.mMediaPlayer);
                    renderView.setVideoSize(this.mMediaPlayer.getVideoWidth(), this.mMediaPlayer.getVideoHeight());
                    renderView.setVideoSampleAspectRatio(this.mMediaPlayer.getVideoSarNum(), this.mMediaPlayer.getVideoSarDen());
                    renderView.setAspectRatio(this.mCurrentAspectRatio);
                }
                setRenderView(renderView);
                return;
            default:
                Log.e(this.TAG, String.format(Locale.getDefault(), "invalid render %d\n", new Object[]{Integer.valueOf(render)}));
                return;
        }
    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    private void setVideoURI(Uri uri, Map<String, String> headers) {
        this.mUri = uri;
        this.mHeaders = headers;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    private void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            release(false);
            ((AudioManager) this.mAppContext.getSystemService("audio")).requestAudioFocus(null, 3, 1);
            try {
                if (this.usingAndroidPlayer) {
                    this.mMediaPlayer = new AndroidMediaPlayer();
                    this.mMediaPlayer.getMediaInfo();
                } else {
                    IjkMediaPlayer ijkMediaPlayer = null;
                    if (this.mUri != null) {
                        ijkMediaPlayer = new IjkMediaPlayer();
                        IjkMediaPlayer.native_setLogLevel(Config.isDebug() ? 3 : 6);
                        if (this.usingMediaCodec) {
                            ijkMediaPlayer.setOption(4, "mediacodec", 1);
                            if (this.usingMediaCodecAutoRotate) {
                                ijkMediaPlayer.setOption(4, "mediacodec-auto-rotate", 1);
                            } else {
                                ijkMediaPlayer.setOption(4, "mediacodec-auto-rotate", 0);
                            }
                        } else {
                            ijkMediaPlayer.setOption(4, "mediacodec", 0);
                        }
                        if (this.usingOpenSLES) {
                            ijkMediaPlayer.setOption(4, "opensles", 1);
                        } else {
                            ijkMediaPlayer.setOption(4, "opensles", 0);
                        }
                        if (TextUtils.isEmpty(this.pixelFormat)) {
                            ijkMediaPlayer.setOption(4, "overlay-format", 842225234);
                        } else {
                            ijkMediaPlayer.setOption(4, "overlay-format", this.pixelFormat);
                        }
                        ijkMediaPlayer.setOption(4, "framedrop", 1);
                        ijkMediaPlayer.setOption(4, "start-on-prepared", 0);
                        ijkMediaPlayer.setOption(1, "http-detect-range-support", 0);
                        ijkMediaPlayer.setOption(1, "timeout", 10000000);
                        ijkMediaPlayer.setOption(1, "reconnect", 1);
                        ijkMediaPlayer.setOption(2, "skip_loop_filter", 48);
                        ijkMediaPlayer.setOption(4, "subtitle", 1);
                    }
                    ijkMediaPlayer.getMediaInfo();
                    this.mMediaPlayer = ijkMediaPlayer;
                }
                if (this.enableBackgroundPlay) {
                    this.mMediaPlayer = new TextureMediaPlayer(this.mMediaPlayer);
                }
                Context context = getContext();
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                if (VERSION.SDK_INT > 14) {
                    this.mMediaPlayer.setDataSource(this.mAppContext, this.mUri, this.mHeaders);
                } else {
                    this.mMediaPlayer.setDataSource(this.mUri.toString());
                }
                bindSurfaceHolder(this.mMediaPlayer, this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException ex) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            } catch (IllegalArgumentException ex2) {
                Log.w(this.TAG, "Unable to open content: " + this.mUri, ex2);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(IMediaController controller) {
        if (this.mMediaController != null) {
            this.mMediaController.hide();
        }
        this.mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null && this.mMediaController != null) {
            View anchorView;
            this.mMediaController.setMediaPlayer(this);
            if (getParent() instanceof View) {
                anchorView = (View) getParent();
            } else {
                anchorView = this;
            }
            this.mMediaController.setAnchorView(anchorView);
            this.mMediaController.setEnabled(isInPlaybackState());
        }
    }

    public void setOnPreparedListener(IMediaPlayer$OnPreparedListener l) {
        this.mOnPreparedListener = l;
    }

    public void setOnCompletionListener(IMediaPlayer$OnCompletionListener l) {
        this.mOnCompletionListener = l;
    }

    public void setOnErrorListener(IMediaPlayer$OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(IMediaPlayer$OnInfoListener l) {
        this.mOnInfoListener = l;
    }

    private void bindSurfaceHolder(IMediaPlayer mp, ISurfaceHolder holder) {
        if (mp != null) {
            if (holder == null) {
                mp.setDisplay(null);
            } else {
                holder.bindToMediaPlayer(mp);
            }
        }
    }

    public void releaseWithoutStop() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setDisplay(null);
        }
    }

    public void release(boolean cleartargetstate) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (cleartargetstate) {
                this.mTargetState = 0;
            }
            ((AudioManager) this.mAppContext.getSystemService("audio")).abandonAudioFocus(null);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = (keyCode == 4 || keyCode == 24 || keyCode == 25 || keyCode == 164 || keyCode == 82 || keyCode == 5 || keyCode == 6) ? false : true;
        if (isInPlaybackState() && isKeyCodeSupported && this.mMediaController != null) {
            if (keyCode == 79 || keyCode == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode == 126) {
                if (this.mMediaPlayer.isPlaying()) {
                    return true;
                }
                start();
                this.mMediaController.hide();
                return true;
            } else if (keyCode != 86 && keyCode != 127) {
                toggleMediaControlsVisiblity();
            } else if (!this.mMediaPlayer.isPlaying()) {
                return true;
            } else {
                pause();
                this.mMediaController.show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getDuration();
        }
        return -1;
    }

    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) this.mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo((long) msec);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = (long) msec;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        if (this.mMediaPlayer != null) {
            return this.mCurrentBufferPercentage;
        }
        return 0;
    }

    private boolean isInPlaybackState() {
        return (this.mMediaPlayer == null || this.mCurrentState == -1 || this.mCurrentState == 0 || this.mCurrentState == 1) ? false : true;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int toggleAspectRatio() {
        this.mCurrentAspectRatioIndex++;
        this.mCurrentAspectRatioIndex %= s_allAspectRatio.length;
        this.mCurrentAspectRatio = s_allAspectRatio[this.mCurrentAspectRatioIndex];
        if (this.mRenderView != null) {
            this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
        }
        return this.mCurrentAspectRatio;
    }

    private void initRenders() {
        this.mAllRenders.clear();
        if (this.enableSurfaceView) {
            this.mAllRenders.add(Integer.valueOf(1));
        }
        if (this.enableTextureView && VERSION.SDK_INT >= 14) {
            this.mAllRenders.add(Integer.valueOf(2));
        }
        if (this.enableNoView) {
            this.mAllRenders.add(Integer.valueOf(0));
        }
        if (this.mAllRenders.isEmpty()) {
            this.mAllRenders.add(Integer.valueOf(1));
        }
        this.mCurrentRender = ((Integer) this.mAllRenders.get(this.mCurrentRenderIndex)).intValue();
        setRender(this.mCurrentRender);
    }

    public int toggleRender() {
        this.mCurrentRenderIndex++;
        this.mCurrentRenderIndex %= this.mAllRenders.size();
        this.mCurrentRender = ((Integer) this.mAllRenders.get(this.mCurrentRenderIndex)).intValue();
        setRender(this.mCurrentRender);
        return this.mCurrentRender;
    }

    private void initBackground() {
        if (!this.enableBackgroundPlay) {
        }
    }

    public void setAspectRatio(int aspectRatio) {
        for (int i = 0; i < s_allAspectRatio.length; i++) {
            if (s_allAspectRatio[i] == aspectRatio) {
                this.mCurrentAspectRatioIndex = i;
                if (this.mRenderView != null) {
                    this.mRenderView.setAspectRatio(this.mCurrentAspectRatio);
                    return;
                }
                return;
            }
        }
    }
}
