package z.xtreamiptv.playerv3.view.interfaces;

import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;

public interface XMLTVInterface extends BaseInterface {
    void epgXMLTV(XMLTVCallback xMLTVCallback);

    void epgXMLTVUpdateFailed(String str);
}
