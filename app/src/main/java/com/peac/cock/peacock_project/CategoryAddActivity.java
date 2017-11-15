package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by wee on 2017. 11. 15..
 */

public class CategoryAddActivity extends AppCompatActivity {

    GridView categoryAddGridView;

    ImageButton incomingButton;
    ImageButton outgoingButton;
    ImageButton transferButton;

    String[] gridViewString = {
            "예술", "아이", "뷰티",
            "카페", "차", "담배",
            "문화", "다이아몬드", "애완",
            "음료", "교육", "음식",
            "교양", "헬스", "집",
            "법", "생활", "사랑",
            "돈", "휴대폰", "반지",
            "별", "교통", "여행"
    };
    int[] gridViewImageId = {
            R.drawable.category_item_art, R.drawable.category_item_baby, R.drawable.category_item_beauty,
            R.drawable.category_item_caffe, R.drawable.category_item_car, R.drawable.category_item_cigarette,
            R.drawable.category_item_culture, R.drawable.category_item_diamond, R.drawable.category_item_dog,
            R.drawable.category_item_drink, R.drawable.category_item_education, R.drawable.category_item_food,
            R.drawable.category_item_gyeong, R.drawable.category_item_health, R.drawable.category_item_home,
            R.drawable.category_item_law, R.drawable.category_item_life, R.drawable.category_item_love,
            R.drawable.category_item_money, R.drawable.category_item_phone, R.drawable.category_item_ring,
            R.drawable.category_item_star, R.drawable.category_item_traffic, R.drawable.category_item_trip
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        incomingButton=findViewById(R.id.category_add_layout_incoming_button);
        outgoingButton=findViewById(R.id.category_add_layout_outgoing_button);
        transferButton=findViewById(R.id.category_add_layout_transfer_button);

        CategoryGridViewActivity adapterViewCategory = new CategoryGridViewActivity(getApplicationContext(), gridViewString, gridViewImageId);
        System.out.println("크리에이터까지 들어옴");

        //get Id
        categoryAddGridView = findViewById(R.id.category_add_grid_view_image_text);

        categoryAddGridView.setAdapter(adapterViewCategory);

        categoryAddGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getApplicationContext(), "클릭!" + gridViewString[i], Toast.LENGTH_LONG).show();
            }
        });

        outgoingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_active_outgoing_button);
                    incomingButton.setBackgroundResource(R.drawable.handwriting_layout_incoming_button);
                    transferButton.setBackgroundResource(R.drawable.handwriting_layout_transfer_button);
                }
                return false;
            }
        });

        incomingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    incomingButton.setBackgroundResource(R.drawable.handwriting_layout_active_incoming_button);
                    outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_outgoing_button);
                    transferButton.setBackgroundResource(R.drawable.handwriting_layout_transfer_button);
                }
                return false;
            }
        });

        transferButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    transferButton.setBackgroundResource(R.drawable.handwriting_layout_active_transfer_button);
                    incomingButton.setBackgroundResource(R.drawable.handwriting_layout_incoming_button);
                    outgoingButton.setBackgroundResource(R.drawable.handwriting_layout_outgoing_button);
                }
                return false;
            }
        });

    }
}
