package z.xtreamiptv.playerv3.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.v2api.model.GetEpisdoeDetailsCallback;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodeDetailAdapter extends Adapter<EpisodeDetailAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private List<GetEpisdoeDetailsCallback> completeList;
    private Context context;
    private List<GetEpisdoeDetailsCallback> dataSet;
    private DatabaseHandler database;
    private Editor editor;
    private List<GetEpisdoeDetailsCallback> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    MyViewHolder myViewHolder;
    private SharedPreferences pref;
    private SimpleDateFormat programTimeFormat;
    private String seriesCover;
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

    public EpisodeDetailAdapter(List<GetEpisdoeDetailsCallback> liveStreamCategories, Context context, String seriesCover) {
        this.dataSet = liveStreamCategories;
        this.context = context;
        this.filterList.addAll(liveStreamCategories);
        this.completeList = liveStreamCategories;
        this.database = new DatabaseHandler(context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.seriesCover = seriesCover;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_layout, parent, false));
        return this.myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            String trimmed = "";
            int streamId = -1;
            holder.ivFavourite.setVisibility(8);
            if (this.dataSet.get(listPosition) != null) {
                if (((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getId() != null) {
                    streamId = Integer.parseInt(((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getId().trim());
                }
                String name = "";
                if (((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getTitle() != null) {
                    holder.movieNameTV.setText(((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getTitle());
                    name = ((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getTitle();
                }
                String StreamIcon = this.seriesCover;
                holder.MovieImage.setImageDrawable(null);
                String containerExtension = "";
                if (((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getContainerExtension() != null) {
                    containerExtension = ((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getContainerExtension();
                }
                if (StreamIcon != null && !StreamIcon.equals("")) {
                    Picasso.with(this.context).load(StreamIcon).placeholder((int) R.drawable.nopostrer).into(holder.MovieImage);
                } else if (VERSION.SDK_INT >= 21) {
                    holder.MovieImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.nopostrer, null));
                }
                final int finalStreamId = streamId;
                final String finalContainerExtension = containerExtension;
                final String finalName = name;
                int i = listPosition;
                final int finalI = i;
                holder.cardView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playSeries(EpisodeDetailAdapter.this.context, selectedPlayer, finalStreamId, null, finalContainerExtension, String.valueOf(finalI), finalName);
                    }
                });
                i = listPosition;
                final int finalI1 = i;
                holder.MovieImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playSeries(EpisodeDetailAdapter.this.context, selectedPlayer, finalStreamId, null, finalContainerExtension, String.valueOf(finalI1), finalName);
                    }
                });
                i = listPosition;
                final int finalI2 = i;
                holder.Movie.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        z.xtreamiptv.playerv3.miscelleneious.common.Utils.playSeries(EpisodeDetailAdapter.this.context, selectedPlayer, finalStreamId, null, finalContainerExtension, String.valueOf(finalI2), finalName);
                    }
                });
            }
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C19251 implements Runnable {
                C19251() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        EpisodeDetailAdapter.this.dataSet = EpisodeDetailAdapter.this.completeList;
                    } else if (!EpisodeDetailAdapter.this.filterList.isEmpty() || EpisodeDetailAdapter.this.filterList.isEmpty()) {
                        EpisodeDetailAdapter.this.dataSet = EpisodeDetailAdapter.this.filterList;
                    }
                    if (EpisodeDetailAdapter.this.dataSet != null && EpisodeDetailAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    EpisodeDetailAdapter.this.text_last_size = EpisodeDetailAdapter.this.text_size;
                    EpisodeDetailAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                EpisodeDetailAdapter.this.filterList = new ArrayList();
                EpisodeDetailAdapter.this.text_size = text.length();
                if (EpisodeDetailAdapter.this.filterList != null) {
                    EpisodeDetailAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    EpisodeDetailAdapter.this.filterList.addAll(EpisodeDetailAdapter.this.completeList);
                } else {
                    if ((EpisodeDetailAdapter.this.dataSet != null && EpisodeDetailAdapter.this.dataSet.size() == 0) || EpisodeDetailAdapter.this.text_last_size > EpisodeDetailAdapter.this.text_size) {
                        EpisodeDetailAdapter.this.dataSet = EpisodeDetailAdapter.this.completeList;
                    }
                    if (EpisodeDetailAdapter.this.dataSet != null) {
                        for (GetEpisdoeDetailsCallback item : EpisodeDetailAdapter.this.dataSet) {
                            if (item.getTitle() != null && item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                                EpisodeDetailAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) EpisodeDetailAdapter.this.context).runOnUiThread(new C19251());
            }
        }).start();
    }
}
