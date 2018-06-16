package z.xtreamiptv.playerv3.v2api.presenter;

import android.content.Context;
import com.google.gson.JsonElement;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.webrequest.RetrofitPost;
import z.xtreamiptv.playerv3.v2api.view.interfaces.SeriesInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeriesPresenter {
    private Context context;
    private SeriesInterface seriesInterface;

    class C14191 implements Callback<JsonElement> {
        C14191() {
        }

        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
            if (response != null && response.body() != null) {
                SeriesPresenter.this.seriesInterface.getSeriesEpisodeInfo((JsonElement) response.body());
            }
        }

        public void onFailure(Call<JsonElement> call, Throwable t) {
            SeriesPresenter.this.seriesInterface.onFinish();
            SeriesPresenter.this.seriesInterface.onFailed(t.getMessage());
        }
    }

    public SeriesPresenter(Context context, SeriesInterface seriesInterface) {
        this.context = context;
        this.seriesInterface = seriesInterface;
    }

    public void getSeriesEpisode(String username, String password, String seriesId) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).seasonsEpisode(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_SERIES_INFO, seriesId).enqueue(new C14191());
        }
    }
}
