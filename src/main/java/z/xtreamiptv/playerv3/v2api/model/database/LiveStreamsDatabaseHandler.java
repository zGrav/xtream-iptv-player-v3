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
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetLiveStreamCategoriesCallback;
import java.util.ArrayList;

public class LiveStreamsDatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "live_streams_v2.db";
    private static final int DATABASE_VERSION = 2;
    private static final String KEY_ADDED = "added";
    private static final String KEY_AVAIL_CHANNEL_CATEGORY_NAME = "category_name";
    private static final String KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION = "container_extension";
    private static final String KEY_AVAIL_CHANNEL_LIVE = "live";
    private static final String KEY_AVAIL_CHANNEL_SERIES_NO = "series_no";
    private static final String KEY_AVAIL_CHANNEL_TYPE_NAME = "type_name";
    private static final String KEY_CATEGORY_ID_LIVE = "categoryID_live";
    private static final String KEY_CATEGORY_ID_LIVE_STREAMS = "categoryID";
    private static final String KEY_CATEGORY_NAME_LIVE = "categoryname_live";
    private static final String KEY_CUSTOMER_SID = "custom_sid";
    private static final String KEY_DB_LIVE_STREAMS_CATEGORY = "live_streams_status_category";
    private static final String KEY_DB_LIVE_STREAMS_CATEGORY_ID = "live_streams_status_category_id";
    private static final String KEY_DB_LIVE_STREAMS_CAT_CATEGORY = "live_streams_cat_status_category";
    private static final String KEY_DB_LIVE_STREAMS_CAT_CATEGORY_ID = "live_streams_cat_status_category_id";
    private static final String KEY_DB_LIVE_STREAMS_CAT_LAST_UPDATED_DATE = "live_streams_cat_last_updated_date";
    private static final String KEY_DB_LIVE_STREAMS_CAT_STATUS_STATE = "live_streams_cat_status_state";
    private static final String KEY_DB_LIVE_STREAMS_LAST_UPDATED_DATE = "live_streams_last_updated_date";
    private static final String KEY_DB_LIVE_STREAMS_STATUS_STATE = "live_streams_status_state";
    private static final String KEY_DIRECT_SOURCE = "direct_source";
    private static final String KEY_EPG_CHANNEL_ID = "epg_channel_id";
    private static final String KEY_ID_LIVE = "id_live";
    private static final String KEY_ID_LIVE_STREAMS = "id";
    private static final String KEY_ID_LIVE_STREAMS_CAT_STATUS = "live_streams_cat_status_id";
    private static final String KEY_ID_LIVE_STREAMS_STATUS = "live_streams_status_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUM_LIVE_STREAMS = "num";
    private static final String KEY_STREAM_ICON = "stream_icon";
    private static final String KEY_STREAM_ID = "stream_id";
    private static final String KEY_STRESM_TYPE = "stream_type";
    private static final String KEY_TV_ARCHIVE = "tv_archive";
    private static final String KEY_TV_ARCHIVE_DURATION = "tv_archive_duration";
    private static final String TABLE_IPTV_LIVE_CATEGORY = "live_streams_category";
    private static final String TABLE_IPTV_LIVE_STREAMS = "live_streams";
    private static final String TABLE_LIVE_STREAM_CAT_STATUS = "live_streams_cat_status";
    private static final String TABLE_LIVE_STREAM_STATUS = "live_streams_status";
    String CREATE_LIVE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS live_streams_category(id_live INTEGER PRIMARY KEY,categoryID_live TEXT,categoryname_live TEXT)";
    String CREATE_LIVE_STREAMS = "CREATE TABLE IF NOT EXISTS live_streams(id INTEGER PRIMARY KEY,num TEXT,name TEXT,stream_type TEXT,stream_id TEXT,stream_icon TEXT,epg_channel_id TEXT,added TEXT,categoryID TEXT,custom_sid TEXT,tv_archive TEXT,direct_source TEXT,tv_archive_duration TEXT,type_name TEXT,category_name TEXT,series_no TEXT,live TEXT,container_extension TEXT)";
    String CREATE_LIVE_STREAM_CAT_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS live_streams_cat_status(live_streams_cat_status_id INTEGER PRIMARY KEY,live_streams_cat_status_state TEXT,live_streams_cat_last_updated_date TEXT,live_streams_cat_status_category TEXT,live_streams_cat_status_category_id TEXT)";
    String CREATE_LIVE_STREAM_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS live_streams_status(live_streams_status_id INTEGER PRIMARY KEY,live_streams_status_state TEXT,live_streams_last_updated_date TEXT,live_streams_status_category TEXT,live_streams_status_category_id TEXT)";
    Context context;
    SQLiteDatabase db;

    public LiveStreamsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CREATE_LIVE_CATEGORY_TABLE);
        db.execSQL(this.CREATE_LIVE_STREAMS);
        db.execSQL(this.CREATE_LIVE_STREAM_CAT_STATUS_TABLE);
        db.execSQL(this.CREATE_LIVE_STREAM_STATUS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS live_streams_category");
        db.execSQL("DROP TABLE IF EXISTS live_streams");
        db.execSQL("DROP TABLE IF EXISTS live_streams_cat_status");
        db.execSQL("DROP TABLE IF EXISTS live_streams_status");
        onCreate(db);
    }

    public void addLiveCategories(ArrayList<GetLiveStreamCategoriesCallback> liveStreamCategoryIdDBModel) {
        try {
            SQLiteDatabase addLiveCategories = getWritableDatabase();
            addLiveCategories.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = liveStreamCategoryIdDBModel.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    values.put(KEY_CATEGORY_ID_LIVE, ((GetLiveStreamCategoriesCallback) liveStreamCategoryIdDBModel.get(i)).getCategoryId());
                    values.put(KEY_CATEGORY_NAME_LIVE, ((GetLiveStreamCategoriesCallback) liveStreamCategoryIdDBModel.get(i)).getCategoryName());
                    addLiveCategories.insert(TABLE_IPTV_LIVE_CATEGORY, null, values);
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllliveCategories() {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM live_streams_category", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
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

    public void addAllliveStreams(ArrayList<GetLiveStreamCallback> availableChanelsList) {
        try {
            SQLiteDatabase addChannel = getWritableDatabase();
            addChannel.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = availableChanelsList.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getNum() != null) {
                        values.put(KEY_NUM_LIVE_STREAMS, String.valueOf(((GetLiveStreamCallback) availableChanelsList.get(i)).getNum()));
                    } else {
                        values.put(KEY_NUM_LIVE_STREAMS, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getName() != null) {
                        values.put(KEY_NAME, ((GetLiveStreamCallback) availableChanelsList.get(i)).getName());
                    } else {
                        values.put(KEY_NAME, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getStreamType() != null) {
                        values.put(KEY_STRESM_TYPE, ((GetLiveStreamCallback) availableChanelsList.get(i)).getStreamType());
                    } else {
                        values.put(KEY_STRESM_TYPE, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getStreamId() != null) {
                        values.put(KEY_STREAM_ID, ((GetLiveStreamCallback) availableChanelsList.get(i)).getStreamId());
                    } else {
                        values.put(KEY_STREAM_ID, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getStreamIcon() != null) {
                        values.put(KEY_STREAM_ICON, ((GetLiveStreamCallback) availableChanelsList.get(i)).getStreamIcon());
                    } else {
                        values.put(KEY_STREAM_ICON, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getEpgChannelId() != null) {
                        values.put(KEY_EPG_CHANNEL_ID, String.valueOf(((GetLiveStreamCallback) availableChanelsList.get(i)).getEpgChannelId()));
                    } else {
                        values.put(KEY_EPG_CHANNEL_ID, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getAdded() != null) {
                        values.put(KEY_ADDED, ((GetLiveStreamCallback) availableChanelsList.get(i)).getAdded());
                    } else {
                        values.put(KEY_ADDED, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getCategoryId() != null) {
                        values.put(KEY_CATEGORY_ID_LIVE_STREAMS, ((GetLiveStreamCallback) availableChanelsList.get(i)).getCategoryId());
                    } else {
                        values.put(KEY_CATEGORY_ID_LIVE_STREAMS, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getCustomSid() != null) {
                        values.put(KEY_CUSTOMER_SID, String.valueOf(((GetLiveStreamCallback) availableChanelsList.get(i)).getCustomSid()));
                    } else {
                        values.put(KEY_CUSTOMER_SID, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getTvArchive() != null) {
                        values.put(KEY_TV_ARCHIVE, ((GetLiveStreamCallback) availableChanelsList.get(i)).getTvArchive());
                    } else {
                        values.put(KEY_TV_ARCHIVE, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getDirectSource() != null) {
                        values.put(KEY_DIRECT_SOURCE, ((GetLiveStreamCallback) availableChanelsList.get(i)).getDirectSource());
                    } else {
                        values.put(KEY_DIRECT_SOURCE, "");
                    }
                    if (((GetLiveStreamCallback) availableChanelsList.get(i)).getTvArchiveDuration() != null) {
                        values.put(KEY_TV_ARCHIVE_DURATION, ((GetLiveStreamCallback) availableChanelsList.get(i)).getTvArchiveDuration());
                    } else {
                        values.put(KEY_TV_ARCHIVE_DURATION, "");
                    }
                    addChannel.insert(TABLE_IPTV_LIVE_STREAMS, null, values);
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

    public int getAvailableChannelsCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM live_streams", null);
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

    public ArrayList<LiveStreamsDBModel> getAllLiveStreasWithCategoryId(String cateogryId) {
        String sort = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT, 0).getString(AppConst.LOGIN_PREF_SORT, "");
        ArrayList<LiveStreamsDBModel> liveStreamsList;
        String selectQuery;
        Cursor cursor;
        LiveStreamsDBModel liveStreamsDBModel;
        if (cateogryId.equals(AppConst.PASSWORD_UNSET)) {
            liveStreamsList = new ArrayList();
            if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM live_streams";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM live_streams ORDER BY added DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM live_streams ORDER BY name DESC";
            } else {
                selectQuery = "SELECT  * FROM live_streams ORDER BY name ASC";
            }
            try {
                cursor = getReadableDatabase().rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
                        liveStreamsDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                        liveStreamsDBModel.setNum(cursor.getString(1));
                        liveStreamsDBModel.setName(cursor.getString(2));
                        liveStreamsDBModel.setStreamType(cursor.getString(3));
                        liveStreamsDBModel.setStreamId(cursor.getString(4));
                        liveStreamsDBModel.setStreamIcon(cursor.getString(5));
                        liveStreamsDBModel.setEpgChannelId(cursor.getString(6));
                        liveStreamsDBModel.setAdded(cursor.getString(7));
                        liveStreamsDBModel.setCategoryId(cursor.getString(8));
                        liveStreamsDBModel.setCustomSid(cursor.getString(9));
                        liveStreamsDBModel.setTvArchive(cursor.getString(10));
                        liveStreamsDBModel.setDirectSource(cursor.getString(11));
                        liveStreamsDBModel.setTvArchiveDuration(cursor.getString(12));
                        liveStreamsDBModel.setTypeName(cursor.getString(13));
                        liveStreamsDBModel.setCategoryName(cursor.getString(14));
                        liveStreamsDBModel.setSeriesNo(cursor.getString(15));
                        liveStreamsDBModel.setLive(cursor.getString(16));
                        liveStreamsDBModel.setContaiinerExtension(cursor.getString(17));
                        liveStreamsList.add(liveStreamsDBModel);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return liveStreamsList;
            } catch (SQLiteDatabaseLockedException e) {
                return null;
            } catch (SQLiteException e2) {
                return null;
            }
        } else if (cateogryId.equals("null")) {
            liveStreamsList = new ArrayList();
            if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "'";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + KEY_ADDED + " DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + KEY_NAME + " DESC";
            } else {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + KEY_NAME + " ASC";
            }
            try {
                cursor = getReadableDatabase().rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);                        liveStreamsDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                        liveStreamsDBModel.setNum(cursor.getString(1));
                        liveStreamsDBModel.setName(cursor.getString(2));
                        liveStreamsDBModel.setStreamType(cursor.getString(3));
                        liveStreamsDBModel.setStreamId(cursor.getString(4));
                        liveStreamsDBModel.setStreamIcon(cursor.getString(5));
                        liveStreamsDBModel.setEpgChannelId(cursor.getString(6));
                        liveStreamsDBModel.setAdded(cursor.getString(7));
                        liveStreamsDBModel.setCategoryId(cursor.getString(8));
                        liveStreamsDBModel.setCustomSid(cursor.getString(9));
                        liveStreamsDBModel.setTvArchive(cursor.getString(10));
                        liveStreamsDBModel.setDirectSource(cursor.getString(11));
                        liveStreamsDBModel.setTvArchiveDuration(cursor.getString(12));
                        liveStreamsDBModel.setTypeName(cursor.getString(13));
                        liveStreamsDBModel.setCategoryName(cursor.getString(14));
                        liveStreamsDBModel.setSeriesNo(cursor.getString(15));
                        liveStreamsDBModel.setLive(cursor.getString(16));
                        liveStreamsDBModel.setContaiinerExtension(cursor.getString(17));
                        liveStreamsList.add(liveStreamsDBModel);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return liveStreamsList;
            } catch (SQLiteDatabaseLockedException e3) {
                return null;
            } catch (SQLiteException e4) {
                return null;
            }
        } else {
            liveStreamsList = new ArrayList();
            if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "'";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + KEY_ADDED + " DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + KEY_NAME + " DESC";
            } else {
                selectQuery = "SELECT  * FROM live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + KEY_NAME + " ASC";
            }
            try {
                cursor = getReadableDatabase().rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
                        liveStreamsDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                        liveStreamsDBModel.setNum(cursor.getString(1));
                        liveStreamsDBModel.setName(cursor.getString(2));
                        liveStreamsDBModel.setStreamType(cursor.getString(3));
                        liveStreamsDBModel.setStreamId(cursor.getString(4));
                        liveStreamsDBModel.setStreamIcon(cursor.getString(5));
                        liveStreamsDBModel.setEpgChannelId(cursor.getString(6));
                        liveStreamsDBModel.setAdded(cursor.getString(7));
                        liveStreamsDBModel.setCategoryId(cursor.getString(8));
                        liveStreamsDBModel.setCustomSid(cursor.getString(9));
                        liveStreamsDBModel.setTvArchive(cursor.getString(10));
                        liveStreamsDBModel.setDirectSource(cursor.getString(11));
                        liveStreamsDBModel.setTvArchiveDuration(cursor.getString(12));
                        liveStreamsDBModel.setTypeName(cursor.getString(13));
                        liveStreamsDBModel.setCategoryName(cursor.getString(14));
                        liveStreamsDBModel.setSeriesNo(cursor.getString(15));
                        liveStreamsDBModel.setLive(cursor.getString(16));
                        liveStreamsDBModel.setContaiinerExtension(cursor.getString(17));
                        liveStreamsList.add(liveStreamsDBModel);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return liveStreamsList;
            } catch (SQLiteDatabaseLockedException e5) {
                return null;
            } catch (SQLiteException e6) {
                return null;
            }
        }
    }

    public int getLiveStreamsCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM live_streams", null);
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

    public void addLiveStreamsCatStatus(DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModel) {
        try {
            SQLiteDatabase addDBUpdatedStatusDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DB_LIVE_STREAMS_CAT_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_LIVE_STREAMS_CAT_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_LIVE_STREAMS_CAT_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_LIVE_STREAMS_CAT_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_LIVE_STREAM_CAT_STATUS, null, values);
            addDBUpdatedStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void addLiveStreamsStatus(DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModel) {
        try {
            SQLiteDatabase addDBUpdatedStatusDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DB_LIVE_STREAMS_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_LIVE_STREAMS_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_LIVE_STREAMS_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_LIVE_STREAMS_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_LIVE_STREAM_STATUS, null, values);
            addDBUpdatedStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public int getLiveStreamsCatDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM live_streams_cat_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public int getLiveStreamsDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM live_streams_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public DatabaseUpdatedStatusDBModel getdateLiveStreamsCatDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM live_streams_cat_status WHERE live_streams_cat_status_category = '" + category + "' AND " + KEY_DB_LIVE_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'", null);
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

    public DatabaseUpdatedStatusDBModel getdateLiveStreamsDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM live_streams_status WHERE live_streams_status_category = '" + category + "' AND " + KEY_DB_LIVE_STREAMS_CATEGORY_ID + " = '" + categoryid + "'", null);
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

    public boolean updateLiveStreamsCatDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM live_streams_cat_status WHERE live_streams_cat_status_category = '" + category + "' AND " + KEY_DB_LIVE_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_LIVE_STREAMS_CAT_STATUS))));
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
            cv.put(KEY_DB_LIVE_STREAMS_CAT_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_LIVE_STREAM_CAT_STATUS, cv, "live_streams_cat_status_id= ?", new String[]{rowId});
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

    public boolean updateLiveStreamsDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM live_streams_status WHERE live_streams_status_category = '" + category + "' AND " + KEY_DB_LIVE_STREAMS_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_LIVE_STREAMS_STATUS))));
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
            cv.put(KEY_DB_LIVE_STREAMS_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_LIVE_STREAM_STATUS, cv, "live_streams_status_id= ?", new String[]{rowId});
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

    public boolean updateLiveStreamsCatDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM live_streams_cat_status WHERE live_streams_cat_status_category = '" + category + "' AND " + KEY_DB_LIVE_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_LIVE_STREAMS_CAT_STATUS))));
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
            cv.put(KEY_DB_LIVE_STREAMS_CAT_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_LIVE_STREAMS_CAT_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_LIVE_STREAM_CAT_STATUS, cv, "live_streams_cat_status_id= ?", new String[]{rowId});
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

    public boolean updateLiveStreamsDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM live_streams_status WHERE live_streams_status_category = '" + category + "' AND " + KEY_DB_LIVE_STREAMS_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_LIVE_STREAMS_STATUS))));
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
            cv.put(KEY_DB_LIVE_STREAMS_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_LIVE_STREAMS_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_LIVE_STREAM_STATUS, cv, "live_streams_status_id= ?", new String[]{rowId});
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

    public void emptyLiveStreamCatRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from live_streams_category");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void emptyLiveStreamRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from live_streams");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void emptyLiveStreamCatandLiveStreamRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from live_streams_category");
            readableDatabase.execSQL("delete from live_streams");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public LiveStreamsDBModel getLiveStreamFavouriteRow(String cateogryId, String streamId) {
        ArrayList<LiveStreamsDBModel> liveStreamsList = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM live_streams WHERE categoryID='" + cateogryId + "' AND " + KEY_STREAM_ID + "='" + streamId + "'", null);
            LiveStreamsDBModel liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    liveStreamsDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                    liveStreamsDBModel.setNum(cursor.getString(1));
                    liveStreamsDBModel.setName(cursor.getString(2));
                    liveStreamsDBModel.setStreamType(cursor.getString(3));
                    liveStreamsDBModel.setStreamId(cursor.getString(4));
                    liveStreamsDBModel.setStreamIcon(cursor.getString(5));
                    liveStreamsDBModel.setEpgChannelId(cursor.getString(6));
                    liveStreamsDBModel.setAdded(cursor.getString(7));
                    liveStreamsDBModel.setCategoryId(cursor.getString(8));
                    liveStreamsDBModel.setCustomSid(cursor.getString(9));
                    liveStreamsDBModel.setTvArchive(cursor.getString(10));
                    liveStreamsDBModel.setDirectSource(cursor.getString(11));
                    liveStreamsDBModel.setTvArchiveDuration(cursor.getString(12));
                    liveStreamsDBModel.setTypeName(cursor.getString(13));
                    liveStreamsDBModel.setCategoryName(cursor.getString(14));
                    liveStreamsDBModel.setSeriesNo(cursor.getString(15));
                    liveStreamsDBModel.setLive(cursor.getString(16));
                    liveStreamsDBModel.setContaiinerExtension(cursor.getString(17));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return liveStreamsDBModel;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public int getLiveStreamCountWithCat(String categoryID) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM live_streams WHERE categoryID='" + categoryID + "'", null);
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

    public ArrayList<LiveStreamsDBModel> getAllLiveStreamsArchive(String cateogryId) {
        String sort = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT, 0).getString(AppConst.LOGIN_PREF_SORT_TV_ARCHIVE, "");
        ArrayList<LiveStreamsDBModel> liveStreamsList;
        Cursor cursor;
        if (cateogryId.equals(AppConst.PASSWORD_UNSET)) {
            String selectQuery;
            liveStreamsList = new ArrayList();
            if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ORDER BY added DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ORDER BY name DESC";
            } else {
                selectQuery = "SELECT  * FROM live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ORDER BY name ASC";
            }
            try {
                cursor = getReadableDatabase().rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        LiveStreamsDBModel liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
                        liveStreamsDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                        liveStreamsDBModel.setNum(cursor.getString(1));
                        liveStreamsDBModel.setName(cursor.getString(2));
                        liveStreamsDBModel.setStreamType(cursor.getString(3));
                        liveStreamsDBModel.setStreamId(cursor.getString(4));
                        liveStreamsDBModel.setStreamIcon(cursor.getString(5));
                        liveStreamsDBModel.setEpgChannelId(cursor.getString(6));
                        liveStreamsDBModel.setAdded(cursor.getString(7));
                        liveStreamsDBModel.setCategoryId(cursor.getString(8));
                        liveStreamsDBModel.setCustomSid(cursor.getString(9));
                        liveStreamsDBModel.setTvArchive(cursor.getString(10));
                        liveStreamsDBModel.setDirectSource(cursor.getString(11));
                        liveStreamsDBModel.setTvArchiveDuration(cursor.getString(12));
                        liveStreamsDBModel.setTypeName(cursor.getString(13));
                        liveStreamsDBModel.setCategoryName(cursor.getString(14));
                        liveStreamsDBModel.setSeriesNo(cursor.getString(15));
                        liveStreamsDBModel.setLive(cursor.getString(16));
                        liveStreamsDBModel.setContaiinerExtension(cursor.getString(17));
                        liveStreamsList.add(liveStreamsDBModel);
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
        liveStreamsList = new ArrayList();
        try {
            cursor = getReadableDatabase().rawQuery("SELECT  * FROM live_streams WHERE stream_type='live' AND tv_archive='1' AND categoryID ='" + cateogryId + "' AND " + KEY_EPG_CHANNEL_ID + " IS NOT NULL AND " + KEY_EPG_CHANNEL_ID + " !='' ORDER BY " + KEY_ADDED + " DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamsDBModel liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);
                    liveStreamsDBModel.setIdAuto(Integer.parseInt(cursor.getString(0)));
                    liveStreamsDBModel.setNum(cursor.getString(1));
                    liveStreamsDBModel.setName(cursor.getString(2));
                    liveStreamsDBModel.setStreamType(cursor.getString(3));
                    liveStreamsDBModel.setStreamId(cursor.getString(4));
                    liveStreamsDBModel.setStreamIcon(cursor.getString(5));
                    liveStreamsDBModel.setEpgChannelId(cursor.getString(6));
                    liveStreamsDBModel.setAdded(cursor.getString(7));
                    liveStreamsDBModel.setCategoryId(cursor.getString(8));
                    liveStreamsDBModel.setCustomSid(cursor.getString(9));
                    liveStreamsDBModel.setTvArchive(cursor.getString(10));
                    liveStreamsDBModel.setDirectSource(cursor.getString(11));
                    liveStreamsDBModel.setTvArchiveDuration(cursor.getString(12));
                    liveStreamsDBModel.setTypeName(cursor.getString(13));
                    liveStreamsDBModel.setCategoryName(cursor.getString(14));
                    liveStreamsDBModel.setSeriesNo(cursor.getString(15));
                    liveStreamsDBModel.setLive(cursor.getString(16));
                    liveStreamsDBModel.setContaiinerExtension(cursor.getString(17));
                    liveStreamsList.add(liveStreamsDBModel);
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

    public void deleteAndRecreateAllLiveTables() {
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
