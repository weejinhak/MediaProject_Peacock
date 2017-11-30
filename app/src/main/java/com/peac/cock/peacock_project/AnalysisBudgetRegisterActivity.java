package com.peac.cock.peacock_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Category;
import com.peac.cock.peacock_project.projectDto.CategoryBudget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wee on 2017. 11. 14..
 */

public class AnalysisBudgetRegisterActivity extends AppCompatActivity implements ValueEventListener {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;

    private EditText budgetRegisterBudgetText;
    private Spinner budgetRegisterCategorySpinner;
    private Button budgetRegisterButton;

    private CategoryBudget categoryBudget;

    private List<String> categoryList;

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

                categoryBudget.setCategoryName(budgetRegisterCategorySpinner.getSelectedItem().toString());
                categoryBudget.setBudget(budgetRegisterBudgetText.getText().toString());

                mDatabase.getReference().child("users").child(uid).child("categoryBudget").push().setValue(categoryBudget);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dataSnapshot.getValue();
        for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("category").getChildren()) {
            Category category = fileSnapshot.getValue(Category.class);
            categoryList.add(category.getCateImageString().toString());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
