package tv.danmaku.ijk.media.player;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import tv.danmaku.ijk.media.player.IjkMediaMeta.IjkStreamMeta;
import tv.danmaku.ijk.media.player.annotations.AccessedByNative;
import tv.danmaku.ijk.media.player.annotations.CalledByNative;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.IjkTrackInfo;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

public final class IjkMediaPlayer extends AbstractMediaPlayer {
    public static final int FFP_PROPV_DECODER_AVCODEC = 1;
    public static final int FFP_PROPV_DECODER_MEDIACODEC = 2;
    public static final int FFP_PROPV_DECODER_UNKNOWN = 0;
    public static final int FFP_PROPV_DECODER_VIDEOTOOLBOX = 3;
    public static final int FFP_PROP_FLOAT_PLAYBACK_RATE = 10003;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_BYTES = 20008;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_DURATION = 20006;
    public static final int FFP_PROP_INT64_AUDIO_CACHED_PACKETS = 20010;
    public static final int FFP_PROP_INT64_AUDIO_DECODER = 20004;
    public static final int FFP_PROP_INT64_SELECTED_AUDIO_STREAM = 20002;
    public static final int FFP_PROP_INT64_SELECTED_VIDEO_STREAM = 20001;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_BYTES = 20007;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_DURATION = 20005;
    public static final int FFP_PROP_INT64_VIDEO_CACHED_PACKETS = 20009;
    public static final int FFP_PROP_INT64_VIDEO_DECODER = 20003;
    public static final int IJK_LOG_DEBUG = 3;
    public static final int IJK_LOG_DEFAULT = 1;
    public static final int IJK_LOG_ERROR = 6;
    public static final int IJK_LOG_FATAL = 7;
    public static final int IJK_LOG_INFO = 4;
    public static final int IJK_LOG_SILENT = 8;
    public static final int IJK_LOG_UNKNOWN = 0;
    public static final int IJK_LOG_VERBOSE = 2;
    public static final int IJK_LOG_WARN = 5;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_ERROR = 100;
    private static final int MEDIA_INFO = 200;
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    protected static final int MEDIA_SET_VIDEO_SAR = 10001;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_TIMED_TEXT = 99;
    public static final int OPT_CATEGORY_CODEC = 2;
    public static final int OPT_CATEGORY_FORMAT = 1;
    public static final int OPT_CATEGORY_PLAYER = 4;
    public static final int OPT_CATEGORY_SWS = 3;
    public static final int PROP_FLOAT_VIDEO_DECODE_FRAMES_PER_SECOND = 10001;
    public static final int PROP_FLOAT_VIDEO_OUTPUT_FRAMES_PER_SECOND = 10002;
    public static final int SDL_FCC_RV16 = 909203026;
    public static final int SDL_FCC_RV32 = 842225234;
    public static final int SDL_FCC_YV12 = 842094169;
    private static final String TAG = IjkMediaPlayer.class.getName();
    private static volatile boolean mIsLibLoaded = false;
    private static volatile boolean mIsNativeInitialized = false;
    private static final IjkLibLoader sLocalLibLoader = new C22801();
    private String mDataSource;
    private EventHandler mEventHandler;
    @AccessedByNative
    private int mListenerContext;
    @AccessedByNative
    private long mNativeMediaDataSource;
    @AccessedByNative
    private long mNativeMediaPlayer;
    @AccessedByNative
    private int mNativeSurfaceTexture;
    private OnControlMessageListener mOnControlMessageListener;
    private OnMediaCodecSelectListener mOnMediaCodecSelectListener;
    private OnNativeInvokeListener mOnNativeInvokeListener;
    private boolean mScreenOnWhilePlaying;
    private boolean mStayAwake;
    private SurfaceHolder mSurfaceHolder;
    private int mVideoHeight;
    private int mVideoSarDen;
    private int mVideoSarNum;
    private int mVideoWidth;
    private WakeLock mWakeLock;

    static class C22801 implements IjkLibLoader {
        C22801() {
        }

        public void loadLibrary(String libName) throws UnsatisfiedLinkError, SecurityException {
            System.loadLibrary(libName);
        }
    }

    public interface OnMediaCodecSelectListener {
        String onMediaCodecSelect(IMediaPlayer iMediaPlayer, String str, int i, int i2);
    }

    public static class DefaultMediaCodecSelector implements OnMediaCodecSelectListener {
        public static final DefaultMediaCodecSelector sInstance = new DefaultMediaCodecSelector();

        @TargetApi(16)
        public String onMediaCodecSelect(IMediaPlayer mp, String mimeType, int profile, int level) {
            if (VERSION.SDK_INT < 16) {
                return null;
            }
            if (TextUtils.isEmpty(mimeType)) {
                return null;
            }
            Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "onSelectCodec: mime=%s, profile=%d, level=%d", new Object[]{mimeType, Integer.valueOf(profile), Integer.valueOf(level)}));
            ArrayList<IjkMediaCodecInfo> candidateCodecList = new ArrayList();
            int numCodecs = MediaCodecList.getCodecCount();
            for (int i = 0; i < numCodecs; i++) {
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
                Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "  found codec: %s", new Object[]{codecInfo.getName()}));
                if (!codecInfo.isEncoder()) {
                    String[] types = codecInfo.getSupportedTypes();
                    if (types != null) {
                        for (String type : types) {
                            if (!TextUtils.isEmpty(type)) {
                                Log.d(IjkMediaPlayer.TAG, String.format(Locale.US, "    mime: %s", new Object[]{type}));
                                if (type.equalsIgnoreCase(mimeType)) {
                                    IjkMediaCodecInfo candidate = IjkMediaCodecInfo.setupCandidate(codecInfo, mimeType);
                                    if (candidate != null) {
                                        candidateCodecList.add(candidate);
                                        Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "candidate codec: %s rank=%d", new Object[]{codecInfo.getName(), Integer.valueOf(candidate.mRank)}));
                                        candidate.dumpProfileLevels(mimeType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (candidateCodecList.isEmpty()) {
                return null;
            }
            IjkMediaCodecInfo bestCodec = (IjkMediaCodecInfo) candidateCodecList.get(0);
            Iterator it = candidateCodecList.iterator();
            while (it.hasNext()) {
                IjkMediaCodecInfo codec = (IjkMediaCodecInfo) it.next();
                if (codec.mRank > bestCodec.mRank) {
                    bestCodec = codec;
                }
            }
            if (bestCodec.mRank < IjkMediaCodecInfo.RANK_LAST_CHANCE) {
                Log.w(IjkMediaPlayer.TAG, String.format(Locale.US, "unaccetable codec: %s", new Object[]{bestCodec.mCodecInfo.getName()}));
                return null;
            }
            Log.i(IjkMediaPlayer.TAG, String.format(Locale.US, "selected codec: %s rank=%d", new Object[]{bestCodec.mCodecInfo.getName(), Integer.valueOf(bestCodec.mRank)}));
            return bestCodec.mCodecInfo.getName();
        }
    }

    private static class EventHandler extends Handler {
        private final WeakReference<IjkMediaPlayer> mWeakPlayer;

        public EventHandler(IjkMediaPlayer mp, Looper looper) {
            super(looper);
            this.mWeakPlayer = new WeakReference(mp);
        }

        public void handleMessage(Message msg) {
            IjkMediaPlayer player = (IjkMediaPlayer) this.mWeakPlayer.get();
            if (player == null || player.mNativeMediaPlayer == 0) {
                DebugLog.m14w(IjkMediaPlayer.TAG, "IjkMediaPlayer went away with unhandled events");
                return;
            }
            switch (msg.what) {
                case 0:
                case 99:
                    return;
                case 1:
                    player.notifyOnPrepared();
                    return;
                case 2:
                    player.stayAwake(false);
                    player.notifyOnCompletion();
                    return;
                case 3:
                    long bufferPosition = (long) msg.arg1;
                    if (bufferPosition < 0) {
                        bufferPosition = 0;
                    }
                    long percent = 0;
                    long duration = player.getDuration();
                    if (duration > 0) {
                        percent = (100 * bufferPosition) / duration;
                    }
                    if (percent >= 100) {
                        percent = 100;
                    }
                    player.notifyOnBufferingUpdate((int) percent);
                    return;
                case 4:
                    player.notifyOnSeekComplete();
                    return;
                case 5:
                    player.mVideoWidth = msg.arg1;
                    player.mVideoHeight = msg.arg2;
                    player.notifyOnVideoSizeChanged(player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                    return;
                case 100:
                    DebugLog.m8e(IjkMediaPlayer.TAG, "Error (" + msg.arg1 + "," + msg.arg2 + ")");
                    if (!player.notifyOnError(msg.arg1, msg.arg2)) {
                        player.notifyOnCompletion();
                    }
                    player.stayAwake(false);
                    return;
                case 200:
                    switch (msg.arg1) {
                        case 3:
                            DebugLog.m10i(IjkMediaPlayer.TAG, "Info: MEDIA_INFO_VIDEO_RENDERING_START\n");
                            break;
                    }
                    player.notifyOnInfo(msg.arg1, msg.arg2);
                    return;
                case 10001:
                    player.mVideoSarNum = msg.arg1;
                    player.mVideoSarDen = msg.arg2;
                    player.notifyOnVideoSizeChanged(player.mVideoWidth, player.mVideoHeight, player.mVideoSarNum, player.mVideoSarDen);
                    return;
                default:
                    DebugLog.m8e(IjkMediaPlayer.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    public interface OnControlMessageListener {
        String onControlResolveSegmentUrl(int i);
    }

    public interface OnNativeInvokeListener {
        public static final String ARG_RETRY_COUNTER = "retry_counter";
        public static final String ARG_SEGMENT_INDEX = "segment_index";
        public static final String ARG_URL = "url";
        public static final int ON_CONCAT_RESOLVE_SEGMENT = 65536;
        public static final int ON_HTTP_OPEN = 65538;
        public static final int ON_LIVE_RETRY = 65540;
        public static final int ON_TCP_OPEN = 65537;

        boolean onNativeInvoke(int i, Bundle bundle);
    }

    private native String _getAudioCodecInfo();

    private static native String _getColorFormatName(int i);

    private native int _getLoopCount();

    private native Bundle _getMediaMeta();

    private native float _getPropertyFloat(int i, float f);

    private native long _getPropertyLong(int i, long j);

    private native String _getVideoCodecInfo();

    private native void _pause() throws IllegalStateException;

    private native void _release();

    private native void _reset();

    private native void _setDataSource(String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSource(IMediaDataSource iMediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setDataSourceFd(int i) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private native void _setLoopCount(int i);

    private native void _setOption(int i, String str, long j);

    private native void _setOption(int i, String str, String str2);

    private native void _setPropertyFloat(int i, float f);

    private native void _setPropertyLong(int i, long j);

    private native void _setStreamSelected(int i, boolean z);

    private native void _setVideoSurface(Surface surface);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    private native void native_finalize();

    private static native void native_init();

    private native void native_message_loop(Object obj);

    public static native void native_profileBegin(String str);

    public static native void native_profileEnd();

    public static native void native_setLogLevel(int i);

    private native void native_setup(Object obj);

    public native void _prepareAsync() throws IllegalStateException;

    public native int getAudioSessionId();

    public native long getCurrentPosition();

    public native long getDuration();

    public native boolean isPlaying();

    public native void seekTo(long j) throws IllegalStateException;

    public native void setVolume(float f, float f2);

    public static void loadLibrariesOnce(IjkLibLoader libLoader) {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsLibLoaded) {
                if (libLoader == null) {
                    libLoader = sLocalLibLoader;
                }
                libLoader.loadLibrary("ijkffmpeg");
                libLoader.loadLibrary("ijksdl");
                libLoader.loadLibrary("ijkplayer");
                mIsLibLoaded = true;
            }
        }
    }

    private static void initNativeOnce() {
        synchronized (IjkMediaPlayer.class) {
            if (!mIsNativeInitialized) {
                native_init();
                mIsNativeInitialized = true;
            }
        }
    }

    public IjkMediaPlayer() {
        this(sLocalLibLoader);
    }

    public IjkMediaPlayer(IjkLibLoader libLoader) {
        this.mWakeLock = null;
        initPlayer(libLoader);
    }

    private void initPlayer(IjkLibLoader libLoader) {
        loadLibrariesOnce(libLoader);
        initNativeOnce();
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            looper = Looper.getMainLooper();
            if (looper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                this.mEventHandler = null;
            }
        }
        native_setup(new WeakReference(this));
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurface(Surface surface) {
        if (this.mScreenOnWhilePlaying && surface != null) {
            DebugLog.m14w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        this.mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, null);
    }

    @TargetApi(14)
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            setDataSource(uri.getPath());
            return;
        }
        if ("content".equals(scheme) && "settings".equals(uri.getAuthority())) {
            uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.getDefaultType(uri));
            if (uri == null) {
                throw new FileNotFoundException("Failed to resolve default ringtone");
            }
        }
        AssetFileDescriptor fd = null;
        try {
            fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            if (fd != null) {
                if (fd.getDeclaredLength() < 0) {
                    setDataSource(fd.getFileDescriptor());
                } else {
                    setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
                }
                if (fd != null) {
                    fd.close();
                }
            } else if (fd != null) {
                fd.close();
            }
        } catch (SecurityException e) {
            if (fd != null) {
                fd.close();
            }
            Log.d(TAG, "Couldn't open file on client side, trying server side");
            setDataSource(uri.toString(), (Map) headers);
        } catch (IOException e2) {
            if (fd != null) {
                fd.close();
            }
            Log.d(TAG, "Couldn't open file on client side, trying server side");
            setDataSource(uri.toString(), (Map) headers);
        } catch (Throwable th) {
            if (fd != null) {
                fd.close();
            }
        }
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mDataSource = path;
        _setDataSource(path, null, null);
    }

    public void setDataSource(String path, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (!(headers == null || headers.isEmpty())) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, String> entry : headers.entrySet()) {
                sb.append((String) entry.getKey());
                sb.append(":");
                if (!TextUtils.isEmpty((String) entry.getValue())) {
                    sb.append((String) entry.getValue());
                }
                sb.append("\r\n");
                setOption(1, "headers", sb.toString());
            }
        }
        setDataSource(path);
    }

    @TargetApi(13)
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        if (VERSION.SDK_INT < 12) {
            try {
                Field f = fd.getClass().getDeclaredField("descriptor");
                f.setAccessible(true);
                _setDataSourceFd(f.getInt(fd));
                return;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
        ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(fd);
        try {
            _setDataSourceFd(pfd.getFd());
        } finally {
            pfd.close();
        }
    }

    private void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        setDataSource(fd);
    }

    public void setDataSource(IMediaDataSource mediaDataSource) throws IllegalArgumentException, SecurityException, IllegalStateException {
        _setDataSource(mediaDataSource);
    }

    public String getDataSource() {
        return this.mDataSource;
    }

    public void prepareAsync() throws IllegalStateException {
        _prepareAsync();
    }

    public void start() throws IllegalStateException {
        stayAwake(true);
        _start();
    }

    public void stop() throws IllegalStateException {
        stayAwake(false);
        _stop();
    }

    public void pause() throws IllegalStateException {
        stayAwake(false);
        _pause();
    }

    @SuppressLint({"Wakelock"})
    public void setWakeMode(Context context, int mode) {
        boolean washeld = false;
        if (this.mWakeLock != null) {
            if (this.mWakeLock.isHeld()) {
                washeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        this.mWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(536870912 | mode, IjkMediaPlayer.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (washeld) {
            this.mWakeLock.acquire();
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            if (screenOn && this.mSurfaceHolder == null) {
                DebugLog.m14w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    @SuppressLint({"Wakelock"})
    private void stayAwake(boolean awake) {
        if (this.mWakeLock != null) {
            if (awake && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            SurfaceHolder surfaceHolder = this.mSurfaceHolder;
            boolean z = this.mScreenOnWhilePlaying && this.mStayAwake;
            surfaceHolder.setKeepScreenOn(z);
        }
    }

    public IjkTrackInfo[] getTrackInfo() {
        Bundle bundle = getMediaMeta();
        if (bundle == null) {
            return null;
        }
        IjkMediaMeta mediaMeta = IjkMediaMeta.parse(bundle);
        if (mediaMeta == null || mediaMeta.mStreams == null) {
            return null;
        }
        ArrayList<IjkTrackInfo> trackInfos = new ArrayList();
        Iterator it = mediaMeta.mStreams.iterator();
        while (it.hasNext()) {
            IjkStreamMeta streamMeta = (IjkStreamMeta) it.next();
            IjkTrackInfo trackInfo = new IjkTrackInfo(streamMeta);
            if (streamMeta.mType.equalsIgnoreCase("video")) {
                trackInfo.setTrackType(1);
            } else if (streamMeta.mType.equalsIgnoreCase("audio")) {
                trackInfo.setTrackType(2);
            }
            trackInfos.add(trackInfo);
        }
        return (IjkTrackInfo[]) trackInfos.toArray(new IjkTrackInfo[trackInfos.size()]);
    }

    public int getSelectedTrack(int trackType) {
        switch (trackType) {
            case 1:
                return (int) _getPropertyLong(FFP_PROP_INT64_SELECTED_VIDEO_STREAM, -1);
            case 2:
                return (int) _getPropertyLong(FFP_PROP_INT64_SELECTED_AUDIO_STREAM, -1);
            default:
                return -1;
        }
    }

    public void selectTrack(int track) {
        _setStreamSelected(track, true);
    }

    public void deselectTrack(int track) {
        _setStreamSelected(track, false);
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public int getVideoSarNum() {
        return this.mVideoSarNum;
    }

    public int getVideoSarDen() {
        return this.mVideoSarDen;
    }

    public void release() {
        stayAwake(false);
        updateSurfaceScreenOn();
        resetListeners();
        _release();
    }

    public void reset() {
        stayAwake(false);
        _reset();
        this.mEventHandler.removeCallbacksAndMessages(null);
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
    }

    public void setLooping(boolean looping) {
        int loopCount = looping ? 0 : 1;
        setOption(4, "loop", (long) loopCount);
        _setLoopCount(loopCount);
    }

    public boolean isLooping() {
        if (_getLoopCount() != 1) {
            return true;
        }
        return false;
    }

    @TargetApi(23)
    public void setSpeed(float speed) {
        _setPropertyFloat(FFP_PROP_FLOAT_PLAYBACK_RATE, speed);
    }

    @TargetApi(23)
    public float getSpeed(float speed) {
        return _getPropertyFloat(FFP_PROP_FLOAT_PLAYBACK_RATE, 0.0f);
    }

    public int getVideoDecoder() {
        return (int) _getPropertyLong(FFP_PROP_INT64_VIDEO_DECODER, 2);
    }

    public float getVideoOutputFramesPerSecond() {
        return _getPropertyFloat(10002, 0.0f);
    }

    public float getVideoDecodeFramesPerSecond() {
        return _getPropertyFloat(10001, 0.0f);
    }

    public long getVideoCachedDuration() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_DURATION, 0);
    }

    public long getAudioCachedDuration() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_DURATION, 0);
    }

    public long getVideoCachedBytes() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_BYTES, 0);
    }

    public long getAudioCachedBytes() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_BYTES, 0);
    }

    public long getVideoCachedPackets() {
        return _getPropertyLong(FFP_PROP_INT64_VIDEO_CACHED_PACKETS, 0);
    }

    public long getAudioCachedPackets() {
        return _getPropertyLong(FFP_PROP_INT64_AUDIO_CACHED_PACKETS, 0);
    }

    public MediaInfo getMediaInfo() {
        String[] nodes;
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo.mMediaPlayerName = "ijkplayer";
        String videoCodecInfo = _getVideoCodecInfo();
        if (!TextUtils.isEmpty(videoCodecInfo)) {
            nodes = videoCodecInfo.split(",");
            if (nodes.length >= 2) {
                mediaInfo.mVideoDecoder = nodes[0];
                mediaInfo.mVideoDecoderImpl = nodes[1];
            } else if (nodes.length >= 1) {
                mediaInfo.mVideoDecoder = nodes[0];
                mediaInfo.mVideoDecoderImpl = "";
            }
        }
        String audioCodecInfo = _getAudioCodecInfo();
        if (!TextUtils.isEmpty(audioCodecInfo)) {
            nodes = audioCodecInfo.split(",");
            if (nodes.length >= 2) {
                mediaInfo.mAudioDecoder = nodes[0];
                mediaInfo.mAudioDecoderImpl = nodes[1];
            } else if (nodes.length >= 1) {
                mediaInfo.mAudioDecoder = nodes[0];
                mediaInfo.mAudioDecoderImpl = "";
            }
        }
        try {
            mediaInfo.mMeta = IjkMediaMeta.parse(_getMediaMeta());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return mediaInfo;
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return true;
    }

    public void setOption(int category, String name, String value) {
        _setOption(category, name, value);
    }

    public void setOption(int category, String name, long value) {
        _setOption(category, name, value);
    }

    public Bundle getMediaMeta() {
        return _getMediaMeta();
    }

    public static String getColorFormatName(int mediaCodecColorFormat) {
        return _getColorFormatName(mediaCodecColorFormat);
    }

    public void setAudioStreamType(int streamtype) {
    }

    public void setKeepInBackground(boolean keepInBackground) {
    }

    protected void finalize() throws Throwable {
        super.finalize();
        native_finalize();
    }

    @CalledByNative
    private static void postEventFromNative(Object weakThiz, int what, int arg1, int arg2, Object obj) {
        if (weakThiz != null) {
            IjkMediaPlayer mp = (IjkMediaPlayer) ((WeakReference) weakThiz).get();
            if (mp != null) {
                if (what == 200 && arg1 == 2) {
                    mp.start();
                }
                if (mp.mEventHandler != null) {
                    mp.mEventHandler.sendMessage(mp.mEventHandler.obtainMessage(what, arg1, arg2, obj));
                }
            }
        }
    }

    public void setOnControlMessageListener(OnControlMessageListener listener) {
        this.mOnControlMessageListener = listener;
    }

    public void setOnNativeInvokeListener(OnNativeInvokeListener listener) {
        this.mOnNativeInvokeListener = listener;
    }

    @CalledByNative
    private static boolean onNativeInvoke(Object weakThiz, int what, Bundle args) {
        DebugLog.ifmt(TAG, "onNativeInvoke %d", Integer.valueOf(what));
        if (weakThiz == null || !(weakThiz instanceof WeakReference)) {
            throw new IllegalStateException("<null weakThiz>.onNativeInvoke()");
        }
        IjkMediaPlayer player = (IjkMediaPlayer) ((WeakReference) weakThiz).get();
        if (player == null) {
            throw new IllegalStateException("<null weakPlayer>.onNativeInvoke()");
        }
        OnNativeInvokeListener listener = player.mOnNativeInvokeListener;
        if (listener != null && listener.onNativeInvoke(what, args)) {
            return true;
        }
        switch (what) {
            case 65536:
                OnControlMessageListener onControlMessageListener = player.mOnControlMessageListener;
                if (onControlMessageListener == null) {
                    return false;
                }
                int segmentIndex = args.getInt(OnNativeInvokeListener.ARG_SEGMENT_INDEX, -1);
                if (segmentIndex < 0) {
                    throw new InvalidParameterException("onNativeInvoke(invalid segment index)");
                }
                String newUrl = onControlMessageListener.onControlResolveSegmentUrl(segmentIndex);
                if (newUrl == null) {
                    throw new RuntimeException(new IOException("onNativeInvoke() = <NULL newUrl>"));
                }
                args.putString(OnNativeInvokeListener.ARG_URL, newUrl);
                return true;
            default:
                return false;
        }
    }

    public void setOnMediaCodecSelectListener(OnMediaCodecSelectListener listener) {
        this.mOnMediaCodecSelectListener = listener;
    }

    public void resetListeners() {
        super.resetListeners();
        this.mOnMediaCodecSelectListener = null;
    }

    @CalledByNative
    private static String onSelectCodec(Object weakThiz, String mimeType, int profile, int level) {
        if (weakThiz == null || !(weakThiz instanceof WeakReference)) {
            return null;
        }
        IjkMediaPlayer player = (IjkMediaPlayer) ((WeakReference) weakThiz).get();
        if (player == null) {
            return null;
        }
        OnMediaCodecSelectListener listener = player.mOnMediaCodecSelectListener;
        if (listener == null) {
            listener = DefaultMediaCodecSelector.sInstance;
        }
        return listener.onMediaCodecSelect(player, mimeType, profile, level);
    }
}
