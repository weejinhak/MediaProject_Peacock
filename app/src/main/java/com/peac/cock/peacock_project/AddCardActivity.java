package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectDto.Card;

public class AddCardActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private String uid;

    private Card card;
    private String bankType;

    private EditText cardEditText_nickName;
    private EditText cardEditText_balance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_add);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        card = new Card();
        uid = mAuth.getCurrentUser().getUid();

        cardEditText_nickName = findViewById(R.id.card_add_layout_editText_nickName);
        cardEditText_balance = findViewById(R.id.card_add_layout_editText_balance);

        final Spinner cardArraySpinner = findViewById(R.id.add_card_layout_spinner_cardArrayItem);
        final Button cardAddButton = findViewById(R.id.card_add_layout_add_button);

        cardArraySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        cardAddButton.setOnClickListener(v -> {
            card.setBank(bankType);
            card.setNickname(cardEditText_nickName.getText().toString());
            card.setBalance(Integer.parseInt(cardEditText_balance.getText().toString()));

            mDatabase.getReference().child("users").child(uid).child("asset").child("card").push().setValue(card);
            Intent intent  = new Intent(getApplicationContext(),AssetActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
