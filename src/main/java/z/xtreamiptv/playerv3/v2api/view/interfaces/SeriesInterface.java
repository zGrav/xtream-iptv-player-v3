package z.xtreamiptv.playerv3.v2api.view.interfaces;

import com.google.gson.JsonElement;
import z.xtreamiptv.playerv3.view.interfaces.BaseInterface;

public interface SeriesInterface extends BaseInterface {
    void getSeriesEpisodeInfo(JsonElement jsonElement);
}
