package tv.danmaku.ijk.media.player;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class IjkMediaMeta {
    public static final long AV_CH_BACK_CENTER = 256;
    public static final long AV_CH_BACK_LEFT = 16;
    public static final long AV_CH_BACK_RIGHT = 32;
    public static final long AV_CH_FRONT_CENTER = 4;
    public static final long AV_CH_FRONT_LEFT = 1;
    public static final long AV_CH_FRONT_LEFT_OF_CENTER = 64;
    public static final long AV_CH_FRONT_RIGHT = 2;
    public static final long AV_CH_FRONT_RIGHT_OF_CENTER = 128;
    public static final long AV_CH_LAYOUT_2POINT1 = 11;
    public static final long AV_CH_LAYOUT_2_1 = 259;
    public static final long AV_CH_LAYOUT_2_2 = 1539;
    public static final long AV_CH_LAYOUT_3POINT1 = 15;
    public static final long AV_CH_LAYOUT_4POINT0 = 263;
    public static final long AV_CH_LAYOUT_4POINT1 = 271;
    public static final long AV_CH_LAYOUT_5POINT0 = 1543;
    public static final long AV_CH_LAYOUT_5POINT0_BACK = 55;
    public static final long AV_CH_LAYOUT_5POINT1 = 1551;
    public static final long AV_CH_LAYOUT_5POINT1_BACK = 63;
    public static final long AV_CH_LAYOUT_6POINT0 = 1799;
    public static final long AV_CH_LAYOUT_6POINT0_FRONT = 1731;
    public static final long AV_CH_LAYOUT_6POINT1 = 1807;
    public static final long AV_CH_LAYOUT_6POINT1_BACK = 319;
    public static final long AV_CH_LAYOUT_6POINT1_FRONT = 1739;
    public static final long AV_CH_LAYOUT_7POINT0 = 1591;
    public static final long AV_CH_LAYOUT_7POINT0_FRONT = 1735;
    public static final long AV_CH_LAYOUT_7POINT1 = 1599;
    public static final long AV_CH_LAYOUT_7POINT1_WIDE = 1743;
    public static final long AV_CH_LAYOUT_7POINT1_WIDE_BACK = 255;
    public static final long AV_CH_LAYOUT_HEXAGONAL = 311;
    public static final long AV_CH_LAYOUT_MONO = 4;
    public static final long AV_CH_LAYOUT_OCTAGONAL = 1847;
    public static final long AV_CH_LAYOUT_QUAD = 51;
    public static final long AV_CH_LAYOUT_STEREO = 3;
    public static final long AV_CH_LAYOUT_STEREO_DOWNMIX = 1610612736;
    public static final long AV_CH_LAYOUT_SURROUND = 7;
    public static final long AV_CH_LOW_FREQUENCY = 8;
    public static final long AV_CH_LOW_FREQUENCY_2 = 34359738368L;
    public static final long AV_CH_SIDE_LEFT = 512;
    public static final long AV_CH_SIDE_RIGHT = 1024;
    public static final long AV_CH_STEREO_LEFT = 536870912;
    public static final long AV_CH_STEREO_RIGHT = 1073741824;
    public static final long AV_CH_SURROUND_DIRECT_LEFT = 8589934592L;
    public static final long AV_CH_SURROUND_DIRECT_RIGHT = 17179869184L;
    public static final long AV_CH_TOP_BACK_CENTER = 65536;
    public static final long AV_CH_TOP_BACK_LEFT = 32768;
    public static final long AV_CH_TOP_BACK_RIGHT = 131072;
    public static final long AV_CH_TOP_CENTER = 2048;
    public static final long AV_CH_TOP_FRONT_CENTER = 8192;
    public static final long AV_CH_TOP_FRONT_LEFT = 4096;
    public static final long AV_CH_TOP_FRONT_RIGHT = 16384;
    public static final long AV_CH_WIDE_LEFT = 2147483648L;
    public static final long AV_CH_WIDE_RIGHT = 4294967296L;
    public static final String IJKM_KEY_AUDIO_STREAM = "audio";
    public static final String IJKM_KEY_BITRATE = "bitrate";
    public static final String IJKM_KEY_CHANNEL_LAYOUT = "channel_layout";
    public static final String IJKM_KEY_CODEC_LEVEL = "codec_level";
    public static final String IJKM_KEY_CODEC_LONG_NAME = "codec_long_name";
    public static final String IJKM_KEY_CODEC_NAME = "codec_name";
    public static final String IJKM_KEY_CODEC_PIXEL_FORMAT = "codec_pixel_format";
    public static final String IJKM_KEY_CODEC_PROFILE = "codec_profile";
    public static final String IJKM_KEY_DURATION_US = "duration_us";
    public static final String IJKM_KEY_FORMAT = "format";
    public static final String IJKM_KEY_FPS_DEN = "fps_den";
    public static final String IJKM_KEY_FPS_NUM = "fps_num";
    public static final String IJKM_KEY_HEIGHT = "height";
    public static final String IJKM_KEY_LANGUAGE = "language";
    public static final String IJKM_KEY_SAMPLE_RATE = "sample_rate";
    public static final String IJKM_KEY_SAR_DEN = "sar_den";
    public static final String IJKM_KEY_SAR_NUM = "sar_num";
    public static final String IJKM_KEY_START_US = "start_us";
    public static final String IJKM_KEY_STREAMS = "streams";
    public static final String IJKM_KEY_TBR_DEN = "tbr_den";
    public static final String IJKM_KEY_TBR_NUM = "tbr_num";
    public static final String IJKM_KEY_TYPE = "type";
    public static final String IJKM_KEY_VIDEO_STREAM = "video";
    public static final String IJKM_KEY_WIDTH = "width";
    public static final String IJKM_VAL_TYPE__AUDIO = "audio";
    public static final String IJKM_VAL_TYPE__UNKNOWN = "unknown";
    public static final String IJKM_VAL_TYPE__VIDEO = "video";
    public IjkStreamMeta mAudioStream;
    public long mBitrate;
    public long mDurationUS;
    public String mFormat;
    public Bundle mMediaMeta;
    public long mStartUS;
    public final ArrayList<IjkStreamMeta> mStreams = new ArrayList();
    public IjkStreamMeta mVideoStream;

    public static class IjkStreamMeta {
        public long mBitrate;
        public long mChannelLayout;
        public String mCodecLongName;
        public String mCodecName;
        public String mCodecProfile;
        public int mFpsDen;
        public int mFpsNum;
        public int mHeight;
        public final int mIndex;
        public String mLanguage;
        public Bundle mMeta;
        public int mSampleRate;
        public int mSarDen;
        public int mSarNum;
        public int mTbrDen;
        public int mTbrNum;
        public String mType;
        public int mWidth;

        public IjkStreamMeta(int index) {
            this.mIndex = index;
        }

        public String getString(String key) {
            return this.mMeta.getString(key);
        }

        public int getInt(String key) {
            return getInt(key, 0);
        }

        public int getInt(String key, int defaultValue) {
            String value = getString(key);
            if (!TextUtils.isEmpty(value)) {
                try {
                    defaultValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                }
            }
            return defaultValue;
        }

        public long getLong(String key) {
            return getLong(key, 0);
        }

        public long getLong(String key, long defaultValue) {
            String value = getString(key);
            if (!TextUtils.isEmpty(value)) {
                try {
                    defaultValue = Long.parseLong(value);
                } catch (NumberFormatException e) {
                }
            }
            return defaultValue;
        }

        public String getCodecLongNameInline() {
            if (!TextUtils.isEmpty(this.mCodecLongName)) {
                return this.mCodecLongName;
            }
            if (TextUtils.isEmpty(this.mCodecName)) {
                return "N/A";
            }
            return this.mCodecName;
        }

        public String getCodecShortNameInline() {
            if (TextUtils.isEmpty(this.mCodecName)) {
                return "N/A";
            }
            return this.mCodecName;
        }

        public String getResolutionInline() {
            if (this.mWidth <= 0 || this.mHeight <= 0) {
                return "N/A";
            }
            if (this.mSarNum <= 0 || this.mSarDen <= 0) {
                return String.format(Locale.US, "%d x %d", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight)});
            }
            return String.format(Locale.US, "%d x %d [SAR %d:%d]", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), Integer.valueOf(this.mSarNum), Integer.valueOf(this.mSarDen)});
        }

        public String getFpsInline() {
            if (this.mFpsNum <= 0 || this.mFpsDen <= 0) {
                return "N/A";
            }
            return String.valueOf(((float) this.mFpsNum) / ((float) this.mFpsDen));
        }

        public String getBitrateInline() {
            if (this.mBitrate <= 0) {
                return "N/A";
            }
            if (this.mBitrate < 1000) {
                return String.format(Locale.US, "%d bit/s", new Object[]{Long.valueOf(this.mBitrate)});
            }
            return String.format(Locale.US, "%d kb/s", new Object[]{Long.valueOf(this.mBitrate / 1000)});
        }

        public String getSampleRateInline() {
            if (this.mSampleRate <= 0) {
                return "N/A";
            }
            return String.format(Locale.US, "%d Hz", new Object[]{Integer.valueOf(this.mSampleRate)});
        }

        public String getChannelLayoutInline() {
            if (this.mChannelLayout <= 0) {
                return "N/A";
            }
            if (this.mChannelLayout == 4) {
                return "mono";
            }
            if (this.mChannelLayout == 3) {
                return "stereo";
            }
            return String.format(Locale.US, "%x", new Object[]{Long.valueOf(this.mChannelLayout)});
        }
    }

    public String getString(String key) {
        return this.mMediaMeta.getString(key);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (!TextUtils.isEmpty(value)) {
            try {
                defaultValue = Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        String value = getString(key);
        if (!TextUtils.isEmpty(value)) {
            try {
                defaultValue = Long.parseLong(value);
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public ArrayList<Bundle> getParcelableArrayList(String key) {
        return this.mMediaMeta.getParcelableArrayList(key);
    }

    public String getDurationInline() {
        long secs = (this.mDurationUS + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) / C.MICROS_PER_SECOND;
        long mins = secs / 60;
        secs %= 60;
        long hours = mins / 60;
        mins %= 60;
        return String.format(Locale.US, "%02d:%02d:%02d", new Object[]{Long.valueOf(hours), Long.valueOf(mins), Long.valueOf(secs)});
    }

    public static IjkMediaMeta parse(Bundle mediaMeta) {
        if (mediaMeta == null) {
            return null;
        }
        IjkMediaMeta meta = new IjkMediaMeta();
        meta.mMediaMeta = mediaMeta;
        meta.mFormat = meta.getString(IJKM_KEY_FORMAT);
        meta.mDurationUS = meta.getLong(IJKM_KEY_DURATION_US);
        meta.mStartUS = meta.getLong(IJKM_KEY_START_US);
        meta.mBitrate = meta.getLong(IJKM_KEY_BITRATE);
        int videoStreamIndex = meta.getInt("video", -1);
        int audioStreamIndex = meta.getInt("audio", -1);
        ArrayList<Bundle> streams = meta.getParcelableArrayList(IJKM_KEY_STREAMS);
        if (streams == null) {
            return meta;
        }
        int index = -1;
        Iterator it = streams.iterator();
        while (it.hasNext()) {
            Bundle streamBundle = (Bundle) it.next();
            index++;
            if (streamBundle != null) {
                IjkStreamMeta streamMeta = new IjkStreamMeta(index);
                streamMeta.mMeta = streamBundle;
                streamMeta.mType = streamMeta.getString(IJKM_KEY_TYPE);
                streamMeta.mLanguage = streamMeta.getString(IJKM_KEY_LANGUAGE);
                if (!TextUtils.isEmpty(streamMeta.mType)) {
                    streamMeta.mCodecName = streamMeta.getString(IJKM_KEY_CODEC_NAME);
                    streamMeta.mCodecProfile = streamMeta.getString(IJKM_KEY_CODEC_PROFILE);
                    streamMeta.mCodecLongName = streamMeta.getString(IJKM_KEY_CODEC_LONG_NAME);
                    streamMeta.mBitrate = (long) streamMeta.getInt(IJKM_KEY_BITRATE);
                    if (streamMeta.mType.equalsIgnoreCase("video")) {
                        streamMeta.mWidth = streamMeta.getInt("width");
                        streamMeta.mHeight = streamMeta.getInt("height");
                        streamMeta.mFpsNum = streamMeta.getInt(IJKM_KEY_FPS_NUM);
                        streamMeta.mFpsDen = streamMeta.getInt(IJKM_KEY_FPS_DEN);
                        streamMeta.mTbrNum = streamMeta.getInt(IJKM_KEY_TBR_NUM);
                        streamMeta.mTbrDen = streamMeta.getInt(IJKM_KEY_TBR_DEN);
                        streamMeta.mSarNum = streamMeta.getInt(IJKM_KEY_SAR_NUM);
                        streamMeta.mSarDen = streamMeta.getInt(IJKM_KEY_SAR_DEN);
                        if (videoStreamIndex == index) {
                            meta.mVideoStream = streamMeta;
                        }
                    } else if (streamMeta.mType.equalsIgnoreCase("audio")) {
                        streamMeta.mSampleRate = streamMeta.getInt(IJKM_KEY_SAMPLE_RATE);
                        streamMeta.mChannelLayout = streamMeta.getLong(IJKM_KEY_CHANNEL_LAYOUT);
                        if (audioStreamIndex == index) {
                            meta.mAudioStream = streamMeta;
                        }
                    }
                    meta.mStreams.add(streamMeta);
                }
            }
        }
        return meta;
    }
}
