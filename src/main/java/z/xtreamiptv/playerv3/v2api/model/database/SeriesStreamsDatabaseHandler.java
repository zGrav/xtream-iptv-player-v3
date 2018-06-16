package z.xtreamiptv.playerv3.v2api.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetSeriesStreamCategoriesCallback;
import z.xtreamiptv.playerv3.v2api.model.SeriesDBModel;
import java.util.ArrayList;

public class SeriesStreamsDatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "series_streams_v2.db";
    private static final int DATABASE_VERSION = 2;
    private static final String KEY_CAST = "cast_series_stream_v2";
    private static final String KEY_CATEGORY_ID = "category_id_series_stream_v2";
    private static final String KEY_CATEGORY_ID_SERIES = "category_id_series_v2";
    private static final String KEY_CATEGORY_NAME_SERIES = "category_name_series_v2";
    private static final String KEY_COVER = "stream_cover_series_stream_v2";
    private static final String KEY_DB_SERIES_STREAMS_CATEGORY = "series_streams_status_category";
    private static final String KEY_DB_SERIES_STREAMS_CATEGORY_ID = "series_streams_status_category_id";
    private static final String KEY_DB_SERIES_STREAMS_CAT_CATEGORY = "series_streams_cat_status_category";
    private static final String KEY_DB_SERIES_STREAMS_CAT_CATEGORY_ID = "series_streams_cat_status_category_id";
    private static final String KEY_DB_SERIES_STREAMS_CAT_LAST_UPDATED_DATE = "series_streams_cat_last_updated_date";
    private static final String KEY_DB_SERIES_STREAMS_CAT_STATUS_STATE = "series_streams_cat_status_state";
    private static final String KEY_DB_SERIES_STREAMS_LAST_UPDATED_DATE = "series_streams_last_updated_date";
    private static final String KEY_DB_SERIES_STREAMS_STATUS_STATE = "series_streams_status_state";
    private static final String KEY_DIRECTOR = "director_series_stream_v2";
    private static final String KEY_GENERE = "genre_series_stream_v2";
    private static final String KEY_ID_SERIES_STREAMS = "id_series_stream_v2";
    private static final String KEY_ID_SERIES_STREAMS_CAT_STATUS = "series_streams_cat_status_id";
    private static final String KEY_ID_SERIES_STREAMS_STATUS = "series_streams_status_id";
    private static final String KEY_ID_VOD = "id_series_v2";
    private static final String KEY_LAST_MODIFIED = "last_modified_series_stream_v2";
    private static final String KEY_NAME = "name_series_stream_v2";
    private static final String KEY_NUM_SERIES_STREAMS = "num_series_stream_v2";
    private static final String KEY_PLOT = "plot_series_stream_v2";
    private static final String KEY_RATING = "rating_series_stream_v2";
    private static final String KEY_RELEASE_DATE = "release_date_series_stream_v2";
    private static final String KEY_SERIES_ID = "stream_id_series_stream_v2";
    private static final String KEY_STREAM_TYPE = "stream_type_series_stream_v2";
    private static final String TABLE_IPTV_SERIES_CATEGORY = "series_category_v2";
    private static final String TABLE_IPTV_SERIES_STREAMS = "series_streams_v2";
    private static final String TABLE_SERIES_STREAM_CAT_STATUS = "series_streams_cat_status";
    private static final String TABLE_SERIES_STREAM_STATUS = "series_streams_status";
    String CREATE_SERIES_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS series_category_v2(id_series_v2 INTEGER PRIMARY KEY,category_name_series_v2 TEXT,category_id_series_v2 TEXT)";
    String CREATE_SERIES_STREAMS = "CREATE TABLE IF NOT EXISTS series_streams_v2(id_series_stream_v2 INTEGER PRIMARY KEY,num_series_stream_v2 TEXT,name_series_stream_v2 TEXT,stream_type_series_stream_v2 TEXT,stream_id_series_stream_v2 TEXT,stream_cover_series_stream_v2 TEXT,plot_series_stream_v2 TEXT,cast_series_stream_v2 TEXT,director_series_stream_v2 TEXT,genre_series_stream_v2 TEXT,release_date_series_stream_v2 TEXT,last_modified_series_stream_v2 TEXT,rating_series_stream_v2 TEXT,category_id_series_stream_v2 TEXT)";
    String CREATE_SERIES_STREAM_CAT_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS series_streams_cat_status(series_streams_cat_status_id INTEGER PRIMARY KEY,series_streams_cat_status_state TEXT,series_streams_cat_last_updated_date TEXT,series_streams_cat_status_category TEXT,series_streams_cat_status_category_id TEXT)";
    String CREATE_SERIES_STREAM_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS series_streams_status(series_streams_status_id INTEGER PRIMARY KEY,series_streams_status_state TEXT,series_streams_last_updated_date TEXT,series_streams_status_category TEXT,series_streams_status_category_id TEXT)";
    Context context;
    SQLiteDatabase db;

    public SeriesStreamsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(this.CREATE_SERIES_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(this.CREATE_SERIES_STREAMS);
        sqLiteDatabase.execSQL(this.CREATE_SERIES_STREAM_CAT_STATUS_TABLE);
        sqLiteDatabase.execSQL(this.CREATE_SERIES_STREAM_STATUS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS series_category_v2");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS series_streams_v2");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS series_streams_cat_status");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS series_streams_status");
        onCreate(sqLiteDatabase);
    }

    public void addSeriesCategories(ArrayList<GetSeriesStreamCategoriesCallback> getSeriesStreamCategoriesCallbacks) {
        try {
            SQLiteDatabase addLiveCategories = getWritableDatabase();
            addLiveCategories.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = getSeriesStreamCategoriesCallbacks.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    values.put(KEY_CATEGORY_ID_SERIES, ((GetSeriesStreamCategoriesCallback) getSeriesStreamCategoriesCallbacks.get(i)).getCategoryId());
                    values.put(KEY_CATEGORY_NAME_SERIES, ((GetSeriesStreamCategoriesCallback) getSeriesStreamCategoriesCallbacks.get(i)).getCategoryName());
                    addLiveCategories.insert(TABLE_IPTV_SERIES_CATEGORY, null, values);
                }
            }
            addLiveCategories.setTransactionSuccessful();
            addLiveCategories.endTransaction();
            addLiveCategories.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public ArrayList<LiveStreamCategoryIdDBModel> getAllSeriesCategories() {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM series_category_v2", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(2));
                    categoryListLive.add(movieStreamObj);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return categoryListLive;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public void addAllSeriesStreams(ArrayList<GetSeriesStreamCallback> availableChanelsList) {
        try {
            SQLiteDatabase addChannel = getWritableDatabase();
            addChannel.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = availableChanelsList.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getNum() != null) {
                        values.put(KEY_NUM_SERIES_STREAMS, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getNum()));
                    } else {
                        values.put(KEY_NUM_SERIES_STREAMS, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getName() != null) {
                        values.put(KEY_NAME, ((GetSeriesStreamCallback) availableChanelsList.get(i)).getName());
                    } else {
                        values.put(KEY_NAME, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getStreamType() != null) {
                        values.put(KEY_STREAM_TYPE, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getStreamType()));
                    } else {
                        values.put(KEY_STREAM_TYPE, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getSeriesId() != null) {
                        values.put(KEY_SERIES_ID, ((GetSeriesStreamCallback) availableChanelsList.get(i)).getSeriesId());
                    } else {
                        values.put(KEY_SERIES_ID, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getCover() != null) {
                        values.put(KEY_COVER, ((GetSeriesStreamCallback) availableChanelsList.get(i)).getCover());
                    } else {
                        values.put(KEY_COVER, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getPlot() != null) {
                        values.put(KEY_PLOT, ((GetSeriesStreamCallback) availableChanelsList.get(i)).getPlot());
                    } else {
                        values.put(KEY_PLOT, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getCast() != null) {
                        values.put(KEY_CAST, ((GetSeriesStreamCallback) availableChanelsList.get(i)).getCast());
                    } else {
                        values.put(KEY_CAST, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getDirector() != null) {
                        values.put(KEY_DIRECTOR, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getDirector()));
                    } else {
                        values.put(KEY_DIRECTOR, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getGenre() != null) {
                        values.put(KEY_GENERE, ((GetSeriesStreamCallback) availableChanelsList.get(i)).getGenre());
                    } else {
                        values.put(KEY_GENERE, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getReleaseDate() != null) {
                        values.put(KEY_RELEASE_DATE, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getReleaseDate()));
                    } else {
                        values.put(KEY_RELEASE_DATE, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getLastModified() != null) {
                        values.put(KEY_LAST_MODIFIED, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getLastModified()));
                    } else {
                        values.put(KEY_LAST_MODIFIED, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getRating() != null) {
                        values.put(KEY_RATING, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getRating()));
                    } else {
                        values.put(KEY_RATING, "");
                    }
                    if (((GetSeriesStreamCallback) availableChanelsList.get(i)).getCategoryId() != null) {
                        values.put(KEY_CATEGORY_ID, String.valueOf(((GetSeriesStreamCallback) availableChanelsList.get(i)).getCategoryId()));
                    } else {
                        values.put(KEY_CATEGORY_ID, "");
                    }
                    addChannel.insert(TABLE_IPTV_SERIES_STREAMS, null, values);
                }
            }
            addChannel.setTransactionSuccessful();
            addChannel.endTransaction();
            addChannel.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public ArrayList<SeriesDBModel> getAllSeriesStreamsWithCategoryId(String cateogryId) {
        String sort = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT_SERIES, 0).getString(AppConst.LOGIN_PREF_SORT, "");
        String selectQuery;
        ArrayList<SeriesDBModel> liveStreamsList;
        Cursor cursor;
        if (cateogryId.equals(null) || cateogryId.equals(AppConst.PASSWORD_UNSET)) {
            if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM series_streams_v2";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM series_streams_v2 ORDER BY name_series_stream_v2 DESC";
            } else {
                selectQuery = "SELECT  * FROM series_streams_v2 ORDER BY name_series_stream_v2 ASC";
            }
            liveStreamsList = new ArrayList();
            try {
                cursor = getReadableDatabase().rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        SeriesDBModel seriesDBModel = new SeriesDBModel(null, null, null, 0, null, null, null, null, null, null, null, null, null);
                        seriesDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                        seriesDBModel.setNum(cursor.getString(1));
                        seriesDBModel.setName(cursor.getString(2));
                        seriesDBModel.setStreamType(cursor.getString(3));
                        seriesDBModel.setseriesID(Integer.parseInt(cursor.getString(4)));
                        seriesDBModel.setcover(cursor.getString(5));
                        seriesDBModel.setplot(cursor.getString(6));
                        seriesDBModel.setcast(cursor.getString(7));
                        seriesDBModel.setdirector(cursor.getString(8));
                        seriesDBModel.setgenre(cursor.getString(9));
                        seriesDBModel.setreleaseDate(cursor.getString(10));
                        seriesDBModel.setlast_modified(cursor.getString(11));
                        seriesDBModel.setrating(cursor.getString(12));
                        seriesDBModel.setCategoryId(cursor.getString(13));
                        liveStreamsList.add(seriesDBModel);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return liveStreamsList;
            } catch (SQLiteDatabaseLockedException e) {
                return null;
            } catch (SQLiteException e2) {
                return null;
            }
        }
        if (sort.equals(AppConst.PASSWORD_UNSET)) {
            selectQuery = "SELECT * FROM series_streams_v2 WHERE category_id_series_stream_v2 ='" + cateogryId + "'";
        } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
            selectQuery = "SELECT * FROM series_streams_v2 WHERE category_id_series_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_NAME + " DESC";
        } else {
            selectQuery = "SELECT * FROM series_streams_v2 WHERE category_id_series_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_NAME + " ASC";
        }
        liveStreamsList = new ArrayList();
        try {
            cursor = getReadableDatabase().rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    SeriesDBModel seriesDBModel = new SeriesDBModel(null, null, null, 0, null, null, null, null, null, null, null, null, null);
                    seriesDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                    seriesDBModel.setNum(cursor.getString(1));
                    seriesDBModel.setName(cursor.getString(2));
                    seriesDBModel.setStreamType(cursor.getString(3));
                    seriesDBModel.setseriesID(Integer.parseInt(cursor.getString(4)));
                    seriesDBModel.setcover(cursor.getString(5));
                    seriesDBModel.setplot(cursor.getString(6));
                    seriesDBModel.setcast(cursor.getString(7));
                    seriesDBModel.setdirector(cursor.getString(8));
                    seriesDBModel.setgenre(cursor.getString(9));
                    seriesDBModel.setreleaseDate(cursor.getString(10));
                    seriesDBModel.setlast_modified(cursor.getString(11));
                    seriesDBModel.setrating(cursor.getString(12));
                    seriesDBModel.setCategoryId(cursor.getString(13));
                    liveStreamsList.add(seriesDBModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return liveStreamsList;
        } catch (SQLiteDatabaseLockedException e3) {
            return null;
        } catch (SQLiteException e4) {
            return null;
        }
    }

    public SeriesDBModel getSeriesStreamsWithSeriesId(String seriesId) {
        String selectQuery = "SELECT  * FROM series_streams_v2 WHERE stream_id_series_stream_v2 ='" + seriesId + "'";
        ArrayList<SeriesDBModel> liveStreamsList = new ArrayList();
        SeriesDBModel seriesDBModel = new SeriesDBModel(null, null, null, 0, null, null, null, null, null, null, null, null, null);
        try {
            Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    seriesDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                    seriesDBModel.setNum(cursor.getString(1));
                    seriesDBModel.setName(cursor.getString(2));
                    seriesDBModel.setStreamType(cursor.getString(3));
                    seriesDBModel.setseriesID(Integer.parseInt(cursor.getString(4)));
                    seriesDBModel.setcover(cursor.getString(5));
                    seriesDBModel.setplot(cursor.getString(6));
                    seriesDBModel.setcast(cursor.getString(7));
                    seriesDBModel.setdirector(cursor.getString(8));
                    seriesDBModel.setgenre(cursor.getString(9));
                    seriesDBModel.setreleaseDate(cursor.getString(10));
                    seriesDBModel.setlast_modified(cursor.getString(11));
                    seriesDBModel.setrating(cursor.getString(12));
                    seriesDBModel.setCategoryId(cursor.getString(13));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return seriesDBModel;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public int getAllSeriesStreamCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM series_streams_v2", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public void addSeriesStreamsCatStatus(DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModel) {
        try {
            SQLiteDatabase addDBUpdatedStatusDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DB_SERIES_STREAMS_CAT_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_SERIES_STREAMS_CAT_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_SERIES_STREAMS_CAT_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_SERIES_STREAMS_CAT_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_SERIES_STREAM_CAT_STATUS, null, values);
            addDBUpdatedStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void addSeriesStreamsStatus(DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModel) {
        try {
            SQLiteDatabase addDBUpdatedStatusDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DB_SERIES_STREAMS_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_SERIES_STREAMS_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_SERIES_STREAMS_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_SERIES_STREAMS_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_SERIES_STREAM_STATUS, null, values);
            addDBUpdatedStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public int getSeriesStreamsCatDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM series_streams_cat_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public int getSeriesStreamsDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM series_streams_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public DatabaseUpdatedStatusDBModel getdateSeriesStreamsCatDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM series_streams_cat_status WHERE series_streams_cat_status_category = '" + category + "' AND " + KEY_DB_SERIES_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'", null);
            DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);
            if (res.moveToFirst()) {
                do {
                    updatedStatusDBModel.setIdAuto(Integer.parseInt(res.getString(0)));
                    updatedStatusDBModel.setDbUpadatedStatusState(res.getString(1));
                    updatedStatusDBModel.setDbLastUpdatedDate(res.getString(2));
                    updatedStatusDBModel.setDbCategory(res.getString(3));
                    updatedStatusDBModel.setDbCategoryID(res.getString(4));
                } while (res.moveToNext());
            }
            res.close();
            return updatedStatusDBModel;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    public DatabaseUpdatedStatusDBModel getdateSeriesStreamsDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM series_streams_status WHERE series_streams_status_category = '" + category + "' AND " + KEY_DB_SERIES_STREAMS_CATEGORY_ID + " = '" + categoryid + "'", null);
            DatabaseUpdatedStatusDBModel updatedStatusDBModel = new DatabaseUpdatedStatusDBModel(null, null, null, null);            if (res.moveToFirst()) {
                do {
                    updatedStatusDBModel.setIdAuto(Integer.parseInt(res.getString(0)));
                    updatedStatusDBModel.setDbUpadatedStatusState(res.getString(1));
                    updatedStatusDBModel.setDbLastUpdatedDate(res.getString(2));
                    updatedStatusDBModel.setDbCategory(res.getString(3));
                    updatedStatusDBModel.setDbCategoryID(res.getString(4));
                } while (res.moveToNext());
            }
            res.close();
            return updatedStatusDBModel;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean updateSeriesStreamsCatDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM series_streams_cat_status WHERE series_streams_cat_status_category = '" + category + "' AND " + KEY_DB_SERIES_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_SERIES_STREAMS_CAT_STATUS))));
                    } while (res.moveToNext());
                }
            } else if (this.context != null) {
                Toast.makeText(this.context, "cursor is null", 1).show();
            }
            if (rowId.equals("")) {
                if (res != null) {
                    res.close();
                }
                return false;
            }
            ContentValues cv = new ContentValues();
            cv.put(KEY_DB_SERIES_STREAMS_CAT_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_SERIES_STREAM_CAT_STATUS, cv, "series_streams_cat_status_id= ?", new String[]{rowId});
            if (res == null) {
                return true;
            }
            res.close();
            return true;
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        }
    }

    public boolean updateSeriesStreamsDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM series_streams_status WHERE series_streams_status_category = '" + category + "' AND " + KEY_DB_SERIES_STREAMS_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_SERIES_STREAMS_STATUS))));
                    } while (res.moveToNext());
                }
            } else if (this.context != null) {
                Toast.makeText(this.context, "cursor is null", 1).show();
            }
            if (rowId.equals("")) {
                if (res != null) {
                    res.close();
                }
                return false;
            }
            ContentValues cv = new ContentValues();
            cv.put(KEY_DB_SERIES_STREAMS_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_SERIES_STREAM_STATUS, cv, "series_streams_status_id= ?", new String[]{rowId});
            if (res == null) {
                return true;
            }
            res.close();
            return true;
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        }
    }

    public boolean updateSeriesStreamsCatDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM series_streams_cat_status WHERE series_streams_cat_status_category = '" + category + "' AND " + KEY_DB_SERIES_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_SERIES_STREAMS_CAT_STATUS))));
                    } while (res.moveToNext());
                }
            } else if (this.context != null) {
                Toast.makeText(this.context, "cursor is null", 1).show();
            }
            if (rowId.equals("")) {
                if (res != null) {
                    res.close();
                }
                return false;
            }
            ContentValues cv = new ContentValues();
            cv.put(KEY_DB_SERIES_STREAMS_CAT_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_SERIES_STREAMS_CAT_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_SERIES_STREAM_CAT_STATUS, cv, "series_streams_cat_status_id= ?", new String[]{rowId});
            if (res != null) {
                res.close();
            }
            return true;
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        }
    }

    public boolean updateseriesStreamsDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM series_streams_status WHERE series_streams_status_category = '" + category + "' AND " + KEY_DB_SERIES_STREAMS_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_SERIES_STREAMS_STATUS))));
                    } while (res.moveToNext());
                }
            } else if (this.context != null) {
                Toast.makeText(this.context, "cursor is null", 1).show();
            }
            if (rowId.equals("")) {
                if (res != null) {
                    res.close();
                }
                return false;
            }
            ContentValues cv = new ContentValues();
            cv.put(KEY_DB_SERIES_STREAMS_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_SERIES_STREAMS_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_SERIES_STREAM_STATUS, cv, "series_streams_status_id= ?", new String[]{rowId});
            if (res != null) {
                res.close();
            }
            return true;
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
            return false;
        }
    }

    public void emptySeriesStreamCatRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from series_category_v2");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void emptySeriesStreamRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from series_streams_v2");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void emptySeriesStreamCatandSeriesStreamRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from series_category_v2");
            readableDatabase.execSQL("delete from series_streams_v2");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void deleteAndRecreateAllVSeriesTables() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            onUpgrade(readableDatabase, 0, 0);
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }
}
