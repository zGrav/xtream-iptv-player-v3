package z.xtreamiptv.playerv3.view.utility.epg.domain;

import com.google.common.collect.Lists;
import java.util.List;

public class EPGChannel {
    private final int channelID;
    private final String epgChannelID;
    private List<EPGEvent> events = Lists.newArrayList();
    private final String imageURL;
    private final String name;
    private EPGChannel nextChannel;
    private final String num;
    private EPGChannel previousChannel;
    private final String streamID;

    public EPGChannel(String imageURL, String name, int channelID, String streamID, String num, String epgChannelID) {
        this.imageURL = imageURL;
        this.name = name;
        this.channelID = channelID;
        this.streamID = streamID;
        this.num = num;
        this.epgChannelID = epgChannelID;
    }

    public int getChannelID() {
        return this.channelID;
    }

    public String getStreamID() {
        return this.streamID;
    }

    public String getName() {
        return this.name;
    }

    public String getEpgChannelID() {
        return this.epgChannelID;
    }

    public String getNum() {
        return this.num;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public List<EPGEvent> getEvents() {
        return this.events;
    }

    public EPGChannel getPreviousChannel() {
        return this.previousChannel;
    }

    public void setPreviousChannel(EPGChannel previousChannel) {
        this.previousChannel = previousChannel;
    }

    public EPGChannel getNextChannel() {
        return this.nextChannel;
    }

    public void setNextChannel(EPGChannel nextChannel) {
        this.nextChannel = nextChannel;
    }

    public EPGEvent addEvent(EPGEvent event) {
        this.events.add(event);
        return event;
    }
}
