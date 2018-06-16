package z.xtreamiptv.playerv3.view.interfaces;

import z.xtreamiptv.playerv3.model.callback.LiveStreamsEpgCallback;

public interface LiveStreamsEpgInterface extends BaseInterface {
    void liveStreamsEpg(LiveStreamsEpgCallback liveStreamsEpgCallback);
}
