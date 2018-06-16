package z.xtreamiptv.playerv3.miscelleneious;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;
import z.xtreamiptv.playerv3.R;

public class SectionViewHolder extends ViewHolder {
    TextView name;

    public SectionViewHolder(View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.section);
    }
}
