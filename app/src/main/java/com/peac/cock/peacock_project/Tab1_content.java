package com.peac.cock.peacock_project;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.ListTab1Adapter;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectDto.MessageItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dmsru on 2017-11-13.
 */

public class Tab1_content extends Fragment implements ValueEventListener {

    private TextView textView;
    private ImageButton button1;
    private ImageButton button2;
    private Date date = new Date();
    //  private SimpleDateFormat sdf = new SimpleDateFormat("MM");
    //  private int date2 = Integer.parseInt(sdf.format(date));
    private int date2 = 11;
    private ArrayList<MessageItem> messageItems = new ArrayList<>();
    private ListView listView;
    private ListTab1Adapter listTab1Adapter;

    private HashMap<String, ArrayList<MessageItem>> msgSetPerMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // listview, header 참조 획득.
        View rootView = inflater.inflate(R.layout.activity_tab_list, container, false);

        textView = rootView.findViewById(R.id.currentDateLabel);
        button1 = rootView.findViewById(R.id.previousButton);
        button2 = rootView.findViewById(R.id.forwardButton);
        textView.setText(date2 + "월");

        msgSetPerMonth = new HashMap<>();

        System.out.println(date2 + "currentMonth");

        //getId
        listView = rootView.findViewById(R.id.tab_list_msgList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MessageCategoryAddActivity.class);
                intent.putExtra("messageKey",messageItems.get(position).getMessageKey());
                Toast.makeText(getContext(), "클릭!" + messageItems.get(position).getMessageKey(), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                minusOneMonth();
                textView.setText(date2 + "월");
                listTab1Adapter.setMessageItems(msgSetPerMonth.get(String.valueOf(date2)));
                listView.setAdapter(listTab1Adapter);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addOneMonth();
                textView.setText(date2 + "월");
                listTab1Adapter.setMessageItems(msgSetPerMonth.get(String.valueOf(date2)));
                listView.setAdapter(listTab1Adapter);
            }
        });
        return rootView;
    }


    public void addOneMonth() {
        if (date2 == 12) {

        } else {
            date2 = date2 + 1;
        }
    }

    public void minusOneMonth() {

        if (date2 == 1) {

        } else {
            date2 = date2 - 1;
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dataSnapshot.getValue();

        for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("ledger").getChildren()) {
            LedgerDto ledger = fileSnapshot.getValue(LedgerDto.class);

            String msgContent = ledger.getContent();
            String[] msgContentToken = msgContent.split("\\s");

            String msgDate = ledger.getDate().substring(0, 2);
            Log.d("date", msgDate);

            String msgKey = fileSnapshot.getKey();

            //메시지 read 해올 때 카테고리 id 초기값으로  2131230977 줘야함.
            MessageItem messageItem = new MessageItem(ledger.getCategory().getCateImageId(), msgContentToken[0], ledger.getDate(),
                                                            Integer.parseInt(ledger.getAmount()),msgKey);

            messageItems.add(messageItem);

            if (msgSetPerMonth.get(msgDate) == null) {
                ArrayList<MessageItem> msgItems = new ArrayList<>();
                msgSetPerMonth.put(msgDate, msgItems);
            }
            msgSetPerMonth.get(msgDate).add(messageItem);

            Log.d("getKey",fileSnapshot.getKey());
            Log.d("msgSize", String.valueOf(msgSetPerMonth.get(msgDate).size()));
            Log.d("datedate", String.valueOf(date2));
            Log.d("msgSet", String.valueOf(msgSetPerMonth.get(String.valueOf(date2))));
            Log.d("getImageId", String.valueOf(ledger.getCategory().getCateImageId()));
        }

        listTab1Adapter = new ListTab1Adapter(getContext(), R.layout.activity_list_msg_item, msgSetPerMonth.get(String.valueOf(date2)));
        listView.setAdapter(listTab1Adapter);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference().removeEventListener(this);
    }

    @NonNull
    private String getUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser.getUid();
    }

}

