package com.socialmap.yy.travelbox.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.model.User;

import java.util.List;

public class ExpandAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater = null;
    private String[]   mGroupStrings = null;
    private List<List<User>>   mData = null;

    public ExpandAdapter (Context ctx, List<List<User>> list) {
        mContext = ctx;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupStrings = mContext.getResources().getStringArray(R.array.groups);
        mData = list;
    }

    public void setData(List<List<User>> list) {
        mData = list;
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).size();
    }

    @Override
    public List<User> getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public User getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = (TextView) convertView
                .findViewById(R.id.group_name);
        holder.mGroupName.setText(mGroupStrings[groupPosition]);
        holder.mGroupCount = (TextView) convertView
                .findViewById(R.id.group_count);
        holder.mGroupCount.setText("[" + mData.get(groupPosition).size() + "]");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_layout, null);
        }

        final User u = (User) getChild(groupPosition, childPosition);


        ChildViewHolder holder = new ChildViewHolder();
        holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
        holder.mIcon.setBackgroundResource(getChild(groupPosition,
                childPosition).getImageId());
        holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
        holder.mChildName.setText(getChild(groupPosition, childPosition)
                .getName());
        holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
        holder.mDetail.setText(getChild(groupPosition, childPosition)
                .getDetail());

   /*         convertView.setOnLongClickListener(new View.OnLongClickListener() {

           @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(this.getActivity())
                        .setMessage("确定删除 " + u.getName() + " 吗？")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {

                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, null)
                        .create().show();
                return false;
            }

        });*/







        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        /*很重要：实现ChildView点击事件，必须返回true*/
        return true;
    }

    private class GroupViewHolder {
        TextView mGroupName;
        TextView mGroupCount;
    }

    private class ChildViewHolder {
        ImageView mIcon;
        TextView mChildName;
        TextView mDetail;
    }

}
