package z.xtreamiptv.playerv3.v2api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLiveStreamCallback {
    @SerializedName("added")
    @Expose
    public String added;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("custom_sid")
    @Expose
    public Object customSid;
    @SerializedName("direct_source")
    @Expose
    public String directSource;
    @SerializedName("epg_channel_id")
    @Expose
    public Object epgChannelId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("num")
    @Expose
    public Integer num;
    @SerializedName("stream_icon")
    @Expose
    public String streamIcon;
    @SerializedName("stream_id")
    @Expose
    public Integer streamId;
    @SerializedName("stream_type")
    @Expose
    public String streamType;
    @SerializedName("tv_archive")
    @Expose
    public Integer tvArchive;
    @SerializedName("tv_archive_duration")
    @Expose
    public Integer tvArchiveDuration;

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

    public Object getEpgChannelId() {
        return this.epgChannelId;
    }

    public void setEpgChannelId(Object epgChannelId) {
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

    public Object getCustomSid() {
        return this.customSid;
    }

    public void setCustomSid(Object customSid) {
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
}
