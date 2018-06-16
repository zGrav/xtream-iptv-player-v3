package z.xtreamiptv.playerv3.v2api.view.interfaces;

import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import java.util.List;

public interface PlayerApiInterface extends BaseInterfaceV2 {
    void getLiveStreamCatFailed(String str);

    void getLiveStreamCategories(List<GetLiveStreamCategoriesCallback> list);

    void getLiveStreamFailed(String str);

    void getLiveStreams(List<GetLiveStreamCallback> list);

    void getSeriesCategories(List<GetSeriesStreamCategoriesCallback> list);

    void getSeriesStreamCatFailed(String str);

    void getSeriesStreams(List<GetSeriesStreamCallback> list);

    void getSeriesStreamsFailed(String str);

    void getVODStreamCatFailed(String str);

    void getVODStreamCategories(List<GetVODStreamCategoriesCallback> list);

    void getVODStreams(List<GetVODStreamCallback> list);

    void getVODStreamsFailed(String str);
}
