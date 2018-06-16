package z.xtreamiptv.playerv3.v2api.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class LiveStreamListViewAdapter extends Adapter<LiveStreamListViewAdapter.MyViewHolder> {
    String StreamIcon = "";
    String categoryId = "";
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> dataSet;
    private DatabaseHandler database;
    String epgChannelID = "";
    private List<LiveStreamsDBModel> filterList;
    private SharedPreferences loginPreferencesSharedPref;
    MyViewHolder myViewHolder;
    int streamId = -1;
    String streamType = "";

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_channel_logo)
        ImageView ivChannelLogo;
        @BindView(R.id.iv_favourite)
        ImageView ivFavourite;
        @BindView(R.id.rl_channel_bottom)
        RelativeLayout rlChannelBottom;
        @BindView(R.id.rl_streams_layout)
        RelativeLayout rlStreamsLayout;
        @BindView(R.id.tv_channel_name)
        TextView tvChannelName;
        @BindView(R.id.tv_streamOptions)
        TextView tvStreamOptions;

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
//            target.ivChannelLogo = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_channel_logo, "field 'ivChannelLogo'", ImageView.class);
//            target.tvChannelName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name, "field 'tvChannelName'", TextView.class);
//            target.cardView = (CardView) Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", CardView.class);
//            target.tvStreamOptions = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_streamOptions, "field 'tvStreamOptions'", TextView.class);
//            target.ivFavourite = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_favourite, "field 'ivFavourite'", ImageView.class);
//            target.rlStreamsLayout = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_streams_layout, "field 'rlStreamsLayout'", RelativeLayout.class);
//            target.rlChannelBottom = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_channel_bottom, "field 'rlChannelBottom'", RelativeLayout.class);
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
//        }
//    }

    public LiveStreamListViewAdapter(List<LiveStreamsDBModel> liveStreamCategories, Context context) {
        this.dataSet = liveStreamCategories;
        this.context = context;
        this.filterList = new ArrayList();
        this.filterList.addAll(liveStreamCategories);
        this.completeList = liveStreamCategories;
        this.database = new DatabaseHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_linear_layout, parent, false);
        if (view != null) {
            this.myViewHolder = new MyViewHolder(view);
        } else {
            this.myViewHolder = new MyViewHolder(view);
        }
        return this.myViewHolder;
    }

    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            Context context = this.context;
            String str = AppConst.LOGIN_PREF_SELECTED_PLAYER;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            this.streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId());
            this.categoryId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getCategoryId();
            this.streamType = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamType();
            this.epgChannelID = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getEpgChannelId();
            holder.tvChannelName.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            this.StreamIcon = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon();
            if (!(this.StreamIcon == null || this.StreamIcon.equals(""))) {
                Picasso.with(this.context).load(this.StreamIcon).placeholder((int) R.drawable.iptv_placeholder).into(holder.ivChannelLogo);
            }
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    z.xtreamiptv.playerv3.miscelleneious.common.Utils.playWithPlayer(LiveStreamListViewAdapter.this.context, selectedPlayer, LiveStreamListViewAdapter.this.streamId, LiveStreamListViewAdapter.this.streamType, "", "", LiveStreamListViewAdapter.this.epgChannelID, LiveStreamListViewAdapter.this.StreamIcon);
                }
            });
            holder.rlStreamsLayout.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != 0 || (keyCode != 23 && keyCode != 66)) {
                        return false;
                    }
                    holder.rlChannelBottom.performClick();
                    return true;
                }
            });
            if (this.database.checkFavourite(this.streamId, this.categoryId, "live").size() > 0) {
                holder.ivFavourite.setVisibility(0);
            } else {
                holder.ivFavourite.setVisibility(4);
            }
            holder.rlChannelBottom.setOnClickListener(new OnClickListener() {

                class C15871 implements OnMenuItemClickListener {
                    C15871() {
                    }

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
                        LiveStreamsFavourite.setCategoryID(LiveStreamListViewAdapter.this.categoryId);
                        LiveStreamsFavourite.setStreamID(LiveStreamListViewAdapter.this.streamId);
                        LiveStreamListViewAdapter.this.database.addToFavourite(LiveStreamsFavourite, "live");
                        holder.ivFavourite.setVisibility(0);
                    }

                    private void removeFromFavourite() {
                        LiveStreamListViewAdapter.this.database.deleteFavourite(LiveStreamListViewAdapter.this.streamId, LiveStreamListViewAdapter.this.categoryId, "live");
                        holder.ivFavourite.setVisibility(4);
                    }
                }

                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(LiveStreamListViewAdapter.this.context, holder.tvStreamOptions);
                    popup.inflate(R.menu.menu_card_live_streams);
                    if (LiveStreamListViewAdapter.this.database.checkFavourite(LiveStreamListViewAdapter.this.streamId, LiveStreamListViewAdapter.this.categoryId, "live").size() > 0) {
                        popup.getMenu().getItem(2).setVisible(true);
                    } else {
                        popup.getMenu().getItem(1).setVisible(true);
                    }
                    popup.setOnMenuItemClickListener(new C15871());
                    popup.show();
                }
            });
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C15891 implements Runnable {
                C15891() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        LiveStreamListViewAdapter.this.dataSet = LiveStreamListViewAdapter.this.completeList;
                    } else if (!LiveStreamListViewAdapter.this.filterList.isEmpty() || LiveStreamListViewAdapter.this.filterList.isEmpty()) {
                        LiveStreamListViewAdapter.this.dataSet = LiveStreamListViewAdapter.this.filterList;
                    }
                    if (LiveStreamListViewAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    LiveStreamListViewAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                LiveStreamListViewAdapter.this.filterList = new ArrayList();
                if (LiveStreamListViewAdapter.this.filterList != null) {
                    LiveStreamListViewAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    LiveStreamListViewAdapter.this.filterList.addAll(LiveStreamListViewAdapter.this.completeList);
                } else {
                    for (LiveStreamsDBModel item : LiveStreamListViewAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            LiveStreamListViewAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) LiveStreamListViewAdapter.this.context).runOnUiThread(new C15891());
            }
        }).start();
    }
}
