package com.socialmap.yy.travelbox.adpater;


        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

        import com.socialmap.yy.travelbox.R;

        import java.util.List;
import java.util.Map;

public class TimelineAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public TimelineAdapter(Context context, List<Map<String, Object>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.activity_history, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);

            viewHolder.show_time = (TextView) convertView.findViewById(R.id.show_time);
            viewHolder.local = (TextView) convertView.findViewById(R.id.local);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String titleStr = list.get(position).get("title").toString();
        //String titleStr = "title";
        String timeStr = list.get(position).get("show_time").toString();
        //String timeStr = "time";
        String localStr = list.get(position).get("local").toString();
        //String localStr = "local";

        viewHolder.title.setText(titleStr);
        viewHolder.show_time.setText(timeStr);
        viewHolder.local.setText(localStr);

        return convertView;
    }

    static class ViewHolder {
        public TextView year;
        public TextView month;
        public TextView title;
        public TextView show_time;
        public TextView local;
    }













}
