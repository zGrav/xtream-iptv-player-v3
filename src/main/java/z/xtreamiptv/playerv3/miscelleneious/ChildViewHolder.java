package z.xtreamiptv.playerv3.miscelleneious;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import z.xtreamiptv.playerv3.R;

public class ChildViewHolder extends ViewHolder {
    RecyclerView name;

    public ChildViewHolder(View itemView) {
        super(itemView);
        this.name = (RecyclerView) itemView.findViewById(R.id.my_recycler_view);
    }
}
