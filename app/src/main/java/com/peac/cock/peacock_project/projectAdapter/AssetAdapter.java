package com.peac.cock.peacock_project.projectAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peac.cock.peacock_project.R;
import com.peac.cock.peacock_project.projectDto.AssetItem;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by wee on 2017. 11. 24..
 */

public class AssetAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<AssetItem> assetItems;
    private int layout;

    public AssetAdapter(Context context, int layout, ArrayList<AssetItem> assetItems) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.assetItems = assetItems;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return assetItems.size();
    }

    @Override
    public Object getItem(int position) {
        return assetItems.get(position).getCardName();
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
        AssetItem assetItem = assetItems.get(position);

        TextView cardName = convertView.findViewById(R.id.asset_item_cardName);
        cardName.setText(assetItem.getCardName());

        TextView balance = convertView.findViewById(R.id.asset_item_balance);
        balance.setText(String.valueOf(assetItem.getBalance()));

        return convertView;
    }
}
