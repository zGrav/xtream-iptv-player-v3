package z.xtreamiptv.playerv3.v2api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVODStreamCallback {
    @SerializedName("added")
    @Expose
    public String added;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("container_extension")
    @Expose
    public String containerExtension;
    @SerializedName("custom_sid")
    @Expose
    public Object customSid;
    @SerializedName("direct_source")
    @Expose
    public String directSource;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("num")
    @Expose
    public Integer num;
    @SerializedName("series_no")
    @Expose
    public Object seriesNo;
    @SerializedName("stream_icon")
    @Expose
    public String streamIcon;
    @SerializedName("stream_id")
    @Expose
    public Integer streamId;
    @SerializedName("stream_type")
    @Expose
    public String streamType;

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

    public Object getSeriesNo() {
        return this.seriesNo;
    }

    public void setSeriesNo(Object seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getContainerExtension() {
        return this.containerExtension;
    }

    public void setContainerExtension(String containerExtension) {
        this.containerExtension = containerExtension;
    }

    public Object getCustomSid() {
        return this.customSid;
    }

    public void setCustomSid(Object customSid) {
        this.customSid = customSid;
    }

    public String getDirectSource() {
        return this.directSource;
    }

    public void setDirectSource(String directSource) {
        this.directSource = directSource;
    }
}
