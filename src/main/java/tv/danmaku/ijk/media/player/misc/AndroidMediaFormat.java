package tv.danmaku.ijk.media.player.misc;

import android.annotation.TargetApi;
import android.media.MediaFormat;

public class AndroidMediaFormat implements IMediaFormat {
    private final MediaFormat mMediaFormat;

    public AndroidMediaFormat(MediaFormat mediaFormat) {
        this.mMediaFormat = mediaFormat;
    }

    @TargetApi(16)
    public int getInteger(String name) {
        if (this.mMediaFormat == null) {
            return 0;
        }
        return this.mMediaFormat.getInteger(name);
    }

    @TargetApi(16)
    public String getString(String name) {
        if (this.mMediaFormat == null) {
            return null;
        }
        return this.mMediaFormat.getString(name);
    }

    @TargetApi(16)
    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append(getClass().getName());
        out.append('{');
        if (this.mMediaFormat != null) {
            out.append(this.mMediaFormat.toString());
        } else {
            out.append("null");
        }
        out.append('}');
        return out.toString();
    }
}
