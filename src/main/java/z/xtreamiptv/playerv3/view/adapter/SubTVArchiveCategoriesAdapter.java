package z.xtreamiptv.playerv3.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import z.xtreamiptv.playerv3.model.pojo.XMLTVProgrammePojo;
import z.xtreamiptv.playerv3.view.fragment.SubTVArchiveFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubTVArchiveCategoriesAdapter extends FragmentPagerAdapter {
    private String[] LiveStreamCategoriesIds;
    private Context context;
    private List<String> liveStreamCategoriesNames;
    private final ArrayList<XMLTVProgrammePojo> liveStreamEpgCallback;
    final int liveStreamTotalCategories;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags = new HashMap();
    private final String opened_channel_duration;
    private final String opened_channel_icon;
    private final String opened_channel_id;
    private final String opened_channel_name;
    private final String opened_num;
    private final int opened_stream_id;

    public SubTVArchiveCategoriesAdapter(List<String> liveStreamCategories, ArrayList<XMLTVProgrammePojo> liveStreamsEpgCallback, int opened_stream_id, String opened_num, String opened_channel_name, String opened_channel_icon, String opened_channel_id, String opened_channel_duration, FragmentManager fm, Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.liveStreamTotalCategories = liveStreamCategories.size();
        this.liveStreamCategoriesNames = liveStreamCategories;
        this.liveStreamEpgCallback = liveStreamsEpgCallback;
        this.opened_stream_id = opened_stream_id;
        this.opened_num = opened_num;
        this.opened_channel_name = opened_channel_name;
        this.opened_channel_icon = opened_channel_icon;
        this.opened_channel_id = opened_channel_id;
        this.opened_channel_duration = opened_channel_duration;
        this.context = context;
    }

    public int getCount() {
        return this.liveStreamTotalCategories;
    }

    public Fragment getItem(int position) {
        return SubTVArchiveFragment.newInstance((String) this.liveStreamCategoriesNames.get(position), this.liveStreamEpgCallback, this.opened_stream_id, this.opened_num, this.opened_channel_name, this.opened_channel_icon, this.opened_channel_id, this.opened_channel_duration);
    }

    public CharSequence getPageTitle(int position) {
        return (CharSequence) this.liveStreamCategoriesNames.get(position);
    }
}
