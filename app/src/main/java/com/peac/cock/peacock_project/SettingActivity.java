package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.BackPressCloseHandler;

/**
 * Created by wee on 2017. 11. 14..
 */

public class SettingActivity extends AppCompatActivity implements ValueEventListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private TextView categoryGoTextView;
    private TextView emailTextView;
    private TextView userNameTextView;

    private Intent intent;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        backPressCloseHandler = new BackPressCloseHandler(this);

        intent = new Intent();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        categoryGoTextView = findViewById(R.id.setting_layout_textView_categoryadd);
        emailTextView = findViewById(R.id.setting_layout_textView_userEmail);
        userNameTextView = findViewById(R.id.setting_layout_textView_userName);

        final ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        final ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        final ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        final ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        categoryGoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(),CategoryActivity.class);
                startActivity(intent);
            }
        });


        detailGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getApplicationContext(), DetailTabActivity.class);
                startActivity(intent);
            }
        });

        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getApplicationContext(), AssetActivity.class);
                startActivity(intent);
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), AnalysisActivity.class);
                startActivity(intent);
            }
        });


    }


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

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}