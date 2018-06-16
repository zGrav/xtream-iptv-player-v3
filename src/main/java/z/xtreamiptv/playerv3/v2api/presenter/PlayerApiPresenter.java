package z.xtreamiptv.playerv3.v2api.presenter;

import android.content.Context;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.webrequest.RetrofitPost;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.view.interfaces.PlayerApiInterface;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlayerApiPresenter {
    private Context context;
    private PlayerApiInterface playerApiInterface;

    class C14131 implements Callback<List<GetLiveStreamCategoriesCallback>> {
        C14131() {
        }

        public void onResponse(Call<List<GetLiveStreamCategoriesCallback>> call, Response<List<GetLiveStreamCategoriesCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getLiveStreamCategories((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getLiveStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<GetLiveStreamCategoriesCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getLiveStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    class C14142 implements Callback<List<GetVODStreamCategoriesCallback>> {
        C14142() {
        }

        public void onResponse(Call<List<GetVODStreamCategoriesCallback>> call, Response<List<GetVODStreamCategoriesCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getVODStreamCategories((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getVODStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<GetVODStreamCategoriesCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getVODStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    class C14153 implements Callback<List<GetSeriesStreamCategoriesCallback>> {
        C14153() {
        }

        public void onResponse(Call<List<GetSeriesStreamCategoriesCallback>> call, Response<List<GetSeriesStreamCategoriesCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesCategories((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
            }
        }

        public void onFailure(Call<List<GetSeriesStreamCategoriesCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getSeriesStreamCatFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
        }
    }

    class C14164 implements Callback<List<GetLiveStreamCallback>> {
        C14164() {
        }

        public void onResponse(Call<List<GetLiveStreamCallback>> call, Response<List<GetLiveStreamCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getLiveStreams((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getLiveStreamFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<GetLiveStreamCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getLiveStreamFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    class C14175 implements Callback<List<GetVODStreamCallback>> {
        C14175() {
        }

        public void onResponse(Call<List<GetVODStreamCallback>> call, Response<List<GetVODStreamCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getVODStreams((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getVODStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<GetVODStreamCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getVODStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    class C14186 implements Callback<List<GetSeriesStreamCallback>> {
        C14186() {
        }

        public void onResponse(Call<List<GetSeriesStreamCallback>> call, Response<List<GetSeriesStreamCallback>> response) {
            if (response.body() != null && response.isSuccessful()) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesStreams((List) response.body());
            } else if (response.body() == null) {
                PlayerApiPresenter.this.playerApiInterface.getSeriesStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
                PlayerApiPresenter.this.playerApiInterface.onFinish();
                if (PlayerApiPresenter.this.context != null) {
                    PlayerApiPresenter.this.playerApiInterface.onFailed(PlayerApiPresenter.this.context.getResources().getString(R.string.invalid_request));
                }
            }
        }

        public void onFailure(Call<List<GetSeriesStreamCallback>> call, Throwable t) {
            PlayerApiPresenter.this.playerApiInterface.getSeriesStreamsFailed(AppConst.DB_UPDATED_STATUS_FAILED);
            PlayerApiPresenter.this.playerApiInterface.onFinish();
            PlayerApiPresenter.this.playerApiInterface.onFailed(t.getMessage());
        }
    }

    public PlayerApiPresenter(Context context, PlayerApiInterface playerApiInterface) {
        this.context = context;
        this.playerApiInterface = playerApiInterface;
    }

    public void getLiveStreamCat(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).liveStreamCategories(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_LIVE_CATEGORIES).enqueue(new C14131());
        }
    }

    public void getVODStreamCat(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).vodCategories(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_VOD_CATEGORIES).enqueue(new C14142());
        }
    }

    public void getSeriesStreamCat(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).seriesCategories(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_SERIES_CATEGORIES).enqueue(new C14153());
        }
    }

    public void getLiveStreams(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).allLiveStreams(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_LIVE_STREAMS).enqueue(new C14164());
        }
    }

    public void getVODStream(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).allVODStreams(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_VOD_STREAMS).enqueue(new C14175());
        }
    }

    public void getSeriesStream(String username, String password) {
        Retrofit retrofitObject = Utils.retrofitObject(this.context);
        if (retrofitObject != null) {
            ((RetrofitPost) retrofitObject.create(RetrofitPost.class)).allSeriesStreams(AppConst.CONTENT_TYPE, username, password, AppConst.ACTION_GET_SERIES_STREAMS).enqueue(new C14186());
        }
    }
}
