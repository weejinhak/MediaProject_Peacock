package com.peac.cock.peacock_project;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmsru on 2017-11-15.
 */

public class ListViewActivity extends Activity implements AdapterView.OnItemClickListener {

    String[] placeName;
    TypedArray categoryPicId;
    String[] purchase;
    String[] accountType;

    List<ListViewRowItem> rowItems;
    ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        rowItems = new ArrayList<ListViewRowItem>();

        placeName = getResources().getStringArray(R.array.place_name);

        categoryPicId = getResources().obtainTypedArray(R.array.category_pic_id);

        purchase = getResources().getStringArray(R.array.purchase);

        accountType = getResources().getStringArray(R.array.account_type);

        for (int i = 0; i < placeName.length; i++) {
            ListViewRowItem item = new ListViewRowItem(placeName[i],
                    categoryPicId.getResourceId(i, -1), purchase[i], accountType[i]);
            rowItems.add(item);
        }

        mylistview = (ListView) findViewById(R.id.List);
        ListViewCustomAdapter adapter = new ListViewCustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String member_name = rowItems.get(position).getMember_name();
        Toast.makeText(getApplicationContext(), "" + member_name, Toast.LENGTH_SHORT).show();
    }

}
