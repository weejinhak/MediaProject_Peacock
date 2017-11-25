package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.CategoryGridViewAdapter;
import com.peac.cock.peacock_project.projectDto.Category;

/**
 * Created by wee on 2017. 11. 25..
 */

public class MessageCategoryAddActivity extends AppCompatActivity {
    private GridView categoryAddGridView;
    private ImageButton outgoingButton;
    private Category mCategory;
    private Button categoryAddButton;
    private String categoryType = "지출";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;
    private Intent intent;
    private String messageKey;

    String[] gridViewString = {
            "육아", "의복/미용", "카페/간식",
            "자동차", "문화", "술/유흥",
            "교육", "식사", "경조사",
            "의료/건강", "주거/통신", "생활",
            "교통", "여행/숙박"
    };
    int[] gridViewImageId = {
            R.drawable.baby,R.drawable.beauty,R.drawable.caffe,
            R.drawable.car,R.drawable.culture,R.drawable.drink,
            R.drawable.education,R.drawable.food,R.drawable.gyeong,
            R.drawable.health,R.drawable.home,R.drawable.life,
            R.drawable.traffic,R.drawable.trip
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_category_add);

        //fireBase Auth & Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //getIntent
        //before_intent_get
        intent = getIntent();
        messageKey = intent.getStringExtra("messageKey");


        //getId
        outgoingButton = findViewById(R.id.msg_category_add_layout_outgoing_button);
        categoryAddButton = findViewById(R.id.msg_category_add_grid_view_category_add_button);

        CategoryGridViewAdapter adapterViewCategory = new CategoryGridViewAdapter(getApplicationContext(), gridViewString, gridViewImageId);
        mCategory = new Category();
        //get Id
        categoryAddGridView = findViewById(R.id.msg_category_add_grid_view_image_text);

        categoryAddGridView.setAdapter(adapterViewCategory);

        categoryAddGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getApplicationContext(), "String" + gridViewString[i] + "ID" + gridViewImageId[i], Toast.LENGTH_LONG).show();
                System.out.println(categoryType);
                mCategory.setCateImageString(gridViewString[i]);
                mCategory.setCateImageId(gridViewImageId[i]);
            }
        });

        categoryAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.getReference().child("users").child(uid).child("ledger").child(messageKey).setValue(mCategory);
                Intent intent = new Intent(getApplicationContext(),DetailTabActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
