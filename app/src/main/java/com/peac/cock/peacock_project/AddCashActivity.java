package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.Cash;

public class AddCashActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private String uid;

    private Cash cash;

    private EditText editTextNickName;
    private EditText editTextBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_add);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        cash = new Cash();

        editTextNickName = findViewById(R.id.cash_add_layout_editText_nickName);
        editTextBalance = findViewById(R.id.cash_add_layout_editText_balance);

        final Button addCashButton = findViewById(R.id.cash_add_layout_add_button);


        addCashButton.setOnClickListener(v -> {
            cash.setNickname(editTextNickName.getText().toString());
            cash.setBalance(Integer.parseInt(editTextBalance.getText().toString()));

            mDatabase.getReference().child("users").child(uid).child("asset").child("cash").push().setValue(cash);
            Intent intent  = new Intent(getApplicationContext(),AssetActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
