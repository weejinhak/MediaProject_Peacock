package com.peac.cock.peacock_project;

import android.*;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectSms.PermissionRequester;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dahye on 2017-11-13.
 */

public class AssetActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;
    private DatabaseReference databaseReference;
    private LedgerDto ledgerDto = new LedgerDto();
    private PermissionRequester.Builder requester;
    private boolean isSmsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        //firebase get auth& database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //사용자 SMS권한 얻기
        requester = new PermissionRequester.Builder(this);
        requester.create().request(android.Manifest.permission.RECEIVE_SMS, 10000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
                Toast.makeText(getApplicationContext(), "권한을 얻지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //사용자의 DB에 smsList가 있는지 확인
        isSmsRead();

        //Get Id
        Button assetAddButton = findViewById(R.id.asset_layout_button_assetAdd_button);


        //asset Add call method event
        assetAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSmsList) {
                    readSMSMessage();
                }
                show();
            }
        });

    }

    void show() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("현금");
        ListItems.add("카드");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("자산 유형 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();
                if (selectedText.equals("현금")) {
                    Intent intent = new Intent(getApplicationContext(), AddCashActivity.class);
                    startActivity(intent);
                } else if (selectedText.equals("카드")) {
                    Intent intent = new Intent(getApplicationContext(), AddCardActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.show();

    }

    public void isSmsRead() {
        //smsRead했는지 확인
        databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                String str = (String) dataSnapshot.child("users").child(uid).child("isSMS").getValue();
                isSmsList = Boolean.parseBoolean(str);
                System.out.println(isSmsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //Read_SMS_Phone
    public int readSMSMessage() {
        mDatabase.getReference().child("users").child(uid).child("isSMS").setValue("true");
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, "date DESC");

        String string = "";
        int count = 0;
        while (c.moveToNext()) {
            long messageId = c.getLong(0);
            long threadId = c.getLong(1);
            String address = c.getString(2);
            long contactId = c.getLong(3);
            String contactId_string = String.valueOf(contactId);
            long timestamp = c.getLong(4);
            String body = c.getString(5);
            string = String.format("msgid:%d, threadid:%d, address:%s, " + "contactid:%d, contackstring:%s, timestamp:%d, body:%s", messageId, threadId, address, contactId,
                    contactId_string, timestamp, body);

            String message;
            String[] messageToken;
            String smsCardName = "";
            String smsDate = "";
            String smsTime = "";
            String smsPrice = "";
            String smsPlace = "";

            //국민은행 parsing 후 DB 추가
            if (address.equals("15881688") || address.equals("15881788")) {
                message = body;
                messageToken = message.split("\n");
                if (messageToken.length == 6) {
                    count++;
                    Log.d("***log", ++count + ":" + String.valueOf(messageToken.length));
                    Log.d("messageTokenTotal", messageToken[0] + "\n" + messageToken[1] + "\n" + messageToken[2] +
                            "\n" + messageToken[3] + "\n" + messageToken[4] + "\n" + messageToken[5]);
                    smsCardName = messageToken[1];

                    String date = messageToken[3];
                    String[] dateToken = date.split("\\s");
                    smsDate = dateToken[0];
                    smsTime = dateToken[1];
                    String price = messageToken[4];
                    String[] priceToken = price.split("원");
                    smsPrice = priceToken[0].replaceAll("\\,", "");
                    smsPlace = messageToken[5];

                    ledgerDto.setInOut("지출");
                    ledgerDto.setDate(smsDate);
                    ledgerDto.setTime(smsTime);
                    ledgerDto.setAmount(smsPrice);
                    ledgerDto.setContent(smsPlace);
                    ledgerDto.setAsset(new Asset(smsCardName));
                    mDatabase.getReference().child("users").child(uid).child("ledger").push().setValue(ledgerDto);
                }
            }
        }
        return 0;
    }

    public void cvcFileWrite(){
        String enc=new java.io.OutputStreamWriter(System.out).getEncoding();
        System.out.println("현재 인코딩 :" + enc);
    }


}


