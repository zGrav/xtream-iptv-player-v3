package z.xtreamiptv.playerv3.view.interfaces;

import z.xtreamiptv.playerv3.model.callback.LoginCallback;

public interface LoginInterface extends BaseInterface {
    void stopLoader();

    void validateLogin(LoginCallback loginCallback, String str);
}
