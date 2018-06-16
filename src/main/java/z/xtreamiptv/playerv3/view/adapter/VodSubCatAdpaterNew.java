package z.xtreamiptv.playerv3.view.adapter;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.view.activity.VodActivityNewFlowSecondSubCategories;
import z.xtreamiptv.playerv3.view.activity.VodSubCatActivity;
import java.util.ArrayList;
import java.util.List;

public class VodSubCatAdpaterNew extends Adapter<VodSubCatAdpaterNew.MyViewHolder> {
    private List<LiveStreamCategoryIdDBModel> completeList;
    private Context context;
    private List<LiveStreamCategoryIdDBModel> filterList;
    private LiveStreamDBHandler liveStreamDBHandler;
    private List<LiveStreamCategoryIdDBModel> moviesListl;
    private int text_last_size;
    private int text_size;

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

    public VodSubCatAdpaterNew(List<LiveStreamCategoryIdDBModel> movieList, Context context, LiveStreamDBHandler liveStreamDBHandler) {
        this.filterList = new ArrayList();
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = liveStreamDBHandler;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vod_new_flow_list_item, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        String categoryName = "";
        String categoryId = "";
        LiveStreamCategoryIdDBModel data = (LiveStreamCategoryIdDBModel) this.moviesListl.get(listPosition);
        categoryName = data.getLiveStreamCategoryName();
        categoryId = data.getLiveStreamCategoryID();
        Bundle bundle = new Bundle();
        bundle.putString(AppConst.CATEGORY_ID, categoryId);
        bundle.putString(AppConst.CATEGORY_NAME, categoryName);
        if (!(categoryName == null || categoryName.equals("") || categoryName.isEmpty())) {
            holder.tvMovieCategoryName.setText(categoryName);
        }
        int count = this.liveStreamDBHandler.getSubCatMovieCount(data.getLiveStreamCategoryID(), "movie");
        if (count == 0 || count == -1) {
            holder.tvXubCount.setText("");
        } else {
            holder.tvXubCount.setText(String.valueOf(count));
        }
        final String finalCategoryId = categoryId;
        final String finalCategoryName = categoryName;
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new VodSubCatActivity().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                if (VodSubCatAdpaterNew.this.liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(finalCategoryId).size() > 0) {
                    Intent intent = new Intent(VodSubCatAdpaterNew.this.context, VodActivityNewFlowSecondSubCategories.class);
                    intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                    intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                    VodSubCatAdpaterNew.this.context.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(VodSubCatAdpaterNew.this.context, VodSubCatActivity.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                VodSubCatAdpaterNew.this.context.startActivity(intent);
            }
        });
        holder.rlListOfCategories.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new VodSubCatActivity().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                if (VodSubCatAdpaterNew.this.liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(finalCategoryId).size() > 0) {
                    Intent intent = new Intent(VodSubCatAdpaterNew.this.context, VodActivityNewFlowSecondSubCategories.class);
                    intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                    intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                    VodSubCatAdpaterNew.this.context.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(VodSubCatAdpaterNew.this.context, VodSubCatActivity.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                VodSubCatAdpaterNew.this.context.startActivity(intent);
            }
        });
        if (this.moviesListl.size() != 0) {
            holder.rlOuter.setVisibility(0);
        }
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

            class C20001 implements Runnable {
                C20001() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        VodSubCatAdpaterNew.this.moviesListl = VodSubCatAdpaterNew.this.completeList;
                    } else if (!VodSubCatAdpaterNew.this.filterList.isEmpty() || VodSubCatAdpaterNew.this.filterList.isEmpty()) {
                        VodSubCatAdpaterNew.this.moviesListl = VodSubCatAdpaterNew.this.filterList;
                    }
                    if (VodSubCatAdpaterNew.this.moviesListl != null && VodSubCatAdpaterNew.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    VodSubCatAdpaterNew.this.text_last_size = VodSubCatAdpaterNew.this.text_size;
                    VodSubCatAdpaterNew.this.notifyDataSetChanged();
                }
            }

            public void run() {
                VodSubCatAdpaterNew.this.filterList = new ArrayList();
                VodSubCatAdpaterNew.this.text_size = text.length();
                if (VodSubCatAdpaterNew.this.filterList != null) {
                    VodSubCatAdpaterNew.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    VodSubCatAdpaterNew.this.filterList.addAll(VodSubCatAdpaterNew.this.completeList);
                } else {
                    if ((VodSubCatAdpaterNew.this.moviesListl != null && VodSubCatAdpaterNew.this.moviesListl.size() == 0) || VodSubCatAdpaterNew.this.text_last_size > VodSubCatAdpaterNew.this.text_size) {
                        VodSubCatAdpaterNew.this.moviesListl = VodSubCatAdpaterNew.this.completeList;
                    }
                    if (VodSubCatAdpaterNew.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : VodSubCatAdpaterNew.this.moviesListl) {
                            if (item.getLiveStreamCategoryName() != null && item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                VodSubCatAdpaterNew.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) VodSubCatAdpaterNew.this.context).runOnUiThread(new C20001());
            }
        }).start();
    }
}
