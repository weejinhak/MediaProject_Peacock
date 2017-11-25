package com.peac.cock.peacock_project.projectAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peac.cock.peacock_project.R;
import com.peac.cock.peacock_project.projectDto.Card;

import java.util.ArrayList;

/**
 * Created by wee on 2017. 11. 24..
 */

public class AssetCardAdapter  extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Card> cardItems;
    private int layout;

    public AssetCardAdapter(Context context, int layout, ArrayList<Card> cardItems){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cardItems=cardItems;
        this.layout=layout;
    }


    @Override
    public int getCount() {
        return cardItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cardItems.get(position).getNickname();
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
        Card cardItem = cardItems.get(position);

        TextView bank = convertView.findViewById(R.id.asset_item_bankName);
        bank.setText(cardItem.getBank());

        TextView cardName = convertView.findViewById(R.id.asset_item_cardName);
        cardName.setText(cardItem.getNickname());

        TextView balance = convertView.findViewById(R.id.asset_item_cardbalance);
        balance.setText(String.valueOf(cardItem.getBalance()));

        return convertView;
    }
}
