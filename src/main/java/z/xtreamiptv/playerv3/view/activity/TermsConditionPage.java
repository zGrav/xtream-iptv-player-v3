package z.xtreamiptv.playerv3.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import z.xtreamiptv.playerv3.R;

public class TermsConditionPage extends AppCompatActivity {
    @BindView(R.id.accept)
    Button accept;
    WebView mWebView;

    class C18711 implements OnClickListener {
        C18711() {
        }

        public void onClick(View view) {
            TermsConditionPage.this.startActivity(new Intent(TermsConditionPage.this, LoginActivity.class));
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition_page);
        ButterKnife.bind(this);
        changeStatusBarColor();
        this.mWebView = (WebView) findViewById(R.id.webview);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl("file:///android_asset/terms.html");
        this.accept.setOnClickListener(new C18711());
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
