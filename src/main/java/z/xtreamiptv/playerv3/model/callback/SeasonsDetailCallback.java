package z.xtreamiptv.playerv3.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeasonsDetailCallback {
    @SerializedName("air_date")
    @Expose
    public String airDate;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("cover_big")
    @Expose
    public String coverBig;
    @SerializedName("episode_count")
    @Expose
    public Integer episodeCount;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("season_number")
    @Expose
    public Integer seasonNumber;

    public String getAirDate() {
        return this.airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public Integer getEpisodeCount() {
        return this.episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getSeasonNumber() {
        return this.seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoverBig() {
        return this.coverBig;
    }

    public void setCoverBig(String coverBig) {
        this.coverBig = coverBig;
    }
}
