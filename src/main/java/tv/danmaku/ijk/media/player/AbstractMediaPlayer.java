package tv.danmaku.ijk.media.player;

import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private IMediaPlayer$OnBufferingUpdateListener mOnBufferingUpdateListener;
    private IMediaPlayer$OnCompletionListener mOnCompletionListener;
    private IMediaPlayer$OnErrorListener mOnErrorListener;
    private IMediaPlayer$OnInfoListener mOnInfoListener;
    private IMediaPlayer$OnPreparedListener mOnPreparedListener;
    private IMediaPlayer$OnSeekCompleteListener mOnSeekCompleteListener;
    private IMediaPlayer$OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    public final void setOnPreparedListener(IMediaPlayer$OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public final void setOnCompletionListener(IMediaPlayer$OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public final void setOnBufferingUpdateListener(IMediaPlayer$OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public final void setOnSeekCompleteListener(IMediaPlayer$OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public final void setOnVideoSizeChangedListener(IMediaPlayer$OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public final void setOnErrorListener(IMediaPlayer$OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public final void setOnInfoListener(IMediaPlayer$OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
    }

    protected final void notifyOnPrepared() {
        if (this.mOnPreparedListener != null) {
            this.mOnPreparedListener.onPrepared(this);
        }
    }

    protected final void notifyOnCompletion() {
        if (this.mOnCompletionListener != null) {
            this.mOnCompletionListener.onCompletion(this);
        }
    }

    protected final void notifyOnBufferingUpdate(int percent) {
        if (this.mOnBufferingUpdateListener != null) {
            this.mOnBufferingUpdateListener.onBufferingUpdate(this, percent);
        }
    }

    protected final void notifyOnSeekComplete() {
        if (this.mOnSeekCompleteListener != null) {
            this.mOnSeekCompleteListener.onSeekComplete(this);
        }
    }

    protected final void notifyOnVideoSizeChanged(int width, int height, int sarNum, int sarDen) {
        if (this.mOnVideoSizeChangedListener != null) {
            this.mOnVideoSizeChangedListener.onVideoSizeChanged(this, width, height, sarNum, sarDen);
        }
    }

    protected final boolean notifyOnError(int what, int extra) {
        return this.mOnErrorListener != null && this.mOnErrorListener.onError(this, what, extra);
    }

    protected final boolean notifyOnInfo(int what, int extra) {
        return this.mOnInfoListener != null && this.mOnInfoListener.onInfo(this, what, extra);
    }

    public void setDataSource(IMediaDataSource mediaDataSource) {
        throw new UnsupportedOperationException();
    }
}
