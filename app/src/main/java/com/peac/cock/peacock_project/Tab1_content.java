package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.ListTab1Adapter;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectDto.MessageItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Tab1_content extends Fragment implements ValueEventListener {

    private TextView textView;
    private Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("MM");
    private int selectedDate = Integer.parseInt(sdf.format(date));
    private ArrayList<MessageItem> messageItems = new ArrayList<>();
    private ListView listView;
    private ListTab1Adapter listTab1Adapter;

    private HashMap<String, ArrayList<MessageItem>> msgSetPerMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_tab_list, container, false);

        textView = rootView.findViewById(R.id.currentDateLabel);
        final ImageButton button1 = rootView.findViewById(R.id.previousButton);
        final ImageButton button2 = rootView.findViewById(R.id.forwardButton);
        final String text = selectedDate + "월";
        textView.setText(text);

        msgSetPerMonth = new HashMap<>();

        listView = rootView.findViewById(R.id.tab_list_msgList);

        listTab1Adapter = new ListTab1Adapter(getContext(), R.layout.activity_list_msg_item, msgSetPerMonth.get(String.valueOf(selectedDate)));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), MessageCategoryAddActivity.class);
            intent.putExtra("messageKey",messageItems.get(position).getMessageKey());
            startActivity(intent);
        });

        button1.setOnClickListener(arg0 -> {
            minusOneMonth();
            String text1 = selectedDate + "월";
            textView.setText(text1);
            listTab1Adapter.setMessageItems(msgSetPerMonth.get(String.valueOf(selectedDate)));
            listView.setAdapter(listTab1Adapter);
        });
        button2.setOnClickListener(arg0 -> {
            addOneMonth();
            String text1 = selectedDate + "월";
            textView.setText(text1);
            listTab1Adapter.setMessageItems(msgSetPerMonth.get(String.valueOf(selectedDate)));
            listView.setAdapter(listTab1Adapter);
        });

        return rootView;
    }


    public void addOneMonth() {
        if (selectedDate != 12) {
            selectedDate = selectedDate + 1;
        }
    }

    public void minusOneMonth() {

        if (selectedDate != 1) {
            selectedDate = selectedDate - 1;
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
            String msgKey = fileSnapshot.getKey();

            MessageItem messageItem = new MessageItem(ledger.getCategory().getCateImageId(), msgContentToken[0], ledger.getDate(),
                                                            Integer.parseInt(ledger.getAmount()),msgKey);

            messageItems.add(messageItem);
            if (msgSetPerMonth.get(msgDate) == null) {
                msgSetPerMonth.put(msgDate, new ArrayList<>());
            }
            msgSetPerMonth.get(msgDate).add(messageItem);
        }

        listTab1Adapter.setMessageItems(msgSetPerMonth.get(String.valueOf(selectedDate)));
        listView.setAdapter(listTab1Adapter);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {  }

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

