package com.peac.cock.peacock_project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dmsru on 2017-11-14.
 */

public class ListViewCustomAdapter extends BaseAdapter {

    Context context;
    List<ListViewRowItem> listViewRowItems;

    ListViewCustomAdapter(Context context, List<ListViewRowItem> listViewRowItems) {
        this.context = context;
        this.listViewRowItems = listViewRowItems;
    }

    @Override
    public int getCount() {
        return listViewRowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewRowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listViewRowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    private class ViewHolder {
        ImageView profile_pic;
        TextView member_name;
        TextView status;
        TextView contactType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_itemview, null);
            holder = new ViewHolder();

            holder.member_name = (TextView) convertView
                    .findViewById(R.id.member_name);
            holder.profile_pic = (ImageView) convertView
                    .findViewById(R.id.profile_pic);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.contactType = (TextView) convertView
                    .findViewById(R.id.contact_type);

            ListViewRowItem row_pos = listViewRowItems.get(position);

            holder.profile_pic.setImageResource(row_pos.getProfile_pic_id());
            holder.member_name.setText(row_pos.getMember_name());
            holder.status.setText(row_pos.getStatus());
            holder.contactType.setText(row_pos.getContactType());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}