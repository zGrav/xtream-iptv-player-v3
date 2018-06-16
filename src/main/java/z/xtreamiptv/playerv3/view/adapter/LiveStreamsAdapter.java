package z.xtreamiptv.playerv3.view.adapter;

import android.app.Activity;
import android.content.Context;
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
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LiveStreamsAdapter extends Adapter<LiveStreamsAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> dataSet;
    private DatabaseHandler database;
    private Editor editor;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
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
        @BindView(R.id.tv_movie_name)
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

    public LiveStreamsAdapter(List<LiveStreamsDBModel> liveStreamCategories, Context context) {
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
        View view;
        if (AppConst.LIVE_FLAG == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_layout, parent, false);
            if (view != null) {
                this.myViewHolder = new MyViewHolder(view);
            } else {
                this.myViewHolder = new MyViewHolder(view);
            }
            return this.myViewHolder;
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_linear_layout, parent, false);
        if (view != null) {
            this.myViewHolder = new MyViewHolder(view);
        } else {
            this.myViewHolder = new MyViewHolder(view);
        }
        return this.myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            final int streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId().trim());
            final String categoryId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getCategoryId();
            final String streamType = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamType();
            String epgChannelID = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getEpgChannelId();
            holder.tvTime.setText("");
            holder.progressBar.setVisibility(8);
            holder.tvCurrentLive.setText("");
            if (epgChannelID != null) {
                if (!(epgChannelID.equals("") || this.liveStreamDBHandler == null)) {
                    ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(epgChannelID);
                    String Title = "";
                    if (xmltvProgrammePojos != null) {
                        for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                            String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                            String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                            Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                            String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
                            Long epgStartDateToTimestamp = Long.valueOf(z.xtreamiptv.playerv3.miscelleneious.common.Utils.epgTimeConverter(startDateTime));
                            Long epgStopDateToTimestamp = Long.valueOf(z.xtreamiptv.playerv3.miscelleneious.common.Utils.epgTimeConverter(stopDateTime));
                            if (z.xtreamiptv.playerv3.miscelleneious.common.Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context)) {
                                int epgPercentage = z.xtreamiptv.playerv3.miscelleneious.common.Utils.getPercentageLeft(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context);
                                if (epgPercentage != 0) {
                                    epgPercentage = 100 - epgPercentage;
                                    if (epgPercentage == 0 || Title.equals("")) {
                                        holder.tvTime.setVisibility(8);
                                        holder.progressBar.setVisibility(8);
                                        holder.tvCurrentLive.setVisibility(8);
                                    } else {
                                        if (AppConst.LIVE_FLAG == 0) {
                                            holder.tvTime.setVisibility(0);
                                            loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
                                            this.programTimeFormat = new SimpleDateFormat(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
                                            holder.tvTime.setText(this.programTimeFormat.format(epgStartDateToTimestamp) + " - " + this.programTimeFormat.format(epgStopDateToTimestamp));
                                        }
                                        holder.progressBar.setVisibility(0);
                                        holder.progressBar.setProgress(epgPercentage);
                                        holder.tvCurrentLive.setVisibility(0);
                                        holder.tvCurrentLive.setText(Title);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final String num = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getNum();
            final String name = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName();
            holder.tvChannelName.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            final String StreamIcon = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon();
            final String epgChannelId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getEpgChannelId();
            holder.ivChannelLogo.setImageDrawable(null);
            if (StreamIcon != null && !StreamIcon.equals("")) {
                Picasso.with(this.context).load(StreamIcon).placeholder((int) R.drawable.iptv_placeholder).into(holder.ivChannelLogo);
            } else if (VERSION.SDK_INT >= 21) {
                holder.ivChannelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.iptv_placeholder, null));
            } else {
                holder.ivChannelLogo.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.iptv_placeholder));
            }
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayer(LiveStreamsAdapter.this.context, selectedPlayer, streamId, streamType, num, name, epgChannelId, StreamIcon);
                }
            });
            holder.rlMovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayer(LiveStreamsAdapter.this.context, selectedPlayer, streamId, streamType, num, name, epgChannelId, StreamIcon);
                }
            });
            holder.rlStreamsLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayer(LiveStreamsAdapter.this.context, selectedPlayer, streamId, streamType, num, name, epgChannelId, StreamIcon);
                }
            });
            ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(streamId, categoryId, "live");
            if (checkFavourite == null || checkFavourite.size() <= 0) {
                holder.ivFavourite.setVisibility(4);
            } else {
                holder.ivFavourite.setVisibility(0);
            }
            MyViewHolder myViewHolder = holder;
            final MyViewHolder finalMyViewHolder1 = myViewHolder;
            holder.rlStreamsLayout.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    LiveStreamsAdapter.this.popmenu(finalMyViewHolder1, streamId, categoryId);
                    return true;
                }
            });
            myViewHolder = holder;
            final MyViewHolder finalMyViewHolder2 = myViewHolder;
            holder.rlMovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    LiveStreamsAdapter.this.popmenu(finalMyViewHolder2, streamId, categoryId);
                    return true;
                }
            });
            myViewHolder = holder;
            final MyViewHolder finalMyViewHolder = myViewHolder;
            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    LiveStreamsAdapter.this.popmenu(finalMyViewHolder, streamId, categoryId);
                }
            });
        }
    }

    private void popmenu(final MyViewHolder holder, final int streamId, final String categoryId) {
        PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
        popup.inflate(R.menu.menu_card_live_streams);
        if (this.database.checkFavourite(streamId, categoryId, "live").size() > 0) {
            popup.getMenu().getItem(2).setVisible(true);
        } else {
            popup.getMenu().getItem(1).setVisible(true);
        }
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_add_to_fav:
                        addToFavourite();
                        break;
                    case R.id.nav_play:
                        playChannel();
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
                LiveStreamsAdapter.this.database.addToFavourite(LiveStreamsFavourite, "live");
                holder.ivFavourite.setVisibility(0);
            }

            private void removeFromFavourite() {
                LiveStreamsAdapter.this.database.deleteFavourite(streamId, categoryId, "live");
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

            class C19441 implements Runnable {
                C19441() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        LiveStreamsAdapter.this.dataSet = LiveStreamsAdapter.this.completeList;
                    } else if (!LiveStreamsAdapter.this.filterList.isEmpty() || LiveStreamsAdapter.this.filterList.isEmpty()) {
                        LiveStreamsAdapter.this.dataSet = LiveStreamsAdapter.this.filterList;
                    }
                    if (LiveStreamsAdapter.this.dataSet != null && LiveStreamsAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    LiveStreamsAdapter.this.text_last_size = LiveStreamsAdapter.this.text_size;
                    LiveStreamsAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                LiveStreamsAdapter.this.filterList = new ArrayList();
                LiveStreamsAdapter.this.text_size = text.length();
                if (LiveStreamsAdapter.this.filterList != null) {
                    LiveStreamsAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    LiveStreamsAdapter.this.filterList.addAll(LiveStreamsAdapter.this.completeList);
                } else {
                    if ((LiveStreamsAdapter.this.dataSet != null && LiveStreamsAdapter.this.dataSet.size() == 0) || LiveStreamsAdapter.this.text_last_size > LiveStreamsAdapter.this.text_size) {
                        LiveStreamsAdapter.this.dataSet = LiveStreamsAdapter.this.completeList;
                    }
                    if (LiveStreamsAdapter.this.dataSet != null) {
                        for (LiveStreamsDBModel item : LiveStreamsAdapter.this.dataSet) {
                            if (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) {
                                LiveStreamsAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) LiveStreamsAdapter.this.context).runOnUiThread(new C19441());
            }
        }).start();
    }
}
