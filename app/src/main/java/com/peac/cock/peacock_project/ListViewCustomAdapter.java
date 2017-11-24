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
        ImageView category_pic;
        TextView placeName;
        TextView purchase;
        TextView accountType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_itemview, null);
            holder = new ViewHolder();

            holder.placeName = (TextView) convertView
                    .findViewById(R.id.place_name);
            holder.category_pic = (ImageView) convertView
                    .findViewById(R.id.category_pic);
            holder.purchase = (TextView) convertView.findViewById(R.id.purchase);
            holder.accountType = (TextView) convertView
                    .findViewById(R.id.account_type);

            ListViewRowItem row_pos = listViewRowItems.get(position);

            holder.category_pic.setImageResource(row_pos.getCategoryPicId());
            holder.placeName.setText(row_pos.getPlaceName());
            holder.purchase.setText(row_pos.getPurchase());
            holder.accountType.setText(row_pos.getAccountType());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}