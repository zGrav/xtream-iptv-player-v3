package z.xtreamiptv.playerv3.view.exoplayer2;

import android.content.Context;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.util.Locale;
import java.util.UUID;

import static com.google.android.exoplayer2.drm.UnsupportedDrmException.REASON_INSTANTIATION_ERROR;

final class DemoUtil {
    public static UUID getDrmUuid(String drmScheme, Context context) throws UnsupportedDrmException {
        String toLowerInvariant = Util.toLowerInvariant(drmScheme);
        if (drmScheme == null) {
            if (isDeviceWidevineDRMProvisioned(context)) {
                toLowerInvariant = "widevine";
            } else {
                throw new UnsupportedDrmException(REASON_INSTANTIATION_ERROR);
            }
        }
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

    private final static long DEVICE_IS_PROVISIONED = 0;
    private final static long DEVICE_IS_NOT_PROVISIONED = 1;
    private final static long DEVICE_IS_PROVISIONED_SD_ONLY = 2;
    private final static String WIDEVINE_MIME_TYPE = "video/wvm";



    public static boolean isDeviceWidevineDRMProvisioned(Context context)
    {
        boolean isDrmAvailable = true;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.KITKAT)
        {
            //As Media DRM Package is available only after KITKAT(API Level 19)
            isDrmAvailable = false;
        } else
        {
            DrmManagerClient drmManagerClient = new DrmManagerClient(context);
            DrmInfoRequest drmInfoRequest = new DrmInfoRequest(DrmInfoRequest.TYPE_REGISTRATION_INFO, WIDEVINE_MIME_TYPE);
            drmInfoRequest.put("WVPortalKey", "key provided for drm in widevine portal");
            DrmInfo drmInfo = drmManagerClient.acquireDrmInfo(drmInfoRequest);
            if (drmInfo != null)
            {
                String kWVDrmInfoRequestStatusKey = (String) drmInfo.get("WVDrmInfoRequestStatusKey");
                String drmPath = (String) drmInfo.get("drm_path");
                if ((kWVDrmInfoRequestStatusKey != null && Integer.parseInt(kWVDrmInfoRequestStatusKey) == DEVICE_IS_NOT_PROVISIONED) || (drmPath != null && drmPath.length() == 0))
                {
                    //not supported
                    isDrmAvailable = false;
                }
            }
        }
        return isDrmAvailable;
    }
}
