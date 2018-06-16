package z.xtreamiptv.playerv3.view.interfaces;

import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;
import z.xtreamiptv.playerv3.model.callback.XtreamPanelAPICallback;

public interface LoadChannelsVODTvGuidInterface {
    void laodTvGuideFailed(String str, String str2);

    void loadChannelsAndVOD(XtreamPanelAPICallback xtreamPanelAPICallback, String str);

    void loadChannelsAndVodFailed(String str, String str2);

    void loadTvGuide(XMLTVCallback xMLTVCallback);
}
