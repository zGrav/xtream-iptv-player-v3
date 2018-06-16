package z.xtreamiptv.playerv3.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.view.fragment.ParentalControlLiveCatFragment;
import z.xtreamiptv.playerv3.view.fragment.ParentalControlSettingFragment;
import z.xtreamiptv.playerv3.view.fragment.ParentalControlVODCatFragment;
import java.util.ArrayList;

public class ParentalControlPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    boolean flag = false;
    int mNumOfTabs = 3;
    private String[] tabTitles = new String[3];

    public ParentalControlPagerAdapter(FragmentManager fm, int NumOfTabs, Context myContext, ArrayList<Integer> arrayList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = myContext;
        this.tabTitles[0] = this.context.getResources().getString(R.string.live_categories);
        this.tabTitles[1] = this.context.getResources().getString(R.string.vod_categories);
        this.tabTitles[2] = this.context.getResources().getString(R.string.settings);
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ParentalControlLiveCatFragment();
            case 1:
                return new ParentalControlVODCatFragment();
            case 2:
                return new ParentalControlSettingFragment();
            default:
                return null;
        }
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.tablayout_invoices, null);
        ((TextView) v.findViewById(R.id.tv_tab_service_name)).setText(this.tabTitles[position]);
        return v;
    }

    public int getCount() {
        return this.mNumOfTabs;
    }

    public void selectLiveCatTabView(View view, Typeface fontOPenSansBold, int position) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void selectVODCatTabView(View view, Typeface fontOPenSansBold, int position) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void selectSettingsTabView(View view, Typeface fontOPenSansBold) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void unSelectLiveCatTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void unSelectVODCatTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void unSelectSettingsTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void setDefaultOpningViewTab(View viewDefaultOPeningTab, Typeface font) {
        if (viewDefaultOPeningTab != null) {
            TextView serviceName = (TextView) viewDefaultOPeningTab.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(font);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void setSecondViewTab(View viewSecondTab, Typeface fontOPenSansBold) {
        if (viewSecondTab != null) {
            TextView serviceName = (TextView) viewSecondTab.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }
}
