package z.xtreamiptv.playerv3.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanelAvailableChannelsPojo {
    @SerializedName("added")
    @Expose
    private String added;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("container_extension")
    @Expose
    private Object containerExtension;
    @SerializedName("custom_sid")
    @Expose
    private String customSid;
    @SerializedName("direct_source")
    @Expose
    private String directSource;
    @SerializedName("epg_channel_id")
    @Expose
    private String epgChannelId;
    @SerializedName("live")
    @Expose
    private String live;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("num")
    @Expose
    private Integer num;
    @SerializedName("series_no")
    @Expose
    private Object seriesNo;
    @SerializedName("stream_icon")
    @Expose
    private String streamIcon;
    @SerializedName("stream_id")
    @Expose
    private String streamId;
    @SerializedName("stream_type")
    @Expose
    private String streamType;
    @SerializedName("tv_archive")
    @Expose
    private Integer tvArchive;
    @SerializedName("tv_archive_duration")
    @Expose
    private String tvArchiveDuration;
    @SerializedName("type_name")
    @Expose
    private Object typeName;

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

    public Object getTypeName() {
        return this.typeName;
    }

    public void setTypeName(Object typeName) {
        this.typeName = typeName;
    }

    public String getStreamId() {
        return this.streamId;
    }

    public void setStreamId(String streamId) {
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

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Object getSeriesNo() {
        return this.seriesNo;
    }

    public void setSeriesNo(Object seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getLive() {
        return this.live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public Object getContainerExtension() {
        return this.containerExtension;
    }

    public void setContainerExtension(Object containerExtension) {
        this.containerExtension = containerExtension;
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

    public String getTvArchiveDuration() {
        return this.tvArchiveDuration;
    }

    public void setTvArchiveDuration(String tvArchiveDuration) {
        this.tvArchiveDuration = tvArchiveDuration;
    }
}
