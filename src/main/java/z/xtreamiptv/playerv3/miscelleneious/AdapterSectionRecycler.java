package z.xtreamiptv.playerv3.miscelleneious;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.view.adapter.SubCategoriesChildAdapter;
import java.util.ArrayList;
import java.util.List;

public class AdapterSectionRecycler extends SectionRecyclerViewAdapter<SectionHeader, Child, SectionViewHolder, ChildViewHolder> {
    ArrayList<LiveStreamsDBModel> channelAvailable;
    Context context;
    List<SectionHeader> sectionItemList;

    public AdapterSectionRecycler(Context context, List<SectionHeader> sectionItemList, ArrayList<LiveStreamsDBModel> channelAvailable, RecyclerView childRecylerView) {
        super(context, sectionItemList);
        this.context = context;
        this.channelAvailable = channelAvailable;
        this.sectionItemList = sectionItemList;
    }

    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        return new SectionViewHolder(LayoutInflater.from(this.context).inflate(R.layout.layout_section_header, sectionViewGroup, false));
    }

    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(this.context).inflate(R.layout.layout_section_child, childViewGroup, false));
    }

    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeader section) {
        sectionViewHolder.name.setText(section.sectionText);
    }

    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int sectionPosition, int childPosition, Child child) {
        this.channelAvailable = (ArrayList) ((SectionHeader) this.sectionItemList.get(sectionPosition)).channelSelcted();
        childViewHolder.name.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        childViewHolder.name.setAdapter(new SubCategoriesChildAdapter(this.channelAvailable, this.context));
    }
}
