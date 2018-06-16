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
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.view.activity.LiveActivityNewFlow;
import z.xtreamiptv.playerv3.view.activity.LiveCategoryActivity;
import java.util.ArrayList;
import java.util.List;

public class LiveAdapterNewFlow extends Adapter<LiveAdapterNewFlow.MyViewHolder> {
    private int adapterPosition;
    private List<LiveStreamCategoryIdDBModel> completeList;
    private Context context;
    private DatabaseHandler dbHandeler;
    private List<LiveStreamCategoryIdDBModel> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private List<LiveStreamCategoryIdDBModel> moviesListl;
    private int text_last_size;
    private int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.pb_paging_loader)
        ProgressBar pbPagingLoader;
        @BindView(R.id.rl_list_of_categories)
        RelativeLayout rlListOfCategories;
        @BindView(R.id.rl_outer)
        RelativeLayout rlOuter;
        @BindView(R.id.testing)
        RelativeLayout testing;
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
//            target.tvMovieCategoryName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_category_name, "field 'tvMovieCategoryName'", TextView.class);
//            target.pbPagingLoader = (ProgressBar) Utils.findRequiredViewAsType(source, R.id.pb_paging_loader, "field 'pbPagingLoader'", ProgressBar.class);
//            target.rlOuter = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_outer, "field 'rlOuter'", RelativeLayout.class);
//            target.rlListOfCategories = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_list_of_categories, "field 'rlListOfCategories'", RelativeLayout.class);
//            target.testing = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.testing, "field 'testing'", RelativeLayout.class);
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
//            target.tvMovieCategoryName = null;
//            target.pbPagingLoader = null;
//            target.rlOuter = null;
//            target.rlListOfCategories = null;
//            target.testing = null;
//            target.tvXubCount = null;
//        }
//    }

    public LiveAdapterNewFlow(List<LiveStreamCategoryIdDBModel> movieList, Context context) {
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.dbHandeler = new DatabaseHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_new_flow_list_item, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        int countAll;
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
        final String finalCategoryId = categoryId;
        final String finalCategoryName = categoryName;
        int count = this.liveStreamDBHandler.getSubCatMovieCount(data.getLiveStreamCategoryID(), "live");
        if (count == 0 || count == -1) {
            holder.tvXubCount.setText("");
        } else {
            holder.tvXubCount.setText(String.valueOf(count));
        }
        if (listPosition == 0 && data.getLiveStreamCategoryID().equals(AppConst.PASSWORD_UNSET)) {
            countAll = this.liveStreamDBHandler.getStreamsCount("live");
            if (countAll == 0 || countAll == -1) {
                holder.tvXubCount.setText("");
            } else {
                holder.tvXubCount.setText(String.valueOf(countAll));
            }
        }
        if (listPosition == 1 && data.getLiveStreamCategoryID().equals("-1")) {
            countAll = this.dbHandeler.getFavouriteCount("live");
            if (countAll == 0 || countAll == -1) {
                holder.tvXubCount.setText(AppConst.PASSWORD_UNSET);
            } else {
                holder.tvXubCount.setText(String.valueOf(countAll));
            }
        }
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new LiveActivityNewFlow().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                Intent intent = new Intent(LiveAdapterNewFlow.this.context, LiveCategoryActivity.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                LiveAdapterNewFlow.this.context.startActivity(intent);
            }
        });
        holder.rlListOfCategories.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new LiveActivityNewFlow().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                Intent intent = new Intent(LiveAdapterNewFlow.this.context, LiveCategoryActivity.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                LiveAdapterNewFlow.this.context.startActivity(intent);
            }
        });
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

            class C19291 implements Runnable {
                C19291() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        LiveAdapterNewFlow.this.moviesListl = LiveAdapterNewFlow.this.completeList;
                    } else if (!LiveAdapterNewFlow.this.filterList.isEmpty() || LiveAdapterNewFlow.this.filterList.isEmpty()) {
                        LiveAdapterNewFlow.this.moviesListl = LiveAdapterNewFlow.this.filterList;
                    }
                    if (LiveAdapterNewFlow.this.moviesListl != null && LiveAdapterNewFlow.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    LiveAdapterNewFlow.this.text_last_size = LiveAdapterNewFlow.this.text_size;
                    LiveAdapterNewFlow.this.notifyDataSetChanged();
                }
            }

            public void run() {
                LiveAdapterNewFlow.this.filterList = new ArrayList();
                LiveAdapterNewFlow.this.text_size = text.length();
                if (LiveAdapterNewFlow.this.filterList != null) {
                    LiveAdapterNewFlow.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    LiveAdapterNewFlow.this.filterList.addAll(LiveAdapterNewFlow.this.completeList);
                } else {
                    if ((LiveAdapterNewFlow.this.moviesListl != null && LiveAdapterNewFlow.this.moviesListl.size() == 0) || LiveAdapterNewFlow.this.text_last_size > LiveAdapterNewFlow.this.text_size) {
                        LiveAdapterNewFlow.this.moviesListl = LiveAdapterNewFlow.this.completeList;
                    }
                    if (LiveAdapterNewFlow.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : LiveAdapterNewFlow.this.moviesListl) {
                            if (item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                LiveAdapterNewFlow.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) LiveAdapterNewFlow.this.context).runOnUiThread(new C19291());
            }
        }).start();
    }
}
