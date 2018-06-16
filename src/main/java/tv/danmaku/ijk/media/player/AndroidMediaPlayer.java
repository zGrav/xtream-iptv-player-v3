package tv.danmaku.ijk.media.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
//import android.support.v7.media.SystemMediaRouteProvider;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;
import tv.danmaku.ijk.media.player.misc.AndroidTrackInfo;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;
import tv.danmaku.ijk.media.player.pragma.DebugLog;

public class AndroidMediaPlayer extends AbstractMediaPlayer {
    private static MediaInfo sMediaInfo;
    private String mDataSource;
    private final Object mInitLock = new Object();
    private final AndroidMediaPlayerListenerHolder mInternalListenerAdapter;
    private final MediaPlayer mInternalMediaPlayer;
    private boolean mIsReleased;
    private MediaDataSource mMediaDataSource;

    private class AndroidMediaPlayerListenerHolder implements OnPreparedListener, OnCompletionListener, OnBufferingUpdateListener, OnSeekCompleteListener, OnVideoSizeChangedListener, OnErrorListener, OnInfoListener {
        public final WeakReference<AndroidMediaPlayer> mWeakMediaPlayer;

        public AndroidMediaPlayerListenerHolder(AndroidMediaPlayer mp) {
            this.mWeakMediaPlayer = new WeakReference(mp);
        }

        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return ((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null && AndroidMediaPlayer.this.notifyOnInfo(what, extra);
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            return ((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null && AndroidMediaPlayer.this.notifyOnError(what, extra);
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null) {
                AndroidMediaPlayer.this.notifyOnVideoSizeChanged(width, height, 1, 1);
            }
        }

        public void onSeekComplete(MediaPlayer mp) {
            if (((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null) {
                AndroidMediaPlayer.this.notifyOnSeekComplete();
            }
        }

        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null) {
                AndroidMediaPlayer.this.notifyOnBufferingUpdate(percent);
            }
        }

        public void onCompletion(MediaPlayer mp) {
            if (((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null) {
                AndroidMediaPlayer.this.notifyOnCompletion();
            }
        }

        public void onPrepared(MediaPlayer mp) {
            if (((AndroidMediaPlayer) this.mWeakMediaPlayer.get()) != null) {
                AndroidMediaPlayer.this.notifyOnPrepared();
            }
        }
    }

    @TargetApi(23)
    private static class MediaDataSourceProxy extends MediaDataSource {
        private final IMediaDataSource mMediaDataSource;

        public MediaDataSourceProxy(IMediaDataSource mediaDataSource) {
            this.mMediaDataSource = mediaDataSource;
        }

        public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
            return this.mMediaDataSource.readAt(position, buffer, offset, size);
        }

        public long getSize() throws IOException {
            return this.mMediaDataSource.getSize();
        }

        public void close() throws IOException {
            this.mMediaDataSource.close();
        }
    }

    public AndroidMediaPlayer() {
        synchronized (this.mInitLock) {
            this.mInternalMediaPlayer = new MediaPlayer();
        }
        this.mInternalMediaPlayer.setAudioStreamType(3);
        this.mInternalListenerAdapter = new AndroidMediaPlayerListenerHolder(this);
        attachInternalListeners();
    }

    public MediaPlayer getInternalMediaPlayer() {
        return this.mInternalMediaPlayer;
    }

    public void setDisplay(SurfaceHolder sh) {
        synchronized (this.mInitLock) {
            if (!this.mIsReleased) {
                this.mInternalMediaPlayer.setDisplay(sh);
            }
        }
    }

    @TargetApi(14)
    public void setSurface(Surface surface) {
        this.mInternalMediaPlayer.setSurface(surface);
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mInternalMediaPlayer.setDataSource(context, uri);
    }

    @TargetApi(14)
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mInternalMediaPlayer.setDataSource(context, uri, headers);
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        this.mInternalMediaPlayer.setDataSource(fd);
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mDataSource = path;
        Uri uri = Uri.parse(path);
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme) || !scheme.equalsIgnoreCase("file")) {
            this.mInternalMediaPlayer.setDataSource(path);
        } else {
            this.mInternalMediaPlayer.setDataSource(uri.getPath());
        }
    }

    @TargetApi(23)
    public void setDataSource(IMediaDataSource mediaDataSource) {
        releaseMediaDataSource();
        this.mMediaDataSource = new MediaDataSourceProxy(mediaDataSource);
        this.mInternalMediaPlayer.setDataSource(this.mMediaDataSource);
    }

    public String getDataSource() {
        return this.mDataSource;
    }

    private void releaseMediaDataSource() {
        if (this.mMediaDataSource != null) {
            try {
                this.mMediaDataSource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mMediaDataSource = null;
        }
    }

    public void prepareAsync() throws IllegalStateException {
        this.mInternalMediaPlayer.prepareAsync();
    }

    public void start() throws IllegalStateException {
        this.mInternalMediaPlayer.start();
    }

    public void stop() throws IllegalStateException {
        this.mInternalMediaPlayer.stop();
    }

    public void pause() throws IllegalStateException {
        this.mInternalMediaPlayer.pause();
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        this.mInternalMediaPlayer.setScreenOnWhilePlaying(screenOn);
    }

    public ITrackInfo[] getTrackInfo() {
        return AndroidTrackInfo.fromMediaPlayer(this.mInternalMediaPlayer);
    }

    public int getVideoWidth() {
        return this.mInternalMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        return this.mInternalMediaPlayer.getVideoHeight();
    }

    public int getVideoSarNum() {
        return 1;
    }

    public int getVideoSarDen() {
        return 1;
    }

    public boolean isPlaying() {
        try {
            return this.mInternalMediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            DebugLog.printStackTrace(e);
            return false;
        }
    }

    public void seekTo(long msec) throws IllegalStateException {
        this.mInternalMediaPlayer.seekTo((int) msec);
    }

    public long getCurrentPosition() {
        try {
            return (long) this.mInternalMediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            DebugLog.printStackTrace(e);
            return 0;
        }
    }

    public long getDuration() {
        try {
            return (long) this.mInternalMediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            DebugLog.printStackTrace(e);
            return 0;
        }
    }

    public void release() {
        this.mIsReleased = true;
        this.mInternalMediaPlayer.release();
        releaseMediaDataSource();
        resetListeners();
        attachInternalListeners();
    }

    public void reset() {
        try {
            this.mInternalMediaPlayer.reset();
        } catch (IllegalStateException e) {
            DebugLog.printStackTrace(e);
        }
        releaseMediaDataSource();
        resetListeners();
        attachInternalListeners();
    }

    public void setLooping(boolean looping) {
        this.mInternalMediaPlayer.setLooping(looping);
    }

    public boolean isLooping() {
        return this.mInternalMediaPlayer.isLooping();
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.mInternalMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public int getAudioSessionId() {
        return this.mInternalMediaPlayer.getAudioSessionId();
    }

    public MediaInfo getMediaInfo() {
        if (sMediaInfo == null) {
            MediaInfo module = new MediaInfo();
            module.mVideoDecoder = "android";
            module.mVideoDecoderImpl = "HW";
            module.mAudioDecoder = "android";
            module.mAudioDecoderImpl = "HW";
            sMediaInfo = module;
        }
        return sMediaInfo;
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return true;
    }

    public void setWakeMode(Context context, int mode) {
        this.mInternalMediaPlayer.setWakeMode(context, mode);
    }

    public void setAudioStreamType(int streamtype) {
        this.mInternalMediaPlayer.setAudioStreamType(streamtype);
    }

    public void setKeepInBackground(boolean keepInBackground) {
    }

    private void attachInternalListeners() {
        this.mInternalMediaPlayer.setOnPreparedListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnBufferingUpdateListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnCompletionListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnSeekCompleteListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnVideoSizeChangedListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnErrorListener(this.mInternalListenerAdapter);
        this.mInternalMediaPlayer.setOnInfoListener(this.mInternalListenerAdapter);
    }
}
