package z.xtreamiptv.playerv3.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.miscelleneious.common.AppConst;
import z.xtreamiptv.playerv3.view.adapter.ParentalControlPagerAdapter;
import java.util.ArrayList;

public class ParentalCotrolFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    Typeface fontOPenSansBold;
    Typeface fontOPenSansRegular;
    @BindView(R.id.iv_line)
    ImageView ivLine;
    @BindView(R.id.line_below_tabs)
    View lineBelowTabs;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private FragmentActivity myContext;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.rl_my_invoices)
    RelativeLayout rlMyInvoices;
    private SearchView searchView;
    ArrayList<Integer> tabInvoicesTotalCount = new ArrayList();
    @BindView(R.id.tab_layout_invoices)
    TabLayout tabLayoutInvoices;
    private Toolbar toolbar;
    @BindView(R.id.tv_my_invoices)
    TextView tvMyInvoices;
    Unbinder unbinder;
    @BindView(R.id.view_line_my_invoices)
    View viewLineMyInvoices;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static ParentalCotrolFragment newInstance(String param1, String param2) {
        ParentalCotrolFragment fragment = new ParentalCotrolFragment();
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
        View view = inflater.inflate(R.layout.fragment_parental_cotrol, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        this.context = getContext();
        SharedPreferences preferences = getActivity().getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        setInvoicesTab();
    }

    private void setInvoicesTab() {
        this.tabLayoutInvoices.addTab(this.tabLayoutInvoices.newTab().setText(getResources().getString(R.string.live_categories)));
        this.tabLayoutInvoices.addTab(this.tabLayoutInvoices.newTab().setText(getResources().getString(R.string.vod_categories)));
        this.tabLayoutInvoices.addTab(this.tabLayoutInvoices.newTab().setText(getResources().getString(R.string.settings)));
        this.tabLayoutInvoices.setTabGravity(0);
        final ParentalControlPagerAdapter adapter = new ParentalControlPagerAdapter(getChildFragmentManager(), this.tabLayoutInvoices.getTabCount(), getContext(), this.tabInvoicesTotalCount);
        this.pager.setAdapter(adapter);
        this.tabLayoutInvoices.setupWithViewPager(this.pager);
        for (int i = 0; i < this.tabLayoutInvoices.getTabCount(); i++) {
            this.tabLayoutInvoices.getTabAt(i).setCustomView(adapter.getTabView(i));
        }
        View viewDefaultOPeningTab = this.tabLayoutInvoices.getTabAt(0).getCustomView();
        View viewSecondTab = this.tabLayoutInvoices.getTabAt(1).getCustomView();
        adapter.setDefaultOpningViewTab(viewDefaultOPeningTab, this.fontOPenSansBold);
        adapter.setSecondViewTab(viewSecondTab, this.fontOPenSansBold);
        this.pager.setCurrentItem(0);
        this.pager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(this.tabLayoutInvoices));
        this.tabLayoutInvoices.addOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabSelected(Tab tab) {
                ParentalCotrolFragment.this.pager.setCurrentItem(tab.getPosition());
                int postion = tab.getPosition();
                View view = tab.getCustomView();
                switch (postion) {
                    case 0:
                        adapter.selectLiveCatTabView(view, ParentalCotrolFragment.this.fontOPenSansBold, postion);
                        return;
                    case 1:
                        adapter.selectVODCatTabView(view, ParentalCotrolFragment.this.fontOPenSansBold, postion);
                        return;
                    case 2:
                        adapter.selectSettingsTabView(view, ParentalCotrolFragment.this.fontOPenSansBold);
                        return;
                    default:
                        return;
                }
            }

            public void onTabUnselected(Tab tab) {
                int postion = tab.getPosition();
                View view = tab.getCustomView();
                switch (postion) {
                    case 0:
                        adapter.unSelectLiveCatTabView(view, ParentalCotrolFragment.this.fontOPenSansRegular);
                        return;
                    case 1:
                        adapter.unSelectVODCatTabView(view, ParentalCotrolFragment.this.fontOPenSansRegular);
                        return;
                    case 2:
                        adapter.unSelectSettingsTabView(view, ParentalCotrolFragment.this.fontOPenSansRegular);
                        return;
                    default:
                        return;
                }
            }

            public void onTabReselected(Tab tab) {
            }
        });
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
        this.mListener = null;
    }
}
