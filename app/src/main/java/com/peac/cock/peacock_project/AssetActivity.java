package com.peac.cock.peacock_project;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.AssetCardAdapter;
import com.peac.cock.peacock_project.projectAdapter.AssetCashAdapter;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;
import com.peac.cock.peacock_project.projectDto.Category;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectSms.PermissionRequester;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dahye on 2017-11-13.
 */

public class AssetActivity extends Fragment implements ValueEventListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;
    private LedgerDto ledgerDto = new LedgerDto();
    private Category category= new Category();
    private PermissionRequester.Builder requester;
    private boolean isSmsList;
    private ArrayList<LedgerDto> arrayLedgerDto;
    private ArrayList<Card> cardItems;
    private ArrayList<Cash> cashItems;
    private int totalBalance;

    private Button assetAddButton;
    private TextView totalBalanceTextView;
    private ListView cardListView;
    private ListView cashListView;

    private Intent intent;

    public AssetActivity() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_asset, null);

        //사용자 SMS권한 얻기
        requester = new PermissionRequester.Builder(getActivity());
        requester.create().request(android.Manifest.permission.RECEIVE_SMS, 10000, new PermissionRequester.OnClickDenyButtonListener() {
            @Override
            public void onClick(Activity activity) {
                Toast.makeText(getActivity().getApplicationContext(), "권한을 얻지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        intent = new Intent();

        arrayLedgerDto = new ArrayList<>();
        category = new Category();
        cardItems = new ArrayList<>();
        cashItems = new ArrayList<>();

        //Get Id
        assetAddButton = view.findViewById(R.id.asset_layout_button_assetAdd_button);
        totalBalanceTextView = view.findViewById(R.id.asset_layout_textView_assetMoney_textView);
        cardListView = view.findViewById(R.id.asset_layout_card_listView);
        cashListView = view.findViewById(R.id.aseet_layout_cash_listView);

        //list View Adapter
        AssetCardAdapter cardAdapter = new AssetCardAdapter(getActivity(), R.layout.activity_asset_card_item, cardItems);
        AssetCashAdapter cashAdapter = new AssetCashAdapter(getActivity(), R.layout.activity_asset_cash_item, cashItems);
        cardListView.setAdapter(cardAdapter);
        cashListView.setAdapter(cashAdapter);

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

        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebase get auth& database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

    }

    //Dialog Show Method
    public void show() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("현금");
        ListItems.add("카드");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("자산 유형 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(getActivity().getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();
                if (selectedText.equals("현금")) {
                    intent.setClass(getActivity().getApplicationContext(), AddCashActivity.class);
                    startActivity(intent);
                } else if (selectedText.equals("카드")) {
                    intent.setClass(getActivity().getApplicationContext(), AddCardActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.show();

    }

    //Read_SMS_Phone
    public int readSMSMessage() {
        mDatabase.getReference().child("users").child(uid).child("isSMS").setValue("true");
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getActivity().getContentResolver();
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

                    category.setCateImageString("미분류");
                    category.setCateImageId(2131230981);
                    ledgerDto.setInOut("지출");
                    ledgerDto.setDate(smsDate);
                    ledgerDto.setTime(smsTime);
                    ledgerDto.setAmount(smsPrice);
                    ledgerDto.setContent(smsPlace);
                    ledgerDto.setAsset(new Asset(smsCardName));
                    ledgerDto.setCategory(category);
                    mDatabase.getReference().child("users").child(uid).child("ledger").push().setValue(ledgerDto);
                }
            }
        }
        return 0;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dataSnapshot.getValue();
        String str = (String) dataSnapshot.child("users").child(uid).child("isSMS").getValue();
        isSmsList = Boolean.parseBoolean(str);
        for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("ledger").getChildren()) {
            LedgerDto ledger = fileSnapshot.getValue(LedgerDto.class);
            arrayLedgerDto.add(ledger);
        }
        for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("asset").child("card").getChildren()) {
            Card card = fileSnapshot.getValue(Card.class);
            Card cardItem = new Card(card.getBank(), card.getNickname(), card.getBalance());
            cardItems.add(cardItem);
            totalBalance += card.getBalance();
        }
        for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("asset").child("cash").getChildren()) {
            Cash cash = fileSnapshot.getValue(Cash.class);
            Cash cashItem = new Cash(cash.getNickname(), cash.getBalance());
            cashItems.add(cashItem);
            totalBalance += cash.getBalance();
        }
        //전체 자산금액 setTextView
        totalBalanceTextView.setText(String.valueOf(totalBalance) + "원");

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


