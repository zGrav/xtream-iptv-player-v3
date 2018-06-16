package tv.danmaku.ijk.media.player;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class IjkMediaCodecInfo {
    public static final int RANK_ACCEPTABLE = 700;
    public static final int RANK_LAST_CHANCE = 600;
    public static final int RANK_MAX = 1000;
    public static final int RANK_NON_STANDARD = 100;
    public static final int RANK_NO_SENSE = 0;
    public static final int RANK_SECURE = 300;
    public static final int RANK_SOFTWARE = 200;
    public static final int RANK_TESTED = 800;
    private static final String TAG = "IjkMediaCodecInfo";
    private static Map<String, Integer> sKnownCodecList;
    public MediaCodecInfo mCodecInfo;
    public String mMimeType;
    public int mRank = 0;

    private static synchronized Map<String, Integer> getKnownCodecList() {
        Map<String, Integer> map;
        synchronized (IjkMediaCodecInfo.class) {
            if (sKnownCodecList != null) {
                map = sKnownCodecList;
            } else {
                sKnownCodecList = new TreeMap(String.CASE_INSENSITIVE_ORDER);
                sKnownCodecList.put("OMX.Nvidia.h264.decode", Integer.valueOf(800));
                sKnownCodecList.put("OMX.Nvidia.h264.decode.secure", Integer.valueOf(RANK_SECURE));
                sKnownCodecList.put("OMX.Intel.hw_vd.h264", Integer.valueOf(IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE));
                sKnownCodecList.put("OMX.Intel.VideoDecoder.AVC", Integer.valueOf(800));
                sKnownCodecList.put("OMX.qcom.video.decoder.avc", Integer.valueOf(800));
                sKnownCodecList.put("OMX.ittiam.video.decoder.avc", Integer.valueOf(0));
                sKnownCodecList.put("OMX.SEC.avc.dec", Integer.valueOf(800));
                sKnownCodecList.put("OMX.SEC.AVC.Decoder", Integer.valueOf(799));
                sKnownCodecList.put("OMX.SEC.avcdec", Integer.valueOf(798));
                sKnownCodecList.put("OMX.SEC.avc.sw.dec", Integer.valueOf(200));
                sKnownCodecList.put("OMX.Exynos.avc.dec", Integer.valueOf(800));
                sKnownCodecList.put("OMX.Exynos.AVC.Decoder", Integer.valueOf(799));
                sKnownCodecList.put("OMX.k3.video.decoder.avc", Integer.valueOf(800));
                sKnownCodecList.put("OMX.IMG.MSVDX.Decoder.AVC", Integer.valueOf(800));
                sKnownCodecList.put("OMX.TI.DUCATI1.VIDEO.DECODER", Integer.valueOf(800));
                sKnownCodecList.put("OMX.rk.video_decoder.avc", Integer.valueOf(800));
                sKnownCodecList.put("OMX.amlogic.avc.decoder.awesome", Integer.valueOf(800));
                sKnownCodecList.put("OMX.MARVELL.VIDEO.HW.CODA7542DECODER", Integer.valueOf(800));
                sKnownCodecList.put("OMX.MARVELL.VIDEO.H264DECODER", Integer.valueOf(200));
                sKnownCodecList.remove("OMX.Action.Video.Decoder");
                sKnownCodecList.remove("OMX.allwinner.video.decoder.avc");
                sKnownCodecList.remove("OMX.BRCM.vc4.decoder.avc");
                sKnownCodecList.remove("OMX.brcm.video.h264.hw.decoder");
                sKnownCodecList.remove("OMX.brcm.video.h264.decoder");
                sKnownCodecList.remove("OMX.cosmo.video.decoder.avc");
                sKnownCodecList.remove("OMX.duos.h264.decoder");
                sKnownCodecList.remove("OMX.hantro.81x0.video.decoder");
                sKnownCodecList.remove("OMX.hantro.G1.video.decoder");
                sKnownCodecList.remove("OMX.hisi.video.decoder");
                sKnownCodecList.remove("OMX.LG.decoder.video.avc");
                sKnownCodecList.remove("OMX.MS.AVC.Decoder");
                sKnownCodecList.remove("OMX.RENESAS.VIDEO.DECODER.H264");
                sKnownCodecList.remove("OMX.RTK.video.decoder");
                sKnownCodecList.remove("OMX.sprd.h264.decoder");
                sKnownCodecList.remove("OMX.ST.VFM.H264Dec");
                sKnownCodecList.remove("OMX.vpu.video_decoder.avc");
                sKnownCodecList.remove("OMX.WMT.decoder.avc");
                sKnownCodecList.remove("OMX.bluestacks.hw.decoder");
                sKnownCodecList.put("OMX.google.h264.decoder", Integer.valueOf(200));
                sKnownCodecList.put("OMX.google.h264.lc.decoder", Integer.valueOf(200));
                sKnownCodecList.put("OMX.k3.ffmpeg.decoder", Integer.valueOf(200));
                sKnownCodecList.put("OMX.ffmpeg.video.decoder", Integer.valueOf(200));
                sKnownCodecList.put("OMX.sprd.soft.h264.decoder", Integer.valueOf(200));
                map = sKnownCodecList;
            }
        }
        return map;
    }

    @TargetApi(16)
    public static IjkMediaCodecInfo setupCandidate(MediaCodecInfo codecInfo, String mimeType) {
        if (codecInfo == null || VERSION.SDK_INT < 16) {
            return null;
        }
        String name = codecInfo.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        int rank;
        name = name.toLowerCase(Locale.US);
        if (!name.startsWith("omx.")) {
            rank = 100;
        } else if (name.startsWith("omx.pv")) {
            rank = 200;
        } else if (name.startsWith("omx.google.")) {
            rank = 200;
        } else if (name.startsWith("omx.ffmpeg.")) {
            rank = 200;
        } else if (name.startsWith("omx.k3.ffmpeg.")) {
            rank = 200;
        } else if (name.startsWith("omx.avcodec.")) {
            rank = 200;
        } else if (name.startsWith("omx.ittiam.")) {
            rank = 0;
        } else if (!name.startsWith("omx.mtk.")) {
            Integer knownRank = (Integer) getKnownCodecList().get(name);
            if (knownRank != null) {
                rank = knownRank.intValue();
            } else {
                try {
                    if (codecInfo.getCapabilitiesForType(mimeType) != null) {
                        rank = 700;
                    } else {
                        rank = RANK_LAST_CHANCE;
                    }
                } catch (Throwable th) {
                    rank = RANK_LAST_CHANCE;
                }
            }
        } else if (VERSION.SDK_INT < 18) {
            rank = 0;
        } else {
            rank = 800;
        }
        IjkMediaCodecInfo candidate = new IjkMediaCodecInfo();
        candidate.mCodecInfo = codecInfo;
        candidate.mRank = rank;
        candidate.mMimeType = mimeType;
        return candidate;
    }

    @TargetApi(16)
    public void dumpProfileLevels(String mimeType) {
        if (VERSION.SDK_INT >= 16) {
            try {
                CodecCapabilities caps = this.mCodecInfo.getCapabilitiesForType(mimeType);
                int maxProfile = 0;
                int maxLevel = 0;
                if (!(caps == null || caps.profileLevels == null)) {
                    for (CodecProfileLevel profileLevel : caps.profileLevels) {
                        if (profileLevel != null) {
                            maxProfile = Math.max(maxProfile, profileLevel.profile);
                            maxLevel = Math.max(maxLevel, profileLevel.level);
                        }
                    }
                }
                Log.i(TAG, String.format(Locale.US, "%s", new Object[]{getProfileLevelName(maxProfile, maxLevel)}));
            } catch (Throwable th) {
                Log.i(TAG, "profile-level: exception");
            }
        }
    }

    public static String getProfileLevelName(int profile, int level) {
        return String.format(Locale.US, " %s Profile Level %s (%d,%d)", new Object[]{getProfileName(profile), getLevelName(level), Integer.valueOf(profile), Integer.valueOf(level)});
    }

    public static String getProfileName(int profile) {
        switch (profile) {
            case 1:
                return "Baseline";
            case 2:
                return "Main";
            case 4:
                return "Extends";
            case 8:
                return "High";
            case 16:
                return "High10";
            case 32:
                return "High422";
            case 64:
                return "High444";
            default:
                return "Unknown";
        }
    }

    public static String getLevelName(int level) {
        switch (level) {
            case 1:
                return "1";
            case 2:
                return "1b";
            case 4:
                return "11";
            case 8:
                return "12";
            case 16:
                return "13";
            case 32:
                return AppConst.DB_EPG_ID;
            case 64:
                return "21";
            case 128:
                return "22";
            case 256:
                return AppConst.DB_LIVE_STREAMS_CAT_ID;
            case 512:
                return "31";
            case 1024:
                return "32";
            case 2048:
                return AppConst.DB_LIVE_STREAMS_ID;
            case 4096:
                return "41";
            case 8192:
                return "42";
            case 16384:
                return AppConst.DB_VOD_STREAMS_CAT_ID;
            case 32768:
                return "51";
            case 65536:
                return "52";
            default:
                return AppConst.PASSWORD_UNSET;
        }
    }
}
