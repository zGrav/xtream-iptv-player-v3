package z.xtreamiptv.playerv3.model;

public class VODDBModel {
    private String added;
    private String categoryId;
    private String containerExtension;
    private String customSid;
    private String directSource;
    private int idAutoVOD;
    private String name;
    private String num;
    private String seriesNo;
    private String streamIcon;
    private String streamId;
    private String streamType;

    public int getIdAutoVOD() {
        return this.idAutoVOD;
    }

    public void setIdAutoVOD(int idAutoVOD) {
        this.idAutoVOD = idAutoVOD;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
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

    public String getSeriesNo() {
        return this.seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getContainerExtension() {
        return this.containerExtension;
    }

    public void setContainerExtension(String containerExtension) {
        this.containerExtension = containerExtension;
    }

    public String getCustomSid() {
        return this.customSid;
    }

    public void setCustomSid(String customSid) {
        this.customSid = customSid;
    }

    public String getDirectSource() {
        return this.directSource;
    }

    public void setDirectSource(String directSource) {
        this.directSource = directSource;
    }

    public VODDBModel(String num, String name, String streamType, String streamId, String streamIcon, String added, String categoryId, String seriesNo, String containerExtension, String customSid, String directSource) {
        this.num = num;
        this.name = name;
        this.streamType = streamType;
        this.streamId = streamId;
        this.streamIcon = streamIcon;
        this.added = added;
        this.categoryId = categoryId;
        this.seriesNo = seriesNo;
        this.containerExtension = containerExtension;
        this.customSid = customSid;
        this.directSource = directSource;
    }
}
