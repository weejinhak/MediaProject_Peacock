package com.peac.cock.peacock_project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.Cash;

/**
 * Created by wee on 2017. 11. 13..
 */

public class AddCashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private Cash cash;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_add);

        //firebase get auth & database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        cash = new Cash();
        uid=mAuth.getCurrentUser().getUid();

        final EditText editTextNickName = findViewById(R.id.cash_add_layout_editText_nickName);
        final EditText editTextBalance = findViewById(R.id.cash_add_layout_editText_balance);
        Button addCashButton = findViewById(R.id.cash_add_layout_add_button);


        addCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash.setCashNickName(editTextNickName.getText().toString());
                cash.setBalance(editTextBalance.getText().toString());


                //uid에 맞는 정보 디비 입력
                mDatabase.getReference().child("users").child(uid).child("asset").child("cash").push().setValue(cash);

            }
        });

    }
}
