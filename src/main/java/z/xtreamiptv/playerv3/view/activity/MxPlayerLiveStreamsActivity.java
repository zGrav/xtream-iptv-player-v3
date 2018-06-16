package z.xtreamiptv.playerv3.view.activity;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import com.google.android.exoplayer2.util.MimeTypes;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;

public class MxPlayerLiveStreamsActivity extends AppCompatActivity {
    private static final String _MX_PLAYER_CLASS_NAME = "com.mxtech.videoplayer.ad.ActivityScreen";
    private static final String _MX_PLAYER_CLASS_NAME_PRO = "com.mxtech.videoplayer.pro.ActivityScreen";
    private static final String _MX_PLAYER_PACKAGE_NAME = "com.mxtech.videoplayer.ad";
    private static final String _MX_PLAYER_PACKAGE_NAME_PRO = "com.mxtech.videoplayer.pro";
    private Uri contentUri;
    private Context context;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    private String mFilePath;

    class C18121 implements OnClickListener {
        C18121() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            try {
                MxPlayerLiveStreamsActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.mxtech.videoplayer.ad")));
            } catch (ActivityNotFoundException e) {
                try {
                    MxPlayerLiveStreamsActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad")));
                } catch (ActivityNotFoundException exception) {
                    Utils.showToast(MxPlayerLiveStreamsActivity.this.context, String.valueOf(exception));
                }
            }
        }
    }

    class C18132 implements OnClickListener {
        C18132() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            MxPlayerLiveStreamsActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        initialize();
    }

    private void initialize() {
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String streamType = getIntent().getStringExtra("STREAM_TYPE");
        this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/live/" + username + "/" + password + "/" + opened_stream_id + "." + allowedFormat;
        this.contentUri = Uri.parse(this.mFilePath);
        Intent intent;
        if (!appInstalledOrNot(_MX_PLAYER_PACKAGE_NAME_PRO)) {
            try {
                intent = new Intent();
                intent.setClassName(_MX_PLAYER_PACKAGE_NAME, _MX_PLAYER_CLASS_NAME);
                intent.putExtra("package", getPackageName());
                intent.setDataAndType(this.contentUri, MimeTypes.APPLICATION_M3U8);
                startActivity(intent);
                finish();
            } catch (ActivityNotFoundException e) {
                mxPlayerNotFoundDialogBox();
            }
        } else if (this.context != null) {
            try {
                intent = new Intent();
                intent.setClassName(_MX_PLAYER_PACKAGE_NAME_PRO, _MX_PLAYER_CLASS_NAME_PRO);
                intent.putExtra("package", getPackageName());
                intent.setDataAndType(this.contentUri, MimeTypes.APPLICATION_M3U8);
                intent.setPackage(_MX_PLAYER_PACKAGE_NAME_PRO);
                startActivity(intent);
                finish();
            } catch (ActivityNotFoundException e2) {
                mxPlayerNotFoundDialogBox();
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        if (this.context == null) {
            return false;
        }
        try {
            this.context.getPackageManager().getPackageInfo(uri, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void mxPlayerNotFoundDialogBox() {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.media_player));
        builder.setMessage(getResources().getString(R.string.alert_mx_player));
        builder.setPositiveButton(getResources().getString(R.string.install_it), new C18121());
        builder.setNegativeButton(getResources().getString(R.string.cancel_small), new C18132());
        builder.setCancelable(false);
        builder.create().show();
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        if (VERSION.SDK_INT >= 19) {
            window.clearFlags(67108864);
        }
        if (VERSION.SDK_INT >= 21) {
            window.addFlags(Integer.MIN_VALUE);
        }
        if (VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }
}
