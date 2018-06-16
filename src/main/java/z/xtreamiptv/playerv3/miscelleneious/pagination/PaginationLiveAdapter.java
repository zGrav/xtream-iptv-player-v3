package z.xtreamiptv.playerv3.miscelleneious.pagination;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import z.xtreamiptv.playerv3.view.activity.LiveActivityNewFlow;
import z.xtreamiptv.playerv3.view.activity.LiveCategoryActivity;
import java.util.ArrayList;
import java.util.Iterator;

public class PaginationLiveAdapter extends Adapter<ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private Context context;
    FrameLayout frameLayout;
    ViewHolder holder;
    private boolean isLoadingAdded = false;
    MovieVH movieVH;
    private ArrayList<LiveStreamCategoryIdDBModel> moviesListl;

    protected class LoadingVH extends ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    protected class MovieVH extends ViewHolder {
        @BindView(2131362340)
        ProgressBar pbPagingLoader;
        @BindView(2131362403)
        RelativeLayout rlOuter;
        private TextView textView;
        @BindView(2131362583)
        TextView tvMovieCategoryName;

        public MovieVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);
        }
    }

//    public class MovieVH_ViewBinding implements Unbinder {
//        private MovieVH target;
//
//        @UiThread
//        public MovieVH_ViewBinding(MovieVH target, View source) {
//            this.target = target;
//            target.tvMovieCategoryName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_category_name, "field 'tvMovieCategoryName'", TextView.class);
//            target.pbPagingLoader = (ProgressBar) Utils.findRequiredViewAsType(source, R.id.pb_paging_loader, "field 'pbPagingLoader'", ProgressBar.class);
//            target.rlOuter = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_outer, "field 'rlOuter'", RelativeLayout.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MovieVH target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.tvMovieCategoryName = null;
//            target.pbPagingLoader = null;
//            target.rlOuter = null;
//        }
//    }

    public PaginationLiveAdapter(ArrayList<LiveStreamCategoryIdDBModel> movieList, Context context, FrameLayout frameLayout) {
        this.moviesListl = movieList;
        this.context = context;
        this.frameLayout = frameLayout;
    }

    public int getItemCount() {
        return this.moviesListl == null ? 0 : this.moviesListl.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 0:
                return getViewHolder(parent, inflater);
            case 1:
                return new LoadingVH(inflater.inflate(R.layout.layout_live_new_flow_list_item, parent, false));
            default:
                return null;
        }
    }

    @NonNull
    private ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        return new MovieVH(inflater.inflate(R.layout.layout_live_new_flow_list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        LiveStreamCategoryIdDBModel movieData = (LiveStreamCategoryIdDBModel) this.moviesListl.get(position);
        switch (getItemViewType(position)) {
            case 0:
                final MovieVH movieVH = (MovieVH) holder;
                String categoryName = "";
                String categoryId = "";
                LiveStreamCategoryIdDBModel data = (LiveStreamCategoryIdDBModel) this.moviesListl.get(position);
                categoryName = data.getLiveStreamCategoryName();
                categoryId = data.getLiveStreamCategoryID();
                Bundle bundle = new Bundle();
                bundle.putString(AppConst.CATEGORY_ID, categoryId);
                bundle.putString(AppConst.CATEGORY_NAME, categoryName);
                if (!(categoryName == null || categoryName.equals("") || categoryName.isEmpty())) {
                    movieVH.tvMovieCategoryName.setText(categoryName);
                }
                final String finalCategoryId = categoryId;
                final String finalCategoryName = categoryName;
                movieVH.rlOuter.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        new LiveActivityNewFlow().progressBar(movieVH.pbPagingLoader);
                        if (!(movieVH == null || movieVH.pbPagingLoader == null)) {
                            movieVH.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                            movieVH.pbPagingLoader.setVisibility(0);
                        }
                        Intent intent = new Intent(PaginationLiveAdapter.this.context, LiveCategoryActivity.class);
                        intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                        intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                        PaginationLiveAdapter.this.context.startActivity(intent);
                    }
                });
                return;
            default:
                return;
        }
    }

    public int getItemViewType(int position) {
        return (position == this.moviesListl.size() + -1 && this.isLoadingAdded) ? 1 : 0;
    }

    public void add(LiveStreamCategoryIdDBModel mc) {
        this.moviesListl.add(mc);
        notifyItemInserted(this.moviesListl.size() - 1);
    }

    public void addAll(ArrayList<LiveStreamCategoryIdDBModel> mcList) {
        Iterator it = mcList.iterator();
        while (it.hasNext()) {
            add((LiveStreamCategoryIdDBModel) it.next());
        }
    }

    public void remove(LiveStreamCategoryIdDBModel city) {
        int position = this.moviesListl.indexOf(city);
        if (position > -1) {
            this.moviesListl.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        this.isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        this.isLoadingAdded = true;
        add(new LiveStreamCategoryIdDBModel(null, null, 0));
    }

    public void removeLoadingFooter() {
        this.isLoadingAdded = false;
        int position = this.moviesListl.size() - 1;
        if (getItem(position) != null) {
            this.moviesListl.remove(position);
            notifyItemRemoved(position);
        }
    }

    public LiveStreamCategoryIdDBModel getItem(int position) {
        return (LiveStreamCategoryIdDBModel) this.moviesListl.get(position);
    }

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(8);
        }
    }
}
