package tv.danmaku.ijk.media.player;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;

@TargetApi(14)
public class TextureMediaPlayer extends MediaPlayerProxy implements IMediaPlayer, ISurfaceTextureHolder {
    private SurfaceTexture mSurfaceTexture;
    private ISurfaceTextureHost mSurfaceTextureHost;

    public TextureMediaPlayer(IMediaPlayer backEndMediaPlayer) {
        super(backEndMediaPlayer);
    }

    public void releaseSurfaceTexture() {
        if (this.mSurfaceTexture != null) {
            if (this.mSurfaceTextureHost != null) {
                this.mSurfaceTextureHost.releaseSurfaceTexture(this.mSurfaceTexture);
            } else {
                this.mSurfaceTexture.release();
            }
            this.mSurfaceTexture = null;
        }
    }

    public void reset() {
        super.reset();
        releaseSurfaceTexture();
    }

    public void release() {
        super.release();
        releaseSurfaceTexture();
    }

    public void setDisplay(SurfaceHolder sh) {
        if (this.mSurfaceTexture == null) {
            super.setDisplay(sh);
        }
    }

    public void setSurface(Surface surface) {
        if (this.mSurfaceTexture == null) {
            super.setSurface(surface);
        }
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (this.mSurfaceTexture != surfaceTexture) {
            releaseSurfaceTexture();
            this.mSurfaceTexture = surfaceTexture;
            if (surfaceTexture == null) {
                super.setSurface(null);
            } else {
                super.setSurface(new Surface(surfaceTexture));
            }
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurfaceTexture;
    }

    public void setSurfaceTextureHost(ISurfaceTextureHost surfaceTextureHost) {
        this.mSurfaceTextureHost = surfaceTextureHost;
    }
}
