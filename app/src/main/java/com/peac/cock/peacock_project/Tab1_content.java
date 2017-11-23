package com.peac.cock.peacock_project;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by dmsru on 2017-11-13.
 */

public class Tab1_content extends Fragment implements AdapterView.OnItemClickListener{

    Calendar cal = Calendar.getInstance();
    String[] placeName;
    TypedArray categoryPicId;
    String[] purchase;
    String[] accountType;


    List<ListViewRowItem> rowItems;
    ListView mylistview;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;

    Date date= new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MM");
    int date2=Integer.parseInt(sdf.format(date));
    //private ImageView categoryImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_tab_list, container, false);
        rowItems = new ArrayList<ListViewRowItem>();
        placeName = getResources().getStringArray(R.array.place_name);
        categoryPicId = getResources().obtainTypedArray(R.array.category_pic_id);
        purchase = getResources().getStringArray(R.array.purchase);
        accountType = getResources().getStringArray(R.array.account_type);

        TextView textView;
        ImageButton button1;
        ImageButton button2;

        textView = (TextView) rootView.findViewById(R.id.currentDateLabel) ;
        button1 = (ImageButton) rootView.findViewById(R.id.previousButton);
        button2 = (ImageButton) rootView.findViewById(R.id.forwardButton);

        for (int i = 0; i < placeName.length; i++) {
            ListViewRowItem item = new ListViewRowItem(placeName[i],
                    categoryPicId.getResourceId(i, -1), purchase[i], accountType[i]);
            rowItems.add(item);
        }

        textView.setText(date2+"월");

        mylistview = (ListView) rootView.findViewById(R.id.List);
        ListViewCustomAdapter adapter = new ListViewCustomAdapter(getContext(), rowItems);
        mylistview.setAdapter(adapter);
        mylistview.setOnItemClickListener(this);

        //fireBase Auth & Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid=mAuth.getCurrentUser().getUid();

       // categoryImage = rootView.findViewById(R.id.category_add_layout_incoming_button);

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                minusOneMonth();
                textView.setText(date2+"월");
            }
        });
                button2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        addOneMonth();
                       textView.setText(date2+"월");
                    }
                });
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), CategoryAddActivity.class);
        startActivity(intent);
        String placeName = rowItems.get(position).getPlaceName();
        Toast.makeText(getContext(), "" + placeName, Toast.LENGTH_SHORT).show();
    }
    public  void addOneMonth()
    {
        if(date2==12){

        }else{
            date2=date2+1;
}


    }
    public void minusOneMonth()
    {

       if(date2==1){

       }else{
           date2=date2-1;
       }

    }
}
