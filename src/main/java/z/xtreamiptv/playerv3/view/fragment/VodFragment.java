package z.xtreamiptv.playerv3.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.AdapterSectionRecycler;
import z.xtreamiptv.playerv3.miscelleneious.Child;
import z.xtreamiptv.playerv3.miscelleneious.SectionHeader;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.miscelleneious.common.Utils;
import z.xtreamiptv.playerv3.model.FavouriteDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.VODDBModel;
import z.xtreamiptv.playerv3.model.callback.VodStreamsCallback;
import z.xtreamiptv.playerv3.model.database.DatabaseHandler;
import z.xtreamiptv.playerv3.model.database.DatabaseUpdatedStatusDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import z.xtreamiptv.playerv3.model.database.PasswordStatusDBModel;
import z.xtreamiptv.playerv3.view.activity.ImportEPGActivity;
import z.xtreamiptv.playerv3.view.activity.ImportStreamsActivity;
import z.xtreamiptv.playerv3.view.adapter.SubCategoriesChildAdapter;
import z.xtreamiptv.playerv3.view.adapter.VodAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VodFragment extends Fragment implements OnNavigationItemSelectedListener {
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_ID = "";
    public static final String ACTIVE_LIVE_STREAM_CATEGORY_NAME = "";
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryList = new ArrayList();
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal = new ArrayList();
    private static ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal_menu = new ArrayList();
    private SharedPreferences SharedPreferencesSort;
    private Editor SharedPreferencesSortEditor;
    private AdapterSectionRecycler adapterRecycler;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private PopupWindow changeSortPopUp;
    public Context context;
    DatabaseHandler database;
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive = new DatabaseUpdatedStatusDBModel(null, null, null, null);
    private Editor editor;
    VODDBModel favouriteStream = new VODDBModel(null, null, null, null,null, null, null, null, null, null, null);
    private ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
    private ArrayList<VodStreamsCallback> favouriteVOD = new ArrayList();
    private String getActiveLiveStreamCategoryId;
    private String getActiveLiveStreamCategoryName;
    private boolean isSubcaetgroy = false;
    private LayoutManager layoutManager;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private SharedPreferences pref;
    private ProgressDialog progressDialog;
    SearchView searchView;
    private Toolbar toolbar;
    @BindView(R.id.tv_no_record_found)
    TextView tvNoRecordFound;
    @BindView(R.id.tv_noStream)
    TextView tvNoStream;
    @BindView(R.id.tv_view_provider)
    TextView tvViewProvider;
    Unbinder unbinder;
    private VodAdapter vodAdapter;

    class C20531 implements OnQueryTextListener {
        C20531() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            VodFragment.this.tvNoRecordFound.setVisibility(8);
            if (!(VodFragment.this.vodAdapter == null || VodFragment.this.tvNoStream == null || VodFragment.this.tvNoStream.getVisibility() == 0)) {
                VodFragment.this.vodAdapter.filter(newText, VodFragment.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C20542 implements OnClickListener {
        C20542() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsVodandSeries(VodFragment.this.context);
        }
    }

    class C20553 implements OnClickListener {
        C20553() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C20564 implements OnClickListener {
        C20564() {
        }

        public void onClick(DialogInterface dialog, int which) {
            VodFragment.this.loadTvGuid();
        }
    }

    class C20575 implements OnClickListener {
        C20575() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C20586 implements View.OnClickListener {
        C20586() {
        }

        public void onClick(View view) {
            VodFragment.this.changeSortPopUp.dismiss();
        }
    }

    public static VodFragment newInstance(String category_id, String vodCategoriesName) {
        Bundle args = new Bundle();
        args.putString("", category_id);
        args.putString("cat_name", vodCategoriesName);
        VodFragment fragment = new VodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActiveLiveStreamCategoryId = getArguments().getString("");
        this.getActiveLiveStreamCategoryName = getArguments().getString("cat_name");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.SharedPreferencesSort = getActivity().getSharedPreferences(AppConst.LOGIN_PREF_SORT_VOD, 0);
        this.SharedPreferencesSortEditor = this.SharedPreferencesSort.edit();
        if (this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "").equals("")) {
            this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
            this.SharedPreferencesSortEditor.commit();
        }
        String str = this.getActiveLiveStreamCategoryId;
        boolean z = true;
        switch (str.hashCode()) {
            case 48:
                if (str.equals(AppConst.PASSWORD_UNSET)) {
                    z = true;
                    break;
                }
                break;
            case 1444:
                if (str.equals("-1")) {
                    z = false;
                    break;
                }
                break;
        }
        View view;
        if (z == false) {
            view = inflater.inflate(R.layout.fragment_vod_streams, container, false);
            this.unbinder = ButterKnife.bind(this, view);
            atStart();
            setLayout();
            getFavourites();
            return view;
        } else if (z == true) {
            view = inflater.inflate(R.layout.fragment_vod_streams, container, false);
            this.unbinder = ButterKnife.bind(this, view);
            atStart();
            setLayout();
            getAllMovies();
            return view;
        } else {
            subCategoryListFinal = this.liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(this.getActiveLiveStreamCategoryId);
            if (subCategoryListFinal == null || subCategoryListFinal.size() != 0) {
                view = inflater.inflate(R.layout.fragment_vod_subcategories, container, false);
                this.unbinder = ButterKnife.bind(this, view);
                atStart();
                setSubCategoryLayout(subCategoryListFinal);
                return view;
            }
            view = inflater.inflate(R.layout.fragment_vod_streams, container, false);
            this.unbinder = ButterKnife.bind(this, view);
            atStart();
            setLayout();
            return view;
        }
    }

    private ArrayList<LiveStreamCategoryIdDBModel> subCategoryListFinal() {
        Iterator it = subCategoryList.iterator();
        while (it.hasNext()) {
            LiveStreamCategoryIdDBModel listItem = (LiveStreamCategoryIdDBModel) it.next();
            if (Integer.parseInt(this.getActiveLiveStreamCategoryId) == listItem.getParentId()) {
                subCategoryListFinal.add(listItem);
            }
        }
        return subCategoryListFinal;
    }

    private void setSubCategoryLayout(ArrayList<LiveStreamCategoryIdDBModel> subCategoryList) {
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        initializeSubCat(subCategoryList);
    }

    private void initializeSubCat(ArrayList<LiveStreamCategoryIdDBModel> subCategoryList) {
        if (this.myRecyclerView != null && this.context != null) {
            ArrayList<LiveStreamsDBModel> listSize;
            this.myRecyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
            this.myRecyclerView.setLayoutManager(new LinearLayoutManager(this.context, 0, true));
            this.myRecyclerView.setLayoutManager(linearLayoutManager);
            this.myRecyclerView.setHasFixedSize(true);
            List<Child> childList = new ArrayList();
            ArrayList<LiveStreamsDBModel> channelAvailable = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.getActiveLiveStreamCategoryId, "movie");
            RecyclerView childRecylerView = new RecyclerView(this.context);
            SubCategoriesChildAdapter subCategoriesChildAdapter = new SubCategoriesChildAdapter(channelAvailable, this.context);
            childRecylerView.setAdapter(subCategoriesChildAdapter);
            childList.add(new Child("Bill Gates"));
            List<SectionHeader> sections = new ArrayList();
            Iterator it = subCategoryList.iterator();
            while (it.hasNext()) {
                LiveStreamCategoryIdDBModel listITem = (LiveStreamCategoryIdDBModel) it.next();
                listSize = new ArrayList();
                listSize = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(listITem.getLiveStreamCategoryID(), "movie");
                if (listSize != null && listSize.size() > 0) {
                    sections.add(new SectionHeader(childRecylerView, listITem.getLiveStreamCategoryName(), this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(listITem.getLiveStreamCategoryID(), "movie"), subCategoriesChildAdapter, childList));
                }
            }
            ArrayList arrayList = new ArrayList();
            listSize = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.getActiveLiveStreamCategoryId, "movie");
            if (listSize != null && listSize.size() > 0) {
                sections.add(new SectionHeader(childRecylerView, this.getActiveLiveStreamCategoryName, this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.getActiveLiveStreamCategoryId, "movie"), subCategoriesChildAdapter, childList));
            }
            onFinish();
            this.adapterRecycler = new AdapterSectionRecycler(this.context, sections, channelAvailable, childRecylerView);
            this.myRecyclerView.setAdapter(this.adapterRecycler);
        }
    }

    private void setLayout() {
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        this.pref = getActivity().getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG_VOD = this.pref.getInt(AppConst.VOD, 0);
        if (AppConst.LIVE_FLAG_VOD == 1) {
            initialize1();
        } else {
            initialize();
        }
    }

    private void setToolbarLogoImagewithSearchView() {
        this.toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (this.context != null && this.toolbar != null) {
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
    }

    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        this.toolbar.inflateMenu(R.menu.menu_search_text_icon);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout1) {
            if (this.context != null) {
                Utils.logoutUser(this.context);
            }
            return true;
        } else if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint(getResources().getString(R.string.search_vod));
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C20531());
            return true;
        } else if (id == R.id.menu_load_channels_vod1) {
            Builder alertDialog = new Builder(this.context);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C20542());
            alertDialog.setNegativeButton("NO", new C20553());
            alertDialog.show();
            return true;
        } else if (id == R.id.menu_load_tv_guide1) {
            Builder alertDialog = new Builder(this.context);
            alertDialog.setTitle(getResources().getString(R.string.confirm_refresh));
            alertDialog.setMessage(getResources().getString(R.string.confirm_procees));
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("YES", new C20564());
            alertDialog.setNegativeButton("NO", new C20575());
            alertDialog.show();
            return true;
        } else {
            if (id == R.id.layout_view_grid) {
                if (this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                    this.editor.putInt(AppConst.VOD, 0);
                    this.editor.commit();
                    initialize();
                    getAllMovies();
                } else if (this.getActiveLiveStreamCategoryId.equals("-1")) {
                    this.editor.putInt(AppConst.VOD, 0);
                    this.editor.commit();
                    initialize();
                } else {
                    subCategoryListFinal_menu.clear();
                    subCategoryListFinal_menu = this.liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(this.getActiveLiveStreamCategoryId);
                    if (subCategoryListFinal_menu.size() <= 0) {
                        this.editor.putInt(AppConst.VOD, 0);
                        this.editor.commit();
                        initialize();
                    }
                }
            }
            if (id == R.id.layout_view_linear) {
                if (this.getActiveLiveStreamCategoryId.equals(AppConst.PASSWORD_UNSET)) {
                    this.editor.putInt(AppConst.VOD, 1);
                    this.editor.commit();
                    getAllMovies();
                    initialize1();
                } else if (this.getActiveLiveStreamCategoryId.equals("-1")) {
                    this.editor.putInt(AppConst.VOD, 1);
                    this.editor.commit();
                    initialize1();
                } else {
                    subCategoryListFinal_menu = this.liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(this.getActiveLiveStreamCategoryId);
                    if (subCategoryListFinal_menu.size() <= 0) {
                        this.editor.putInt(AppConst.VOD, 1);
                        this.editor.commit();
                        initialize1();
                    }
                }
            }
            if (id == R.id.menu_sort) {
                showSortPopup(getActivity());
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private void showSortPopup(Activity context) {
        final View layout = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.sort_layout, (RelativeLayout) context.findViewById(R.id.rl_password_prompt));
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        Button savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        Button closedBT = (Button) layout.findViewById(R.id.bt_close);
        final RadioGroup rgRadio = (RadioGroup) layout.findViewById(R.id.rg_radio);
        RadioButton normal = (RadioButton) layout.findViewById(R.id.rb_normal);
        RadioButton last_added = (RadioButton) layout.findViewById(R.id.rb_lastadded);
        RadioButton atoz = (RadioButton) layout.findViewById(R.id.rb_atoz);
        RadioButton ztoa = (RadioButton) layout.findViewById(R.id.rb_ztoa);
        String sort = this.SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT, "");
        if (sort.equals("1")) {
            last_added.setChecked(true);
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            atoz.setChecked(true);
        } else if (sort.equals(AppConst.DB_LIVE_STREAMS_CAT_ID)) {
            ztoa.setChecked(true);
        } else {
            normal.setChecked(true);
        }
        closedBT.setOnClickListener(new C20586());
        final Activity activity = context;
        savePasswordBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RadioButton selectedPlayer1 = (RadioButton) layout.findViewById(rgRadio.getCheckedRadioButtonId());
                if (selectedPlayer1.getText().toString().equals(VodFragment.this.getResources().getString(R.string.sort_last_added))) {
                    VodFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, "1");
                    VodFragment.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(VodFragment.this.getResources().getString(R.string.sort_atoz))) {
                    VodFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_EPG_ID);
                    VodFragment.this.SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(VodFragment.this.getResources().getString(R.string.sort_ztoa))) {
                    VodFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.DB_LIVE_STREAMS_CAT_ID);
                    VodFragment.this.SharedPreferencesSortEditor.commit();
                } else {
                    VodFragment.this.SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT, AppConst.PASSWORD_UNSET);
                    VodFragment.this.SharedPreferencesSortEditor.commit();
                }
                VodFragment.this.pref = activity.getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
                VodFragment.this.editor = VodFragment.this.pref.edit();
                AppConst.LIVE_FLAG_VOD = VodFragment.this.pref.getInt(AppConst.VOD, 0);
                if (AppConst.LIVE_FLAG_VOD == 1) {
                    VodFragment.this.initialize1();
                } else {
                    VodFragment.this.initialize();
                }
                VodFragment.this.changeSortPopUp.dismiss();
            }
        });
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

    private void initialize() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new GridLayoutManager(getContext(), Utils.getNumberOfColumns(this.context) + 1);
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            Context context = this.context;
            String str = AppConst.LOGIN_SHARED_PREFERENCE;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void initialize1() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        if (this.myRecyclerView != null && this.context != null) {
            this.myRecyclerView.setHasFixedSize(true);
            this.layoutManager = new LinearLayoutManager(getContext());
            this.myRecyclerView.setLayoutManager(this.layoutManager);
            this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            Context context = this.context;
            String str = AppConst.LOGIN_SHARED_PREFERENCE;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str, 0);
            String username = this.loginPreferencesSharedPref.getString("username", "");
            String password = this.loginPreferencesSharedPref.getString("password", "");
            setUpdatabaseResult();
        }
    }

    private void setUpdatabaseResult() {
        if (this.context != null) {
            LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            if (!this.getActiveLiveStreamCategoryId.equals("-1")) {
                ArrayList<LiveStreamsDBModel> channelAvailable = liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.getActiveLiveStreamCategoryId, "movie");
                onFinish();
                if (channelAvailable != null && this.myRecyclerView != null && channelAvailable.size() != 0) {
                    this.vodAdapter = new VodAdapter(channelAvailable, getContext());
                    this.myRecyclerView.setAdapter(this.vodAdapter);
                } else if (this.tvNoStream != null) {
                    this.tvNoStream.setVisibility(0);
                }
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    public void getFavourites() {
        this.favouriteStreams.clear();
        if (this.myRecyclerView != null) {
            this.myRecyclerView.setAdapter(this.vodAdapter);
        }
        if (this.context != null) {
            this.database = new DatabaseHandler(this.context);
            Iterator it = this.database.getAllFavourites(AppConst.VOD).iterator();
            while (it.hasNext()) {
                FavouriteDBModel favListItem = (FavouriteDBModel) it.next();
                LiveStreamsDBModel channelAvailable = new LiveStreamDBHandler(this.context).getLiveStreamFavouriteRow(favListItem.getCategoryID(), String.valueOf(favListItem.getStreamID()));
                if (channelAvailable != null) {
                    this.favouriteStreams.add(channelAvailable);
                }
            }
            onFinish();
            if (!(this.myRecyclerView == null || this.favouriteStreams == null || this.favouriteStreams.size() == 0)) {
                this.vodAdapter = new VodAdapter(this.favouriteStreams, getContext());
                this.myRecyclerView.setAdapter(this.vodAdapter);
                this.vodAdapter.notifyDataSetChanged();
                this.tvNoStream.setVisibility(4);
            }
            if (this.tvNoStream != null && this.favouriteStreams != null && this.favouriteStreams.size() == 0) {
                if (this.myRecyclerView != null) {
                    this.myRecyclerView.setAdapter(this.vodAdapter);
                }
                this.tvNoStream.setText(getResources().getString(R.string.no_fav_vod_found));
                this.tvNoStream.setVisibility(0);
            }
        }
    }

    public void atStart() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(0);
        }
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void getAllMovies() {
        atStart();
        ActivityCompat.invalidateOptionsMenu(getActivity());
        setHasOptionsMenu(true);
        setToolbarLogoImagewithSearchView();
        this.pref = getActivity().getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG_VOD = this.pref.getInt(AppConst.VOD, 0);
        if (AppConst.LIVE_FLAG_VOD == 1) {
            this.context = getContext();
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            if (!(this.myRecyclerView == null || this.context == null)) {
                this.myRecyclerView.setHasFixedSize(true);
                this.layoutManager = new LinearLayoutManager(getContext());
                this.myRecyclerView.setLayoutManager(this.layoutManager);
                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        } else {
            this.context = getContext();
            this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            if (!(this.myRecyclerView == null || this.context == null)) {
                this.myRecyclerView.setHasFixedSize(true);
                this.layoutManager = new GridLayoutManager(getContext(), Utils.getNumberOfColumns(this.context) + 1);
                this.myRecyclerView.setLayoutManager(this.layoutManager);
                this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        }
        if (this.context != null) {
            LiveStreamDBHandler liveStreamDBHandler = new LiveStreamDBHandler(this.context);
            this.categoryWithPasword = new ArrayList();
            this.liveListDetailUnlcked = new ArrayList();
            this.liveListDetailUnlckedDetail = new ArrayList();
            this.liveListDetailAvailable = new ArrayList();
            ArrayList<LiveStreamsDBModel> channelAvailable = liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "movie");
            if (liveStreamDBHandler.getParentalStatusCount() <= 0 || channelAvailable == null) {
                this.liveListDetailAvailable = channelAvailable;
            } else {
                this.listPassword = getPasswordSetCategories();
                if (this.listPassword != null) {
                    this.liveListDetailUnlckedDetail = getUnlockedCategories(channelAvailable, this.listPassword);
                }
                this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
            }
            onFinish();
            if (this.liveListDetailAvailable != null && this.myRecyclerView != null && this.liveListDetailAvailable.size() != 0) {
                this.vodAdapter = new VodAdapter(this.liveListDetailAvailable, getContext());
                this.myRecyclerView.setAdapter(this.vodAdapter);
            } else if (this.tvNoStream != null) {
                this.tvNoStream.setVisibility(0);
            }
        }
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }
}
