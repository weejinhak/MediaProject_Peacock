package com.peac.cock.peacock_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wee on 2017. 11. 14..
 */

public class CategoryGridViewActivity extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public CategoryGridViewActivity(Context mContext, String[] gridViewString, int[] gridViewImageId) {
        this.mContext = mContext;
        this.gridViewString = gridViewString;
        this.gridViewImageId = gridViewImageId;
    }


    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.item_category_gridview, null);
            TextView textViewAndroid = gridViewAndroid.findViewById(R.id.category_gridView_text);
            ImageView imageViewAndroid = gridViewAndroid.findViewById(R.id.category_gridView_image);
            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = convertView;
        }

        return gridViewAndroid;

    }
}
