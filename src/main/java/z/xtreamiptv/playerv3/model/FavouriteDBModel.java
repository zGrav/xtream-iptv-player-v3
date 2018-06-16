package z.xtreamiptv.playerv3.model;

public class FavouriteDBModel {
    private String categoryID;
    private int id;
    private int streamID;
    private String type;

    public FavouriteDBModel(int streamID, String categoryID, String type) {
        this.streamID = streamID;
        this.categoryID = categoryID;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStreamID() {
        return this.streamID;
    }

    public String getType() {
        return this.type;
    }

    public void setStreamID(int streamID) {
        this.streamID = streamID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryID() {
        return this.categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
