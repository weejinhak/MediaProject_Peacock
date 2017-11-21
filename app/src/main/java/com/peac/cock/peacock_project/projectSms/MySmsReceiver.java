package com.peac.cock.peacock_project.projectSms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by wee on 2017. 11. 21..
 */

public class MySmsReceiver extends BroadcastReceiver {
    public MySmsReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String str = ""; // 출력할 문자열 저장
        if (bundle != null) { // 수신된 내용이 있으면
            // 실제 메세지는 Object타입의 배열에 PDU 형식으로 저장됨

            Object[] pdus = (Object[]) bundle.get("pdus");

            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += msgs[i].getOriginatingAddress() + "에게 문자왔음, " + msgs[i].getMessageBody().toString() + "\n";
            }
            Toast.makeText(context,
                    str, Toast.LENGTH_LONG).show();

        }
    }
}
