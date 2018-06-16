package z.xtreamiptv.playerv3.v2api.view.utility.epg.domain;

import android.os.Parcelable;
import android.view.View.BaseSavedState;

public class EPGState extends BaseSavedState {
    private EPGEvent currentEvent = null;

    public EPGState(Parcelable superState) {
        super(superState);
    }

    public EPGEvent getCurrentEvent() {
        return this.currentEvent;
    }

    public void setCurrentEvent(EPGEvent currentEvent) {
        this.currentEvent = currentEvent;
    }
}
