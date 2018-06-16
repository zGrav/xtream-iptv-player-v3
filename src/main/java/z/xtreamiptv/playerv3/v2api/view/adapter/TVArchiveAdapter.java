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
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import z.xtreamiptv.playerv3.v2api.view.activity.SubTVArchiveActivity;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TVArchiveAdapter extends Adapter<TVArchiveAdapter.MyViewHolder> {
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

    public TVArchiveAdapter(List<LiveStreamsDBModel> liveStreamCategories, Context context) {
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
            String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            final int streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId().trim());
            String categoryId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getCategoryId();
            String streamType = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamType();
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
            final String tvArchiveDuration = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getTvArchiveDuration();
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
                    Intent TVArchiveIntent = new Intent(TVArchiveAdapter.this.context, SubTVArchiveActivity.class);
                    TVArchiveIntent.putExtra("OPENED_CHANNEL_ID", epgChannelId);
                    TVArchiveIntent.putExtra("OPENED_STREAM_ID", streamId);
                    TVArchiveIntent.putExtra("OPENED_NUM", num);
                    TVArchiveIntent.putExtra("OPENED_NAME", name);
                    TVArchiveIntent.putExtra("OPENED_STREAM_ICON", StreamIcon);
                    TVArchiveIntent.putExtra("OPENED_ARCHIVE_DURATION", tvArchiveDuration);
                    TVArchiveAdapter.this.context.startActivity(TVArchiveIntent);
                }
            });
            holder.rlMovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent TVArchiveIntent = new Intent(TVArchiveAdapter.this.context, SubTVArchiveActivity.class);
                    TVArchiveIntent.putExtra("OPENED_CHANNEL_ID", epgChannelId);
                    TVArchiveIntent.putExtra("OPENED_STREAM_ID", streamId);
                    TVArchiveIntent.putExtra("OPENED_NUM", num);
                    TVArchiveIntent.putExtra("OPENED_NAME", name);
                    TVArchiveIntent.putExtra("OPENED_STREAM_ICON", StreamIcon);
                    TVArchiveIntent.putExtra("OPENED_ARCHIVE_DURATION", tvArchiveDuration);
                    TVArchiveAdapter.this.context.startActivity(TVArchiveIntent);
                }
            });
            holder.rlStreamsLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent TVArchiveIntent = new Intent(TVArchiveAdapter.this.context, SubTVArchiveActivity.class);
                    TVArchiveIntent.putExtra("OPENED_CHANNEL_ID", epgChannelId);
                    TVArchiveIntent.putExtra("OPENED_STREAM_ID", streamId);
                    TVArchiveIntent.putExtra("OPENED_NUM", num);
                    TVArchiveIntent.putExtra("OPENED_NAME", name);
                    TVArchiveIntent.putExtra("OPENED_STREAM_ICON", StreamIcon);
                    TVArchiveIntent.putExtra("OPENED_ARCHIVE_DURATION", tvArchiveDuration);
                    TVArchiveAdapter.this.context.startActivity(TVArchiveIntent);
                }
            });
            ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(streamId, categoryId, "live");
            if (checkFavourite == null || checkFavourite.size() <= 0) {
                holder.ivFavourite.setVisibility(4);
            } else {
                holder.ivFavourite.setVisibility(0);
            }
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C16221 implements Runnable {
                C16221() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        TVArchiveAdapter.this.dataSet = TVArchiveAdapter.this.completeList;
                    } else if (!TVArchiveAdapter.this.filterList.isEmpty() || TVArchiveAdapter.this.filterList.isEmpty()) {
                        TVArchiveAdapter.this.dataSet = TVArchiveAdapter.this.filterList;
                    }
                    if (TVArchiveAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    TVArchiveAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                TVArchiveAdapter.this.filterList = new ArrayList();
                if (TVArchiveAdapter.this.filterList != null) {
                    TVArchiveAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    TVArchiveAdapter.this.filterList.addAll(TVArchiveAdapter.this.completeList);
                } else {
                    for (LiveStreamsDBModel item : TVArchiveAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            TVArchiveAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) TVArchiveAdapter.this.context).runOnUiThread(new C16221());
            }
        }).start();
    }
}
