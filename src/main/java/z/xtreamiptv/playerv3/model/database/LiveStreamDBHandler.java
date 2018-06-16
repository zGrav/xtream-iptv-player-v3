package z.xtreamiptv.playerv3.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import z.xtreamiptv.playerv3.model.pojo.PanelAvailableChannelsPojo;
import z.xtreamiptv.playerv3.model.pojo.PanelLivePojo;
import z.xtreamiptv.playerv3.model.pojo.PanelMoviePojo;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LiveStreamDBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "iptv_live_streams.db";
    private static final int DATABASE_VERSION = 2;
    private static final String KEY_ADDED = "added";
    private static final String KEY_ADDED_VOD = "added";
    private static final String KEY_AVAIL_CHANNEL_CATEGORY_NAME = "category_name";
    private static final String KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION = "container_extension";
    private static final String KEY_AVAIL_CHANNEL_LIVE = "live";
    private static final String KEY_AVAIL_CHANNEL_SERIES_NO = "series_no";
    private static final String KEY_AVAIL_CHANNEL_TYPE_NAME = "type_name";
    private static final String KEY_CATEGORY_ID = "categoryID";
    private static final String KEY_CATEGORY_ID_LIVE = "categoryID_live";
    private static final String KEY_CATEGORY_ID_LIVE_STREAMS = "categoryID";
    private static final String KEY_CATEGORY_ID_MOVIE = "categoryID_movie";
    private static final String KEY_CATEGORY_ID_VOD = "categoryId";
    private static final String KEY_CATEGORY_NAME = "categoryname";
    private static final String KEY_CATEGORY_NAME_LIVE = "categoryname_live";
    private static final String KEY_CATEGORY_NAME_MOVIE = "categoryname_movie";
    private static final String KEY_CHANNEL_ID = "channel_id";
    private static final String KEY_CONTAINER_EXT_VOD = "containerExtension";
    private static final String KEY_CUSTOMER_SID = "custom_sid";
    private static final String KEY_CUSTOM_SID_VOD = "customSid";
    private static final String KEY_DB_CATEGORY = "iptv_db_updated_status_category";
    private static final String KEY_DB_CATEGORY_ID = "iptv_db_updated_status_category_id";
    private static final String KEY_DB_LAST_UPDATED_DATE = "iptv_db_updated_status_last_updated_date";
    private static final String KEY_DB_STATUS_STATE = "iptv_db_updated_status_state";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DIRECT_SOURCE = "direct_source";
    private static final String KEY_DIRECT_SOURCE_VOD = "directSource";
    private static final String KEY_EPG_CHANNEL_ID = "epg_channel_id";
    private static final String KEY_ID = "id";
    private static final String KEY_ID_AUTO_EPG = "id_epg_aut0";
    private static final String KEY_ID_AUTO_VOD = "id_auto_vod";
    private static final String KEY_ID_DB_UPDATE_STATUS = "iptv_db_update_status_id";
    private static final String KEY_ID_LIVE = "id_live";
    private static final String KEY_ID_LIVE_STREAMS = "id";
    private static final String KEY_ID_MAG_PORTAL = "id_auto_mag";
    private static final String KEY_ID_MOVIE = "id_movie";
    private static final String KEY_ID_PARENT_ID = "paent_id";
    private static final String KEY_ID_PASWORD = "id_password";
    private static final String KEY_ID_PASWORD_STATUS = "id_password_status";
    private static final String KEY_MAG_PORTAL = "mag_portal";
    private static final String KEY_NAME = "name";
    private static final String KEY_NAME_VOD = "name";
    private static final String KEY_NUM_LIVE_STREAMS = "num";
    private static final String KEY_NUM_VOD = "num_";
    private static final String KEY_PASSWORD_STATUS = "password_status";
    private static final String KEY_PASSWORD_STATUS_CAT_ID = "password_status_cat_id";
    private static final String KEY_PASSWORD_USER_DETAIL = "user_detail";
    private static final String KEY_SERIAL_NO_VOD = "seriesNo";
    private static final String KEY_START = "start";
    private static final String KEY_STOP = "stop";
    private static final String KEY_STREAMTYPE_VOD = "streamType";
    private static final String KEY_STREAM_ICON = "stream_icon";
    private static final String KEY_STREAM_ICON_VOD = "streamIcon";
    private static final String KEY_STREAM_ID = "stream_id";
    private static final String KEY_STREAM_ID_VOD = "streamId";
    private static final String KEY_STRESM_TYPE = "stream_type";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TV_ARCHIVE = "tv_archive";
    private static final String KEY_TV_ARCHIVE_DURATION = "tv_archive_duration";
    private static final String KEY_USER_DETAIL = "password_user_detail";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String TABLE_DATABASE_UPDATE_STATUS = "iptv_db_update_status";
    private static final String TABLE_EPG = "epg";
    private static final String TABLE_IPTV_AVAILABLE_CHANNNELS = "iptv_live_streams";
    private static final String TABLE_IPTV_LIVE_CATEGORY = "iptv_live_category";
    private static final String TABLE_IPTV_LIVE_STREAMS_CATEGORY = "iptv_live_streams_category";
    private static final String TABLE_IPTV_MAG_PORTAL = "iptv_mag_portal_table";
    private static final String TABLE_IPTV_MOVIE_CATEGORY = "iptv_movie_category";
    private static final String TABLE_IPTV_PASSWORD = "iptv_password_table";
    private static final String TABLE_IPTV_PASSWORD_STATUS = "iptv_password_status_table";
    private static final String TABLE_IPTV_VOD_STREAMS = "iptv_vod_streams";
    String CREATE_DB_UPDATED_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS iptv_db_update_status(iptv_db_update_status_id INTEGER PRIMARY KEY,iptv_db_updated_status_state TEXT,iptv_db_updated_status_last_updated_date TEXT,iptv_db_updated_status_category TEXT,iptv_db_updated_status_category_id TEXT)";
    String CREATE_EPG_TABLE = "CREATE TABLE IF NOT EXISTS epg(id_epg_aut0 INTEGER PRIMARY KEY,title TEXT,start TEXT,stop TEXT,description TEXT,channel_id TEXT)";
    String CREATE_LIVE_AVAILABLE_CHANELS = "CREATE TABLE IF NOT EXISTS iptv_live_streams(id INTEGER PRIMARY KEY,num TEXT,name TEXT,stream_type TEXT,stream_id TEXT,stream_icon TEXT,epg_channel_id TEXT,added TEXT,categoryID TEXT,custom_sid TEXT,tv_archive TEXT,direct_source TEXT,tv_archive_duration TEXT,type_name TEXT,category_name TEXT,series_no TEXT,live TEXT,container_extension TEXT)";
    String CREATE_LIVE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS iptv_live_category(id_live INTEGER PRIMARY KEY,categoryID_live TEXT,categoryname_live TEXT,paent_id TEXT)";
    String CREATE_LIVE_STREAM_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS iptv_live_streams_category(id INTEGER PRIMARY KEY,categoryID TEXT,categoryname TEXT)";
    String CREATE_MAG_PORTAL_TABLE = "CREATE TABLE IF NOT EXISTS iptv_mag_portal_table(id_auto_mag INTEGER PRIMARY KEY,mag_portal TEXT)";
    String CREATE_MOVIE_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS iptv_movie_category(id_movie INTEGER PRIMARY KEY,categoryID_movie TEXT,categoryname_movie TEXT,paent_id TEXT)";
    String CREATE_PASSWORD_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS iptv_password_status_table(id_password_status INTEGER PRIMARY KEY,password_status_cat_id TEXT,password_user_detail TEXT,password_status TEXT)";
    String CREATE_PASSWORD_TABLE = "CREATE TABLE IF NOT EXISTS iptv_password_table(id_password INTEGER PRIMARY KEY,user_detail TEXT,password TEXT)";
    String CREATE_VOD_TABLE = "CREATE TABLE IF NOT EXISTS iptv_vod_streams(id_auto_vod INTEGER PRIMARY KEY,num_ TEXT,name TEXT,streamType TEXT,streamId TEXT,streamIcon TEXT,added TEXT,categoryId TEXT,seriesNo TEXT,containerExtension TEXT,customSid TEXT,directSource TEXT)";
    Context context;
    SQLiteDatabase db;

    public LiveStreamDBHandler(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CREATE_LIVE_CATEGORY_TABLE);
        db.execSQL(this.CREATE_MOVIE_CATEGORY_TABLE);
        db.execSQL(this.CREATE_EPG_TABLE);
        db.execSQL(this.CREATE_PASSWORD_TABLE);
        db.execSQL(this.CREATE_PASSWORD_STATUS_TABLE);
        db.execSQL(this.CREATE_LIVE_STREAM_CATEGORY_TABLE);
        db.execSQL(this.CREATE_LIVE_AVAILABLE_CHANELS);
        db.execSQL(this.CREATE_VOD_TABLE);
        db.execSQL(this.CREATE_DB_UPDATED_STATUS_TABLE);
        db.execSQL(this.CREATE_MAG_PORTAL_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS iptv_live_category");
        db.execSQL("DROP TABLE IF EXISTS iptv_movie_category");
        db.execSQL("DROP TABLE IF EXISTS epg");
        db.execSQL("DROP TABLE IF EXISTS iptv_password_table");
        db.execSQL("DROP TABLE IF EXISTS iptv_password_status_table");
        db.execSQL("DROP TABLE IF EXISTS iptv_live_streams_category");
        db.execSQL("DROP TABLE IF EXISTS iptv_live_streams");
        db.execSQL("DROP TABLE IF EXISTS iptv_vod_streams");
        db.execSQL("DROP TABLE IF EXISTS iptv_db_update_status");
        db.execSQL("DROP TABLE IF EXISTS iptv_mag_portal_table");
        onCreate(db);
    }

    public void deleteAndRecreateAllTables() {
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

    public void addMagPortal(String magPortalUrl) {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_MAG_PORTAL, magPortalUrl);
            readableDatabase.insert(TABLE_IPTV_MAG_PORTAL, null, values);
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public int getMagportal(String magPortalUrl) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM iptv_mag_portal_table WHERE mag_portal='" + magPortalUrl + "'", null);
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

    public void addMovieCategories(ArrayList<PanelMoviePojo> liveStreamCategoryIdDBModel) {
        try {
            SQLiteDatabase addMoviesCategories = getWritableDatabase();
            addMoviesCategories.beginTransaction();
            ContentValues values = new ContentValues();
            int totalMovie = liveStreamCategoryIdDBModel.size();
            if (totalMovie != 0) {
                for (int i = 0; i < totalMovie; i++) {
                    values.put(KEY_CATEGORY_ID_MOVIE, ((PanelMoviePojo) liveStreamCategoryIdDBModel.get(i)).getCategoryId());
                    values.put(KEY_CATEGORY_NAME_MOVIE, ((PanelMoviePojo) liveStreamCategoryIdDBModel.get(i)).getCategoryName());
                    values.put(KEY_ID_PARENT_ID, ((PanelMoviePojo) liveStreamCategoryIdDBModel.get(i)).getParentId());
                    addMoviesCategories.insert(TABLE_IPTV_MOVIE_CATEGORY, null, values);
                }
            }
            addMoviesCategories.setTransactionSuccessful();
            addMoviesCategories.endTransaction();
            addMoviesCategories.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void addAllAvailableChannel(Map<String, PanelAvailableChannelsPojo> availableChanelsList) {
        try {
            SQLiteDatabase addChannel = getWritableDatabase();
            addChannel.beginTransaction();
            ContentValues values = new ContentValues();
            for (PanelAvailableChannelsPojo availabaleChanelValue : availableChanelsList.values()) {
                if (availabaleChanelValue.getNum() != null) {
                    values.put(KEY_NUM_LIVE_STREAMS, String.valueOf(availabaleChanelValue.getNum()));
                } else {
                    values.put(KEY_NUM_LIVE_STREAMS, "");
                }
                if (availabaleChanelValue.getName() != null) {
                    values.put("name", availabaleChanelValue.getName());
                } else {
                    values.put("name", "");
                }
                if (availabaleChanelValue.getStreamType() != null) {
                    values.put(KEY_STRESM_TYPE, availabaleChanelValue.getStreamType());
                } else {
                    values.put(KEY_STRESM_TYPE, "");
                }
                if (availabaleChanelValue.getStreamId() != null) {
                    values.put(KEY_STREAM_ID, availabaleChanelValue.getStreamId());
                } else {
                    values.put(KEY_STREAM_ID, "");
                }
                if (availabaleChanelValue.getStreamIcon() != null) {
                    values.put(KEY_STREAM_ICON, availabaleChanelValue.getStreamIcon());
                } else {
                    values.put(KEY_STREAM_ICON, "");
                }
                if (availabaleChanelValue.getEpgChannelId() != null) {
                    values.put(KEY_EPG_CHANNEL_ID, availabaleChanelValue.getEpgChannelId());
                } else {
                    values.put(KEY_EPG_CHANNEL_ID, "");
                }
                if (availabaleChanelValue.getAdded() != null) {
                    values.put("added", availabaleChanelValue.getAdded());
                } else {
                    values.put("added", "");
                }
                if (availabaleChanelValue.getCategoryId() != null) {
                    values.put("categoryID", availabaleChanelValue.getCategoryId());
                } else {
                    values.put("categoryID", "");
                }
                if (availabaleChanelValue.getCustomSid() != null) {
                    values.put(KEY_CUSTOMER_SID, availabaleChanelValue.getCustomSid());
                } else {
                    values.put(KEY_CUSTOMER_SID, "");
                }
                if (availabaleChanelValue.getTvArchive() != null) {
                    values.put(KEY_TV_ARCHIVE, availabaleChanelValue.getTvArchive());
                } else {
                    values.put(KEY_TV_ARCHIVE, "");
                }
                if (availabaleChanelValue.getDirectSource() != null) {
                    values.put(KEY_DIRECT_SOURCE, availabaleChanelValue.getDirectSource());
                } else {
                    values.put(KEY_DIRECT_SOURCE, "");
                }
                if (availabaleChanelValue.getTvArchiveDuration() != null) {
                    values.put(KEY_TV_ARCHIVE_DURATION, availabaleChanelValue.getTvArchiveDuration());
                } else {
                    values.put(KEY_TV_ARCHIVE_DURATION, "");
                }
                if (availabaleChanelValue.getTypeName() != null) {
                    values.put(KEY_AVAIL_CHANNEL_TYPE_NAME, String.valueOf(availabaleChanelValue.getTypeName()));
                } else {
                    values.put(KEY_AVAIL_CHANNEL_TYPE_NAME, "");
                }
                if (availabaleChanelValue.getCategoryName() != null) {
                    values.put("category_name", availabaleChanelValue.getCategoryName());
                } else {
                    values.put("category_name", "");
                }
                if (availabaleChanelValue.getSeriesNo() != null) {
                    values.put(KEY_AVAIL_CHANNEL_SERIES_NO, String.valueOf(availabaleChanelValue.getSeriesNo()));
                } else {
                    values.put(KEY_AVAIL_CHANNEL_SERIES_NO, "");
                }
                if (availabaleChanelValue.getLive() != null) {
                    values.put(KEY_AVAIL_CHANNEL_SERIES_NO, String.valueOf(availabaleChanelValue.getSeriesNo()));
                } else {
                    values.put(KEY_AVAIL_CHANNEL_SERIES_NO, "");
                }
                if (availabaleChanelValue.getLive() != null) {
                    values.put(KEY_AVAIL_CHANNEL_LIVE, availabaleChanelValue.getLive());
                } else {
                    values.put(KEY_AVAIL_CHANNEL_LIVE, "");
                }
                if (availabaleChanelValue.getContainerExtension() != null) {
                    values.put(KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION, String.valueOf(availabaleChanelValue.getContainerExtension()));
                } else {
                    values.put(KEY_AVAIL_CHANNEL_CONTAINER_EXTENSION, "");
                }
                addChannel.insert(TABLE_IPTV_AVAILABLE_CHANNNELS, null, values);
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllliveCategories() {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_live_category INNER JOIN iptv_live_streams ON iptv_live_category.categoryID_live = iptv_live_streams.categoryID GROUP BY iptv_live_streams.categoryID ORDER BY iptv_live_category.id_live", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel liveStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    liveStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    liveStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    liveStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    liveStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
                    categoryListLive.add(liveStreamObj);
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllMovieCategories() {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_movie_category", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllMovieCategoriesHavingParentIdZero() {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_movie_category WHERE paent_id=0", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllMovieCategoriesHavingParentIdZero(int startLimit, int endLimit) {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_movie_category WHERE id_movie>=" + startLimit + " AND " + KEY_ID_MOVIE + "<=" + endLimit + " AND " + KEY_ID_PARENT_ID + "=0", null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllLiveCategoriesHavingParentIdNotZero(String getActiveLiveStreamCategoryId) {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_live_category WHERE paent_id=" + getActiveLiveStreamCategoryId, null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
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

    public ArrayList<LiveStreamCategoryIdDBModel> getAllMovieCategoriesHavingParentIdNotZero(String getActiveLiveStreamCategoryId) {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_movie_category WHERE paent_id=" + getActiveLiveStreamCategoryId, null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);

                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
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

    public int getAvailableChannelsCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM iptv_live_streams", null);
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

    public int getStreamsCount(String type) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM iptv_live_streams WHERE stream_type='" + type + "' OR " + KEY_STRESM_TYPE + "='created_live'", null);
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

    public int getSubCatMovieCount(String categoryID, String type) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM iptv_live_streams WHERE categoryID='" + categoryID + "' AND " + KEY_STRESM_TYPE + " LIKE '%" + type + "%'", null);
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

    public int getEPGCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM epg", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public int getLiveStreamsCount(String catID) {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  COUNT(*) FROM iptv_live_streams WHERE categoryID='" + catID + "'", null);
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

    public void addEPG(List<XMLTVProgrammePojo> programmePojos) {
        try {
            SQLiteDatabase addEPGData = getWritableDatabase();
            addEPGData.beginTransaction();
            ContentValues values = new ContentValues();
            int totalPrograms = programmePojos.size();
            if (totalPrograms != 0) {
                for (int i = 0; i < totalPrograms; i++) {
                    values.put(KEY_TITLE, ((XMLTVProgrammePojo) programmePojos.get(i)).getTitle());
                    values.put("start", ((XMLTVProgrammePojo) programmePojos.get(i)).getStart());
                    values.put(KEY_STOP, ((XMLTVProgrammePojo) programmePojos.get(i)).getStop());
                    values.put(KEY_DESCRIPTION, ((XMLTVProgrammePojo) programmePojos.get(i)).getDesc());
                    values.put(KEY_CHANNEL_ID, ((XMLTVProgrammePojo) programmePojos.get(i)).getChannel());
                    addEPGData.insert(TABLE_EPG, null, values);
                }
            }
            addEPGData.setTransactionSuccessful();
            addEPGData.endTransaction();
            addEPGData.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public ArrayList<XMLTVProgrammePojo> getEPG(String channelID) {
        ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM epg WHERE channel_id='" + channelID + "' ORDER BY " + "start" + " ASC", null);
            if (cursor.moveToFirst()) {
                do {
                    XMLTVProgrammePojo favourite = new XMLTVProgrammePojo();
                    favourite.setTitle(cursor.getString(1));
                    favourite.setStart(cursor.getString(2));
                    favourite.setStop(cursor.getString(3));
                    favourite.setDesc(cursor.getString(4));
                    favourite.setChannel(cursor.getString(5));
                    xmltvProgrammePojos.add(favourite);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return xmltvProgrammePojos;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public ArrayList<LiveStreamsDBModel> getAllLiveStreamsArchive(String cateogryId) {
        String sort = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT_TV_ARCHIVE, 0).getString(AppConst.LOGIN_PREF_SORT, "");
        ArrayList<LiveStreamsDBModel> liveStreamsList;
        String selectQuery;
        Cursor cursor;
        if (cateogryId.equals(AppConst.PASSWORD_UNSET)) {
            liveStreamsList = new ArrayList();
            if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ORDER BY added DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ORDER BY name DESC";
            } else {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND epg_channel_id IS NOT NULL AND epg_channel_id !='' ORDER BY name ASC";
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
        if (sort.equals(AppConst.PASSWORD_UNSET)) {
            selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND categoryID ='" + cateogryId + "' AND " + KEY_EPG_CHANNEL_ID + " IS NOT NULL AND " + KEY_EPG_CHANNEL_ID + " !='' ";
        } else if (sort.equals("1")) {
            selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND categoryID ='" + cateogryId + "' AND " + KEY_EPG_CHANNEL_ID + " IS NOT NULL AND " + KEY_EPG_CHANNEL_ID + " !='' ORDER BY " + "added" + " DESC";
        } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
            selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND categoryID ='" + cateogryId + "' AND " + KEY_EPG_CHANNEL_ID + " IS NOT NULL AND " + KEY_EPG_CHANNEL_ID + " !='' ORDER BY " + "name" + " DESC";
        } else {
            selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='live' AND tv_archive='1' AND categoryID ='" + cateogryId + "' AND " + KEY_EPG_CHANNEL_ID + " IS NOT NULL AND " + KEY_EPG_CHANNEL_ID + " !='' ORDER BY " + "name" + " ASC";
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
        } catch (SQLiteDatabaseLockedException e3) {
            return null;
        } catch (SQLiteException e4) {
            return null;
        }
    }

    public ArrayList<LiveStreamsDBModel> getAllLiveStreasWithCategoryId(String cateogryId, String type) {
        SharedPreferences sort_prefrence = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT, 0);
        SharedPreferences sort_prefrence_vod = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SORT_VOD, 0);
        String sort = sort_prefrence.getString(AppConst.LOGIN_PREF_SORT, "");
        String sort_vod = sort_prefrence_vod.getString(AppConst.LOGIN_PREF_SORT, "");
        ArrayList<LiveStreamsDBModel> liveStreamsList;
        String selectQuery;
        Cursor cursor;
        LiveStreamsDBModel liveStreamsDBModel;
        if (cateogryId.equals(AppConst.PASSWORD_UNSET)) {
            liveStreamsList = new ArrayList();
            if (type == "movie") {
                if (sort_vod.equals(AppConst.PASSWORD_UNSET)) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "'";
                } else if (sort_vod.equals("1")) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' ORDER BY " + "added" + " DESC";
                } else if (sort_vod.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' ORDER BY " + "name" + " DESC";
                } else {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' ORDER BY " + "name" + " ASC";
                }
            } else if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' OR " + KEY_STRESM_TYPE + "='created_live'";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' ORDER BY " + "added" + " DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' ORDER BY " + "name" + " DESC";
            } else {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE stream_type='" + type + "' ORDER BY " + "name" + " ASC";
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
            } catch (SQLiteDatabaseLockedException e) {
                return null;
            } catch (SQLiteException e2) {
                return null;
            }
        } else if (cateogryId.equals("null")) {
            liveStreamsList = new ArrayList();
            if (type == "movie") {
                if (sort_vod.equals(AppConst.PASSWORD_UNSET)) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "'";
                } else if (sort_vod.equals("1")) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "added" + " DESC";
                } else if (sort_vod.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " DESC";
                } else {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " ASC";
                }
            } else if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "'";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "added" + " DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " DESC";
            } else {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " ASC";
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
            if (type == "movie") {
                if (sort_vod.equals(AppConst.PASSWORD_UNSET)) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "'";
                } else if (sort_vod.equals("1")) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "added" + " DESC";
                } else if (sort_vod.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " DESC";
                } else {
                    selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " ASC";
                }
            } else if (sort.equals(AppConst.PASSWORD_UNSET)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "'";
            } else if (sort.equals("1")) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "added" + " DESC";
            } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " DESC";
            } else {
                selectQuery = "SELECT  * FROM iptv_live_streams WHERE categoryID ='" + cateogryId + "' ORDER BY " + "name" + " ASC";
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

    public void addLiveCategories(ArrayList<PanelLivePojo> liveStreamCategoryIdDBModel) {
        try {
            SQLiteDatabase addLiveCategories = getWritableDatabase();
            addLiveCategories.beginTransaction();
            ContentValues values = new ContentValues();
            int totalLive = liveStreamCategoryIdDBModel.size();
            if (totalLive != 0) {
                for (int i = 0; i < totalLive; i++) {
                    values.put(KEY_CATEGORY_ID_LIVE, ((PanelLivePojo) liveStreamCategoryIdDBModel.get(i)).getCategoryId());
                    values.put(KEY_CATEGORY_NAME_LIVE, ((PanelLivePojo) liveStreamCategoryIdDBModel.get(i)).getCategoryName());
                    values.put(KEY_ID_PARENT_ID, ((PanelLivePojo) liveStreamCategoryIdDBModel.get(i)).getParentId());
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

    public LiveStreamsDBModel getLiveStreamFavouriteRow(String cateogryId, String streamId) {
        ArrayList<LiveStreamsDBModel> liveStreamsList = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM iptv_live_streams WHERE categoryID='" + cateogryId + "' AND " + KEY_STREAM_ID + "='" + streamId + "'", null);
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

    public int getDBStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM iptv_db_update_status", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public DatabaseUpdatedStatusDBModel getdateDBStatus(String category, String categoryid) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM iptv_db_update_status WHERE iptv_db_updated_status_category = '" + category + "' AND " + KEY_DB_CATEGORY_ID + " = '" + categoryid + "'", null);
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

    public boolean updateDBStatus(String category, String categoryid, String updatedDBStatus) {
        try {
            String query = "SELECT rowid FROM iptv_db_update_status WHERE iptv_db_updated_status_category = '" + category + "' AND " + KEY_DB_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_DB_UPDATE_STATUS))));
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
            cv.put(KEY_DB_STATUS_STATE, updatedDBStatus);
            readableDatabase.update(TABLE_DATABASE_UPDATE_STATUS, cv, "iptv_db_update_status_id= ?", new String[]{rowId});
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

    public void makeEmptyChanelsRecord() {
        try {
            SQLiteDatabase makeEmptyChanelsRecordDB = getWritableDatabase();
            makeEmptyChanelsRecordDB.execSQL("delete from iptv_live_streams");
            makeEmptyChanelsRecordDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public boolean updateDBStatusAndDate(String category, String categoryid, String updatedDBStatus, String upadtedDate) {
        try {
            String query = "SELECT rowid FROM iptv_db_update_status WHERE iptv_db_updated_status_category = '" + category + "' AND " + KEY_DB_CATEGORY_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_DB_UPDATE_STATUS))));
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
            cv.put(KEY_DB_STATUS_STATE, updatedDBStatus);
            cv.put(KEY_DB_LAST_UPDATED_DATE, upadtedDate);
            readableDatabase.update(TABLE_DATABASE_UPDATE_STATUS, cv, "iptv_db_update_status_id= ?", new String[]{rowId});
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

    public void makeEmptyLiveCategory() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from iptv_live_category");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void makeEmptyMovieCategory() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from iptv_movie_category");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void makeEmptyEPG() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from epg");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void addDBUpdatedStatus(DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModel) {
        try {
            SQLiteDatabase addDBUpdatedStatusDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_DB_STATUS_STATE, databaseUpdatedStatusDBModel.getDbUpadatedStatusState());
            values.put(KEY_DB_LAST_UPDATED_DATE, databaseUpdatedStatusDBModel.getDbLastUpdatedDate());
            values.put(KEY_DB_CATEGORY, databaseUpdatedStatusDBModel.getDbCategory());
            values.put(KEY_DB_CATEGORY_ID, databaseUpdatedStatusDBModel.getGetDbCategoryID());
            addDBUpdatedStatusDB.insert(TABLE_DATABASE_UPDATE_STATUS, null, values);
            addDBUpdatedStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void makeEmptyAllTablesRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from iptv_live_category");
            readableDatabase.execSQL("delete from iptv_movie_category");
            readableDatabase.execSQL("delete from epg");
            readableDatabase.execSQL("delete from iptv_live_streams");
            readableDatabase.execSQL("delete from iptv_db_update_status");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void makeEmptyAllChannelsVODTablesRecords() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from iptv_live_category");
            readableDatabase.execSQL("delete from iptv_movie_category");
            readableDatabase.execSQL("delete from iptv_live_streams");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public ArrayList<PasswordStatusDBModel> getAllPasswordStatus() {
        ArrayList<PasswordStatusDBModel> passwordStatusDBModelArrayList = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_password_status_table", null);
            if (cursor.moveToFirst()) {
                do {
                    PasswordStatusDBModel passwordStatusDBModel = new PasswordStatusDBModel(null, null, null);
                    passwordStatusDBModel.setIdPaswordStaus(Integer.parseInt(cursor.getString(0)));
                    passwordStatusDBModel.setPasswordStatusCategoryId(cursor.getString(1));
                    passwordStatusDBModel.setPasswordStatusUserDetail(cursor.getString(2));
                    passwordStatusDBModel.setPasswordStatus(cursor.getString(3));
                    passwordStatusDBModelArrayList.add(passwordStatusDBModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return passwordStatusDBModelArrayList;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public void addPassword(PasswordDBModel passwordDBModel) {
        try {
            SQLiteDatabase addPasswordDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PASSWORD_USER_DETAIL, passwordDBModel.getUserDetail());
            values.put("password", passwordDBModel.getUserPassword());
            addPasswordDB.insert(TABLE_IPTV_PASSWORD, null, values);
            addPasswordDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public void addPasswordStatus(PasswordStatusDBModel passwordStatusDBModel) {
        try {
            SQLiteDatabase addPasswordStatusDB = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_PASSWORD_STATUS_CAT_ID, passwordStatusDBModel.getPasswordStatusCategoryId());
            values.put(KEY_USER_DETAIL, passwordStatusDBModel.getPasswordStatusUserDetail());
            values.put(KEY_PASSWORD_STATUS, passwordStatusDBModel.getPasswordStatus());
            addPasswordStatusDB.insert(TABLE_IPTV_PASSWORD_STATUS, null, values);
            addPasswordStatusDB.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public PasswordStatusDBModel getPasswordStatus(String userDetail, String categoryID) {
        try {
            String rowId = "";
            Cursor res = getReadableDatabase().rawQuery("SELECT * FROM iptv_password_status_table WHERE password_user_detail = '" + userDetail + "' AND " + KEY_PASSWORD_STATUS_CAT_ID + " = '" + categoryID + "'", null);
            PasswordStatusDBModel passwordStatusDBModel = new PasswordStatusDBModel(null, null, null);            if (res.moveToFirst()) {
                do {
                    passwordStatusDBModel.setIdPaswordStaus(Integer.parseInt(res.getString(0)));
                    passwordStatusDBModel.setPasswordStatusCategoryId(res.getString(1));
                    passwordStatusDBModel.setPasswordStatusUserDetail(res.getString(2));
                    passwordStatusDBModel.setPasswordStatus(res.getString(3));
                } while (res.moveToNext());
            }
            res.close();
            return passwordStatusDBModel;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public boolean updatePasswordStatus(String username, String categoryid, String updatePasswordStatus) {
        try {
            String query = "SELECT rowid FROM iptv_password_status_table WHERE password_user_detail = '" + username + "' AND " + KEY_PASSWORD_STATUS_CAT_ID + " = '" + categoryid + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_PASWORD_STATUS))));
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
            cv.put(KEY_PASSWORD_STATUS, updatePasswordStatus);
            readableDatabase.update(TABLE_IPTV_PASSWORD_STATUS, cv, "id_password_status= ?", new String[]{rowId});
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

    public ArrayList<PasswordDBModel> getAllPassword() {
        ArrayList<PasswordDBModel> passwordList = new ArrayList();
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM iptv_password_table", null);
            if (cursor.moveToFirst()) {
                do {
                    PasswordDBModel passwordDBModel = new PasswordDBModel(null, null);
                    passwordDBModel.setId(Integer.parseInt(cursor.getString(0)));
                    passwordDBModel.setUserDetail(cursor.getString(1));
                    passwordDBModel.setUserPassword(cursor.getString(2));
                    passwordList.add(passwordDBModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return passwordList;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public boolean upDatePassword(String userName, String newPassword) {
        try {
            String query = "SELECT rowid FROM iptv_password_table WHERE user_detail = '" + userName + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            String rowId = "";
            Cursor res = readableDatabase.rawQuery(query, null);
            if (res != null) {
                if (res.moveToFirst()) {
                    do {
                        rowId = String.valueOf(Integer.parseInt(res.getString(res.getColumnIndex(KEY_ID_PASWORD))));
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
            cv.put("password", newPassword);
            readableDatabase.update(TABLE_IPTV_PASSWORD, cv, "id_password= ?", new String[]{rowId});
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

    public void makeEmptyStatus() {
        try {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            readableDatabase = getWritableDatabase();
            readableDatabase.execSQL("delete from iptv_db_update_status");
            readableDatabase.close();
        } catch (SQLiteDatabaseLockedException e) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        } catch (SQLiteException e2) {
            Log.w(NotificationCompat.CATEGORY_MESSAGE, "exception");
        }
    }

    public int getParentalStatusCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM iptv_password_status_table", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public int getMovieCategoryCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM iptv_movie_category", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public int getLiveCategoryCount() {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM iptv_live_category", null);
            int cnt = cursor.getCount();
            cursor.close();
            return cnt;
        } catch (SQLiteDatabaseLockedException e) {
            return 0;
        } catch (SQLiteException e2) {
            return 0;
        }
    }

    public ArrayList<LiveStreamCategoryIdDBModel> getMovieCategoriesinRange(int startLimit, int endLimit) {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        SQLiteDatabase getAllMovieCategoriesDB = getReadableDatabase();
        getAllMovieCategoriesDB.beginTransaction();
        try {
            Cursor cursor = getAllMovieCategoriesDB.rawQuery("SELECT * FROM iptv_movie_category WHERE id_movie>=" + startLimit + " AND " + KEY_ID_MOVIE + "<=" + endLimit, null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
                    categoryListLive.add(movieStreamObj);
                } while (cursor.moveToNext());
            }
            cursor.close();
            getAllMovieCategoriesDB.setTransactionSuccessful();
            getAllMovieCategoriesDB.endTransaction();
            return categoryListLive;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }

    public ArrayList<LiveStreamCategoryIdDBModel> getLiveCategoriesinRange(int startLimit, int endLimit) {
        ArrayList<LiveStreamCategoryIdDBModel> categoryListLive = new ArrayList();
        SQLiteDatabase getLiveCategoriesinRange = getReadableDatabase();
        getLiveCategoriesinRange.beginTransaction();
        try {
            Cursor cursor = getLiveCategoriesinRange.rawQuery("SELECT * FROM iptv_live_category WHERE id_live>=" + startLimit + " AND " + KEY_ID_LIVE + "<=" + endLimit, null);
            if (cursor.moveToFirst()) {
                do {
                    LiveStreamCategoryIdDBModel movieStreamObj = new LiveStreamCategoryIdDBModel(null, null, 0);
                    movieStreamObj.setId(Integer.parseInt(cursor.getString(0)));
                    movieStreamObj.setLiveStreamCategoryID(cursor.getString(1));
                    movieStreamObj.setLiveStreamCategoryName(cursor.getString(2));
                    movieStreamObj.setParentId(Integer.parseInt(cursor.getString(3)));
                    categoryListLive.add(movieStreamObj);
                } while (cursor.moveToNext());
            }
            cursor.close();
            getLiveCategoriesinRange.setTransactionSuccessful();
            getLiveCategoriesinRange.endTransaction();
            return categoryListLive;
        } catch (SQLiteDatabaseLockedException e) {
            return null;
        } catch (SQLiteException e2) {
            return null;
        }
    }
}
