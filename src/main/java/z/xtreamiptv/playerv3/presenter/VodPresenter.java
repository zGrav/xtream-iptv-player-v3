package z.xtreamiptv.playerv3.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.callback.VodInfoCallback;
import z.xtreamiptv.playerv3.model.webrequest.RetrofitPost;
import z.xtreamiptv.playerv3.view.interfaces.VodInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VodPresenter {
    private Context context;
    private VodInterface vodInteface;

    class C14101 implements Callback<VodInfoCallback> {
        C14101() {
        }

        public void onResponse(@NonNull Call<VodInfoCallback> call, @NonNull Response<VodInfoCallback> response) {
            VodPresenter.this.vodInteface.onFinish();
            if (response.isSuccessful()) {
                VodPresenter.this.vodInteface.vodInfo((VodInfoCallback) response.body());
            } else if (response.body() == null) {
                VodPresenter.this.vodInteface.onFailed(AppConst.INVALID_REQUEST);
            }
        }

        public void onFailure(@NonNull Call<VodInfoCallback> call, @NonNull Throwable t) {
            VodPresenter.this.vodInteface.onFinish();
            VodPresenter.this.vodInteface.onFailed(t.getMessage());
        }
    }

    public VodPresenter(VodInterface vodInteface, Context context) {
        this.vodInteface = vodInteface;
        this.context = context;
    }

    public void vodInfo(String username, String password, int streamId) {
        this.vodInteface.atStart();
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).vodInfo(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_VOD_INFO, streamId).enqueue(new C14101());
        }
    }
}
