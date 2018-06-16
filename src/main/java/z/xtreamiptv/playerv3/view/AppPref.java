package z.xtreamiptv.playerv3.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import z.xtreamiptv.playerv3.R;

public class AppPref {
    private static Context appContext;
    private static AppPref appPref;
    private SharedPreferences preferences = appContext.getSharedPreferences(appContext.getString(R.string.app_name), 0);
    private Editor editor = this.preferences.edit();

    public static AppPref getInstance(Context appContext2) {
        appContext = appContext2;
        if (appPref != null) {
            return appPref;
        }
        appPref = new AppPref();
        return appPref;
    }

    public int getResizeMode() {
        return this.preferences.getInt("resize_mode", 0);
    }

    public void setResizeMode(int mode) {
        this.editor.putInt("resize_mode", mode);
        this.editor.commit();
    }
}
