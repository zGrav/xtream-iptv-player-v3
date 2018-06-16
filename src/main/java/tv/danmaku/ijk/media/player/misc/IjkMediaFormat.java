package tv.danmaku.ijk.media.player.misc;

import android.annotation.TargetApi;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import tv.danmaku.ijk.media.player.IjkMediaMeta;
import tv.danmaku.ijk.media.player.IjkMediaMeta.IjkStreamMeta;

public class IjkMediaFormat implements IMediaFormat {
    public static final String CODEC_NAME_H264 = "h264";
    public static final String KEY_IJK_BIT_RATE_UI = "ijk-bit-rate-ui";
    public static final String KEY_IJK_CHANNEL_UI = "ijk-channel-ui";
    public static final String KEY_IJK_CODEC_LONG_NAME_UI = "ijk-codec-long-name-ui";
    public static final String KEY_IJK_CODEC_PIXEL_FORMAT_UI = "ijk-pixel-format-ui";
    public static final String KEY_IJK_CODEC_PROFILE_LEVEL_UI = "ijk-profile-level-ui";
    public static final String KEY_IJK_FRAME_RATE_UI = "ijk-frame-rate-ui";
    public static final String KEY_IJK_RESOLUTION_UI = "ijk-resolution-ui";
    public static final String KEY_IJK_SAMPLE_RATE_UI = "ijk-sample-rate-ui";
    private static final Map<String, Formatter> sFormatterMap = new HashMap();
    public final IjkStreamMeta mMediaFormat;

    private static abstract class Formatter {
        protected abstract String doFormat(IjkMediaFormat ijkMediaFormat);

        private Formatter() {
        }

        public String format(IjkMediaFormat mediaFormat) {
            String value = doFormat(mediaFormat);
            if (TextUtils.isEmpty(value)) {
                return getDefaultString();
            }
            return value;
        }

        protected String getDefaultString() {
            return "N/A";
        }
    }

    class C22891 extends Formatter {
        C22891() {
            super();
        }

        public String doFormat(IjkMediaFormat mediaFormat) {
            return IjkMediaFormat.this.mMediaFormat.getString(IjkMediaMeta.IJKM_KEY_CODEC_LONG_NAME);
        }
    }

    class C22902 extends Formatter {
        C22902() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            int bitRate = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_BITRATE);
            if (bitRate <= 0) {
                return null;
            }
            if (bitRate < 1000) {
                return String.format(Locale.US, "%d bit/s", new Object[]{Integer.valueOf(bitRate)});
            }
            return String.format(Locale.US, "%d kb/s", new Object[]{Integer.valueOf(bitRate / 1000)});
        }
    }

    class C22913 extends Formatter {
        C22913() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            String profile = mediaFormat.getString(IjkMediaMeta.IJKM_KEY_CODEC_PROFILE);
            if (TextUtils.isEmpty(profile)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(profile);
            String codecName = mediaFormat.getString(IjkMediaMeta.IJKM_KEY_CODEC_NAME);
            if (!TextUtils.isEmpty(codecName) && codecName.equalsIgnoreCase(IjkMediaFormat.CODEC_NAME_H264)) {
                int level = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_CODEC_LEVEL);
                if (level < 10) {
                    return sb.toString();
                }
                sb.append(" Profile Level ");
                sb.append((level / 10) % 10);
                if (level % 10 != 0) {
                    sb.append(".");
                    sb.append(level % 10);
                }
            }
            return sb.toString();
        }
    }

    class C22924 extends Formatter {
        C22924() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            return mediaFormat.getString(IjkMediaMeta.IJKM_KEY_CODEC_PIXEL_FORMAT);
        }
    }

    class C22935 extends Formatter {
        C22935() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            int width = mediaFormat.getInteger("width");
            int height = mediaFormat.getInteger("height");
            int sarNum = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_SAR_NUM);
            int sarDen = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_SAR_DEN);
            if (width <= 0 || height <= 0) {
                return null;
            }
            if (sarNum <= 0 || sarDen <= 0) {
                return String.format(Locale.US, "%d x %d", new Object[]{Integer.valueOf(width), Integer.valueOf(height)});
            }
            return String.format(Locale.US, "%d x %d [SAR %d:%d]", new Object[]{Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(sarNum), Integer.valueOf(sarDen)});
        }
    }

    class C22946 extends Formatter {
        C22946() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            int fpsNum = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_FPS_NUM);
            int fpsDen = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_FPS_DEN);
            if (fpsNum <= 0 || fpsDen <= 0) {
                return null;
            }
            return String.valueOf(((float) fpsNum) / ((float) fpsDen));
        }
    }

    class C22957 extends Formatter {
        C22957() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            if (mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_SAMPLE_RATE) <= 0) {
                return null;
            }
            return String.format(Locale.US, "%d Hz", new Object[]{Integer.valueOf(mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_SAMPLE_RATE))});
        }
    }

    class C22968 extends Formatter {
        C22968() {
            super();
        }

        protected String doFormat(IjkMediaFormat mediaFormat) {
            int channelLayout = mediaFormat.getInteger(IjkMediaMeta.IJKM_KEY_CHANNEL_LAYOUT);
            if (channelLayout <= 0) {
                return null;
            }
            if (((long) channelLayout) == 4) {
                return "mono";
            }
            if (((long) channelLayout) == 3) {
                return "stereo";
            }
            return String.format(Locale.US, "%x", new Object[]{Integer.valueOf(channelLayout)});
        }
    }

    public IjkMediaFormat(IjkStreamMeta streamMeta) {
        sFormatterMap.put(KEY_IJK_CODEC_LONG_NAME_UI, new C22891());
        sFormatterMap.put(KEY_IJK_BIT_RATE_UI, new C22902());
        sFormatterMap.put(KEY_IJK_CODEC_PROFILE_LEVEL_UI, new C22913());
        sFormatterMap.put(KEY_IJK_CODEC_PIXEL_FORMAT_UI, new C22924());
        sFormatterMap.put(KEY_IJK_RESOLUTION_UI, new C22935());
        sFormatterMap.put(KEY_IJK_FRAME_RATE_UI, new C22946());
        sFormatterMap.put(KEY_IJK_SAMPLE_RATE_UI, new C22957());
        sFormatterMap.put(KEY_IJK_CHANNEL_UI, new C22968());
        this.mMediaFormat = streamMeta;
    }

    @TargetApi(16)
    public int getInteger(String name) {
        if (this.mMediaFormat == null) {
            return 0;
        }
        return this.mMediaFormat.getInt(name);
    }

    public String getString(String name) {
        if (this.mMediaFormat == null) {
            return null;
        }
        if (sFormatterMap.containsKey(name)) {
            return ((Formatter) sFormatterMap.get(name)).format(this);
        }
        return this.mMediaFormat.getString(name);
    }
}
