package com.peac.cock.peacock_project;

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

/**
 * Created by wee on 2017. 11. 14..
 */

public class AddCardActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private String uid;
    private Card card;
    private String bankType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_add);

        //fireBase Auth & Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        card = new Card();
        uid=mAuth.getCurrentUser().getUid();

        //get Id
        Spinner cardArraySpiner = findViewById(R.id.add_card_layout_spinner_cardArrayItem);
        final EditText cardEditText_nickName = findViewById(R.id.card_add_layout_editText_nickName);
        final EditText cardEditText_balance = findViewById(R.id.card_add_layout_editText_balance);
        Button cardAddButton=findViewById(R.id.card_add_layout_add_button);

        cardArraySpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankType = (String) parent.getItemAtPosition(position);
                System.out.println(bankType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cardAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.setBank(bankType);
                card.setNickname(cardEditText_nickName.getText().toString());
                card.setBalance(cardEditText_balance.getText().toString());

                System.out.println(card);

                //uid에 맞는 정보 디비 입력
                mDatabase.getReference().child("users").child(uid).child("asset").child("card").push().setValue(card);

            }
        });



    }
}
