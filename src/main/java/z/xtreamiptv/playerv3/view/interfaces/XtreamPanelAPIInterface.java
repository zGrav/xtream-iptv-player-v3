package z.xtreamiptv.playerv3.view.interfaces;

import z.xtreamiptv.playerv3.model.callback.XtreamPanelAPICallback;

public interface XtreamPanelAPIInterface extends BaseInterface {
    void panelAPI(XtreamPanelAPICallback xtreamPanelAPICallback, String str);

    void panelApiFailed(String str);
}
