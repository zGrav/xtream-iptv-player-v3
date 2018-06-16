package z.xtreamiptv.playerv3.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.view.activity.ViewDetailsActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class SubCategoriesChildAdapter extends Adapter<SubCategoriesChildAdapter.MyViewHolder> {
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> dataSet;
    DatabaseHandler database;
    private Editor editor;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
    private LiveStreamsDBModel liveStreamsDBModel;
    private SharedPreferences loginPrefXtream;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences pref;
    private SharedPreferences settingsPrefs;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_movie)
        RelativeLayout Movie;
        @BindView(R.id.rl_movie_image)
        ImageView MovieImage;
        @BindView(R.id.tv_movie_name)
        TextView MovieName;
        @BindView(R.id.card_view)
        RelativeLayout cardView;
        @BindView(R.id.iv_favourite)
        ImageView ivFavourite;
        @BindView(R.id.ll_menu)
        LinearLayout llMenu;
        @BindView(R.id.tv_movie_name1)
        TextView movieNameTV;
        @BindView(R.id.rl_movie_bottom)
        RelativeLayout rlMovieBottom;
        @BindView(R.id.tv_streamOptions)
        TextView tvStreamOptions;

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
//            target.MovieName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name, "field 'MovieName'", TextView.class);
//            target.Movie = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie, "field 'Movie'", RelativeLayout.class);
//            target.movieNameTV = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name1, "field 'movieNameTV'", TextView.class);
//            target.MovieImage = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_movie_image, "field 'MovieImage'", ImageView.class);
//            target.cardView = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", RelativeLayout.class);
//            target.tvStreamOptions = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_streamOptions, "field 'tvStreamOptions'", TextView.class);
//            target.ivFavourite = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_favourite, "field 'ivFavourite'", ImageView.class);
//            target.rlMovieBottom = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie_bottom, "field 'rlMovieBottom'", RelativeLayout.class);
//            target.llMenu = (LinearLayout) Utils.findRequiredViewAsType(source, R.id.ll_menu, "field 'llMenu'", LinearLayout.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MyViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.MovieName = null;
//            target.Movie = null;
//            target.movieNameTV = null;
//            target.MovieImage = null;
//            target.cardView = null;
//            target.tvStreamOptions = null;
//            target.ivFavourite = null;
//            target.rlMovieBottom = null;
//            target.llMenu = null;
//        }
//    }

    public SubCategoriesChildAdapter(List<LiveStreamsDBModel> vodCategories, Context context) {
        this.dataSet = vodCategories;
        this.context = context;
        this.filterList.addAll(vodCategories);
        this.completeList = vodCategories;
        this.database = new DatabaseHandler(context);
        this.liveStreamsDBModel = this.liveStreamsDBModel;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subcateories_cihild_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            int i;
            String str;
            String str2;
            String str3;
            String str4;
            String str5;
            MyViewHolder myViewHolder;
            int i2;
            String str6;
            String str7;
            String str8;
            String str9;
            String str10;
            Context context = this.context;
            String str11 = AppConst.LOGIN_PREF_SELECTED_PLAYER;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str11, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            final int streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId());
            final String categoryId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getCategoryId();
            final String containerExtension = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getContaiinerExtension();
            final String streamType = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamType();
            final String num = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getNum();
            holder.MovieName.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            holder.movieNameTV.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            String StreamIcon = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon();
            final String movieName = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName();
            holder.MovieImage.setImageDrawable(null);
            if (StreamIcon != null) {
                if (!StreamIcon.equals("")) {
                    Picasso.with(this.context).load(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon()).placeholder((int) R.drawable.noposter).into(holder.MovieImage);
                    if (this.database.checkFavourite(streamId, categoryId, AppConst.VOD).size() <= 0) {
                        holder.ivFavourite.setVisibility(0);
                    } else {
                        holder.ivFavourite.setVisibility(4);
                    }
                    holder.cardView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, selectedPlayer, streamId, streamType, containerExtension, num, movieName);
                        }
                    });
                    i = streamId;
                    str = selectedPlayer;
                    str2 = streamType;
                    str3 = containerExtension;
                    str4 = num;
                    str5 = movieName;
                    final String finalStr = str;
                    final int finalI = i;
                    final String finalStr1 = str2;
                    final String finalStr2 = str3;
                    final String finalStr3 = str4;
                    final String finalStr4 = str5;
                    holder.MovieImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (SubCategoriesChildAdapter.this.getIsXtream1_06(SubCategoriesChildAdapter.this.context)) {
                                z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, finalStr, finalI, finalStr1, finalStr2, finalStr3, finalStr4);
                                return;
                            }
                            SubCategoriesChildAdapter.this.startViewDeatilsActivity(finalI, finalStr4, finalStr, finalStr1, finalStr2, categoryId, finalStr3);
                        }
                    });
                    i = streamId;
                    str = selectedPlayer;
                    str2 = streamType;
                    str3 = containerExtension;
                    str4 = num;
                    str5 = movieName;
                    final String finalStr5 = str;
                    final int finalI1 = i;
                    final String finalStr6 = str2;
                    final String finalStr7 = str3;
                    final String finalStr8 = str4;
                    final String finalStr9 = str5;
                    holder.Movie.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (SubCategoriesChildAdapter.this.getIsXtream1_06(SubCategoriesChildAdapter.this.context)) {
                                z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, finalStr5, finalI1, finalStr6, finalStr7, finalStr8, finalStr9);
                                return;
                            }
                            SubCategoriesChildAdapter.this.startViewDeatilsActivity(finalI1, finalStr9, finalStr5, finalStr6, finalStr7, categoryId, finalStr8);
                        }
                    });
                    myViewHolder = holder;
                    i2 = streamId;
                    str6 = movieName;
                    str7 = selectedPlayer;
                    str8 = streamType;
                    str9 = containerExtension;
                    str10 = num;
                    final MyViewHolder finalMyViewHolder = myViewHolder;
                    final int finalI2 = i2;
                    final String finalStr10 = str6;
                    final String finalStr11 = str7;
                    final String finalStr12 = str8;
                    final String finalStr13 = str9;
                    final String finalStr14 = str10;
                    holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            SubCategoriesChildAdapter.this.popmenu(finalMyViewHolder, finalI2, categoryId, finalStr10, finalStr11, finalStr12, finalStr13, finalStr14);
                            return true;
                        }
                    });
                    myViewHolder = holder;
                    i2 = streamId;
                    str6 = movieName;
                    str7 = selectedPlayer;
                    str8 = streamType;
                    str9 = containerExtension;
                    str10 = num;
                    final MyViewHolder finalMyViewHolder1 = myViewHolder;
                    final int finalI3 = i2;
                    final String finalStr15 = str6;
                    final String finalStr16 = str7;
                    final String finalStr17 = str8;
                    final String finalStr18 = str9;
                    final String finalStr19 = str10;
                    holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            SubCategoriesChildAdapter.this.popmenu(finalMyViewHolder1, finalI3, categoryId, finalStr15, finalStr16, finalStr17, finalStr18, finalStr19);
                            return true;
                        }
                    });
                    myViewHolder = holder;
                    i2 = streamId;
                    str6 = movieName;
                    str7 = selectedPlayer;
                    str8 = streamType;
                    str9 = containerExtension;
                    str10 = num;
                    final MyViewHolder finalMyViewHolder2 = myViewHolder;
                    final int finalI4 = i2;
                    final String finalStr20 = str6;
                    final String finalStr21 = str7;
                    final String finalStr22 = str8;
                    final String finalStr23 = str9;
                    final String finalStr110 = str10;
                    holder.llMenu.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SubCategoriesChildAdapter.this.popmenu(finalMyViewHolder2, finalI4, categoryId, finalStr20, finalStr21, finalStr22, finalStr23, finalStr110);
                        }
                    });
                }
            }
            if (VERSION.SDK_INT >= 21) {
                holder.MovieImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.noposter, null));
            }
            if (this.database.checkFavourite(streamId, categoryId, AppConst.VOD).size() <= 0) {
                holder.ivFavourite.setVisibility(4);
            } else {
                holder.ivFavourite.setVisibility(0);
            }
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, selectedPlayer, streamId, streamType, containerExtension, num, movieName);
                }
            });
            i = streamId;
            str = selectedPlayer;
            str2 = streamType;
            str3 = containerExtension;
            str4 = num;
            str5 = movieName;
            final String finalStr = str;
            final int finalI = i;
            final String finalStr1 = str2;
            final String finalStr2 = str3;
            final String finalStr3 = str4;
            final String finalStr4 = str5;
            holder.MovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (SubCategoriesChildAdapter.this.getIsXtream1_06(SubCategoriesChildAdapter.this.context)) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, finalStr, finalI, finalStr1, finalStr2, finalStr3, finalStr4);
                        return;
                    }
                    SubCategoriesChildAdapter.this.startViewDeatilsActivity(finalI, finalStr4, finalStr, finalStr1, finalStr2, categoryId, finalStr3);
                }
            });
            i = streamId;
            str = selectedPlayer;
            str2 = streamType;
            str3 = containerExtension;
            str4 = num;
            str5 = movieName;
            final String finalStr5 = str;
            final int finalI1 = i;
            final String finalStr6 = str2;
            final String finalStr7 = str3;
            final String finalStr8 = str4;
            final String finalStr9 = str5;
            holder.Movie.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (SubCategoriesChildAdapter.this.getIsXtream1_06(SubCategoriesChildAdapter.this.context)) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, finalStr5, finalI1, finalStr6, finalStr7, finalStr8, finalStr9);
                        return;
                    }
                    SubCategoriesChildAdapter.this.startViewDeatilsActivity(finalI1, finalStr9, finalStr5, finalStr6, finalStr7, categoryId, finalStr8);
                }
            });
            myViewHolder = holder;
            i2 = streamId;
            str6 = movieName;
            str7 = selectedPlayer;
            str8 = streamType;
            str9 = containerExtension;
            str10 = num;
            final MyViewHolder finalMyViewHolder = myViewHolder;
            final int finalI2 = i2;
            final String finalStr10 = str6;
            final String finalStr11 = str7;
            final String finalStr12 = str8;
            final String finalStr13 = str9;
            final String finalStr14 = str10;
            holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    SubCategoriesChildAdapter.this.popmenu(finalMyViewHolder, finalI2, categoryId, finalStr10, finalStr11, finalStr12, finalStr13, finalStr14);
                    return true;
                }
            });
            myViewHolder = holder;
            i2 = streamId;
            str6 = movieName;
            str7 = selectedPlayer;
            str8 = streamType;
            str9 = containerExtension;
            str10 = num;
            final MyViewHolder finalMyViewHolder1 = myViewHolder;
            final int finalI3 = i2;
            final String finalStr15 = str6;
            final String finalStr16 = str7;
            final String finalStr17 = str8;
            final String finalStr18 = str9;
            final String finalStr19 = str10;
            holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    SubCategoriesChildAdapter.this.popmenu(finalMyViewHolder1, finalI3, categoryId, finalStr15, finalStr16, finalStr17, finalStr18, finalStr19);
                    return true;
                }
            });
            myViewHolder = holder;
            i2 = streamId;
            str6 = movieName;
            str7 = selectedPlayer;
            str8 = streamType;
            str9 = containerExtension;
            str10 = num;
            final MyViewHolder finalMyViewHolder2 = myViewHolder;
            final int finalI4 = i2;
            final String finalStr20 = str6;
            final String finalStr21 = str7;
            final String finalStr22 = str8;
            final String finalStr23 = str9;
            final String finalStr110 = str10;
            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SubCategoriesChildAdapter.this.popmenu(finalMyViewHolder2, finalI4, categoryId, finalStr20, finalStr21, finalStr22, finalStr23, finalStr110);
                }
            });
        }
    }

    private boolean getIsXtream1_06(Context context) {
        if (context == null) {
            return false;
        }
        this.loginPrefXtream = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IS_XTREAM_1_06, 0);
        return this.loginPrefXtream.getBoolean(AppConst.IS_XTREAM_1_06, false);
    }

    private void startViewDeatilsActivity(int streamId, String movieName, String selectedPlayer, String streamType, String containerExtension, String categoryId, String num) {
        if (this.context != null) {
            Intent viewDetailsActivityIntent = new Intent(this.context, ViewDetailsActivity.class);
            viewDetailsActivityIntent.putExtra(AppConst.STREAM_ID, String.valueOf(streamId));
            viewDetailsActivityIntent.putExtra("movie", movieName);
            viewDetailsActivityIntent.putExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER, selectedPlayer);
            viewDetailsActivityIntent.putExtra("streamType", streamType);
            viewDetailsActivityIntent.putExtra("containerExtension", containerExtension);
            viewDetailsActivityIntent.putExtra("categoryID", categoryId);
            viewDetailsActivityIntent.putExtra("num", num);
            this.context.startActivity(viewDetailsActivityIntent);
        }
    }

    private void popmenu(MyViewHolder holder, int streamId, String categoryId, String movieName, String selectedPlayer, String streamType, String containerExtension, String num) {
        PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
        popup.inflate(R.menu.menu_card_vod);
        if (getIsXtream1_06(this.context)) {
            popup.getMenu().removeItem(R.id.menu_view_details);
        }
        if (this.database.checkFavourite(streamId, categoryId, AppConst.VOD).size() > 0) {
            popup.getMenu().getItem(2).setVisible(true);
        } else {
            popup.getMenu().getItem(1).setVisible(true);
        }
        final int i = streamId;
        final String str = movieName;
        final String str2 = selectedPlayer;
        final String str3 = streamType;
        final String str4 = containerExtension;
        final String str5 = categoryId;
        final String str6 = num;
        final MyViewHolder myViewHolder = holder;
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_view_details:
                        startViewDeatilsActivity(i, str, str2, str3, str4, str5, str6);
                        break;
                    case R.id.nav_add_to_fav:
                        addToFavourite();
                        break;
                    case R.id.nav_play:
                        playMovie();
                        break;
                    case R.id.nav_remove_from_fav:
                        removeFromFavourite();
                        break;
                }
                return false;
            }

            private void startViewDeatilsActivity(int streamId, String movieName, String selectedPlayer, String streamType, String containerExtension, String categoryId, String num) {
                if (SubCategoriesChildAdapter.this.context != null) {
                    Intent viewDetailsActivityIntent = new Intent(SubCategoriesChildAdapter.this.context, ViewDetailsActivity.class);
                    viewDetailsActivityIntent.putExtra(AppConst.STREAM_ID, String.valueOf(streamId));
                    viewDetailsActivityIntent.putExtra("movie", movieName);
                    viewDetailsActivityIntent.putExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER, selectedPlayer);
                    viewDetailsActivityIntent.putExtra("streamType", streamType);
                    viewDetailsActivityIntent.putExtra("containerExtension", containerExtension);
                    viewDetailsActivityIntent.putExtra("categoryID", categoryId);
                    viewDetailsActivityIntent.putExtra("num", num);
                    SubCategoriesChildAdapter.this.context.startActivity(viewDetailsActivityIntent);
                }
            }

            private void playMovie() {
                myViewHolder.cardView.performClick();
            }

            private void addToFavourite() {
                FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel(0, null, null);
                LiveStreamsFavourite.setCategoryID(str5);
                LiveStreamsFavourite.setStreamID(i);
                SubCategoriesChildAdapter.this.database.addToFavourite(LiveStreamsFavourite, AppConst.VOD);
                myViewHolder.ivFavourite.setVisibility(0);
            }

            private void removeFromFavourite() {
                SubCategoriesChildAdapter.this.database.deleteFavourite(i, str5, AppConst.VOD);
                myViewHolder.ivFavourite.setVisibility(4);
            }
        });
        popup.show();
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C19751 implements Runnable {
                C19751() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SubCategoriesChildAdapter.this.dataSet = SubCategoriesChildAdapter.this.completeList;
                    } else if (!SubCategoriesChildAdapter.this.filterList.isEmpty() || SubCategoriesChildAdapter.this.filterList.isEmpty()) {
                        SubCategoriesChildAdapter.this.dataSet = SubCategoriesChildAdapter.this.filterList;
                    }
                    if (SubCategoriesChildAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    SubCategoriesChildAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SubCategoriesChildAdapter.this.filterList = new ArrayList();
                if (SubCategoriesChildAdapter.this.filterList != null) {
                    SubCategoriesChildAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SubCategoriesChildAdapter.this.filterList.addAll(SubCategoriesChildAdapter.this.completeList);
                } else {
                    for (LiveStreamsDBModel item : SubCategoriesChildAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            SubCategoriesChildAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) SubCategoriesChildAdapter.this.context).runOnUiThread(new C19751());
            }
        }).start();
    }
}
