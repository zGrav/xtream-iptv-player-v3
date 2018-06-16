package z.xtreamiptv.playerv3.model.webrequest;

import com.google.gson.JsonElement;
import z.xtreamiptv.playerv3.model.callback.LiveStreamsCallback;
import z.xtreamiptv.playerv3.model.callback.LiveStreamsEpgCallback;
import z.xtreamiptv.playerv3.model.callback.LoginCallback;
import z.xtreamiptv.playerv3.model.callback.VodInfoCallback;
import z.xtreamiptv.playerv3.model.callback.VodStreamsCallback;
import z.xtreamiptv.playerv3.model.callback.XMLTVCallback;
import z.xtreamiptv.playerv3.model.callback.XtreamPanelAPICallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitPost {
    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetLiveStreamCallback>> allLiveStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetSeriesStreamCallback>> allSeriesStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetVODStreamCallback>> allVODStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/xmltv.php")
    Call<XMLTVCallback> epgXMLTV(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetLiveStreamCategoriesCallback>> liveStreamCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<LiveStreamsCallback>> liveStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("category_id") String str5);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<LiveStreamsEpgCallback> liveStreamsEpg(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("stream_id") Integer num);

    @FormUrlEncoded
    @POST("/panel_api.php")
    Call<XtreamPanelAPICallback> panelAPI(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<JsonElement> seasonsEpisode(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("series_id") String str5);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetSeriesStreamCategoriesCallback>> seriesCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<LoginCallback> validateLogin(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/panel_api.php")
    Call<LoginCallback> validateLoginUsingPanelApi(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<GetVODStreamCategoriesCallback>> vodCategories(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<VodInfoCallback> vodInfo(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("vod_id") int i);

    @FormUrlEncoded
    @POST("/player_api.php")
    Call<List<VodStreamsCallback>> vodStreams(@Header("Content-Type") String str, @Field("username") String str2, @Field("password") String str3, @Field("action") String str4, @Field("category_id") String str5);
}
