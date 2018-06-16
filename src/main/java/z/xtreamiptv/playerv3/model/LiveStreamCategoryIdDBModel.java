package z.xtreamiptv.playerv3.model;

import android.content.Context;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LiveStreamCategoryIdDBModel implements Serializable {
    private int id;
    private String liveStreamCategoryID;
    private String liveStreamCategoryName;
    private int parentId;

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public LiveStreamCategoryIdDBModel(String liveStreamCategoryID, String liveStreamCategoryName, int parentId) {
        this.liveStreamCategoryID = liveStreamCategoryID;
        this.liveStreamCategoryName = liveStreamCategoryName;
        this.parentId = parentId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLiveStreamCategoryID() {
        return this.liveStreamCategoryID;
    }

    public void setLiveStreamCategoryID(String liveStreamCategoryID) {
        this.liveStreamCategoryID = liveStreamCategoryID;
    }

    public String getLiveStreamCategoryName() {
        return this.liveStreamCategoryName;
    }

    public void setLiveStreamCategoryName(String liveStreamCategoryName) {
        this.liveStreamCategoryName = liveStreamCategoryName;
    }

    public static List<LiveStreamCategoryIdDBModel> createMovies(int itemCount, Context context, LiveStreamDBHandler liveStreamDBHandler, int startValue, int endValue) {
        return listOfMOview(liveStreamDBHandler.getAllMovieCategoriesHavingParentIdZero(startValue, endValue), context, liveStreamDBHandler);
    }

    public static ArrayList<LiveStreamCategoryIdDBModel> createChannels(int itemCount, Context context, LiveStreamDBHandler liveStreamDBHandler, int startValue, int endValue) {
        return listOfChannels(liveStreamDBHandler.getLiveCategoriesinRange(startValue, endValue), context, liveStreamDBHandler);
    }

    public static ArrayList<LiveStreamCategoryIdDBModel> listOfMOview(ArrayList<LiveStreamCategoryIdDBModel> movieList, Context context, LiveStreamDBHandler liveStreamDBHandler) {
        if (context == null) {
            return null;
        }
        liveStreamDBHandler = new LiveStreamDBHandler(context);
        ArrayList<LiveStreamCategoryIdDBModel> moviesCategory = movieList;
        ArrayList<LiveStreamCategoryIdDBModel> arrayList = new ArrayList();
        Iterator it = moviesCategory.iterator();
        while (it.hasNext()) {
            LiveStreamCategoryIdDBModel list = (LiveStreamCategoryIdDBModel) it.next();
            String categoryID = list.getLiveStreamCategoryID();
            ArrayList<LiveStreamsDBModel> listChannels = liveStreamDBHandler.getAllLiveStreasWithCategoryId(categoryID, "movie");
            Iterator it2 = liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(categoryID).iterator();
            while (it2.hasNext()) {
                if (liveStreamDBHandler.getAllLiveStreasWithCategoryId(String.valueOf(((LiveStreamCategoryIdDBModel) it2.next()).getLiveStreamCategoryID()), "movie").size() > 0) {
                    arrayList.add(list);
                    break;
                }
            }
            if (listChannels.size() > 0) {
                arrayList.add(list);
            }
        }
        return arrayList;
    }

    public static ArrayList<LiveStreamCategoryIdDBModel> listOfChannels(ArrayList<LiveStreamCategoryIdDBModel> movieList, Context context, LiveStreamDBHandler liveStreamDBHandler) {
        if (context == null) {
            return null;
        }
        liveStreamDBHandler = new LiveStreamDBHandler(context);
        ArrayList<LiveStreamCategoryIdDBModel> moviesCategory = movieList;
        ArrayList<LiveStreamCategoryIdDBModel> arrayList = new ArrayList();
        Iterator it = moviesCategory.iterator();
        while (it.hasNext()) {
            LiveStreamCategoryIdDBModel list = (LiveStreamCategoryIdDBModel) it.next();
            String categoryID = list.getLiveStreamCategoryID();
            ArrayList<LiveStreamsDBModel> listChannels = liveStreamDBHandler.getAllLiveStreasWithCategoryId(categoryID, "live");
            Iterator it2 = liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(categoryID).iterator();
            while (it2.hasNext()) {
                if (liveStreamDBHandler.getAllLiveStreasWithCategoryId(String.valueOf(((LiveStreamCategoryIdDBModel) it2.next()).getLiveStreamCategoryID()), "live").size() > 0) {
                    arrayList.add(list);
                    break;
                }
            }
            if (listChannels.size() > 0) {
                arrayList.add(list);
            }
        }
        return arrayList;
    }
}
