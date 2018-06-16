package tv.danmaku.ijk.media.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class MediaPlayerProxy implements IMediaPlayer {
    protected final IMediaPlayer mBackEndMediaPlayer;

    public MediaPlayerProxy(IMediaPlayer backEndMediaPlayer) {
        this.mBackEndMediaPlayer = backEndMediaPlayer;
    }

    public IMediaPlayer getInternalMediaPlayer() {
        return this.mBackEndMediaPlayer;
    }

    public void setDisplay(SurfaceHolder sh) {
        this.mBackEndMediaPlayer.setDisplay(sh);
    }

    @TargetApi(14)
    public void setSurface(Surface surface) {
        this.mBackEndMediaPlayer.setSurface(surface);
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mBackEndMediaPlayer.setDataSource(context, uri);
    }

    @TargetApi(14)
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mBackEndMediaPlayer.setDataSource(context, uri, headers);
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        this.mBackEndMediaPlayer.setDataSource(fd);
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        this.mBackEndMediaPlayer.setDataSource(path);
    }

    public void setDataSource(IMediaDataSource mediaDataSource) {
        this.mBackEndMediaPlayer.setDataSource(mediaDataSource);
    }

    public String getDataSource() {
        return this.mBackEndMediaPlayer.getDataSource();
    }

    public void prepareAsync() throws IllegalStateException {
        this.mBackEndMediaPlayer.prepareAsync();
    }

    public void start() throws IllegalStateException {
        this.mBackEndMediaPlayer.start();
    }

    public void stop() throws IllegalStateException {
        this.mBackEndMediaPlayer.stop();
    }

    public void pause() throws IllegalStateException {
        this.mBackEndMediaPlayer.pause();
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        this.mBackEndMediaPlayer.setScreenOnWhilePlaying(screenOn);
    }

    public int getVideoWidth() {
        return this.mBackEndMediaPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        return this.mBackEndMediaPlayer.getVideoHeight();
    }

    public boolean isPlaying() {
        return this.mBackEndMediaPlayer.isPlaying();
    }

    public void seekTo(long msec) throws IllegalStateException {
        this.mBackEndMediaPlayer.seekTo(msec);
    }

    public long getCurrentPosition() {
        return this.mBackEndMediaPlayer.getCurrentPosition();
    }

    public long getDuration() {
        return this.mBackEndMediaPlayer.getDuration();
    }

    public void release() {
        this.mBackEndMediaPlayer.release();
    }

    public void reset() {
        this.mBackEndMediaPlayer.reset();
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.mBackEndMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public int getAudioSessionId() {
        return this.mBackEndMediaPlayer.getAudioSessionId();
    }

    public MediaInfo getMediaInfo() {
        return this.mBackEndMediaPlayer.getMediaInfo();
    }

    public void setLogEnabled(boolean enable) {
    }

    public boolean isPlayable() {
        return false;
    }

    public void setOnPreparedListener(IMediaPlayer$OnPreparedListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnPreparedListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnPreparedListener(new IMediaPlayer$OnPreparedListener() {
                public void onPrepared(IMediaPlayer mp) {
                    finalListener.onPrepared(MediaPlayerProxy.this);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnPreparedListener(null);
    }

    public void setOnCompletionListener(IMediaPlayer$OnCompletionListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnCompletionListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnCompletionListener(new IMediaPlayer$OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    finalListener.onCompletion(MediaPlayerProxy.this);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnCompletionListener(null);
    }

    public void setOnBufferingUpdateListener(IMediaPlayer$OnBufferingUpdateListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnBufferingUpdateListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer$OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    finalListener.onBufferingUpdate(MediaPlayerProxy.this, percent);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnBufferingUpdateListener(null);
    }

    public void setOnSeekCompleteListener(IMediaPlayer$OnSeekCompleteListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnSeekCompleteListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnSeekCompleteListener(new IMediaPlayer$OnSeekCompleteListener() {
                public void onSeekComplete(IMediaPlayer mp) {
                    finalListener.onSeekComplete(MediaPlayerProxy.this);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnSeekCompleteListener(null);
    }

    public void setOnVideoSizeChangedListener(IMediaPlayer$OnVideoSizeChangedListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnVideoSizeChangedListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnVideoSizeChangedListener(new IMediaPlayer$OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                    finalListener.onVideoSizeChanged(MediaPlayerProxy.this, width, height, sar_num, sar_den);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnVideoSizeChangedListener(null);
    }

    public void setOnErrorListener(IMediaPlayer$OnErrorListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnErrorListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnErrorListener(new IMediaPlayer$OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int what, int extra) {
                    return finalListener.onError(MediaPlayerProxy.this, what, extra);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnErrorListener(null);
    }

    public void setOnInfoListener(IMediaPlayer$OnInfoListener listener) {
        if (listener != null) {
            final IMediaPlayer$OnInfoListener finalListener = listener;
            this.mBackEndMediaPlayer.setOnInfoListener(new IMediaPlayer$OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                    return finalListener.onInfo(MediaPlayerProxy.this, what, extra);
                }
            });
            return;
        }
        this.mBackEndMediaPlayer.setOnInfoListener(null);
    }

    public void setAudioStreamType(int streamtype) {
        this.mBackEndMediaPlayer.setAudioStreamType(streamtype);
    }

    public void setKeepInBackground(boolean keepInBackground) {
        this.mBackEndMediaPlayer.setKeepInBackground(keepInBackground);
    }

    public int getVideoSarNum() {
        return this.mBackEndMediaPlayer.getVideoSarNum();
    }

    public int getVideoSarDen() {
        return this.mBackEndMediaPlayer.getVideoSarDen();
    }

    public void setWakeMode(Context context, int mode) {
        this.mBackEndMediaPlayer.setWakeMode(context, mode);
    }

    public ITrackInfo[] getTrackInfo() {
        return this.mBackEndMediaPlayer.getTrackInfo();
    }

    public void setLooping(boolean looping) {
        this.mBackEndMediaPlayer.setLooping(looping);
    }

    public boolean isLooping() {
        return this.mBackEndMediaPlayer.isLooping();
    }
}
