package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.peac.cock.peacock_project.projectAdapter.BackPressCloseHandler;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;
import com.peac.cock.peacock_project.projectDto.Category;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectSms.PermissionRequester;

import java.util.ArrayList;
import java.util.List;

public class AssetActivity extends AppCompatActivity implements ValueEventListener {

    private FirebaseDatabase mDatabase;
    private String uid;

    private LedgerDto ledgerDto;
    private boolean isSmsList;

    private ArrayList<Card> cardItems;
    private ArrayList<Cash> cashItems;
    private Category category;

    private int totalBalance;

    private TextView totalBalanceTextView;

    private Intent intent;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_asset);

        ledgerDto = new LedgerDto();
        backPressCloseHandler=new BackPressCloseHandler(this);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        intent = new Intent();

        final ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        final ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        final ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        final ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        detailGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), DetailTabActivity.class);
            startActivity(intent);
        });

        assetGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), AssetActivity.class);
            startActivity(intent);
        });
        settingGoButton.setOnClickListener(v -> {
            intent.setClass(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });
        analysisGoButton.setOnClickListener(v -> {
            intent.setClass(getApplicationContext(), AnalysisActivity.class);
            startActivity(intent);
        });

        final PermissionRequester.Builder requester = new PermissionRequester.Builder(this);
        requester.create().request(android.Manifest.permission.RECEIVE_SMS, 10000, activity -> Toast.makeText(getApplicationContext(), "권한을 얻지 못했습니다.", Toast.LENGTH_SHORT).show());

        category=new Category();
        cardItems = new ArrayList<>();
        cashItems = new ArrayList<>();

        totalBalanceTextView = findViewById(R.id.asset_layout_textView_assetMoney_textView);

        final Button assetAddButton = findViewById(R.id.asset_layout_button_assetAdd_button);
        final ListView cardListView = findViewById(R.id.asset_layout_card_listView);
        final ListView cashListView = findViewById(R.id.aseet_layout_cash_listView);

        final AssetCardAdapter cardAdapter = new AssetCardAdapter(this, R.layout.activity_asset_card_item, cardItems);
        final AssetCashAdapter cashAdapter = new AssetCashAdapter(this, R.layout.activity_asset_cash_item, cashItems);

        cardListView.setAdapter(cardAdapter);
        cashListView.setAdapter(cashAdapter);

        assetAddButton.setOnClickListener(v -> {
            if (!isSmsList) {
                readSMSMessage();
            }
            show();
        });
    }

    void show() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("현금");
        ListItems.add("카드");

        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("자산 유형 선택");
        builder.setItems(items, (dialog, pos) -> {
            String selectedText = items[pos].toString();
            if (selectedText.equals("현금")) {
                Intent intent = new Intent(getApplicationContext(), AddCashActivity.class);
                startActivity(intent);
            } else if (selectedText.equals("카드")) {
                Intent intent = new Intent(getApplicationContext(), AddCardActivity.class);
                startActivity(intent);
            }
        });
        builder.show();

    }

    //Read SMS in Phone
    public void readSMSMessage() {
        mDatabase.getReference().child("users").child(uid).child("isSMS").setValue("true");
        Uri allMessage = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        @SuppressLint("Recycle") Cursor c = cr.query(allMessage, new String[]{"_id", "thread_id", "address", "person", "date", "body"}, null, null, "date DESC");

        while (c.moveToNext()) {
        //    long messageId = c.getLong(0);
        //    long threadId = c.getLong(1);
            String address = c.getString(2);
        //    long contactId = c.getLong(3);
        //    String contactId_string = String.valueOf(contactId);
        //    long timestamp = c.getLong(4);
            String body = c.getString(5);
        //    @SuppressLint("DefaultLocale") String text = String.format("msgid:%d, threadid:%d, address:%s, " + "contactid:%d, contackstring:%s, timestamp:%d, body:%s", messageId, threadId, address, contactId, contactId_string, timestamp, body);

            if (address.equals("15881688")) { // 국민은행 번호
                String[] messageToken = body.split("\n");
                if (messageToken.length == 6) {
                    String smsCardName = messageToken[1];

                    String date = messageToken[3];
                    String[] dateToken = date.split("\\{space}");
                    String smsDate = dateToken[0];
                    String smsTime = dateToken[1];
                    String price = messageToken[4];
                    String[] priceToken = price.split("원");
                    String smsPrice = priceToken[0].replaceAll(",", "");
                    String smsPlace = messageToken[5];

                    category.setCateImageId(R.drawable.category_unclassified);
                    category.setCateImageString("미분류");
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
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dataSnapshot.getValue();
        String str = (String) dataSnapshot.child("users").child(uid).child("isSMS").getValue();
        isSmsList = Boolean.parseBoolean(str);

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

        String text = String.valueOf(totalBalance) + "원";
        totalBalanceTextView.setText(text);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {  }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference().removeEventListener(this);
    }

    @NonNull
    private String getUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser.getUid();
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}

