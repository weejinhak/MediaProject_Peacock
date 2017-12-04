package com.peac.cock.peacock_project;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;
import com.peac.cock.peacock_project.projectDto.Category;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HandwritingActivity extends AppCompatActivity {

    private String state;
    private String kState;
    private String amount;

    private FirebaseDatabase mDatabase;
    private String uid;
    private DatabaseReference databaseReference;

    private LedgerDto ledgerDto = new LedgerDto();

    private Calendar dateTime = Calendar.getInstance();
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private Spinner inOut;
    private Spinner category;
    private List<Category> categoryList;
    private EditText content;
    private EditText money;
    private Spinner asset;
    private List<Asset> assetList;
    private Button date;
    private Button time;
    private EditText memo;

    private Intent intent;

    private AlertDialog.Builder dialog;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_handwriting);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        intent = getIntent();
        amount = intent.getStringExtra("amount");
        state = intent.getStringExtra("state");

        final ImageButton backButton = findViewById(R.id.handwriting_layout_back_button);
        final ImageButton saveButton = findViewById(R.id.handwriting_layout_save_button);

        inOut = findViewById(R.id.handwriting_layout_inout_spinner);
        category = findViewById(R.id.handwriting_layout_category_spinner);
        content = findViewById(R.id.handwriting_layout_content_text);
        money = findViewById(R.id.handwriting_layout_amount_text);
        asset = findViewById(R.id.handwriting_layout_asset_spinner);
        date = findViewById(R.id.handwriting_layout_date_text);
        time = findViewById(R.id.handwriting_layout_time_text);
        time = findViewById(R.id.handwriting_layout_time_text);
        memo = findViewById(R.id.handwriting_layout_memo_text);

        final ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.inOut, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inOut.setAdapter(stateAdapter);

        switch (state) {
            case "outgoing":
                inOut.setSelection(0);
                kState = "지출";
                break;
            case "incoming":
                inOut.setSelection(1);
                kState = "수입";
                break;
            default:
                inOut.setSelection(2);
                kState = "이체";
                break;
        }

        callAssetList();

        money.setText(amount);

        dateFormat = new SimpleDateFormat("MM/dd");
        date.setText(dateFormat.format(dateTime.getTime()));

        timeFormat = new SimpleDateFormat("HH:mm");
        time.setText(timeFormat.format(dateTime.getTime()));

        updateCategoryList();

        dialog = new AlertDialog.Builder(this);

        backButton.setOnClickListener(view -> {
            dialog.setMessage("페이지에서 나가면 작성된 정보가 사라집니다. 그래도 나가시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            (dialog1, id) -> {
                                dialog1.dismiss();
                                Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                                intent.putExtra("amount", amount);
                                intent.putExtra("state", state);
                                startActivity(intent);
                            })
                    .setNegativeButton("No",
                            (dialog1, id) -> dialog1.dismiss());
            AlertDialog alert = dialog.create();
            alert.setTitle("잠깐!");
            alert.setIcon(R.drawable.calculator_layout_warning_icon);
            alert.show();
        });

        saveButton.setOnClickListener(view -> {
            ledgerDto.setInOut(inOut.getSelectedItem().toString());
            ledgerDto.setCategory((Category) category.getSelectedItem());
            ledgerDto.setContent(content.getText().toString());
            ledgerDto.setAmount(money.getText().toString());
            ledgerDto.setAsset((Asset)asset.getSelectedItem());
            ledgerDto.setDate(date.getText().toString());
            ledgerDto.setTime(time.getText().toString());
            ledgerDto.setMemo(memo.getText().toString());

            mDatabase.getReference().child("users").child(uid).child("ledger").push().setValue(ledgerDto);

            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });


        date.setOnClickListener(view -> updateDate());

        time.setOnClickListener(view -> updateTime());

        inOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateState();
                updateCategoryList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        asset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                asset.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateTime() {
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    DatePickerDialog.OnDateSetListener d = (view, year, month, dayOfMonth) -> {
        dateTime.set(Calendar.YEAR, year);
        dateTime.set(Calendar.MONTH, month);
        dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDateText();
    };

    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateTime.set(Calendar.MINUTE, minute);
        updateTimeText();
    };

    private void updateDateText() {
        date.setText(dateFormat.format(dateTime.getTime()));
    }

    private void updateTimeText() {
        time.setText(timeFormat.format(dateTime.getTime()));
    }

    private void updateCategoryList() {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("미분류",R.drawable.category_unclassified));

        databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("category").child(kState).getChildren()) {
                    System.out.println(fileSnapshot.getValue());
                    Category c = fileSnapshot.getValue(Category.class);
                    System.out.println(c);
                    categoryList.add(c);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);
        category.setSelection(0);
    }

    private void updateState() {
        kState = inOut.getSelectedItem().toString();
    }

    private void callAssetList() {
        assetList = new ArrayList<>();
        assetList.add(new Cash("기본", 0));

        databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("asset").child("card").getChildren()) {
                    Card card = new Card();
                    card.setBank(fileSnapshot.child("bank").getValue(String.class));
                    card.setNickname(fileSnapshot.child("nickname").getValue(String.class));
                    card.setBalance(fileSnapshot.child("balance").getValue(Integer.class));
                    assetList.add(card);
                }
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("asset").child("cash").getChildren()) {
                    Cash cash = new Cash();
                    cash.setNickname(fileSnapshot.child("nickname").getValue(String.class));
                    System.out.println(cash.getNickname());
                    cash.setBalance(fileSnapshot.child("balance").getValue(Integer.class));
                    assetList.add(cash);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        ArrayAdapter<Asset> assetAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assetList);
        assetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asset.setAdapter(assetAdapter);
        asset.setSelection(0);
    }
}
