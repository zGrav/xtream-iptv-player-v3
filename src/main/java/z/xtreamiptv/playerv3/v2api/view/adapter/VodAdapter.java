package z.xtreamiptv.playerv3.v2api.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
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
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.v2api.view.activity.ViewDetailsActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class VodAdapter extends Adapter<VodAdapter.MyViewHolder> {
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> dataSet;
    DatabaseHandler database;
    private Editor editor;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
    private SharedPreferences loginPrefXtream;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences pref;
    private SharedPreferences settingsPrefs;
    public int text_last_size;
    public int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_movie)
        RelativeLayout Movie;
        @BindView(R.id.iv_movie_image)
        ImageView MovieImage;
        @BindView(R.id.tv_movie_name)
        TextView MovieName;
        @BindView(R.id.card_view)
        CardView cardView;
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
//            target.movieNameTV = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name1, "field 'movieNameTV'", TextView.class);
//            target.Movie = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie, "field 'Movie'", RelativeLayout.class);
//            target.MovieImage = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_movie_image, "field 'MovieImage'", ImageView.class);
//            target.cardView = (CardView) Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", CardView.class);
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
//            target.movieNameTV = null;
//            target.Movie = null;
//            target.MovieImage = null;
//            target.cardView = null;
//            target.tvStreamOptions = null;
//            target.ivFavourite = null;
//            target.rlMovieBottom = null;
//            target.llMenu = null;
//        }
//    }

    public VodAdapter(List<LiveStreamsDBModel> vodCategories, Context context) {
        this.dataSet = vodCategories;
        this.context = context;
        this.filterList.addAll(vodCategories);
        this.completeList = vodCategories;
        this.database = new DatabaseHandler(context);
    }


    public z.xtreamiptv.playerv3.v2api.view.adapter.VodAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        this.pref = this.context.getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        z.xtreamiptv.playerv3.miscelleneious.common.AppConst.LIVE_FLAG_VOD = this.pref.getInt(AppConst.VOD, 0);
        if (z.xtreamiptv.playerv3.miscelleneious.common.AppConst.LIVE_FLAG_VOD == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_linear_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_layout, parent, false);
            if (view != null) {
            }
        }
        return new MyViewHolder(view);    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            int finalStreamId2;
            int finalStreamId6;
            int finalStreamId1;
            int finalStreamId;
            int finalStreamId4 = 0;
            MyViewHolder myViewHolder;
            String str;
            String str2;
            String str3;
            String str4;
            String str5;
            String str6;
            int finalStreamId3;
            MyViewHolder myViewHolder2;
            String str7;
            String str8;
            String str9;
            String str10;
            int finalStreamId5;
            MyViewHolder myViewHolder3;
            String str11;
            String str12;
            String str13;
            String str14;
            Context context = this.context;
            String str15 = AppConst.LOGIN_PREF_SELECTED_PLAYER;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str15, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            int streamId = 0;
            if (((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId() != null) {
                streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId());
            }
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
                    finalStreamId2 = streamId;
                    finalStreamId6 = streamId;
                    final int finalStreamId7 = finalStreamId2;
                    final int finalStreamId10 = finalStreamId6;
                    holder.cardView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (VodAdapter.this.getIsXtream1_06(VodAdapter.this.context)) {
                                z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, selectedPlayer, finalStreamId10, streamType, containerExtension, num, movieName);
                                return;
                            }
                            VodAdapter.this.startViewDeatilsActivity(finalStreamId7, movieName, selectedPlayer, streamType, containerExtension, categoryId, num);
                        }
                    });
                    finalStreamId1 = streamId;
                    final int finalStreamId8 = finalStreamId2;
                    holder.MovieImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (VodAdapter.this.getIsXtream1_06(VodAdapter.this.context)) {
                                z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, selectedPlayer, finalStreamId10, streamType, containerExtension, num, movieName);
                                return;
                            }
                            VodAdapter.this.startViewDeatilsActivity(finalStreamId8, movieName, selectedPlayer, streamType, containerExtension, categoryId, num);
                        }
                    });
                    finalStreamId = streamId;
                    final int finalStreamId9 = finalStreamId2;
                    holder.Movie.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (VodAdapter.this.getIsXtream1_06(VodAdapter.this.context)) {
                                z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, selectedPlayer, finalStreamId10, streamType, containerExtension, num, movieName);
                                return;
                            }
                            VodAdapter.this.startViewDeatilsActivity(finalStreamId9, movieName, selectedPlayer, streamType, containerExtension, categoryId, num);
                        }
                    });
                    finalStreamId4 = streamId;
                    myViewHolder = holder;
                    str = categoryId;
                    str2 = movieName;
                    str3 = selectedPlayer;
                    str4 = streamType;
                    str5 = containerExtension;
                    str6 = num;
                    final MyViewHolder finalMyViewHolder = myViewHolder;
                    final int finalStreamId11 = finalStreamId4;
                    final String finalStr = str;
                    final String finalStr1 = str2;
                    final String finalStr2 = str3;
                    final String finalStr3 = str4;
                    final String finalStr4 = str5;
                    final String finalStr5 = str6;
                    holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            VodAdapter.this.popmenu(finalMyViewHolder, finalStreamId11, finalStr, finalStr1, finalStr2, finalStr3, finalStr4, finalStr5);
                            return true;
                        }
                    });
                    finalStreamId3 = streamId;
                    myViewHolder2 = holder;
                    str5 = categoryId;
                    str6 = movieName;
                    str7 = selectedPlayer;
                    str8 = streamType;
                    str9 = containerExtension;
                    str10 = num;
                    final MyViewHolder finalMyViewHolder1 = myViewHolder2;
                    final int finalStreamId12 = finalStreamId3;
                    final String finalStr6 = str5;
                    final String finalStr7 = str6;
                    final String finalStr8 = str7;
                    final String finalStr9 = str8;
                    final String finalStr10 = str9;
                    final String finalStr11 = str10;
                    holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            VodAdapter.this.popmenu(finalMyViewHolder1, finalStreamId12, finalStr6, finalStr7, finalStr8, finalStr9, finalStr10, finalStr11);
                            return true;
                        }
                    });
                    myViewHolder2 = holder;
                    str5 = categoryId;
                    str6 = movieName;
                    str7 = selectedPlayer;
                    str8 = streamType;
                    str9 = containerExtension;
                    str10 = num;
                    final MyViewHolder finalMyViewHolder2 = myViewHolder2;
                    final int finalStreamId13 = finalStreamId3;
                    final String finalStr12 = str5;
                    final String finalStr13 = str6;
                    final String finalStr14 = str7;
                    final String finalStr15 = str8;
                    final String finalStr16 = str9;
                    final String finalStr17 = str10;
                    holder.cardView.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            VodAdapter.this.popmenu(finalMyViewHolder2, finalStreamId13, finalStr12, finalStr13, finalStr14, finalStr15, finalStr16, finalStr17);
                            return true;
                        }
                    });
                    finalStreamId5 = streamId;
                    myViewHolder3 = holder;
                    str9 = categoryId;
                    str10 = movieName;
                    str11 = selectedPlayer;
                    str12 = streamType;
                    str13 = containerExtension;
                    str14 = num;
                    final MyViewHolder finalMyViewHolder3 = myViewHolder3;
                    final int finalStreamId14 = finalStreamId5;
                    final String finalStr18 = str9;
                    final String finalStr19 = str10;
                    final String finalStr20 = str11;
                    final String finalStr21 = str12;
                    final String finalStr22 = str13;
                    final String finalStr23 = str14;
                    holder.llMenu.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            VodAdapter.this.popmenu(finalMyViewHolder3, finalStreamId14, finalStr18, finalStr19, finalStr20, finalStr21, finalStr22, finalStr23);
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
            finalStreamId2 = streamId;
            finalStreamId6 = streamId;
            final int finalStreamId7 = finalStreamId2;
            final int finalStreamId10 = finalStreamId6;
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (VodAdapter.this.getIsXtream1_06(VodAdapter.this.context)) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, selectedPlayer, finalStreamId10, streamType, containerExtension, num, movieName);
                        return;
                    }
                    VodAdapter.this.startViewDeatilsActivity(finalStreamId7, movieName, selectedPlayer, streamType, containerExtension, categoryId, num);
                }
            });
            finalStreamId1 = streamId;
            final int finalStreamId8 = finalStreamId2;
            holder.MovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (VodAdapter.this.getIsXtream1_06(VodAdapter.this.context)) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, selectedPlayer, finalStreamId10, streamType, containerExtension, num, movieName);
                        return;
                    }
                    VodAdapter.this.startViewDeatilsActivity(finalStreamId8, movieName, selectedPlayer, streamType, containerExtension, categoryId, num);
                }
            });
            finalStreamId = streamId;
            final int finalStreamId9 = finalStreamId2;
            holder.Movie.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (VodAdapter.this.getIsXtream1_06(VodAdapter.this.context)) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, selectedPlayer, finalStreamId10, streamType, containerExtension, num, movieName);
                        return;
                    }
                    VodAdapter.this.startViewDeatilsActivity(finalStreamId9, movieName, selectedPlayer, streamType, containerExtension, categoryId, num);
                }
            });
            myViewHolder = holder;
            str = categoryId;
            str2 = movieName;
            str3 = selectedPlayer;
            str4 = streamType;
            str5 = containerExtension;
            str6 = num;
            final MyViewHolder finalMyViewHolder = myViewHolder;
            final int finalStreamId11 = finalStreamId4;
            final String finalStr = str;
            final String finalStr1 = str2;
            final String finalStr2 = str3;
            final String finalStr3 = str4;
            final String finalStr4 = str5;
            final String finalStr5 = str6;
            holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    VodAdapter.this.popmenu(finalMyViewHolder, finalStreamId11, finalStr, finalStr1, finalStr2, finalStr3, finalStr4, finalStr5);
                    return true;
                }
            });
            finalStreamId3 = streamId;
            myViewHolder2 = holder;
            str5 = categoryId;
            str6 = movieName;
            str7 = selectedPlayer;
            str8 = streamType;
            str9 = containerExtension;
            str10 = num;
            final MyViewHolder finalMyViewHolder1 = myViewHolder2;
            final int finalStreamId12 = finalStreamId3;
            final String finalStr6 = str5;
            final String finalStr7 = str6;
            final String finalStr8 = str7;
            final String finalStr9 = str8;
            final String finalStr10 = str9;
            final String finalStr11 = str10;
            holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    VodAdapter.this.popmenu(finalMyViewHolder1, finalStreamId12, finalStr6, finalStr7, finalStr8, finalStr9, finalStr10, finalStr11);
                    return true;
                }
            });
            myViewHolder2 = holder;
            str5 = categoryId;
            str6 = movieName;
            str7 = selectedPlayer;
            str8 = streamType;
            str9 = containerExtension;
            str10 = num;
            final MyViewHolder finalMyViewHolder2 = myViewHolder2;
            final int finalStreamId13 = finalStreamId3;
            final String finalStr12 = str5;
            final String finalStr13 = str6;
            final String finalStr14 = str7;
            final String finalStr15 = str8;
            final String finalStr16 = str9;
            final String finalStr17 = str10;
            holder.cardView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    VodAdapter.this.popmenu(finalMyViewHolder2, finalStreamId13, finalStr12, finalStr13, finalStr14, finalStr15, finalStr16, finalStr17);
                    return true;
                }
            });
            finalStreamId5 = streamId;
            myViewHolder3 = holder;
            str9 = categoryId;
            str10 = movieName;
            str11 = selectedPlayer;
            str12 = streamType;
            str13 = containerExtension;
            str14 = num;
            final MyViewHolder finalMyViewHolder3 = myViewHolder3;
            final int finalStreamId14 = finalStreamId5;
            final String finalStr18 = str9;
            final String finalStr19 = str10;
            final String finalStr20 = str11;
            final String finalStr21 = str12;
            final String finalStr22 = str13;
            final String finalStr23 = str14;
            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    VodAdapter.this.popmenu(finalMyViewHolder3, finalStreamId14, finalStr18, finalStr19, finalStr20, finalStr21, finalStr22, finalStr23);
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
        if (this.context != null) {
            PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
            boolean isXteam1_06 = getIsXtream1_06(this.context);
            popup.inflate(R.menu.menu_card_vod);
            if (isXteam1_06) {
                popup.getMenu().removeItem(R.id.menu_view_details);
            }
            ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(streamId, categoryId, AppConst.VOD);
            if (checkFavourite == null || checkFavourite.size() <= 0) {
                popup.getMenu().getItem(1).setVisible(true);
            } else {
                popup.getMenu().getItem(2).setVisible(true);
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
                    if (VodAdapter.this.context != null) {
                        Intent viewDetailsActivityIntent = new Intent(VodAdapter.this.context, ViewDetailsActivity.class);
                        viewDetailsActivityIntent.putExtra(AppConst.STREAM_ID, String.valueOf(streamId));
                        viewDetailsActivityIntent.putExtra("movie", movieName);
                        viewDetailsActivityIntent.putExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER, selectedPlayer);
                        viewDetailsActivityIntent.putExtra("streamType", streamType);
                        viewDetailsActivityIntent.putExtra("containerExtension", containerExtension);
                        viewDetailsActivityIntent.putExtra("categoryID", categoryId);
                        viewDetailsActivityIntent.putExtra("num", num);
                        VodAdapter.this.context.startActivity(viewDetailsActivityIntent);
                    }
                }

                private void playMovie() {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerVOD(VodAdapter.this.context, str2, i, str3, str4, str6, str);
                }

                private void addToFavourite() {
                    FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel(0, null, null);
                    LiveStreamsFavourite.setCategoryID(str5);
                    LiveStreamsFavourite.setStreamID(i);
                    VodAdapter.this.database.addToFavourite(LiveStreamsFavourite, AppConst.VOD);
                    myViewHolder.ivFavourite.setVisibility(0);
                }

                private void removeFromFavourite() {
                    VodAdapter.this.database.deleteFavourite(i, str5, AppConst.VOD);
                    myViewHolder.ivFavourite.setVisibility(4);
                }
            });
            popup.show();
        }
    }

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(8);
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C16321 implements Runnable {
                C16321() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        VodAdapter.this.dataSet = VodAdapter.this.completeList;
                    } else if (!VodAdapter.this.filterList.isEmpty() || VodAdapter.this.filterList.isEmpty()) {
                        VodAdapter.this.dataSet = VodAdapter.this.filterList;
                    }
                    if (VodAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    VodAdapter.this.text_last_size = VodAdapter.this.text_size;
                    VodAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                VodAdapter.this.filterList = new ArrayList();
                VodAdapter.this.text_size = text.length();
                if (VodAdapter.this.filterList != null) {
                    VodAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    VodAdapter.this.filterList.addAll(VodAdapter.this.completeList);
                } else {
                    if (VodAdapter.this.dataSet.size() == 0 || VodAdapter.this.text_last_size > VodAdapter.this.text_size) {
                        VodAdapter.this.dataSet = VodAdapter.this.completeList;
                    }
                    for (LiveStreamsDBModel item : VodAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            VodAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) VodAdapter.this.context).runOnUiThread(new C16321());
            }
        }).start();
    }
}
