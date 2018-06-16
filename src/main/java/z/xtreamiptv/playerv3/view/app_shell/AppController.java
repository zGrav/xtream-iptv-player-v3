package z.xtreamiptv.playerv3.view.app_shell;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import z.xtreamiptv.playerv3.BuildConfig;
import z.xtreamiptv.playerv3.R;
import io.realm.Realm;
import io.realm.RealmConfiguration.Builder;

public class AppController extends Application {
    public static AppController sInstance;
    protected String userAgent;

    public static AppController getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static synchronized AppController getInstanceSyn() {
        AppController appController;
        synchronized (AppController.class) {
            appController = sInstance;
        }
        return appController;
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;
        this.userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        Realm.init(this);
        Realm.setDefaultConfiguration(new Builder().name("z.xtreamiptv.iptvsmarters").deleteRealmIfMigrationNeeded().schemaVersion(1).build());
    }

    public Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory((Context) this, (TransferListener) bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(this.userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals(BuildConfig.FLAVOR);
    }

    public void onLowMemory() {
        Glide.get(this).clearMemory();
        super.onLowMemory();
    }

    public void onTrimMemory(int level) {
        Glide.get(this).trimMemory(level);
        super.onTrimMemory(level);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
