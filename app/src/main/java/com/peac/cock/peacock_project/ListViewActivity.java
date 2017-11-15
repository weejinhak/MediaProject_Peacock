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

        String[] member_names;
        TypedArray profile_pics;
        String[] statues;
        String[] contactType;

        List<ListViewRowItem> rowItems;
        ListView mylistview;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        rowItems = new ArrayList<ListViewRowItem>();

        member_names = getResources().getStringArray(R.array.Member_names);

        profile_pics = getResources().obtainTypedArray(R.array.profile_pics);

        statues = getResources().getStringArray(R.array.contactType);

        contactType = getResources().getStringArray(R.array.contactType);

        for(int i =0; i< member_names.length; i++){
        ListViewRowItem item = new ListViewRowItem(member_names[i],
        profile_pics.getResourceId(i, -1), statues[i],
        contactType[i]);
        rowItems.add(item);
        }

        mylistview = (ListView) findViewById(R.id.List);
        ListViewCustomAdapter adapter = new ListViewCustomAdapter(this, rowItems);
        mylistview.setAdapter(adapter);

        mylistview.setOnItemClickListener(this);

        }

@Override
public  void onItemClick(AdapterView<?> parent, View view, int position,
                         long id) {
        String member_name = rowItems.get(position).getMember_name();
        Toast.makeText(getApplicationContext(), "" + member_name,
        Toast.LENGTH_SHORT).show();
        }

        }
