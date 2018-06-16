package z.xtreamiptv.playerv3.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.gms.cast.TextTrackStyle;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.callback.SeasonsDetailCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.v2api.model.GetEpisdoeDetailsCallback;
import z.xtreamiptv.playerv3.v2api.model.database.SeriesStreamsDatabaseHandler;
import z.xtreamiptv.playerv3.view.activity.EpisodeDetailActivity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeasonsAdapter extends Adapter<SeasonsAdapter.MyViewHolder> {
    private List<SeasonsDetailCallback> completeList;
    private Context context;
    private DatabaseHandler dbHandeler;
    private List<GetEpisdoeDetailsCallback> episodeList;
    private List<SeasonsDetailCallback> filterList;
    private LiveStreamDBHandler liveStreamDBHandler;
    private List<SeasonsDetailCallback> moviesListl;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    public int text_last_size;
    public int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.iv_foraward_arrow)
        ImageView ivForawardArrow;
        @BindView(R.id.iv_tv_icon)
        ImageView ivTvIcon;
        @BindView(R.id.pb_paging_loader)
        ProgressBar pbPagingLoader;
        @BindView(R.id.rl_list_of_categories)
        RelativeLayout rlListOfCategories;
        @BindView(R.id.rl_outer)
        RelativeLayout rlOuter;
        @BindView(R.id.tv_movie_category_name)
        TextView tvMovieCategoryName;
        @BindView(R.id.tv_sub_cat_count)
        TextView tvXubCount;

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
//            target.ivTvIcon = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_tv_icon, "field 'ivTvIcon'", ImageView.class);
//            target.tvMovieCategoryName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_category_name, "field 'tvMovieCategoryName'", TextView.class);
//            target.ivForawardArrow = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_foraward_arrow, "field 'ivForawardArrow'", ImageView.class);
//            target.pbPagingLoader = (ProgressBar) Utils.findRequiredViewAsType(source, R.id.pb_paging_loader, "field 'pbPagingLoader'", ProgressBar.class);
//            target.rlListOfCategories = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_list_of_categories, "field 'rlListOfCategories'", RelativeLayout.class);
//            target.rlOuter = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_outer, "field 'rlOuter'", RelativeLayout.class);
//            target.tvXubCount = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_sub_cat_count, "field 'tvXubCount'", TextView.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MyViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.ivTvIcon = null;
//            target.tvMovieCategoryName = null;
//            target.ivForawardArrow = null;
//            target.pbPagingLoader = null;
//            target.rlListOfCategories = null;
//            target.rlOuter = null;
//            target.tvXubCount = null;
//        }
//    }

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.09f;
            if (hasFocus) {
                if (!hasFocus) {
                    to = TextTrackStyle.DEFAULT_FONT_SCALE;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                Log.e("id is", "" + this.view.getTag());
                this.view.setBackgroundResource(R.drawable.shape_list_categories_focused);
            } else if (!hasFocus) {
                if (!hasFocus) {
                    to = TextTrackStyle.DEFAULT_FONT_SCALE;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                this.view.setBackgroundResource(R.drawable.shape_list_categories);
            }
        }

        private void performScaleXAnimation(float to) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this.view, "scaleX", new float[]{to});
            scaleXAnimator.setDuration(150);
            scaleXAnimator.start();
        }

        private void performScaleYAnimation(float to) {
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this.view, "scaleY", new float[]{to});
            scaleYAnimator.setDuration(150);
            scaleYAnimator.start();
        }

        private void performAlphaAnimation(boolean hasFocus) {
            if (hasFocus) {
                float toAlpha = hasFocus ? 0.6f : 0.5f;
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.view, "alpha", new float[]{toAlpha});
                alphaAnimator.setDuration(150);
                alphaAnimator.start();
            }
        }
    }

    public SeasonsAdapter(List<GetEpisdoeDetailsCallback> episodeList, List<SeasonsDetailCallback> seasonsDetailCallbacks, Context context) {
        this.filterList = new ArrayList();
        this.filterList.addAll(seasonsDetailCallbacks);
        this.completeList = seasonsDetailCallbacks;
        this.moviesListl = seasonsDetailCallbacks;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.dbHandeler = new DatabaseHandler(context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(context);
        this.episodeList = episodeList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vod_new_flow_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        String categoryName = "";
        String categoryId = "";
        String seasonCover = "";
        SeasonsDetailCallback data = (SeasonsDetailCallback) this.moviesListl.get(listPosition);
        categoryName = data.getName();
        categoryId = data.getCoverBig();
        int episodeCount = data.getEpisodeCount().intValue();
        int seasonNumber = data.getSeasonNumber().intValue();
        seasonCover = data.getCoverBig();
        Bundle bundle = new Bundle();
        bundle.putString(AppConst.CATEGORY_ID, categoryId);
        bundle.putString(AppConst.CATEGORY_NAME, categoryName);
        if (!(categoryName == null || categoryName.equals("") || categoryName.isEmpty())) {
            holder.tvMovieCategoryName.setText(categoryName);
        }
        String finalCategoryId = categoryId;
        String finalCategoryName = categoryName;
        final int finalSeasonNumber = seasonNumber;
        final String finalSeasonCover = seasonCover;
        MyViewHolder myViewHolder = holder;
        final MyViewHolder finalMyViewHolder1 = myViewHolder;
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new EpisodeDetailActivity().progressBar(finalMyViewHolder1.pbPagingLoader);
                if (!(finalMyViewHolder1 == null || finalMyViewHolder1.pbPagingLoader == null)) {
                    finalMyViewHolder1.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    finalMyViewHolder1.pbPagingLoader.setVisibility(0);
                }
                Intent intent = new Intent(SeasonsAdapter.this.context, EpisodeDetailActivity.class);
                intent.putExtra(AppConst.SEASON_NUMBER, finalSeasonNumber);
                intent.putExtra(AppConst.EPISODELIST, (Serializable) SeasonsAdapter.this.episodeList);
                intent.putExtra(AppConst.SEASON_COVER_BIG, finalSeasonCover);
                SeasonsAdapter.this.context.startActivity(intent);
            }
        });
        myViewHolder = holder;
        final MyViewHolder finalMyViewHolder = myViewHolder;
        holder.rlListOfCategories.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new EpisodeDetailActivity().progressBar(finalMyViewHolder.pbPagingLoader);
                if (!(finalMyViewHolder == null || finalMyViewHolder.pbPagingLoader == null)) {
                    finalMyViewHolder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    finalMyViewHolder.pbPagingLoader.setVisibility(0);
                }
                Intent intent = new Intent(SeasonsAdapter.this.context, EpisodeDetailActivity.class);
                intent.putExtra(AppConst.SEASON_NUMBER, finalSeasonNumber);
                intent.putExtra(AppConst.EPISODELIST, (Serializable) SeasonsAdapter.this.episodeList);
                intent.putExtra(AppConst.SEASON_COVER_BIG, finalSeasonCover);
                SeasonsAdapter.this.context.startActivity(intent);
            }
        });
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
    }

    public List<GetEpisdoeDetailsCallback> getEpisodeList() {
        return this.episodeList;
    }

    public int getItemCount() {
        return this.moviesListl.size();
    }

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(8);
        }
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C19571 implements Runnable {
                C19571() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SeasonsAdapter.this.moviesListl = SeasonsAdapter.this.completeList;
                    } else if (!SeasonsAdapter.this.filterList.isEmpty() || SeasonsAdapter.this.filterList.isEmpty()) {
                        SeasonsAdapter.this.moviesListl = SeasonsAdapter.this.filterList;
                    }
                    if (SeasonsAdapter.this.moviesListl != null && SeasonsAdapter.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                        tvNoRecordFound.setText(SeasonsAdapter.this.context.getResources().getString(R.string.no_record_found));
                    }
                    SeasonsAdapter.this.text_last_size = SeasonsAdapter.this.text_size;
                    SeasonsAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SeasonsAdapter.this.filterList = new ArrayList();
                SeasonsAdapter.this.text_size = text.length();
                if (SeasonsAdapter.this.filterList != null) {
                    SeasonsAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SeasonsAdapter.this.filterList.addAll(SeasonsAdapter.this.completeList);
                } else {
                    if ((SeasonsAdapter.this.moviesListl != null && SeasonsAdapter.this.moviesListl.size() == 0) || SeasonsAdapter.this.text_last_size > SeasonsAdapter.this.text_size) {
                        SeasonsAdapter.this.moviesListl = SeasonsAdapter.this.completeList;
                    }
                    if (SeasonsAdapter.this.moviesListl != null) {
                        for (SeasonsDetailCallback item : SeasonsAdapter.this.moviesListl) {
                            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                                SeasonsAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) SeasonsAdapter.this.context).runOnUiThread(new C19571());
            }
        }).start();
    }
}
