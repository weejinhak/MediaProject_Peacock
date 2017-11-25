package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wee on 2017. 11. 14..
 */

public class SettingActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();

        TextView categoryGoTextView=findViewById(R.id.setting_layout_textView_categoryadd);
        final TextView emailTextView=findViewById(R.id.setting_layout_textView_userEmail);
        final TextView userNameTextView=findViewById(R.id.setting_layout_textView_userName);

        ImageButton assetGoButton= findViewById(R.id.setting_layout_asset_go_button);
        ImageButton breakDownGoButton=findViewById(R.id.setting_layout_breakdown_go_button);
        ImageButton analysisGoButton=findViewById(R.id.setting_layout_analysis_go_button);
        ImageButton settingGoButton=findViewById(R.id.setting_layout_setting_go_button);

        //get DataBase
        DatabaseReference databaseReference = mDatabase.getReference();
        //database get email && add ArrayList
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                String name= dataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).child("name").getValue(String.class);
                emailTextView.setText(mAuth.getCurrentUser().getEmail());
                userNameTextView.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        categoryGoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),CategoryActivity.class);
                startActivity(intent);
            }
        });

        //하단 페이지 넘기기 클릭 이벤트
        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),AssetActivity.class);
                startActivity(intent);
            }
        });
        breakDownGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailTabActivity.class);
                startActivity(intent);
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), AnalysisActivity.class);
                startActivity(intent);
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "여기가 세팅 페이지야!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
