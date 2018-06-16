package z.xtreamiptv.playerv3.view.cast;

import android.content.Context;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.CastOptions.Builder;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.view.cast.expandedcontrols.ExpandedControlsActivity;
import java.util.List;

public class CastOptionsProvider implements OptionsProvider {
    public CastOptions getCastOptions(Context context) {
        return new Builder().setReceiverApplicationId(context.getString(R.string.app_id)).setCastMediaOptions(new CastMediaOptions.Builder().setNotificationOptions(new NotificationOptions.Builder().setTargetActivityClassName(ExpandedControlsActivity.class.getName()).build()).setExpandedControllerActivityClassName(ExpandedControlsActivity.class.getName()).build()).build();
    }

    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
