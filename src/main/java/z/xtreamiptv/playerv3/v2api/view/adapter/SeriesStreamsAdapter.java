package z.xtreamiptv.playerv3.v2api.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.v2api.model.SeriesDBModel;
import z.xtreamiptv.playerv3.v2api.view.activity.SeriesDetailActivity;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SeriesStreamsAdapter extends Adapter<SeriesStreamsAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private List<SeriesDBModel> completeList;
    private Context context;
    private List<SeriesDBModel> dataSet;
    private DatabaseHandler database;
    private Editor editor;
    private List<SeriesDBModel> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    MyViewHolder myViewHolder;
    private SharedPreferences pref;
    private SimpleDateFormat programTimeFormat;
    public int text_last_size;
    public int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_channel_logo)
        ImageView ivChannelLogo;
        @BindView(R.id.iv_favourite)
        ImageView ivFavourite;
        @BindView(R.id.ll_menu)
        LinearLayout llMenu;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.rl_channel_bottom)
        RelativeLayout rlChannelBottom;
        @BindView(R.id.rl_movie_image)
        RelativeLayout rlMovieImage;
        @BindView(R.id.rl_streams_layout)
        RelativeLayout rlStreamsLayout;
        @BindView(R.id.tv_channel_name)
        TextView tvChannelName;
        @BindView(R.id.tv_current_live)
        TextView tvCurrentLive;
        @BindView(R.id.tv_streamOptions)
        TextView tvStreamOptions;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);
        }
    }

//    public class MyViewHolder_ViewBinding implements Unbinder {
//        private MyViewHolder target;
//
//        @UiThread
//        public MyViewHolder_ViewBinding(MyViewHolder target, View source) {
//            this.target = target;
//            target.ivChannelLogo = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_channel_logo, "field 'ivChannelLogo'", ImageView.class);
//            target.tvChannelName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name, "field 'tvChannelName'", TextView.class);
//            target.cardView = (CardView) Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", CardView.class);
//            target.tvStreamOptions = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_streamOptions, "field 'tvStreamOptions'", TextView.class);
//            target.ivFavourite = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_favourite, "field 'ivFavourite'", ImageView.class);
//            target.rlStreamsLayout = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_streams_layout, "field 'rlStreamsLayout'", RelativeLayout.class);
//            target.rlChannelBottom = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_channel_bottom, "field 'rlChannelBottom'", RelativeLayout.class);
//            target.llMenu = (LinearLayout) Utils.findRequiredViewAsType(source, R.id.ll_menu, "field 'llMenu'", LinearLayout.class);
//            target.progressBar = (ProgressBar) Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
//            target.tvCurrentLive = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_current_live, "field 'tvCurrentLive'", TextView.class);
//            target.tvTime = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
//            target.rlMovieImage = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie_image, "field 'rlMovieImage'", RelativeLayout.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MyViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.ivChannelLogo = null;
//            target.tvChannelName = null;
//            target.cardView = null;
//            target.tvStreamOptions = null;
//            target.ivFavourite = null;
//            target.rlStreamsLayout = null;
//            target.rlChannelBottom = null;
//            target.llMenu = null;
//            target.progressBar = null;
//            target.tvCurrentLive = null;
//            target.tvTime = null;
//            target.rlMovieImage = null;
//        }
//    }

    public SeriesStreamsAdapter(List<SeriesDBModel> liveStreamCategories, Context context) {
        this.dataSet = liveStreamCategories;
        this.context = context;
        this.filterList.addAll(liveStreamCategories);
        this.completeList = liveStreamCategories;
        this.database = new DatabaseHandler(context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.pref = this.context.getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG = this.pref.getInt(AppConst.LIVE_STREAM, 0);
        if (AppConst.LIVE_FLAG == 1) {
            this.myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_layout, parent, false));
            return this.myViewHolder;
        }
        this.myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_linear_layout, parent, false));
        return this.myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            String finalNum;
            String finalName;
            String finalStreamType;
            int finalSeriesID;
            String finalCover;
            String finalPlot;
            String finalCast;
            String finalDirector;
            String finalGenre;
            String finalReleaseDate;
            String finalLast_modified;
            String finalRating;
            String finalCategoryId1;
            int finalSeriesID1;
            String finalCategoryId;
            ArrayList<FavouriteDBModel> checkFavourite;
            MyViewHolder myViewHolder;
            int i;
            String str;
            String num = "";
            String name = "";
            String streamType = "";
            int seriesID = -1;
            String cover = "";
            String plot = "";
            String cast = "";
            String director = "";
            String genre = "";
            String releaseDate = "";
            String last_modified = "";
            String rating = "";
            String categoryId = "";
            if (this.dataSet != null) {
                SeriesDBModel seriesDBModel = (SeriesDBModel) this.dataSet.get(listPosition);
                if (seriesDBModel.getNum() != null) {
                    num = seriesDBModel.getNum();
                }
                if (seriesDBModel.getName() != null) {
                    name = seriesDBModel.getName();
                }
                if (seriesDBModel.getStreamType() != null) {
                    streamType = seriesDBModel.getStreamType();
                }
                if (seriesDBModel.getseriesID() != -1) {
                    seriesID = seriesDBModel.getseriesID();
                }
                if (seriesDBModel.getcover() != null) {
                    cover = seriesDBModel.getcover();
                }
                if (seriesDBModel.getplot() != null) {
                    plot = seriesDBModel.getplot();
                }
                if (seriesDBModel.getcast() != null) {
                    cast = seriesDBModel.getcast();
                }
                if (seriesDBModel.getdirector() != null) {
                    director = seriesDBModel.getdirector();
                }
                if (seriesDBModel.getgenre() != null) {
                    genre = seriesDBModel.getgenre();
                }
                if (seriesDBModel.getreleaseDate() != null) {
                    releaseDate = seriesDBModel.getreleaseDate();
                }
                if (seriesDBModel.getlast_modified() != null) {
                    last_modified = seriesDBModel.getlast_modified();
                }
                if (seriesDBModel.getrating() != null) {
                    rating = seriesDBModel.getrating();
                }
                if (seriesDBModel.getCategoryId() != null) {
                    categoryId = seriesDBModel.getCategoryId();
                }
            }
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            holder.tvTime.setText("");
            holder.progressBar.setVisibility(8);
            holder.tvCurrentLive.setText("");
            holder.tvChannelName.setText(((SeriesDBModel) this.dataSet.get(listPosition)).getName());
            holder.ivChannelLogo.setImageDrawable(null);
            if (cover != null) {
                if (!cover.equals("")) {
                    Picasso.with(this.context).load(cover).placeholder((int) R.drawable.iptv_placeholder).into(holder.ivChannelLogo);
                    finalNum = num;
                    finalName = name;
                    finalStreamType = streamType;
                    finalSeriesID = seriesID;
                    finalCover = cover;
                    finalPlot = plot;
                    finalCast = cast;
                    finalDirector = director;
                    finalGenre = genre;
                    finalReleaseDate = releaseDate;
                    finalLast_modified = last_modified;
                    finalRating = rating;
                    finalCategoryId1 = categoryId;
                    final String finalNum1 = finalNum;
                    final String finalName1 = finalName;
                    final String finalStreamType1 = finalStreamType;
                    final int finalSeriesID2 = finalSeriesID;
                    final String finalCover1 = finalCover;
                    final String finalPlot1 = finalPlot;
                    final String finalCast1 = finalCast;
                    final String finalDirector1 = finalDirector;
                    final String finalGenre1 = finalGenre;
                    final String finalReleaseDate1 = finalReleaseDate;
                    final String finalLast_modified1 = finalLast_modified;
                    final String finalRating1 = finalRating;
                    final String finalCategoryId2 = finalCategoryId1;
                    holder.cardView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum1, finalName1, finalStreamType1, finalSeriesID2, finalCover1, finalPlot1, finalCast1, finalDirector1, finalGenre1, finalReleaseDate1, finalLast_modified1, finalRating1, finalCategoryId2);
                        }
                    });
                    holder.rlMovieImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum1, finalName1, finalStreamType1, finalSeriesID2, finalCover1, finalPlot1, finalCast1, finalDirector1, finalGenre1, finalReleaseDate1, finalLast_modified1, finalRating1, finalCategoryId2);
                        }
                    });
                    holder.rlStreamsLayout.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum1, finalName1, finalStreamType1, finalSeriesID2, finalCover1, finalPlot1, finalCast1, finalDirector1, finalGenre1, finalReleaseDate1, finalLast_modified1, finalRating1, finalCategoryId2);
                        }
                    });
                    finalSeriesID1 = seriesID;
                    finalCategoryId = categoryId;
                    checkFavourite = this.database.checkFavourite(finalSeriesID1, finalCategoryId, "series");
                    if (checkFavourite != null || checkFavourite.size() <= 0) {
                        holder.ivFavourite.setVisibility(4);
                    } else {
                        holder.ivFavourite.setVisibility(0);
                    }
                    myViewHolder = holder;
                    i = finalSeriesID1;
                    str = finalCategoryId;
                    final MyViewHolder finalMyViewHolder = myViewHolder;
                    final int finalI = i;
                    final String finalStr = str;
                    holder.rlStreamsLayout.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            SeriesStreamsAdapter.this.popmenu(finalMyViewHolder, finalI, finalStr);
                            return true;
                        }
                    });
                    myViewHolder = holder;
                    i = finalSeriesID1;
                    str = finalCategoryId;
                    final MyViewHolder finalMyViewHolder1 = myViewHolder;
                    final int finalI1 = i;
                    final String finalStr1 = str;
                    holder.rlMovieImage.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            SeriesStreamsAdapter.this.popmenu(finalMyViewHolder1, finalI1, finalStr1);
                            return true;
                        }
                    });
                    myViewHolder = holder;
                    i = finalSeriesID1;
                    str = finalCategoryId;
                    final MyViewHolder finalMyViewHolder2 = myViewHolder;
                    final int finalI2 = i;
                    final String finalStr2 = str;
                    holder.llMenu.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SeriesStreamsAdapter.this.popmenu(finalMyViewHolder2, finalI2, finalStr2);
                        }
                    });
                }
            }
            if (VERSION.SDK_INT >= 21) {
                holder.ivChannelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.iptv_placeholder, null));
            } else {
                holder.ivChannelLogo.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.iptv_placeholder));
            }
            finalNum = num;
            finalName = name;
            finalStreamType = streamType;
            finalSeriesID = seriesID;
            finalCover = cover;
            finalPlot = plot;
            finalCast = cast;
            finalDirector = director;
            finalGenre = genre;
            finalReleaseDate = releaseDate;
            finalLast_modified = last_modified;
            finalRating = rating;
            finalCategoryId1 = categoryId;
            final String finalNum1 = finalNum;
            final String finalName1 = finalName;
            final String finalStreamType1 = finalStreamType;
            final int finalSeriesID2 = finalSeriesID;
            final String finalCover1 = finalCover;
            final String finalPlot1 = finalPlot;
            final String finalCast1 = finalCast;
            final String finalDirector1 = finalDirector;
            final String finalGenre1 = finalGenre;
            final String finalReleaseDate1 = finalReleaseDate;
            final String finalLast_modified1 = finalLast_modified;
            final String finalRating1 = finalRating;
            final String finalCategoryId2 = finalCategoryId1;
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum1, finalName1, finalStreamType1, finalSeriesID2, finalCover1, finalPlot1, finalCast1, finalDirector1, finalGenre1, finalReleaseDate1, finalLast_modified1, finalRating1, finalCategoryId2);
                }
            });
            holder.rlMovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum1, finalName1, finalStreamType1, finalSeriesID2, finalCover1, finalPlot1, finalCast1, finalDirector1, finalGenre1, finalReleaseDate1, finalLast_modified1, finalRating1, finalCategoryId2);
                }
            });
            holder.rlStreamsLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum1, finalName1, finalStreamType1, finalSeriesID2, finalCover1, finalPlot1, finalCast1, finalDirector1, finalGenre1, finalReleaseDate1, finalLast_modified1, finalRating1, finalCategoryId2);
                }
            });
            finalSeriesID1 = seriesID;
            finalCategoryId = categoryId;
            checkFavourite = this.database.checkFavourite(finalSeriesID1, finalCategoryId, "series");
            if (checkFavourite != null || checkFavourite.size() <= 0) {
                holder.ivFavourite.setVisibility(4);
            } else {
                holder.ivFavourite.setVisibility(0);
            }
            myViewHolder = holder;
            i = finalSeriesID1;
            str = finalCategoryId;
            final MyViewHolder finalMyViewHolder = myViewHolder;
            final int finalI = i;
            final String finalStr = str;
            holder.rlStreamsLayout.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    SeriesStreamsAdapter.this.popmenu(finalMyViewHolder, finalI, finalStr);
                    return true;
                }
            });
            myViewHolder = holder;
            i = finalSeriesID1;
            str = finalCategoryId;
            final MyViewHolder finalMyViewHolder1 = myViewHolder;
            final int finalI1 = i;
            final String finalStr1 = str;
            holder.rlMovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    SeriesStreamsAdapter.this.popmenu(finalMyViewHolder1, finalI1, finalStr1);
                    return true;
                }
            });
            myViewHolder = holder;
            i = finalSeriesID1;
            str = finalCategoryId;
            final MyViewHolder finalMyViewHolder2 = myViewHolder;
            final int finalI2 = i;
            final String finalStr2 = str;
            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SeriesStreamsAdapter.this.popmenu(finalMyViewHolder2, finalI2, finalStr2);
                }
            });
        }
    }

    private void startSeriesViewActivitit(String num, String name, String streamType, int seriesID, String cover, String plot, String cast, String director, String genre, String releaseDate, String last_modified, String rating, String categoryId) {
        if (this.context != null) {
            Intent viewDetailsActivityIntent = new Intent(this.context, SeriesDetailActivity.class);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_NUM, num);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_NAME, name);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_STREAM_TYPE, streamType);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_SERIES_ID, String.valueOf(seriesID));
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_COVER, cover);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_PLOT, plot);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_CAST, cast);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_DIRECTOR, director);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_GENERE, genre);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_RELEASE_DATE, releaseDate);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_LAST_MODIFIED, last_modified);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_RATING, rating);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_CATEGORY_ID, categoryId);
            this.context.startActivity(viewDetailsActivityIntent);
        }
    }

    private void popmenu(final MyViewHolder holder, final int streamId, final String categoryId) {
        PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
        popup.inflate(R.menu.menu_card_series_streams);
        if (this.database.checkFavourite(streamId, categoryId, "series").size() > 0) {
            popup.getMenu().getItem(2).setVisible(false);
        } else {
            popup.getMenu().getItem(1).setVisible(false);
        }
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_add_to_fav:
                        addToFavourite();
                        break;
                    case R.id.nav_remove_from_fav:
                        removeFromFavourite();
                        break;
                }
                return false;
            }

            private void playChannel() {
                holder.cardView.performClick();
            }

            private void addToFavourite() {
                FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel(0, null, null);
                LiveStreamsFavourite.setCategoryID(categoryId);
                LiveStreamsFavourite.setStreamID(streamId);
                SeriesStreamsAdapter.this.database.addToFavourite(LiveStreamsFavourite, "series");
                holder.ivFavourite.setVisibility(0);
            }

            private void removeFromFavourite() {
                SeriesStreamsAdapter.this.database.deleteFavourite(streamId, categoryId, "series");
                holder.ivFavourite.setVisibility(4);
            }
        });
        popup.show();
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C16151 implements Runnable {
                C16151() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SeriesStreamsAdapter.this.dataSet = SeriesStreamsAdapter.this.completeList;
                    } else if (!SeriesStreamsAdapter.this.filterList.isEmpty() || SeriesStreamsAdapter.this.filterList.isEmpty()) {
                        SeriesStreamsAdapter.this.dataSet = SeriesStreamsAdapter.this.filterList;
                    }
                    if (SeriesStreamsAdapter.this.dataSet != null && SeriesStreamsAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    SeriesStreamsAdapter.this.text_last_size = SeriesStreamsAdapter.this.text_size;
                    SeriesStreamsAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SeriesStreamsAdapter.this.filterList = new ArrayList();
                SeriesStreamsAdapter.this.text_size = text.length();
                if (SeriesStreamsAdapter.this.filterList != null) {
                    SeriesStreamsAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SeriesStreamsAdapter.this.filterList.addAll(SeriesStreamsAdapter.this.completeList);
                } else {
                    if ((SeriesStreamsAdapter.this.dataSet != null && SeriesStreamsAdapter.this.dataSet.size() == 0) || SeriesStreamsAdapter.this.text_last_size > SeriesStreamsAdapter.this.text_size) {
                        SeriesStreamsAdapter.this.dataSet = SeriesStreamsAdapter.this.completeList;
                    }
                    if (SeriesStreamsAdapter.this.dataSet != null) {
                        for (SeriesDBModel item : SeriesStreamsAdapter.this.dataSet) {
                            if (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) {
                                SeriesStreamsAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) SeriesStreamsAdapter.this.context).runOnUiThread(new C16151());
            }
        }).start();
    }
}
