package z.xtreamiptv.playerv3.view.interfaces;

import android.widget.TextView;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.callback.LiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.model.callback.LiveStreamsCallback;
import z.xtreamiptv.playerv3.model.callback.LiveStreamsEpgCallback;
import java.util.ArrayList;
import java.util.List;

public interface LiveStreamsInterface extends BaseInterface {
    void liveStreamCategories(List<LiveStreamCategoriesCallback> list);

    void liveStreams(List<LiveStreamsCallback> list, ArrayList<FavouriteDBModel> arrayList);

    void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback, TextView textView, TextView textView2);
}
