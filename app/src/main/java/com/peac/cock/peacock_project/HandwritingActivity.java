package com.peac.cock.peacock_project;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

public class HandwritingActivity extends AppCompatActivity {

    private String state;
    private String amount;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;

    LedgerDto ledgerDto = new LedgerDto();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting);

        //firebase get auth & database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //before_intent_get
        final Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        state = intent.getStringExtra("state");

        final ImageButton backButton = findViewById(R.id.handwriting_layout_back_button);
        final ImageButton saveButton = findViewById(R.id.handwriting_layout_save_button);

        final EditText inOut = findViewById(R.id.handwriting_layout_inout_text);
        final EditText category = findViewById(R.id.handwriting_layout_category_text);
        final EditText content = findViewById(R.id.handwriting_layout_content_text);
        final EditText money = findViewById(R.id.handwriting_layout_amount_text);
        final EditText asset = findViewById(R.id.handwriting_layout_asset_text);
        final EditText date = findViewById(R.id.handwriting_layout_date_text);
        final EditText memo = findViewById(R.id.handwriting_layout_memo_text);

        money.setText(amount);


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
                ledgerDto.setInOut(inOut.getText().toString());
                ledgerDto.setCategory(category.getText().toString());
                ledgerDto.setContent(content.getText().toString());
                ledgerDto.setAmount(money.getText().toString());
                ledgerDto.setAsset(new Card("kb", asset.getText().toString()));
                ledgerDto.setDate(date.getText().toString());
                ledgerDto.setMemo(memo.getText().toString());


                mDatabase.getReference().child("users").child(uid).child("ledger").push().setValue(ledgerDto);
                Toast.makeText(getApplicationContext(), "저장되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
