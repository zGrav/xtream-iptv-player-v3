package z.xtreamiptv.playerv3.v2api.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.v2api.view.fragment.VodFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VodCategoriesAdapter extends FragmentPagerAdapter {
    private Context context;
    final int liveStreamTotalCategories;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags = new HashMap();
    private ArrayList<LiveStreamCategoryIdDBModel> subCategoryList;
    private String[] vodCategoriesIds;
    private String[] vodCategoriesNames;

    public VodCategoriesAdapter(List<LiveStreamCategoryIdDBModel> vodCategories, FragmentManager fm, Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.liveStreamTotalCategories = vodCategories.size();
        this.vodCategoriesNames = new String[this.liveStreamTotalCategories];
        this.vodCategoriesIds = new String[this.liveStreamTotalCategories];
        this.context = context;
        this.subCategoryList = this.subCategoryList;
        for (int i = 0; i < this.liveStreamTotalCategories; i++) {
            String categoryName = ((LiveStreamCategoryIdDBModel) vodCategories.get(i)).getLiveStreamCategoryName();
            String categoryId = ((LiveStreamCategoryIdDBModel) vodCategories.get(i)).getLiveStreamCategoryID();
            this.vodCategoriesNames[i] = categoryName;
            this.vodCategoriesIds[i] = categoryId;
        }
    }

    public int getCount() {
        return this.liveStreamTotalCategories;
    }

    public Fragment getItem(int position) {
        return VodFragment.newInstance(this.vodCategoriesIds[position], this.vodCategoriesNames[position]);
    }

    public CharSequence getPageTitle(int position) {
        return this.vodCategoriesNames[position];
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Fragment obj = (Fragment) super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            this.mFragmentTags.put(Integer.valueOf(position), obj.getTag());
        }
        return obj;
    }

    public VodFragment getFragment(int position) {
        String tag = (String) this.mFragmentTags.get(Integer.valueOf(position));
        if (tag == null) {
            return null;
        }
        return (VodFragment) this.mFragmentManager.findFragmentByTag(tag);
    }
}
