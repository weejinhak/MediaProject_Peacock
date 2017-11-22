package com.peac.cock.peacock_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by wee on 2017. 11. 8..
 */

public class LodingActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;

    private ArrayList<String> uidList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setStatusBarColor(Color.parseColor("#000000"));
        setContentView(R.layout.activity_loding);

        mDatabase=FirebaseDatabase.getInstance();

        startLoading();

        DatabaseReference databaseReference = mDatabase.getReference();
        //database get email && add ArrayList
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for (DataSnapshot fileSnapshot : dataSnapshot.child("users").getChildren()) {
                    String str = fileSnapshot.child("email").getValue(String.class);
                    System.out.println(str);//이메일 파싱
                    //System.out.println(fileSnapshot.getKey());//중요 -Uid 파싱
                    uidList.add(str);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.putStringArrayListExtra("uidList",uidList);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


}
