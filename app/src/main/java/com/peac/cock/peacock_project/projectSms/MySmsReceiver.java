package com.peac.cock.peacock_project.projectSms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

/**
 * Created by wee on 2017. 11. 21..
 */

public class MySmsReceiver extends BroadcastReceiver {

    private LedgerDto ledgerDto;
    private FirebaseAuth auth;
    private FirebaseDatabase mDatabase;
    private String uid;

    public MySmsReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String str = ""; // 출력할 문자열 저장
        String number = "";
        String body="";

        ledgerDto= new LedgerDto();
        auth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        uid=auth.getCurrentUser().getUid();

        if (bundle != null) { // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨

            Object[] pdus = (Object[]) bundle.get("pdus");

            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress() + "에게 문자왔음, " + msgs[i].getMessageBody().toString() + "\n";
                number += msgs[i].getOriginatingAddress();
                body += msgs[i].getMessageBody().toString();

            }
            //System.out.println(str);
            System.out.println("문자전화번호:" + number);

            //message 파싱후 DB저장
            if(number.equals("01032368187")){
                String message=body;
                String[] messageToken = message.split("\n");
                String smsCardName = "";
                String smsDate = "";
                String smsTime = "";
                String smsPrice = "";
                String smsPlace = "";

                if (messageToken.length == 6) {

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
            Log.d("Tag",body);
            System.out.println("문자내용"+"\n"+body);
        }
    }
}
