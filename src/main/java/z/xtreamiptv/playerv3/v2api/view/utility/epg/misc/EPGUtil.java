package z.xtreamiptv.playerv3.v2api.view.utility.epg.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.Listener;
import com.squareup.picasso.Picasso.Builder;
import com.squareup.picasso.Target;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EPGUtil {
    private static final String TAG = "EPGUtil";
    private static Context context1;
    private static DateTimeFormatter dtfShortTime = null;
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private static Picasso picasso = null;

    static class C17421 implements Listener {
        C17421() {
        }

        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
            Log.e(EPGUtil.TAG, exception.getMessage());
        }
    }

    public static String getShortTime(Context context, long timeMillis) {
        context1 = context;
        loginPreferencesSharedPref_time_format = context1.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        dtfShortTime = DateTimeFormat.forPattern(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
        return dtfShortTime.print(timeMillis);
    }

    public static String getWeekdayName(long dateMillis) {
        return new LocalDate(dateMillis).dayOfWeek().getAsText();
    }

    public static String getEPGdayName(long dateMillis) {
        LocalDate date = new LocalDate(dateMillis);
        return date.dayOfWeek().getAsShortText() + " " + date.getDayOfMonth() + "/" + date.getMonthOfYear();
    }

    public static void loadImageInto(Context context, String url, int width, int height, Target target) {
        initPicasso(context);
        if (url == null || url.equals("")) {
            picasso.load(R.drawable.iptv_placeholder).into(target);
        } else {
            picasso.load(url).resize(width, height).centerInside().into(target);
        }
    }

    private static void initPicasso(Context context) {
        if (picasso == null) {
            picasso = new Builder(context).downloader(new OkHttpDownloader(new OkHttpClient())).listener(new C17421()).build();
        }
    }
}
