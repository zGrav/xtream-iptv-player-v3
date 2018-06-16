package z.xtreamiptv.playerv3.model.pojo;

import java.io.Serializable;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "programme", strict = false)
public class XMLTVProgrammePojo implements Serializable {
    @Path("category")
    @Text(required = false)
    private String category;
    @Attribute(name = "channel", required = false)
    private String channel;
    @Path("country")
    @Text(required = false)
    private String country;
    @Path("date")
    @Text(required = false)
    private String date;
    @Path("desc")
    @Text(required = false)
    private String desc;
    @Path("episode-num")
    @Text(required = false)
    private String episode_num;
    @Path("icon")
    @Text(required = false)
    private String icon;
    @Attribute(name = "start", required = false)
    private String start;
    @Attribute(name = "stop", required = false)
    private String stop;
    @Path("sub-title")
    @Text(required = false)
    private String sub_title;
    @Path("title")
    @Text(required = false)
    private String title;

    public String getSub_title() {
        return this.sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStop() {
        return this.stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEpisodeNum() {
        return this.episode_num;
    }

    public void setEpisodeNum(String episodeNum) {
        this.episode_num = episodeNum;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString() {
        return "ClassPojo [stop = " + this.stop + ",  title = " + this.title + ", category = " + this.category + ", episode-num = " + this.episode_num + ", date = " + this.date + ", country = " + this.country + ", icon = " + this.icon + ", sub-title = " + this.sub_title + ",desc = " + this.desc + ", start = " + this.start + ", channel = " + this.channel + "]";
    }
}
