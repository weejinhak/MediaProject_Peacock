package com.peac.cock.peacock_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;

import java.util.ArrayList;
import java.util.List;

public class BudgetAnalysisActivity extends AppCompatActivity {

    private float rainfall[] = {98.8f, 123.8f, 161.6f, 24.2f, 52f, 58.2f, 35.4f, 13.3f, 78.4f, 203.4f, 240.2f, 159.7f};
    private String monthName[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private BarChart barChart;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_analysis);

        //firebase get auth & database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

    //    barChart = (BarChart) findViewById(R.id.bar_chart);

      //  callDetailData();

        setupBarChart(12, 50);
    }

    private void setupBarChart(int count, int range) {
        ArrayList<BarEntry> yVals = new ArrayList<>();
        float barWidth = 9f;
        float spaceForBar = 10f;

        for(int i = 0 ; i < count ; i++) {
            float val = (float) (Math.random() * range);
            yVals.add(new BarEntry(i * spaceForBar, val));
        }

        BarDataSet set1;

        set1 = new BarDataSet(yVals, "Data Set1");

        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);

        barChart.setData(data);
    }

    private void callDetailData() {

        databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("asset").child("card").getChildren()) {
                    Card card = new Card();
                    card.setBank(fileSnapshot.child("bank").getValue(String.class));
                    card.setNickname(fileSnapshot.child("nickname").getValue(String.class));
                    card.setBalance(fileSnapshot.child("balance").getValue(String.class));
            //        assetList.add(card);
                }
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("asset").child("cash").getChildren()) {
                    Cash cash = new Cash();
                    cash.setNickname(fileSnapshot.child("nickname").getValue(String.class));
                    System.out.println(cash.getNickname());
                    cash.setBalance(fileSnapshot.child("balance").getValue(String.class));
              //      assetList.add(cash);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}