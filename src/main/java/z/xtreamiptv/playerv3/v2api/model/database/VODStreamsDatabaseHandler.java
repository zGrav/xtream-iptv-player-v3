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
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCallback;
import z.xtreamiptv.playerv3.v2api.model.GetVODStreamCategoriesCallback;
import java.util.ArrayList;

public class VODStreamsDatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vod_streams_v2.db";
    private static final int DATABASE_VERSION = 2;
    private static final String KEY_ADDED = "added_vod_stream_v2";
    private static final String KEY_AVAIL_CHANNEL_CATEGORY_NAME = "category_name_vod_stream_v2";
    private static final String KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION = "container_extension_vod_stream_v2";
    private static final String KEY_AVAIL_CHANNEL_LIVE = "live_vod_stream_v2";
    private static final String KEY_AVAIL_CHANNEL_SERIES_NO = "series_no_vod_stream_v2";
    private static final String KEY_AVAIL_CHANNEL_TYPE_NAME = "type_name_vod_stream_v2";
    private static final String KEY_CATEGORY_ID_LIVE_STREAMS = "categoryID_vod_stream_v2";
    private static final String KEY_CATEGORY_ID_VOD = "category_id_movie_v2";
    private static final String KEY_CATEGORY_NAME_VOD = "category_name_movie_v2";
    private static final String KEY_CUSTOMER_SID = "custom_sid_vod_stream_v2";
    private static final String KEY_DB_VOD_STREAMS_CATEGORY = "vod_streams_status_category";
    private static final String KEY_DB_VOD_STREAMS_CATEGORY_ID = "vod_streams_status_category_id";
    private static final String KEY_DB_VOD_STREAMS_CAT_CATEGORY = "vod_streams_cat_status_category";
    private static final String KEY_DB_VOD_STREAMS_CAT_CATEGORY_ID = "vod_streams_cat_status_category_id";
    private static final String KEY_DB_VOD_STREAMS_CAT_LAST_UPDATED_DATE = "vod_streams_cat_last_updated_date";
    private static final String KEY_DB_VOD_STREAMS_CAT_STATUS_STATE = "vod_streams_cat_status_state";
    private static final String KEY_DB_VOD_STREAMS_LAST_UPDATED_DATE = "vod_streams_last_updated_date";
    private static final String KEY_DB_VOD_STREAMS_STATUS_STATE = "vod_streams_status_state";
    private static final String KEY_DIRECT_SOURCE = "direct_source_vod_stream_v2";
    private static final String KEY_EPG_CHANNEL_ID = "epg_channel_id_vod_stream_v2";
    private static final String KEY_ID_LIVE_STREAMS = "id_vod_stream_v2";
    private static final String KEY_ID_VOD = "id_movie_v2";
    private static final String KEY_ID_VOD_STREAMS_CAT_STATUS = "vod_streams_cat_status_id";
    private static final String KEY_ID_VOD_STREAMS_STATUS = "vod_streams_status_id";
    private static final String KEY_NAME = "name_vod_stream_v2";
    private static final String KEY_NUM_LIVE_STREAMS = "num_vod_stream_v2";
    private static final String KEY_STREAM_ICON = "stream_icon_vod_stream_v2";
    private static final String KEY_STREAM_ID = "stream_id_vod_stream_v2";
    private static final String KEY_STRESM_TYPE = "stream_type_vod_stream_v2";
    private static final String KEY_TV_ARCHIVE = "tv_archive_vod_stream_v2";
    private static final String KEY_TV_ARCHIVE_DURATION = "tv_archive_duration_vod_stream_v2";
    private static final String TABLE_IPTV_VOD_CATEGORY = "vod_category_v2";
    private static final String TABLE_IPTV_VOD_STREAMS = "vod_streams_v2";
    private static final String TABLE_VOD_STREAM_CAT_STATUS = "vod_streams_cat_status";
    private static final String TABLE_VOD_STREAM_STATUS = "vod_streams_status";
    String CREATE_LIVE_STREAM_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS vod_streams_status(vod_streams_status_id INTEGER PRIMARY KEY,vod_streams_status_state TEXT,vod_streams_last_updated_date TEXT,vod_streams_status_category TEXT,vod_streams_status_category_id TEXT)";
    String CREATE_VOD_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS vod_category_v2(id_movie_v2 INTEGER PRIMARY KEY,category_id_movie_v2 TEXT,category_name_movie_v2 TEXT)";
    String CREATE_VOD_STREAMS = "CREATE TABLE IF NOT EXISTS vod_streams_v2(id_vod_stream_v2 INTEGER PRIMARY KEY,num_vod_stream_v2 TEXT,name_vod_stream_v2 TEXT,stream_type_vod_stream_v2 TEXT,stream_id_vod_stream_v2 TEXT,stream_icon_vod_stream_v2 TEXT,epg_channel_id_vod_stream_v2 TEXT,added_vod_stream_v2 TEXT,categoryID_vod_stream_v2 TEXT,custom_sid_vod_stream_v2 TEXT,tv_archive_vod_stream_v2 TEXT,direct_source_vod_stream_v2 TEXT,tv_archive_duration_vod_stream_v2 TEXT,type_name_vod_stream_v2 TEXT,category_name_vod_stream_v2 TEXT,series_no_vod_stream_v2 TEXT,live_vod_stream_v2 TEXT,container_extension_vod_stream_v2 TEXT)";
    String CREATE_VOD_STREAM_CAT_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS vod_streams_cat_status(vod_streams_cat_status_id INTEGER PRIMARY KEY,vod_streams_cat_status_state TEXT,vod_streams_cat_last_updated_date TEXT,vod_streams_cat_status_category TEXT,vod_streams_cat_status_category_id TEXT)";
    Context context;
    SQLiteDatabase db;

    public VODStreamsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(this.CREATE_VOD_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(this.CREATE_VOD_STREAMS);
        sqLiteDatabase.execSQL(this.CREATE_VOD_STREAM_CAT_STATUS_TABLE);
        sqLiteDatabase.execSQL(this.CREATE_LIVE_STREAM_STATUS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS vod_category_v2");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS vod_streams_v2");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS vod_streams_cat_status");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS vod_streams_status");
        onCreate(sqLiteDatabase);
    }

    public void addVODCategories(ArrayList<GetVODStreamCategoriesCallback> liveStreamCategoryIdDBModel) {
        try {
            SQLiteDatabase addLiveCategories = getWritableDatabase();
            addLiveCategories.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = liveStreamCategoryIdDBModel.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    values.put(KEY_CATEGORY_ID_VOD, ((GetVODStreamCategoriesCallback) liveStreamCategoryIdDBModel.get(i)).getCategoryId());
                    values.put(KEY_CATEGORY_NAME_VOD, ((GetVODStreamCategoriesCallback) liveStreamCategoryIdDBModel.get(i)).getCategoryName());
                    addLiveCategories.insert(TABLE_IPTV_VOD_CATEGORY, null, values);
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllVODCategories() {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM vod_category_v2", null);
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

    public void addAllVODStreams(ArrayList<GetVODStreamCallback> availableChanelsList) {
        try {
            SQLiteDatabase addChannel = getWritableDatabase();
            addChannel.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = availableChanelsList.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getNum() != null) {
                        values.put(KEY_NUM_LIVE_STREAMS, String.valueOf(((GetVODStreamCallback) availableChanelsList.get(i)).getNum()));
                    } else {
                        values.put(KEY_NUM_LIVE_STREAMS, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getName() != null) {
                        values.put(KEY_NAME, ((GetVODStreamCallback) availableChanelsList.get(i)).getName());
                    } else {
                        values.put(KEY_NAME, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getStreamType() != null) {
                        values.put(KEY_STRESM_TYPE, ((GetVODStreamCallback) availableChanelsList.get(i)).getStreamType());
                    } else {
                        values.put(KEY_STRESM_TYPE, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getStreamId() != null) {
                        values.put(KEY_STREAM_ID, ((GetVODStreamCallback) availableChanelsList.get(i)).getStreamId());
                    } else {
                        values.put(KEY_STREAM_ID, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getStreamIcon() != null) {
                        values.put(KEY_STREAM_ICON, ((GetVODStreamCallback) availableChanelsList.get(i)).getStreamIcon());
                    } else {
                        values.put(KEY_STREAM_ICON, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getAdded() != null) {
                        values.put(KEY_ADDED, ((GetVODStreamCallback) availableChanelsList.get(i)).getAdded());
                    } else {
                        values.put(KEY_ADDED, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getCategoryId() != null) {
                        values.put(KEY_CATEGORY_ID_LIVE_STREAMS, ((GetVODStreamCallback) availableChanelsList.get(i)).getCategoryId());
                    } else {
                        values.put(KEY_CATEGORY_ID_LIVE_STREAMS, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getCustomSid() != null) {
                        values.put(KEY_CUSTOMER_SID, String.valueOf(((GetVODStreamCallback) availableChanelsList.get(i)).getCustomSid()));
                    } else {
                        values.put(KEY_CUSTOMER_SID, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getDirectSource() != null) {
                        values.put(KEY_DIRECT_SOURCE, ((GetVODStreamCallback) availableChanelsList.get(i)).getDirectSource());
                    } else {
                        values.put(KEY_DIRECT_SOURCE, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getSeriesNo() != null) {
                        values.put(KEY_AVAIL_CHANNEL_SERIES_NO, String.valueOf(((GetVODStreamCallback) availableChanelsList.get(i)).getSeriesNo()));
                    } else {
                        values.put(KEY_AVAIL_CHANNEL_SERIES_NO, "");
                    }
                    if (((GetVODStreamCallback) availableChanelsList.get(i)).getContainerExtension() != null) {
                        values.put(KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION, String.valueOf(((GetVODStreamCallback) availableChanelsList.get(i)).getContainerExtension()));
                    } else {
                        values.put(KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION, "");
                    }
                    addChannel.insert(TABLE_IPTV_VOD_STREAMS, null, values);
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

    public int getAllVODCountCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM vod_streams_v2", null);
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
            values.put(KEY_DB_VOD_STREAMS_CAT_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_VOD_STREAMS_CAT_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_VOD_STREAMS_CAT_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_VOD_STREAMS_CAT_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_VOD_STREAM_CAT_STATUS, null, values);
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
            values.put(KEY_DB_VOD_STREAMS_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_VOD_STREAMS_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_VOD_STREAMS_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_VOD_STREAMS_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_VOD_STREAM_STATUS, null, values);
            addDBUpdatedStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public int getVODStreamsCatDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM vod_streams_cat_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public int getVODStreamsDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM vod_streams_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public DatabaseUpdatedStatusDBModel getdateVODStreamsCatDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM vod_streams_cat_status WHERE vod_streams_cat_status_category = '" + category + "' AND " + KEY_DB_VOD_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'", null);
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

    public DatabaseUpdatedStatusDBModel getdateVODStreamsDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM vod_streams_status WHERE vod_streams_status_category = '" + category + "' AND " + KEY_DB_VOD_STREAMS_CATEGORY_ID + " = '" + categoryid + "'", null);
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

    public boolean updateVODStreamsCatDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM vod_streams_cat_status WHERE vod_streams_cat_status_category = '" + category + "' AND " + KEY_DB_VOD_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_VOD_STREAMS_CAT_STATUS))));
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
            cv.put(KEY_DB_VOD_STREAMS_CAT_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_VOD_STREAM_CAT_STATUS, cv, "vod_streams_cat_status_id= ?", new String[]{rowId});
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

    public boolean updateVODStreamsDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM vod_streams_status WHERE vod_streams_status_category = '" + category + "' AND " + KEY_DB_VOD_STREAMS_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_VOD_STREAMS_STATUS))));
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
            cv.put(KEY_DB_VOD_STREAMS_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_VOD_STREAM_STATUS, cv, "vod_streams_status_id= ?", new String[]{rowId});
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

    public boolean updateVODStreamsCatDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM vod_streams_cat_status WHERE vod_streams_cat_status_category = '" + category + "' AND " + KEY_DB_VOD_STREAMS_CAT_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_VOD_STREAMS_CAT_STATUS))));
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
            cv.put(KEY_DB_VOD_STREAMS_CAT_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_VOD_STREAMS_CAT_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_VOD_STREAM_CAT_STATUS, cv, "vod_streams_cat_status_id= ?", new String[]{rowId});
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

    public boolean updateVODStreamsDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM vod_streams_status WHERE vod_streams_status_category = '" + category + "' AND " + KEY_DB_VOD_STREAMS_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_VOD_STREAMS_STATUS))));
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
            cv.put(KEY_DB_VOD_STREAMS_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_VOD_STREAMS_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_VOD_STREAM_STATUS, cv, "vod_streams_status_id= ?", new String[]{rowId});
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

    public void emptyVODStreamCatRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from vod_category_v2");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void emptyVODStreamRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from vod_streams_v2");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void emptyVODStreamCatandVODStreamRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from vod_category_v2");
            readableDatabase.execSQL("delete from vod_streams_v2");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public int getVODwithCatCount(String categoryID) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM vod_streams_v2 WHERE categoryID_vod_stream_v2='" + categoryID + "'", null);
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

    public ArrayList<LiveStreamsDBModel> getAllVODStreasWithCategoryId(String cateogryId) {
        String sort_vod = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT_VOD, 0).getString(AppConst.LOGIN_PREF_SORT, "");
        ArrayList<LiveStreamsDBModel> liveStreamsList;
        String selectQuery;
        Cursor cursor;
        LiveStreamsDBModel liveStreamsDBModel;
        if (cateogryId.equals(AppConst.PASSWORD_UNSET)) {
            liveStreamsList = new ArrayList();
            if (sort_vod.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM vod_streams_v2";
            } else if (sort_vod.equals("1")) {
                selectQuery = "SELECT  * FROM vod_streams_v2 ORDER BY added_vod_stream_v2 DESC";
            } else if (sort_vod.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM vod_streams_v2 ORDER BY name_vod_stream_v2 DESC";
            } else {
                selectQuery = "SELECT  * FROM vod_streams_v2 ORDER BY name_vod_stream_v2 ASC";
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
            if (sort_vod.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "'";
            } else if (sort_vod.equals("1")) {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_ADDED + " DESC";
            } else if (sort_vod.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_NAME + " DESC";
            } else {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_NAME + " ASC";
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
            } catch (SQLiteDatabaseLockedException e3) {
                return null;
            } catch (SQLiteException e4) {
                return null;
            }
        } else {
            liveStreamsList = new ArrayList();
            if (sort_vod.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "'";
            } else if (sort_vod.equals("1")) {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_ADDED + " DESC";
            } else if (sort_vod.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_NAME + " DESC";
            } else {
                selectQuery = "SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2 ='" + cateogryId + "' ORDER BY " + KEY_NAME + " ASC";
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
            } catch (SQLiteDatabaseLockedException e5) {
                return null;
            } catch (SQLiteException e6) {
                return null;
            }
        }
    }

    public void deleteAndRecreateAllVODTables() {
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

    public LiveStreamsDBModel getLiveStreamFavouriteRow(String cateogryId, String streamId) {
        ArrayList<LiveStreamsDBModel> liveStreamsList = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM vod_streams_v2 WHERE categoryID_vod_stream_v2='" + cateogryId + "' AND " + KEY_STREAM_ID + "='" + streamId + "'", null);
            LiveStreamsDBModel liveStreamsDBModel = new LiveStreamsDBModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, null, null, null, null);            if (cursor.moveToFirst()) {
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
}
