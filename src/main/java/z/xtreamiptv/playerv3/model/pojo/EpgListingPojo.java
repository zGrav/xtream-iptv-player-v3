package z.xtreamiptv.playerv3.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EpgListingPojo {
    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("epg_id")
    @Expose
    private String epgId;
    @SerializedName("has_archive")
    @Expose
    private Integer hasArchive;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("now_playing")
    @Expose
    private Integer nowPlaying;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("start_timestamp")
    @Expose
    private String startTimestamp;
    @SerializedName("stop_timestamp")
    @Expose
    private String stopTimestamp;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpgId() {
        return this.epgId;
    }

    public void setEpgId(String epgId) {
        this.epgId = epgId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStartTimestamp() {
        return this.startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getStopTimestamp() {
        return this.stopTimestamp;
    }

    public void setStopTimestamp(String stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    public Integer getNowPlaying() {
        return this.nowPlaying;
    }

    public void setNowPlaying(Integer nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public Integer getHasArchive() {
        return this.hasArchive;
    }

    public void setHasArchive(Integer hasArchive) {
        this.hasArchive = hasArchive;
    }
}
