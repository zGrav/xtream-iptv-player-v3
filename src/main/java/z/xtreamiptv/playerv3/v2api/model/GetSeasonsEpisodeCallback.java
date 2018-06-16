package z.xtreamiptv.playerv3.v2api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetSeasonsEpisodeCallback {
    @SerializedName("episodes")
    @Expose
    public GetEpisodesPojo episodes;
    @SerializedName("seasons")
    @Expose
    public List<Integer> seasons = null;

    public GetEpisodesPojo getEpisodes() {
        return this.episodes;
    }

    public void setEpisodes(GetEpisodesPojo episodes) {
        this.episodes = episodes;
    }

    public List<Integer> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(List<Integer> seasons) {
        this.seasons = seasons;
    }
}
