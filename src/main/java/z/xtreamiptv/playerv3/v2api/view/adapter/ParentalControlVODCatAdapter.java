package z.xtreamiptv.playerv3.v2api.view.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.v2api.view.activity.ParentalControlActivitity;
import java.util.ArrayList;
import java.util.Iterator;

public class ParentalControlVODCatAdapter extends Adapter<ParentalControlVODCatAdapter.ViewHolder> {
    private ArrayList<LiveStreamCategoryIdDBModel> arrayList;
    private ArrayList<LiveStreamCategoryIdDBModel> completeList;
    private Context context;
    private ParentalControlActivitity dashboardActivity;
    private ArrayList<LiveStreamCategoryIdDBModel> filterList;
    private Typeface fontOPenSansBold;
    private LiveStreamDBHandler liveStreamDBHandler;
    private PasswordStatusDBModel passwordStatusDBModel;
    private SharedPreferences preferencesIPTV;
    private String username = "";
    private ViewHolder vh;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        @BindView(R.id.tv_category_name)
        TextView categoryNameTV;
        @BindView(R.id.rl_category)
        RelativeLayout categoryRL;
        @BindView(R.id.rl_category1)
        RelativeLayout categoryRL1;
        @BindView(R.id.iv_lock_staus)
        ImageView lockIV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

//    public class ViewHolder_ViewBinding implements Unbinder {
//        private ViewHolder target;
//
//        @UiThread
//        public ViewHolder_ViewBinding(ViewHolder target, View source) {
//            this.target = target;
//            target.categoryNameTV = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_category_name, "field 'categoryNameTV'", TextView.class);
//            target.categoryRL = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_category, "field 'categoryRL'", RelativeLayout.class);
//            target.categoryRL1 = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_category1, "field 'categoryRL1'", RelativeLayout.class);
//            target.lockIV = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_lock_staus, "field 'lockIV'", ImageView.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            ViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.categoryNameTV = null;
//            target.categoryRL = null;
//            target.categoryRL1 = null;
//            target.lockIV = null;
//        }
//    }

    public ParentalControlVODCatAdapter(ArrayList<LiveStreamCategoryIdDBModel> arrayList1, Context context, ParentalControlActivitity dashboardActivity, Typeface fontOPenSansBold) {
        this.arrayList = arrayList1;
        this.context = context;
        this.dashboardActivity = dashboardActivity;
        this.fontOPenSansBold = fontOPenSansBold;
        this.completeList = arrayList1;
        if (context != null) {
            this.preferencesIPTV = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            this.username = this.preferencesIPTV.getString("username", "");
            this.liveStreamDBHandler = new LiveStreamDBHandler(context);
            this.passwordStatusDBModel = new PasswordStatusDBModel(null, null, null);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_category_list_item, parent, false));
        return this.vh;
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (this.arrayList != null) {
            LiveStreamCategoryIdDBModel categoryItem = (LiveStreamCategoryIdDBModel) this.arrayList.get(position);
            final String categoryId = categoryItem.getLiveStreamCategoryID();
            final String categoryName = categoryItem.getLiveStreamCategoryName();
            setLockStatus(holder, categoryId);
            holder.categoryNameTV.setText(categoryItem.getLiveStreamCategoryName());
            holder.categoryRL.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ParentalControlVODCatAdapter.this.passwordStatusDBModel = ParentalControlVODCatAdapter.this.liveStreamDBHandler.getPasswordStatus(ParentalControlVODCatAdapter.this.username, categoryId);
                    if (ParentalControlVODCatAdapter.this.passwordStatusDBModel != null && ParentalControlVODCatAdapter.this.passwordStatusDBModel.getPasswordStatus() != null && ParentalControlVODCatAdapter.this.passwordStatusDBModel.getPasswordStatus().equals("1")) {
                        holder.lockIV.setImageResource(R.drawable.lock_open);
                        ParentalControlVODCatAdapter.this.liveStreamDBHandler.updatePasswordStatus(ParentalControlVODCatAdapter.this.username, categoryId, AppConst.PASSWORD_UNSET);
                        if (ParentalControlVODCatAdapter.this.context != null) {
                            z.xtreamiptv.playerv3.miscelleneious.common.Utils.showToast(ParentalControlVODCatAdapter.this.context, ParentalControlVODCatAdapter.this.context.getResources().getString(R.string.unlocked) + " " + categoryName);
                        }
                    } else if (ParentalControlVODCatAdapter.this.passwordStatusDBModel != null && ParentalControlVODCatAdapter.this.passwordStatusDBModel.getPasswordStatus() != null && ParentalControlVODCatAdapter.this.passwordStatusDBModel.getPasswordStatus().equals(AppConst.PASSWORD_UNSET)) {
                        holder.lockIV.setImageResource(R.drawable.lock);
                        ParentalControlVODCatAdapter.this.liveStreamDBHandler.updatePasswordStatus(ParentalControlVODCatAdapter.this.username, categoryId, "1");
                        if (ParentalControlVODCatAdapter.this.context != null) {
                            z.xtreamiptv.playerv3.miscelleneious.common.Utils.showToast(ParentalControlVODCatAdapter.this.context, ParentalControlVODCatAdapter.this.context.getResources().getString(R.string.locked) + " " + categoryName);
                        }
                    } else if (ParentalControlVODCatAdapter.this.passwordStatusDBModel != null) {
                        ParentalControlVODCatAdapter.this.passwordStatusDBModel.setPasswordStatusCategoryId(categoryId);
                        ParentalControlVODCatAdapter.this.passwordStatusDBModel.setPasswordStatusUserDetail(ParentalControlVODCatAdapter.this.username);
                        ParentalControlVODCatAdapter.this.passwordStatusDBModel.setPasswordStatus("1");
                        ParentalControlVODCatAdapter.this.liveStreamDBHandler.addPasswordStatus(ParentalControlVODCatAdapter.this.passwordStatusDBModel);
                        holder.lockIV.setImageResource(R.drawable.lock);
                        if (ParentalControlVODCatAdapter.this.context != null) {
                            z.xtreamiptv.playerv3.miscelleneious.common.Utils.showToast(ParentalControlVODCatAdapter.this.context, ParentalControlVODCatAdapter.this.context.getResources().getString(R.string.locked) + " " + categoryName);
                        }
                    }
                }
            });
        }
        holder.categoryRL1.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || (keyCode != 23 && keyCode != 66)) {
                    return false;
                }
                holder.categoryRL.performClick();
                return true;
            }
        });
    }

    private void setLockStatus(ViewHolder holder, String categoryId) {
        this.liveStreamDBHandler.getAllPasswordStatus();
        this.passwordStatusDBModel = this.liveStreamDBHandler.getPasswordStatus(this.username, categoryId);
        if (VERSION.SDK_INT <= 21) {
            holder.lockIV.setImageResource(R.drawable.lock_open);
        }
        if (VERSION.SDK_INT >= 21) {
            holder.lockIV.setImageDrawable(this.context.getResources().getDrawable(R.drawable.lock_open, null));
        }
        if (this.passwordStatusDBModel != null && this.passwordStatusDBModel.getPasswordStatus() != null && this.passwordStatusDBModel.getPasswordStatus().equals("1")) {
            if (VERSION.SDK_INT <= 21) {
                holder.lockIV.setImageResource(R.drawable.lock);
            }
            if (VERSION.SDK_INT >= 21) {
                holder.lockIV.setImageDrawable(this.context.getResources().getDrawable(R.drawable.lock, null));
            }
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public void filter(final String text, final TextView tvNoRecordFound, ProgressDialog progressDialog) {
        new Thread(new Runnable() {

            class C16061 implements Runnable {
                C16061() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        ParentalControlVODCatAdapter.this.arrayList = ParentalControlVODCatAdapter.this.completeList;
                        tvNoRecordFound.setVisibility(4);
                    } else if (ParentalControlVODCatAdapter.this.filterList.size() == 0) {
                        ParentalControlVODCatAdapter.this.arrayList = ParentalControlVODCatAdapter.this.filterList;
                        tvNoRecordFound.setVisibility(0);
                        if (ParentalControlVODCatAdapter.this.context != null) {
                            tvNoRecordFound.setText(ParentalControlVODCatAdapter.this.context.getResources().getString(R.string.no_record_found));
                        }
                    } else if (!ParentalControlVODCatAdapter.this.filterList.isEmpty() || ParentalControlVODCatAdapter.this.filterList.isEmpty()) {
                        ParentalControlVODCatAdapter.this.arrayList = ParentalControlVODCatAdapter.this.filterList;
                        tvNoRecordFound.setVisibility(4);
                    }
                    ParentalControlVODCatAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                ParentalControlVODCatAdapter.this.filterList = new ArrayList();
                if (ParentalControlVODCatAdapter.this.filterList != null) {
                    ParentalControlVODCatAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    ParentalControlVODCatAdapter.this.filterList.addAll(ParentalControlVODCatAdapter.this.completeList);
                } else {
                    Iterator it = ParentalControlVODCatAdapter.this.completeList.iterator();
                    while (it.hasNext()) {
                        LiveStreamCategoryIdDBModel item = (LiveStreamCategoryIdDBModel) it.next();
                        if (item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                            ParentalControlVODCatAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) ParentalControlVODCatAdapter.this.context).runOnUiThread(new C16061());
            }
        }).start();
    }
}
