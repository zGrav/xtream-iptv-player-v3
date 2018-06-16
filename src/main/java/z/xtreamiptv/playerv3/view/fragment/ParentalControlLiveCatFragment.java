package z.xtreamiptv.playerv3.view.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.view.activity.ImportEPGActivity;
import z.xtreamiptv.playerv3.view.activity.ImportStreamsActivity;
import z.xtreamiptv.playerv3.view.activity.ParentalControlActivitity;
import z.xtreamiptv.playerv3.view.adapter.ParentalControlLiveCatAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParentalControlLiveCatFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    ParentalControlActivitity dashboardActivity;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    @BindView(R.id.empty_view)
    TextView emptyView;
    private Typeface fontOPenSansBold;
    LiveStreamDBHandler liveStreamDBHandler;
    private ParentalControlLiveCatAdapter mAdapter;
    private LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private FragmentActivity myContext;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private Toolbar toolbar;
    Unbinder unbinder;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    class C20231 implements OnQueryTextListener {
        C20231() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            if (ParentalControlLiveCatFragment.this.pbLoader != null) {
                ParentalControlLiveCatFragment.this.pbLoader.setVisibility(4);
            }
            if (ParentalControlLiveCatFragment.this.progressDialog != null) {
                ParentalControlLiveCatFragment.this.progressDialog.dismiss();
            }
            if (!(ParentalControlLiveCatFragment.this.emptyView == null || ParentalControlLiveCatFragment.this.mAdapter == null)) {
                ParentalControlLiveCatFragment.this.emptyView.setVisibility(8);
                ParentalControlLiveCatFragment.this.mAdapter.filter(newText, ParentalControlLiveCatFragment.this.emptyView, ParentalControlLiveCatFragment.this.progressDialog);
            }
            return true;
        }
    }

    class C20242 implements OnClickListener {
        C20242() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsVodandSeries(ParentalControlLiveCatFragment.this.context);
        }
    }

    class C20253 implements OnClickListener {
        C20253() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C20264 implements OnClickListener {
        C20264() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ParentalControlLiveCatFragment.this.loadTvGuid();
        }
    }

    class C20275 implements OnClickListener {
        C20275() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public static ParentalControlLiveCatFragment newInstance(String param1, String param2) {
        ParentalControlLiveCatFragment fragment = new ParentalControlLiveCatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        View view = inflater.inflate(R.layout.fragment_parental_control_categories, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        initializeData();
        setMenuBar();
        return view;
    }

    private void initializeData() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.fontOPenSansBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/open_sans.ttf");
        this.dashboardActivity = (ParentalControlActivitity) this.context;
        this.myRecyclerView.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.myRecyclerView.setLayoutManager(this.mLayoutManager);
        ArrayList<LiveStreamCategoryIdDBModel> liveCategories = new LiveStreamDBHandler(this.context).getAllliveCategories();
        Map<String, String> map = new HashMap();
        if (liveCategories != null) {
            Iterator it = liveCategories.iterator();
            while (it.hasNext()) {
                LiveStreamCategoryIdDBModel listItem = (LiveStreamCategoryIdDBModel) it.next();
                map.put(listItem.getLiveStreamCategoryID(), listItem.getLiveStreamCategoryName());
            }
        }
        String[] categoriesArray = (String[]) map.values().toArray(new String[0]);
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
        if (liveCategories != null && liveCategories.size() > 0 && this.myRecyclerView != null && this.emptyView != null) {
            this.myRecyclerView.setVisibility(0);
            this.emptyView.setVisibility(8);
            this.mAdapter = new ParentalControlLiveCatAdapter(liveCategories, getContext(), this.dashboardActivity, this.fontOPenSansBold);
            this.myRecyclerView.setAdapter(this.mAdapter);
        } else if (this.myRecyclerView != null && this.emptyView != null) {
            this.myRecyclerView.setVisibility(8);
            this.emptyView.setVisibility(0);
            this.emptyView.setText("No Live Categories Found");
        }
    }

    private void setMenuBar() {
        setHasOptionsMenu(true);
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
            return;
        }
        throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
        setHasOptionsMenu(false);
        this.mListener = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) {
            menu.clear();
        }
        this.toolbar.inflateMenu(R.menu.menu_search);
        TypedValue tv = new TypedValue();
        if (this.context.getTheme().resolveAttribute(16843499, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, this.context.getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
            }
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout1:
                if (this.context != null) {
                    Utils.logoutUser(this.context);
                    break;
                }
                break;
            case R.id.action_search:
                this.searchView = (SearchView) MenuItemCompat.getActionView(item);
                this.searchView.setQueryHint(getResources().getString(R.string.search_categories));
                SearchManager searchManager = (SearchManager) this.context.getSystemService("search");
                this.searchView.setIconifiedByDefault(false);
                this.searchView.setOnQueryTextListener(new C20231());
                return true;
            case R.id.menu_load_channels_vod1:
                Builder alertDialog = new Builder(this.context);
                alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
                alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
                alertDialog.setIcon(R.drawable.questionmark);
                alertDialog.setPositiveButton("YES", new C20242());
                alertDialog.setNegativeButton("NO", new C20253());
                alertDialog.show();
                return true;
            case R.id.menu_load_tv_guide1:
                Builder alertDialog1 = new Builder(this.context);
                alertDialog1.setTitle(getResources().getString(R.string.confirm_refresh));
                alertDialog1.setMessage(getResources().getString(R.string.confirm_procees));
                alertDialog1.setIcon(R.drawable.questionmark);
                alertDialog1.setPositiveButton("YES", new C20264());
                alertDialog1.setNegativeButton("NO", new C20275());
                alertDialog1.show();
                return true;
        }
        return false;
    }

    private void loadChannelsAndVod() {
        if (this.context == null) {
            return;
        }
        if (getChannelVODUpdateStatus()) {
            new LiveStreamDBHandler(this.context).makeEmptyAllChannelsVODTablesRecords();
            startActivity(new Intent(this.context, ImportStreamsActivity.class));
        } else if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.upadating_channels_vod));
        }
    }

    private boolean getChannelVODUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        if (this.databaseUpdatedStatusDBModelLive == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null) {
            return true;
        }
        if (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            return true;
        }
        return false;
    }

    private void loadTvGuid() {
        if (this.context == null) {
            return;
        }
        if (getEPGUpdateStatus()) {
            SharedPreferences loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            Editor loginPrefsEditor = loginPreferencesAfterLogin.edit();
            if (loginPrefsEditor != null) {
                loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "autoLoad");
                loginPrefsEditor.commit();
                String skipButton = loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
                new LiveStreamDBHandler(this.context).makeEmptyEPG();
                startActivity(new Intent(this.context, ImportEPGActivity.class));
            }
        } else if (this.context != null) {
            Utils.showToast(this.context, getResources().getString(R.string.upadating_tv_guide));
        }
    }

    private boolean getEPGUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) {
            return true;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals("")) {
            return true;
        }
        return false;
    }

    private boolean getChannelEPGUpdateStatus() {
        if (this.liveStreamDBHandler == null || this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        this.databaseUpdatedStatusDBModelLive = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_CHANNELS, "1");
        this.databaseUpdatedStatusDBModelEPG = this.liveStreamDBHandler.getdateDBStatus(AppConst.DB_EPG, AppConst.DB_EPG_ID);
        if (this.databaseUpdatedStatusDBModelLive == null || this.databaseUpdatedStatusDBModelEPG == null) {
            return false;
        }
        if (this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState() == null || this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState() == null) {
            return true;
        }
        if ((this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH)) || ((this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) || ((this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED)) || (this.databaseUpdatedStatusDBModelLive.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FAILED) && this.databaseUpdatedStatusDBModelEPG.getDbUpadatedStatusState().equals(AppConst.DB_UPDATED_STATUS_FINISH))))) {
            return true;
        }
        return false;
    }
}
