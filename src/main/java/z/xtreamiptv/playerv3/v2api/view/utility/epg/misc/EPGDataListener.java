package z.xtreamiptv.playerv3.v2api.view.utility.epg.misc;

import z.xtreamiptv.playerv3.v2api.view.utility.epg.EPG;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.EPGData;

public class EPGDataListener {
    private EPG epg;

    public EPGDataListener(EPG epg) {
        this.epg = epg;
    }

    public void processData(EPGData data) {
        this.epg.setEPGData(data);
        this.epg.recalculateAndRedraw(null, false, null, null);
    }
}
