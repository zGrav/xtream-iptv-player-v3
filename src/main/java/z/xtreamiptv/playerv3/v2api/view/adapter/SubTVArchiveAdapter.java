package z.xtreamiptv.playerv3.v2api.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SubTVArchiveAdapter extends Adapter<SubTVArchiveAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private Context context;
    String currentFormatDateAfter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date());
    private ArrayList<XMLTVProgrammePojo> dataSet;
    private final String getActiveLiveStreamCategoryId;
    private SharedPreferences loginPreferencesSharedPref;
    private final int nowPlaying;
    private final boolean nowPlayingFlag;
    private final String streamChannelDuration;
    private final String streamChannelID;
    private final int streamID;
    private final String streamIcon;
    private final String streamName;
    private final String streamNum;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.ll_main_layout)
        LinearLayout ll_main_layout;
        @BindView(R.id.rl_archive_layout)
        RelativeLayout rl_archive_layout;
        @BindView(R.id.tv_channel_name)
        TextView tvChannelName;
        @BindView(R.id.tv_date_time)
        TextView tvDateTime;
        @BindView(R.id.tv_now_playing)
        TextView tvNowPlaying;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

//    public class MyViewHolder_ViewBinding implements Unbinder {
//        private MyViewHolder target;
//
//        @UiThread
//        public MyViewHolder_ViewBinding(MyViewHolder target, View source) {
//            this.target = target;
//            target.tvDateTime = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_date_time, "field 'tvDateTime'", TextView.class);
//            target.tvChannelName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_channel_name, "field 'tvChannelName'", TextView.class);
//            target.tvNowPlaying = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_now_playing, "field 'tvNowPlaying'", TextView.class);
//            target.rl_archive_layout = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_archive_layout, "field 'rl_archive_layout'", RelativeLayout.class);
//            target.ll_main_layout = (LinearLayout) Utils.findRequiredViewAsType(source, R.id.ll_main_layout, "field 'll_main_layout'", LinearLayout.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MyViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.tvDateTime = null;
//            target.tvChannelName = null;
//            target.tvNowPlaying = null;
//            target.rl_archive_layout = null;
//            target.ll_main_layout = null;
//        }
//    }

    public SubTVArchiveAdapter(ArrayList liveStreamsEpg, int nowPlayingPosition, boolean nowPlayingFlag, String getActiveLiveStreamCategoryId, int getActiveLiveStreamId, String getActiveLiveStreamNum, String getActiveLiveStreamName, String getActiveLiveStreamIcon, String getActiveLiveStreamChannelId, String getActiveLiveStreamChannelDuration, Context context) {
        this.dataSet = liveStreamsEpg;
        this.context = context;
        this.nowPlaying = nowPlayingPosition;
        this.getActiveLiveStreamCategoryId = getActiveLiveStreamCategoryId;
        this.nowPlayingFlag = nowPlayingFlag;
        this.streamID = getActiveLiveStreamId;
        this.streamNum = getActiveLiveStreamNum;
        this.streamName = getActiveLiveStreamName;
        this.streamIcon = getActiveLiveStreamIcon;
        this.streamChannelID = getActiveLiveStreamChannelId;
        this.streamChannelDuration = getActiveLiveStreamChannelDuration;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_epg_layout, parent, false);
        if (view != null) {
            view.setFocusable(true);
        }
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            String getStart = ((XMLTVProgrammePojo) this.dataSet.get(listPosition)).getStart();
            String getStop = ((XMLTVProgrammePojo) this.dataSet.get(listPosition)).getStop();
            Long epgDateToTimestamp = Long.valueOf(z.xtreamiptv.playerv3.miscelleneious.common.Utils.epgTimeConverter(getStart));
            Long epgDateToTimestampStop = Long.valueOf(z.xtreamiptv.playerv3.miscelleneious.common.Utils.epgTimeConverter(getStop));
            final String getStopTime = String.valueOf((epgDateToTimestampStop.longValue() - epgDateToTimestamp.longValue()) / ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
            loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
            String timeFormat = loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, "");
            String getTime = new SimpleDateFormat(timeFormat, Locale.US).format(epgDateToTimestamp);
            String getTimeStop = new SimpleDateFormat(timeFormat, Locale.US).format(epgDateToTimestampStop);
            final String getStartFormatedTime = new SimpleDateFormat("yyyy-MM-dd:HH-mm", Locale.US).format(epgDateToTimestamp);
            String getTitle = ((XMLTVProgrammePojo) this.dataSet.get(listPosition)).getTitle();
            holder.tvDateTime.setText(getTime + " - " + getTimeStop);
            holder.tvChannelName.setText(getTitle);
            if (this.currentFormatDateAfter == null || !this.currentFormatDateAfter.equals(this.getActiveLiveStreamCategoryId)) {
                holder.rl_archive_layout.setBackgroundColor(this.context.getResources().getColor(R.color.white));
            } else if (listPosition == this.nowPlaying && this.nowPlayingFlag) {
                holder.rl_archive_layout.setBackgroundColor(this.context.getResources().getColor(R.color.active_green));
            } else {
                holder.rl_archive_layout.setBackgroundColor(this.context.getResources().getColor(R.color.white));
            }
            holder.rl_archive_layout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerArchive(SubTVArchiveAdapter.this.context, selectedPlayer, SubTVArchiveAdapter.this.streamID, SubTVArchiveAdapter.this.streamNum, SubTVArchiveAdapter.this.streamName, SubTVArchiveAdapter.this.streamChannelID, SubTVArchiveAdapter.this.streamIcon, getStartFormatedTime, SubTVArchiveAdapter.this.streamChannelDuration, getStopTime);
                }
            });
            holder.ll_main_layout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayerArchive(SubTVArchiveAdapter.this.context, selectedPlayer, SubTVArchiveAdapter.this.streamID, SubTVArchiveAdapter.this.streamNum, SubTVArchiveAdapter.this.streamName, SubTVArchiveAdapter.this.streamChannelID, SubTVArchiveAdapter.this.streamIcon, getStartFormatedTime, SubTVArchiveAdapter.this.streamChannelDuration, getStopTime);
                }
            });
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(String text, TextView tvNoRecordFound) {
    }
}
