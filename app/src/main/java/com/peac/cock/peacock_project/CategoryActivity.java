package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.CategoryGridViewAdapter;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private String uid;

    private ArrayList<String> getDbImageStringList;
    private ArrayList<Integer> getDbImageIdList;

    private String categoryType;

    private CategoryGridViewAdapter categoryGridViewAdapter;
    private GridView categoryGridView;

    private String[] gridViewString;
    private int[] gridViewImageId;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_category);

        intent = getIntent();

        if(intent.getStringExtra("categoryType") != null) {
            categoryType = intent.getStringExtra("categoryType");
        } else {
            categoryType = "수입";
        }

        final int state;
        switch (categoryType) {
            case "수입":
                state = 0;
                break;
            case "지출":
                state = 1;
                break;
            default:
                state = 2;
                break;
        }

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        getDbImageStringList = new ArrayList<>();
        getDbImageIdList = new ArrayList<>();

        categoryGridView = findViewById(R.id.category_grid_view_image_text);

        final ImageButton addCategoryButton = findViewById(R.id.category_grid_view_category_add_button);
        final ImageButton backButton = findViewById(R.id.category_layout_back_button);

        final TabLayout tabLayout = findViewById(R.id.category_layout_tabs);

        TabLayout.Tab tab = tabLayout.getTabAt(state);
        tab.select();

        callDatabase();

        categoryGridView.setOnItemClickListener((parent, view, i, id) -> {
        });

        addCategoryButton.setOnClickListener(v -> {
            intent.setClass(getApplicationContext(), CategoryAddActivity.class);
            intent.putExtra("categoryType", categoryType);
            startActivity(intent);
        });

        backButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        categoryType = "수입";
                        callDatabase();
                        break;
                    case 1:
                        categoryType = "지출";
                        callDatabase();
                        break;
                    case 2:
                        categoryType = "이체";
                        callDatabase();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    void callDatabase() {
        DatabaseReference databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();

                getDbImageStringList = new ArrayList<>();
                getDbImageIdList = new ArrayList<>();

                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("category").child(categoryType).getChildren()) {
                    long id = (long) fileSnapshot.child("cateImageId").getValue();
                    int imgId = (int) id;
                    String imgString = fileSnapshot.child("cateImageString").getValue(String.class);
                    getDbImageStringList.add(imgString);
                    getDbImageIdList.add(imgId);
                }

                gridViewString = new String[getDbImageStringList.size()];
                gridViewImageId = new int[getDbImageIdList.size()];

                for (int i = 0; i < getDbImageStringList.size(); i++) {
                    gridViewString[i] = getDbImageStringList.get(i);
                    gridViewImageId[i] = getDbImageIdList.get(i);
                }
                categoryGridViewAdapter = new CategoryGridViewAdapter(getApplicationContext(), gridViewString, gridViewImageId);
                categoryGridView.setAdapter(categoryGridViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
