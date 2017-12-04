package com.peac.cock.peacock_project.projectAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peac.cock.peacock_project.R;
import com.peac.cock.peacock_project.projectDto.Cash;

import java.util.ArrayList;

public class AssetCashAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Cash> cashItems;
    private int layout;


    public AssetCashAdapter(Context context, int layout, ArrayList<Cash> cashItems) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cashItems = cashItems;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return cashItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cashItems.get(position).getNickname();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        Cash cashItem = cashItems.get(position);

        TextView cashNickName = convertView.findViewById(R.id.asset_item_cashName);
        cashNickName.setText(cashItem.getNickname());

        TextView cashBalance = convertView.findViewById(R.id.asset_item_cashbalance);
        cashBalance.setText(String.valueOf(cashItem.getBalance()));

        return convertView;
    }
}
