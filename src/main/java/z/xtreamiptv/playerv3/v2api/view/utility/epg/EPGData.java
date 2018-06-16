package z.xtreamiptv.playerv3.v2api.view.utility.epg;

import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGChannel;
import z.xtreamiptv.playerv3.v2api.view.utility.epg.domain.EPGEvent;
import java.util.List;

public interface EPGData {
    EPGChannel addNewChannel(String str, String str2, String str3, String str4, String str5);

    EPGChannel getChannel(int i);

    int getChannelCount();

    EPGEvent getEvent(int i, int i2);

    List<EPGEvent> getEvents(int i);

    EPGChannel getOrCreateChannel(String str, String str2, String str3, String str4, String str5);

    boolean hasData();
}
