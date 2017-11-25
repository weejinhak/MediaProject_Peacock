package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.CategoryGridViewAdapter;

import java.util.ArrayList;

/**
 * Created by wee on 2017. 11. 14..
 */

public class CategoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<String> getDbImageStringList;
    private ArrayList<Integer> getDbImageIdList;
    private String uid;
    private String state = "수입";
    private CategoryGridViewAdapter categoryGridViewAdapter;
    private GridView categoryGridView;

    private String[] gridViewString;
    private int[] gridViewImageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_category);


        //firebase Auth정보 get
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        getDbImageStringList = new ArrayList<>();
        getDbImageIdList = new ArrayList<>();
        uid = mAuth.getCurrentUser().getUid();

        //get Id
        categoryGridView = findViewById(R.id.category_grid_view_image_text);
        FloatingActionButton addCategoryButton = findViewById(R.id.category_grid_view_category_add_button);
        TabLayout tabLayout = findViewById(R.id.category_layout_tabs);

        callDatabase();

        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(getApplicationContext(), "클릭!" + gridViewString[i], Toast.LENGTH_LONG).show();
            }
        });

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryAddActivity.class);
                startActivity(intent);
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        state = "수입";
                        callDatabase();
                        break;
                    case 1:
                        state = "지출";
                        callDatabase();
                        break;
                    case 2:
                        state = "이체";
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

    //tab 클릭시 DB호출
    void callDatabase() {
        //getDB
        DatabaseReference databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();

                getDbImageStringList = new ArrayList<>();
                getDbImageIdList = new ArrayList<>();

                for (DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("category").child(state).getChildren()) {
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
