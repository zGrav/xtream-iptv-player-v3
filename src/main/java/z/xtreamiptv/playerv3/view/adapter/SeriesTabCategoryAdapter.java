package z.xtreamiptv.playerv3.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import z.xtreamiptv.playerv3.model.LiveStreamCategoryIdDBModel;
import z.xtreamiptv.playerv3.view.fragment.SeriesTabFragment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeriesTabCategoryAdapter extends FragmentPagerAdapter {
    private String[] LiveStreamCategoriesIds;
    private Context context;
    private String[] liveStreamCategoriesNames;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags = new HashMap();
    final int seriesStreamTotalCategories;

    public SeriesTabCategoryAdapter(List<LiveStreamCategoryIdDBModel> seriesStreamCategories, FragmentManager fm, Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.seriesStreamTotalCategories = seriesStreamCategories.size();
        this.liveStreamCategoriesNames = new String[this.seriesStreamTotalCategories];
        this.LiveStreamCategoriesIds = new String[this.seriesStreamTotalCategories];
        this.context = context;
        for (int i = 0; i < this.seriesStreamTotalCategories; i++) {
            String categoryName = ((LiveStreamCategoryIdDBModel) seriesStreamCategories.get(i)).getLiveStreamCategoryName();
            String categoryId = ((LiveStreamCategoryIdDBModel) seriesStreamCategories.get(i)).getLiveStreamCategoryID();
            this.liveStreamCategoriesNames[i] = categoryName;
            this.LiveStreamCategoriesIds[i] = categoryId;
        }
    }

    public int getCount() {
        return this.seriesStreamTotalCategories;
    }

    public Fragment getItem(int position) {
        return SeriesTabFragment.newInstance(this.LiveStreamCategoriesIds[position]);
    }

    public CharSequence getPageTitle(int position) {
        return this.liveStreamCategoriesNames[position];
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Fragment obj = (Fragment) super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            this.mFragmentTags.put(Integer.valueOf(position), obj.getTag());
        }
        return obj;
    }

    public SeriesTabFragment getFragment(int position) {
        String tag = (String) this.mFragmentTags.get(Integer.valueOf(position));
        if (tag == null) {
            return null;
        }
        return (SeriesTabFragment) this.mFragmentManager.findFragmentByTag(tag);
    }
}
