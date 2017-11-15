package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by wee on 2017. 11. 14..
 */

public class CategoryActivity extends AppCompatActivity {

    private GridView categoryGridView;
    private Button addCategoryButton;

    private String[] gridViewString = {
            "예술", "아이", "뷰티",
            "카페", "차", "담배",
            "문화", "다이아몬드", "애완",
            "음료", "교육", "음식",
            "교양", "헬스", "집",
            "법", "생활", "사랑",
            "돈", "휴대폰", "반지",
            "별", "교통", "여행"
    };
    private int[] gridViewImageId = {
            R.drawable.category_item_art, R.drawable.category_item_baby, R.drawable.category_item_beauty,
            R.drawable.category_item_caffe, R.drawable.category_item_car, R.drawable.category_item_cigarette,
            R.drawable.category_item_culture, R.drawable.category_item_diamond, R.drawable.category_item_dog,
            R.drawable.category_item_drink, R.drawable.category_item_education, R.drawable.category_item_food,
            R.drawable.category_item_gyeong, R.drawable.category_item_health, R.drawable.category_item_home,
            R.drawable.category_item_law, R.drawable.category_item_life, R.drawable.category_item_love,
            R.drawable.category_item_money, R.drawable.category_item_phone, R.drawable.category_item_ring,
            R.drawable.category_item_star, R.drawable.category_item_traffic, R.drawable.category_item_trip
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_category);

        CategoryGridViewActivity adapterViewCategory = new CategoryGridViewActivity(getApplicationContext(), gridViewString, gridViewImageId);


        //get Id
        categoryGridView = findViewById(R.id.category_grid_view_image_text);
        addCategoryButton = findViewById(R.id.category_grid_view_category_add_button);


        categoryGridView.setAdapter(adapterViewCategory);

        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getApplicationContext(), "클릭!" + gridViewString[i], Toast.LENGTH_LONG).show();
            }
        });

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CategoryActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.category_dialog_spinner, null);
                mBuilder.setTitle("카테고리추가");
                final Spinner mSpinner = mView.findViewById(R.id.category_dialog_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CategoryActivity.this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.spinner_category_list));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(adapter);

                mBuilder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("카테고리를 고르세요")) {
                            Toast.makeText(CategoryActivity.this, mSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();*/
                Intent intent =new Intent(getApplicationContext(),CategoryAddActivity.class);
                startActivity(intent);

            }
        });

    }
}
