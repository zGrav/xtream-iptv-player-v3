package z.xtreamiptv.playerv3.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerInfoCallback {
    @SerializedName("port")
    @Expose
    private String port;
    @SerializedName("rtmp_port")
    @Expose
    private String rtmpPort;
    @SerializedName("time_now")
    @Expose
    private String timeNow;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("url")
    @Expose
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getRtmpPort() {
        return this.rtmpPort;
    }

    public void setRtmpPort(String rtmpPort) {
        this.rtmpPort = rtmpPort;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimeNow() {
        return this.timeNow;
    }

    public void setTimeNow(String timeNow) {
        this.timeNow = timeNow;
    }
}
