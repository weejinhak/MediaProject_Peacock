package com.peac.cock.peacock_project;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HandwritingActivity extends AppCompatActivity {

    private String state;
    private String amount;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;

    private LedgerDto ledgerDto = new LedgerDto();

    private Calendar dateTime = Calendar.getInstance();
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    private ImageButton backButton;
    private ImageButton saveButton;

    private Spinner inOut;
    private Spinner category;
    private ArrayList<String> categoryList;
    private EditText content;
    private EditText money;
    private Spinner asset;
    private Button date;
    private Button time;
    private EditText memo;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting);

        //firebase get auth & database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //before_intent_get
        intent = getIntent();
        amount = intent.getStringExtra("amount");
        state = intent.getStringExtra("state");

        backButton = findViewById(R.id.handwriting_layout_back_button);
        saveButton = findViewById(R.id.handwriting_layout_save_button);

        inOut = findViewById(R.id.handwriting_layout_inout_text);
        category = findViewById(R.id.handwriting_layout_category_text);
        content = findViewById(R.id.handwriting_layout_content_text);
        money = findViewById(R.id.handwriting_layout_amount_text);
        asset = findViewById(R.id.handwriting_layout_asset_text);
        date = findViewById(R.id.handwriting_layout_date_text);
        time = findViewById(R.id.handwriting_layout_time_text);
        memo = findViewById(R.id.handwriting_layout_memo_text);

        // 초기 설정
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.inOut, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inOut.setAdapter(stateAdapter);

        if(state.equals("outgoing")) {
            inOut.setSelection(0);
        } else if(state.equals("incoming")) {
            inOut.setSelection(1);
        } else {
            inOut.setSelection(2);
        }

        money.setText(amount);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(dateFormat.format(dateTime.getTime()).toString());

        timeFormat = new SimpleDateFormat("HH:mm");
        time.setText(timeFormat.format(dateTime.getTime()).toString());

        if(state.equals("outgoing")) {
            inOut.setSelection(0);
        } else if(state.equals("incoming")) {
            inOut.setSelection(1);
        } else {
            inOut.setSelection(2);
        }

        updateCategoryList();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);


        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("페이지에서 나가면 작성된 정보가 사라집니다. 그래도 나가시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
                                        intent.putExtra("amount", amount);
                                        intent.putExtra("state", state);
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog alert = dialog.create();
                alert.setTitle("잠깐!");
                alert.setIcon(R.drawable.calculator_layout_warning_icon);
                alert.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ledgerDto.setInOut(inOut.getSelectedItem().toString());
                ledgerDto.setCategory(category.getSelectedItem().toString());
                ledgerDto.setContent(content.getText().toString());
                ledgerDto.setAmount(money.getText().toString());
                ledgerDto.setAsset(new Card("kb", asset.getSelectedItem().toString()));
                ledgerDto.setDate(date.getText().toString());
                ledgerDto.setMemo(memo.getText().toString());

                mDatabase.getReference().child("users").child(uid).child("ledger").push().setValue(ledgerDto);
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTime();
            }
        });

        inOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateState();
                updateCategoryList();
            }
        });
    }

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateTime() {
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateText();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateTimeText();
        }
    };

    private void updateDateText() {
        date.setText(dateFormat.format(dateTime.getTime()).toString());
    }

    private void updateTimeText() {
        time.setText(timeFormat.format(dateTime.getTime()).toString());
    }

    private void updateCategoryList() {
        DatabaseReference databaseReference = mDatabase.getReference();
        //database get email && add ArrayList
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).getChildren()) {
                    String str = fileSnapshot.child("category").child(state).getValue(String.class);
                    System.out.println(str); // 카테고리 파싱
                    categoryList.add(str);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void updateState() {
        state = inOut.getSelectedItem().toString();
    }
}
