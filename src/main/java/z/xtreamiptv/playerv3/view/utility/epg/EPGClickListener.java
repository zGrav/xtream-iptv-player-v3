package z.xtreamiptv.playerv3.view.utility.epg;

import z.xtreamiptv.playerv3.view.utility.epg.domain.EPGChannel;
import z.xtreamiptv.playerv3.view.utility.epg.domain.EPGEvent;

public interface EPGClickListener {
    void onChannelClicked(int i, EPGChannel ePGChannel);

    void onEventClicked(int i, int i2, EPGEvent ePGEvent);

    void onResetButtonClicked();
}
