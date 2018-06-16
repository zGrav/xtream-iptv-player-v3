package z.xtreamiptv.playerv3.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.view.interfaces.LoginInterface;

public class SplashActivity extends AppCompatActivity implements LoginInterface {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private SharedPreferences loginPreferencesAfterLogin;

    class C18561 implements Runnable {
        C18561() {
        }

        public void run() {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.finish();
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        changeStatusBarColor();
        new Handler().postDelayed(new C18561(), 1000);
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

    public void atStart() {
    }

    public void onFinish() {
    }

    public void onFailed(String errorMessage) {
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
    }

    public void stopLoader() {
    }
}
