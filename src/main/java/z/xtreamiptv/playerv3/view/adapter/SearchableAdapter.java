package z.xtreamiptv.playerv3.view.adapter;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import z.xtreamiptv.playerv3.R;
import z.xtreamiptv.playerv3.model.LiveStreamsDBModel;
import z.xtreamiptv.playerv3.model.database.LiveStreamDBHandler;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SearchableAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<LiveStreamsDBModel> filteredData = null;
    private LiveStreamDBHandler liveStreamDBHandler;
    private ItemFilter mFilter = new ItemFilter();
    private LayoutInflater mInflater;
    public ArrayList<LiveStreamsDBModel> originalData;

    private class ItemFilter extends Filter {
        private ItemFilter() {
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            ArrayList<LiveStreamsDBModel> list = SearchableAdapter.this.originalData;
            int count = list.size();
            ArrayList<LiveStreamsDBModel> nlist = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                LiveStreamsDBModel filterableString = (LiveStreamsDBModel) list.get(i);
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            SearchableAdapter.this.filteredData = (ArrayList) results.values;
            SearchableAdapter.this.notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        ImageView image;
        RelativeLayout ll_list_view;
        TextView text;

        ViewHolder() {
        }
    }

    public SearchableAdapter(Context context, ArrayList<LiveStreamsDBModel> data) {
        this.filteredData = data;
        this.originalData = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
    }

    public int getCount() {
        return this.filteredData.size();
    }

    public Object getItem(int position) {
        return this.filteredData.get(position);
    }

    public ArrayList<LiveStreamsDBModel> getFilteredData() {
        return this.filteredData;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.channel_list, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.list_view);
            holder.image = (ImageView) convertView.findViewById(R.id.tv_logo);
            holder.ll_list_view = (RelativeLayout) convertView.findViewById(R.id.ll_list_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(((LiveStreamsDBModel) this.filteredData.get(position)).getName());
        if (((LiveStreamsDBModel) this.filteredData.get(position)).getStreamIcon() != null && !((LiveStreamsDBModel) this.filteredData.get(position)).getStreamIcon().equals("")) {
            Picasso.with(this.context).load(((LiveStreamsDBModel) this.filteredData.get(position)).getStreamIcon()).placeholder((int) R.drawable.iptv_placeholder).into(holder.image);
        } else if (VERSION.SDK_INT >= 21) {
            holder.image.setImageDrawable(this.context.getResources().getDrawable(R.drawable.iptv_placeholder, null));
        }
        String epgChannelID = ((LiveStreamsDBModel) this.originalData.get(position)).getEpgChannelId();
        return convertView;
    }

    public Filter getFilter() {
        return this.mFilter;
    }
}
