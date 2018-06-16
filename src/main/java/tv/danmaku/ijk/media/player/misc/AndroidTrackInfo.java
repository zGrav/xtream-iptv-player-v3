package tv.danmaku.ijk.media.player.misc;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.TrackInfo;
import android.os.Build.VERSION;
import com.google.android.exoplayer2.C;

public class AndroidTrackInfo implements ITrackInfo {
    private final TrackInfo mTrackInfo;

    public static AndroidTrackInfo[] fromMediaPlayer(MediaPlayer mp) {
        if (VERSION.SDK_INT >= 16) {
            return fromTrackInfo(mp.getTrackInfo());
        }
        return null;
    }

    private static AndroidTrackInfo[] fromTrackInfo(TrackInfo[] trackInfos) {
        if (trackInfos == null) {
            return null;
        }
        AndroidTrackInfo[] androidTrackInfo = new AndroidTrackInfo[trackInfos.length];
        for (int i = 0; i < trackInfos.length; i++) {
            androidTrackInfo[i] = new AndroidTrackInfo(trackInfos[i]);
        }
        return androidTrackInfo;
    }

    private AndroidTrackInfo(TrackInfo trackInfo) {
        this.mTrackInfo = trackInfo;
    }

    @TargetApi(19)
    public IMediaFormat getFormat() {
        if (this.mTrackInfo == null || VERSION.SDK_INT < 19) {
            return null;
        }
        MediaFormat mediaFormat = this.mTrackInfo.getFormat();
        if (mediaFormat != null) {
            return new AndroidMediaFormat(mediaFormat);
        }
        return null;
    }

    @TargetApi(16)
    public String getLanguage() {
        if (this.mTrackInfo == null) {
            return C.LANGUAGE_UNDETERMINED;
        }
        return this.mTrackInfo.getLanguage();
    }

    @TargetApi(16)
    public int getTrackType() {
        if (this.mTrackInfo == null) {
            return 0;
        }
        return this.mTrackInfo.getTrackType();
    }

    @TargetApi(16)
    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append(getClass().getSimpleName());
        out.append('{');
        if (this.mTrackInfo != null) {
            out.append(this.mTrackInfo.toString());
        } else {
            out.append("null");
        }
        out.append('}');
        return out.toString();
    }

    @TargetApi(16)
    public String getInfoInline() {
        if (this.mTrackInfo != null) {
            return this.mTrackInfo.toString();
        }
        return "null";
    }
}
