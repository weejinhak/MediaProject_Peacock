package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.peac.cock.peacock_project.projectAdapter.CategoryGridViewAdapter;
import com.peac.cock.peacock_project.projectDto.Category;


public class MessageCategoryAddActivity extends AppCompatActivity {
    private Category mCategory;
    private FirebaseDatabase mDatabase;
    private String uid;
    private String messageKey;

    String[] gridViewString = {
            "아이", "뷰티", "카페",
            "차", "담배", "문화",
            "애완", "음료", "교육",
            "음식", "교양", "헬스",
            "집", "생활", "사랑",
            "휴대폰", "교통", "여행"
    };
    int[] gridViewImageId = {
           R.drawable.category_item_baby,R.drawable.category_item_beauty,R.drawable.category_item_caffe,
            R.drawable.category_item_car,R.drawable.category_item_cigarette,R.drawable.category_item_culture,
            R.drawable.category_item_dog,R.drawable.category_item_drink,R.drawable.category_item_education,
            R.drawable.category_item_food,R.drawable.category_item_gyeong,R.drawable.category_item_health,
            R.drawable.category_item_home,R.drawable.category_item_life,R.drawable.category_item_love,
            R.drawable.category_item_phone,R.drawable.category_item_traffic,R.drawable.category_item_trip
    };


    String[] matchGridViewString = {
            "육아", "의복/미용", "카페/간식",
            "자동차", "카페/간식", "문화/여가",
            "애완", "카페/간식", "교육",
            "식사", "문화/여가", "의료/건강",
            "주거/통신","주거/통신","사랑",
            "주거/통신", "교통","여행/숙박"
    };
    int[] matchGridViewImageId = {
          R.drawable.baby,R.drawable.beauty,R.drawable.caffe,
            R.drawable.car,R.drawable.caffe,R.drawable.culture,
            R.drawable.category_animal,R.drawable.caffe,R.drawable.education,
            R.drawable.food,R.drawable.culture,R.drawable.health,
            R.drawable.home,R.drawable.home,R.drawable.category_love,
            R.drawable.home,R.drawable.traffic,R.drawable.trip
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_msg_category_add);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        final Intent intent = getIntent();
        messageKey = intent.getStringExtra("messageKey");

        final Button categoryAddButton = findViewById(R.id.msg_category_add_grid_view_category_add_button);

        CategoryGridViewAdapter adapterViewCategory = new CategoryGridViewAdapter(getApplicationContext(), gridViewString, gridViewImageId);
        mCategory = new Category();

        final GridView categoryAddGridView = findViewById(R.id.msg_category_add_grid_view_image_text);
        categoryAddGridView.setAdapter(adapterViewCategory);

        categoryAddGridView.setOnItemClickListener((parent, view, i, id) -> {
            mCategory.setCateImageString(matchGridViewString[i]);
            mCategory.setCateImageId(matchGridViewImageId[i]);
        });

        categoryAddButton.setOnClickListener(v -> {
            mDatabase.getReference().child("users").child(uid).child("ledger").child(messageKey).child("category").setValue(mCategory);
            Intent intent1 = new Intent(getApplicationContext(),DetailTabActivity.class);
            startActivity(intent1);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),DetailTabActivity.class);
        startActivity(intent);
        finish();
    }
}
