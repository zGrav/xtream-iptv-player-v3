package z.xtreamiptv.playerv3.v2api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSeriesStreamCallback {
    @SerializedName("cast")
    @Expose
    public String cast;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("cover")
    @Expose
    public String cover;
    @SerializedName("director")
    @Expose
    public String director;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("last_modified")
    @Expose
    public String lastModified;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("num")
    @Expose
    public Integer num;
    @SerializedName("plot")
    @Expose
    public String plot;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("releaseDate")
    @Expose
    public String releaseDate;
    @SerializedName("series_id")
    @Expose
    public Integer seriesId;
    @SerializedName("stream_type")
    @Expose
    public Object streamType;

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getStreamType() {
        return this.streamType;
    }

    public void setStreamType(Object streamType) {
        this.streamType = streamType;
    }

    public Integer getSeriesId() {
        return this.seriesId;
    }

    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPlot() {
        return this.plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getCast() {
        return this.cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
