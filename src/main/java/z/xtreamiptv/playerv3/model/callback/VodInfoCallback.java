package z.xtreamiptv.playerv3.model.callback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import z.xtreamiptv.playerv3.model.pojo.VodInfoPojo;

public class VodInfoCallback {
    @SerializedName("info")
    @Expose
    private VodInfoPojo info;

    public VodInfoPojo getInfo() {
        return this.info;
    }

    public void setInfo(VodInfoPojo info) {
        this.info = info;
    }
}
