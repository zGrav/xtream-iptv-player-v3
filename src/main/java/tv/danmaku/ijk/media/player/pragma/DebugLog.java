package tv.danmaku.ijk.media.player.pragma;

import android.util.Log;
import java.util.Locale;

public class DebugLog {
    public static final boolean ENABLE_DEBUG = true;
    public static final boolean ENABLE_ERROR = true;
    public static final boolean ENABLE_INFO = true;
    public static final boolean ENABLE_VERBOSE = true;
    public static final boolean ENABLE_WARN = true;

    public static void m8e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void m9e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    public static void efmt(String tag, String fmt, Object... args) {
        Log.e(tag, String.format(Locale.US, fmt, args));
    }

    public static void m10i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void m11i(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
    }

    public static void ifmt(String tag, String fmt, Object... args) {
        Log.i(tag, String.format(Locale.US, fmt, args));
    }

    public static void m14w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void m15w(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
    }

    public static void wfmt(String tag, String fmt, Object... args) {
        Log.w(tag, String.format(Locale.US, fmt, args));
    }

    public static void m6d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void m7d(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
    }

    public static void dfmt(String tag, String fmt, Object... args) {
        Log.d(tag, String.format(Locale.US, fmt, args));
    }

    public static void m12v(String tag, String msg) {
        Log.v(tag, msg);
    }

    public static void m13v(String tag, String msg, Throwable tr) {
        Log.v(tag, msg, tr);
    }

    public static void vfmt(String tag, String fmt, Object... args) {
        Log.v(tag, String.format(Locale.US, fmt, args));
    }

    public static void printStackTrace(Throwable e) {
        e.printStackTrace();
    }

    public static void printCause(Throwable e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            e = cause;
        }
        printStackTrace(e);
    }
}
