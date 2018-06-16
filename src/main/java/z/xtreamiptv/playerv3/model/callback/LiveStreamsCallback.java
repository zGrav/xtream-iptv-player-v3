package z.xtreamiptv.playerv3.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LiveStreamsCallback implements Serializable {
    public String activeEpg = "";
    @SerializedName("added")
    @Expose
    private String added;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("custom_sid")
    @Expose
    private String customSid;
    @SerializedName("direct_source")
    @Expose
    private String directSource;
    @SerializedName("epg_channel_id")
    @Expose
    private String epgChannelId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("num")
    @Expose
    private Integer num;
    @SerializedName("stream_icon")
    @Expose
    private String streamIcon;
    @SerializedName("stream_id")
    @Expose
    private Integer streamId;
    @SerializedName("stream_type")
    @Expose
    private String streamType;
    @SerializedName("tv_archive")
    @Expose
    private Integer tvArchive;
    @SerializedName("tv_archive_duration")
    @Expose
    private Integer tvArchiveDuration;

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamType() {
        return this.streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public Integer getStreamId() {
        return this.streamId;
    }

    public void setStreamId(Integer streamId) {
        this.streamId = streamId;
    }

    public String getStreamIcon() {
        return this.streamIcon;
    }

    public void setStreamIcon(String streamIcon) {
        this.streamIcon = streamIcon;
    }

    public String getEpgChannelId() {
        return this.epgChannelId;
    }

    public void setEpgChannelId(String epgChannelId) {
        this.epgChannelId = epgChannelId;
    }

    public String getAdded() {
        return this.added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCustomSid() {
        return this.customSid;
    }

    public void setCustomSid(String customSid) {
        this.customSid = customSid;
    }

    public Integer getTvArchive() {
        return this.tvArchive;
    }

    public void setTvArchive(Integer tvArchive) {
        this.tvArchive = tvArchive;
    }

    public String getDirectSource() {
        return this.directSource;
    }

    public void setDirectSource(String directSource) {
        this.directSource = directSource;
    }

    public Integer getTvArchiveDuration() {
        return this.tvArchiveDuration;
    }

    public void setTvArchiveDuration(Integer tvArchiveDuration) {
        this.tvArchiveDuration = tvArchiveDuration;
    }

    public String getActiveEpg() {
        return this.activeEpg;
    }

    public void setActiveEpg(String activeEpg) {
        this.activeEpg = activeEpg;
    }

    public String getOriginalStreamType() {
        return "live";
    }
}
