package z.xtreamiptv.playerv3.v2api.view.exoplayer2;

import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.Locale;
import java.util.UUID;

final class DemoUtil {
    public static UUID getDrmUuid(String drmScheme) throws UnsupportedDrmException {
        String toLowerInvariant = Util.toLowerInvariant(drmScheme);
        int i = -1;
        switch (toLowerInvariant.hashCode()) {
            case -1860423953:
                if (toLowerInvariant.equals("playready")) {
                    i = 1;
                    break;
                }
                break;
            case -1400551171:
                if (toLowerInvariant.equals("widevine")) {
                    i = 0;
                    break;
                }
                break;
            case 790309106:
                if (toLowerInvariant.equals("clearkey")) {
                    i = 2;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                return C.WIDEVINE_UUID;
            case 1:
                return C.PLAYREADY_UUID;
            case 2:
                return C.CLEARKEY_UUID;
            default:
                try {
                    return UUID.fromString(drmScheme);
                } catch (RuntimeException e) {
                    throw new UnsupportedDrmException(1);
                }
        }
    }

    public static String buildTrackName(Format format) {
        String trackName;
        if (MimeTypes.isVideo(format.sampleMimeType)) {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildResolutionString(format), buildBitrateString(format)), buildTrackIdString(format)), buildSampleMimeTypeString(format));
        } else if (MimeTypes.isAudio(format.sampleMimeType)) {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format), buildAudioPropertyString(format)), buildBitrateString(format)), buildTrackIdString(format)), buildSampleMimeTypeString(format));
        } else {
            trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format), buildBitrateString(format)), buildTrackIdString(format)), buildSampleMimeTypeString(format));
        }
        return trackName.length() == 0 ? "unknown" : trackName;
    }

    private static String buildResolutionString(Format format) {
        return (format.width == -1 || format.height == -1) ? "" : format.width + "x" + format.height;
    }

    private static String buildAudioPropertyString(Format format) {
        return (format.channelCount == -1 || format.sampleRate == -1) ? "" : format.channelCount + "ch, " + format.sampleRate + "Hz";
    }

    private static String buildLanguageString(Format format) {
        return (TextUtils.isEmpty(format.language) || C.LANGUAGE_UNDETERMINED.equals(format.language)) ? "" : format.language;
    }

    private static String buildBitrateString(Format format) {
        if (format.bitrate == -1) {
            return "";
        }
        return String.format(Locale.US, "%.2fMbit", new Object[]{Float.valueOf(((float) format.bitrate) / 1000000.0f)});
    }

    private static String joinWithSeparator(String first, String second) {
        if (first.length() == 0) {
            return second;
        }
        return second.length() == 0 ? first : first + ", " + second;
    }

    private static String buildTrackIdString(Format format) {
        return format.id == null ? "" : "id:" + format.id;
    }

    private static String buildSampleMimeTypeString(Format format) {
        return format.sampleMimeType == null ? "" : format.sampleMimeType;
    }

    private DemoUtil() {
    }
}
