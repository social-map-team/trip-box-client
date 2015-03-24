package com.socialmap.yy.travelbox.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialmap.yy.travelbox.R;

import java.util.List;
import java.util.Map;

/**
 * Created by gxyzw_000 on 2015/3/23.
 */
public class FindTeamAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public FindTeamAdapter(Context context, List<Map<String, Object>> list) {
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
            convertView = inflater.inflate(R.layout.listitem_find_team, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.group_name);

            viewHolder.member = (TextView) convertView.findViewById(R.id.group_member);
            viewHolder.introduce = (TextView) convertView.findViewById(R.id.group_introduce);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
/*
        String titleStr = list.get(position).get("title").toString();
        String timeStr = list.get(position).get("show_time").toString();
        String localStr = list.get(position).get("local").toString();

        //TODO 注释掉上面这段代码的原因是获取不到show_time对应的View，换上下面这行代码代替
        //String timeStr = "10.1";

        viewHolder.title.setText(titleStr);
        viewHolder.time.setText(timeStr);
        viewHolder.local.setText(localStr);
        */
        return convertView;
    }

    static class ViewHolder {
        public TextView name;
        public TextView member;
        public TextView introduce;
        public ImageView pic;
    }


}