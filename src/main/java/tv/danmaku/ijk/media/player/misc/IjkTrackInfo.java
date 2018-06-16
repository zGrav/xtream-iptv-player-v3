package tv.danmaku.ijk.media.player.misc;

import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import tv.danmaku.ijk.media.player.IjkMediaMeta.IjkStreamMeta;

public class IjkTrackInfo implements ITrackInfo {
    private IjkStreamMeta mStreamMeta;
    private int mTrackType = 0;

    public IjkTrackInfo(IjkStreamMeta streamMeta) {
        this.mStreamMeta = streamMeta;
    }

    public void setMediaMeta(IjkStreamMeta streamMeta) {
        this.mStreamMeta = streamMeta;
    }

    public IMediaFormat getFormat() {
        return new IjkMediaFormat(this.mStreamMeta);
    }

    public String getLanguage() {
        if (this.mStreamMeta == null || TextUtils.isEmpty(this.mStreamMeta.mLanguage)) {
            return C.LANGUAGE_UNDETERMINED;
        }
        return this.mStreamMeta.mLanguage;
    }

    public int getTrackType() {
        return this.mTrackType;
    }

    public void setTrackType(int trackType) {
        this.mTrackType = trackType;
    }

    public String toString() {
        return getClass().getSimpleName() + '{' + getInfoInline() + "}";
    }

    public String getInfoInline() {
        StringBuilder out = new StringBuilder(128);
        switch (this.mTrackType) {
            case 1:
                out.append("VIDEO");
                out.append(", ");
                out.append(this.mStreamMeta.getCodecShortNameInline());
                out.append(", ");
                out.append(this.mStreamMeta.getBitrateInline());
                out.append(", ");
                out.append(this.mStreamMeta.getResolutionInline());
                break;
            case 2:
                out.append("AUDIO");
                out.append(", ");
                out.append(this.mStreamMeta.getCodecShortNameInline());
                out.append(", ");
                out.append(this.mStreamMeta.getBitrateInline());
                out.append(", ");
                out.append(this.mStreamMeta.getSampleRateInline());
                break;
            case 3:
                out.append("TIMEDTEXT");
                break;
            case 4:
                out.append("SUBTITLE");
                break;
            default:
                out.append("UNKNOWN");
                break;
        }
        return out.toString();
    }
}
