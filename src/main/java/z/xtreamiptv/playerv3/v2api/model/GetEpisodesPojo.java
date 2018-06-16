package z.xtreamiptv.playerv3.v2api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetEpisodesPojo {
    @SerializedName("1")
    @Expose
    public List<GetEpisdoeDetailsCallback> episodeDetailList = null;

    public List<GetEpisdoeDetailsCallback> getEpisodeDetailList() {
        return this.episodeDetailList;
    }

    public void setEpisodeDetailList(List<GetEpisdoeDetailsCallback> episodeDetailList) {
        this.episodeDetailList = episodeDetailList;
    }
}
