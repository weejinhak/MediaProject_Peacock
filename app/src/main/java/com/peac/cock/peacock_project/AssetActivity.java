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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectSms.PermissionRequester;

import java.util.ArrayList;
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

        //Get Id
        Button assetAddButton = findViewById(R.id.asset_layout_button_assetAdd_button);
        ImageButton assetGoButton = findViewById(R.id.asset_layout_asset_go_button);
        ImageButton breakDownGoButton = findViewById(R.id.asset_layout_breakdown_go_button);
        ImageButton analysisGoButton = findViewById(R.id.asset_layout_analysis_go_button);
        ImageButton settingGoButton = findViewById(R.id.asset_layout_setting_go_button);

        //하단 페이지 넘기기 클릭 이벤트
        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "여기가 자산 페이지야!", Toast.LENGTH_SHORT).show();
            }
        });
        breakDownGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailTabActivity.class);
                startActivity(intent);
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnalysisActivity.class);
                startActivity(intent);
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //asset Add call method event
        assetAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSMSMessage();
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


    //Read_SMS_Phone
    public int readSMSMessage() {
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


            //국민은행 parsing
            if (address.equals("15881688") || address.equals("15881788")) {
                message = body;
                messageToken = message.split("\n");
                if (messageToken.length == 6) {
                    Log.d("***log", ++count + ":" + String.valueOf(messageToken.length));
                    Log.d("messageTokenTotal", messageToken[0] + "\n" + messageToken[1] + "\n" + messageToken[2] +
                            "\n" + messageToken[3] + "\n" + messageToken[4] + "\n" + messageToken[5]);
                    smsCardName = messageToken[1];

                    String date = messageToken[3];
                    String[] dateToken = date.split("\\s");
                    smsDate = dateToken[0];
                    System.out.println("smdate"+smsDate);
                    smsTime = dateToken[1];

                    String price = messageToken[4];
                    String[] priceToken = price.split("원");
                    smsPrice = priceToken[0].replaceAll("\\,", "");

                    smsPlace = messageToken[5];


                    System.out.println(smsCardName + "\n" + smsDate + "\n" + smsTime + "\n" + smsPrice + "\n" + smsPlace);

                    ledgerDto.setInOut("지출");
                    ledgerDto.setDate(smsDate);
                    ledgerDto.setTime(smsTime);
                    ledgerDto.setAmount(smsPrice);
                    ledgerDto.setContent(smsPlace);
                    ledgerDto.setAsset(new Asset(smsCardName));

                    //mDatabase.getReference().child("users").child(uid).child("ledger").push().setValue(ledgerDto);

                }
            }
        }
        return 0;
    }


}
