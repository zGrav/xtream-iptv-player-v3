package z.xtreamiptv.playerv3.model.callback;

import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "tv", strict = false)
public class XMLTVCallback {
    @ElementList(inline = true, required = false)
    public List<XMLTVProgrammePojo> programmePojos;

    public String toString() {
        return "ClassPojo [programmePojos= " + this.programmePojos + "]";
    }
}
