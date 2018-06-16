package z.xtreamiptv.playerv3.v2api.view.utility.epg.domain;

public class EPGEvent {
    private final EPGChannel channel;
    private final String desc;
    private final long end;
    private EPGEvent nextEvent;
    private EPGEvent previousEvent;
    private final String programUrl;
    public boolean selected;
    private final long start;
    private final String title;

    public EPGEvent(EPGChannel epgChannel, long start, long end, String title, String programUrl, String desc) {
        this.channel = epgChannel;
        this.start = start;
        this.end = end;
        this.title = title;
        this.programUrl = programUrl;
        this.desc = desc;
    }

    public EPGChannel getChannel() {
        return this.channel;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getProgramUrl() {
        return this.programUrl;
    }

    public boolean isCurrent(int timeShift) {
        long now = System.currentTimeMillis() + ((long) timeShift);
        return now >= this.start && now <= this.end;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setNextEvent(EPGEvent nextEvent) {
        this.nextEvent = nextEvent;
    }

    public EPGEvent getNextEvent() {
        return this.nextEvent;
    }

    public void setPreviousEvent(EPGEvent previousEvent) {
        this.previousEvent = previousEvent;
    }

    public EPGEvent getPreviousEvent() {
        return this.previousEvent;
    }
}
