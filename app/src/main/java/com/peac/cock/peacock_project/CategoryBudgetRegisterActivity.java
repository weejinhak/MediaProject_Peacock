package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Category;
import com.peac.cock.peacock_project.projectDto.CategoryBudget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wee on 2017. 11. 14..
 */

public class CategoryBudgetRegisterActivity extends AppCompatActivity implements ValueEventListener {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;

    private EditText budgetRegisterBudgetText;
    private Spinner budgetRegisterCategorySpinner;
    private Button budgetRegisterButton;

    private CategoryBudget categoryBudget;

    private List<Category> categoryList;

    private ArrayAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_budget_register);

        //fireBase Auth & Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().toString();

        categoryBudget = new CategoryBudget();

        categoryList = new ArrayList<>();

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //get Id
        budgetRegisterCategorySpinner = findViewById(R.id.budget_register_layout_category_spinner);
        budgetRegisterBudgetText = findViewById(R.id.budget_register_layout_budget_text);
        budgetRegisterButton = findViewById(R.id.budget_register_layout_register_button);

        budgetRegisterCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                budgetRegisterCategorySpinner.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        budgetRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AnalysisActivity.class);
                intent.putExtra("budgetRegister", "true");

                categoryBudget.setCategory((Category)budgetRegisterCategorySpinner.getSelectedItem());
                categoryBudget.setBudget(budgetRegisterBudgetText.getText().toString());

                mDatabase.getReference().child("users").child(uid).child("categoryBudget").push().setValue(categoryBudget);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dataSnapshot.getValue();
        categoryList.add(new Category("미분류", R.drawable.category_unclassified));

        for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("category").child("지출").getChildren()) {
            Category category = fileSnapshot.getValue(Category.class);
            categoryList.add(category);
        }
        budgetRegisterCategorySpinner.setAdapter(categoryAdapter);
        budgetRegisterCategorySpinner.setSelection(0);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference().removeEventListener(this);
    }

    @NonNull
    private String getUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser.getUid();
    }

}
