package z.xtreamiptv.playerv3.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.model.webrequest.RetrofitPost;
import z.xtreamiptv.playerv3.view.interfaces.LoginInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginPresenter {
    private Context context;
    private LoginInterface loginInteface;

    class C14081 implements Callback<LoginCallback> {
        C14081() {
        }

        public void onResponse(@NonNull Call<LoginCallback> call, @NonNull Response<LoginCallback> response) {
            LoginPresenter.this.loginInteface.atStart();
            if (response.isSuccessful()) {
                LoginPresenter.this.loginInteface.validateLogin((LoginCallback) response.body(), AppConst.VALIDATE_LOGIN);
                LoginPresenter.this.loginInteface.onFinish();
            } else if (response.code() == 404) {
                LoginPresenter.this.loginInteface.onFinish();
                LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
            } else if (response.body() == null) {
                LoginPresenter.this.loginInteface.onFinish();
                if (LoginPresenter.this.context != null) {
                    LoginPresenter.this.loginInteface.onFailed(LoginPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(@NonNull Call<LoginCallback> call, @NonNull Throwable t) {
            if (t.getMessage() != null && t.getMessage().contains("Unable to resolve host")) {
                LoginPresenter.this.loginInteface.onFinish();
                LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
            } else if (t.getMessage() == null || !t.getMessage().contains("Failed to connect")) {
                LoginPresenter.this.loginInteface.onFinish();
                if (t.getMessage() != null) {
                    LoginPresenter.this.loginInteface.onFailed(t.getMessage());
                } else {
                    LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
                }
            } else {
                LoginPresenter.this.loginInteface.onFinish();
                LoginPresenter.this.loginInteface.onFailed(AppConst.FAILED_TO_CONNECT);
            }
        }
    }

    class C14092 implements Callback<LoginCallback> {
        C14092() {
        }

        public void onResponse(@NonNull Call<LoginCallback> call, @NonNull Response<LoginCallback> response) {
            LoginPresenter.this.loginInteface.atStart();
            if (response.isSuccessful()) {
                LoginPresenter.this.loginInteface.validateLogin((LoginCallback) response.body(), AppConst.VALIDATE_LOGIN);
                LoginPresenter.this.loginInteface.onFinish();
            } else if (response.code() == 404) {
                LoginPresenter.this.loginInteface.onFinish();
                LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
            } else if (response.body() == null) {
                LoginPresenter.this.loginInteface.onFinish();
                if (LoginPresenter.this.context != null) {
                    LoginPresenter.this.loginInteface.onFailed(LoginPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(@NonNull Call<LoginCallback> call, @NonNull Throwable t) {
            if (t.getMessage() != null && t.getMessage().contains("Unable to resolve host")) {
                LoginPresenter.this.loginInteface.onFinish();
                LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
            } else if (t.getMessage() == null || !t.getMessage().contains("Failed to connect")) {
                LoginPresenter.this.loginInteface.onFinish();
                if (t.getMessage() != null) {
                    LoginPresenter.this.loginInteface.onFailed(t.getMessage());
                } else {
                    LoginPresenter.this.loginInteface.onFailed(AppConst.NETWORK_ERROR_OCCURED);
                }
            } else {
                LoginPresenter.this.loginInteface.onFinish();
                LoginPresenter.this.loginInteface.onFailed(AppConst.FAILED_TO_CONNECT);
            }
        }
    }

    public LoginPresenter(LoginInterface loginInteface, Context context) {
        this.loginInteface = loginInteface;
        this.context = context;
    }

    public void validateLogin(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).validateLogin(AppConst.CONTENT_TYPE, username, password).enqueue(new C14081());
        } else if (retrofitObject == null) {
            this.loginInteface.stopLoader();
        }
    }

    public void validateLoginUsingPanelAPI(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).validateLoginUsingPanelApi(AppConst.CONTENT_TYPE, username, password).enqueue(new C14092());
        } else if (retrofitObject == null) {
            this.loginInteface.stopLoader();
        }
    }
}
