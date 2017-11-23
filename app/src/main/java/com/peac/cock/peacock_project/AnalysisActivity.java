package com.peac.cock.peacock_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TabHost;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

public class AnalysisActivity extends AppCompatActivity implements ValueEventListener{
    private float rainfall[] = {98.8f, 123.8f, 161.6f, 24.2f, 52f, 58.2f, 35.4f, 13.3f, 78.4f, 203.4f, 240.2f, 159.7f};
    private String monthName[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private PieChart pieChart;
    private BarChart barChart;

    private ArrayList<LedgerDto> ledgers;
    private HashMap<String, String> datas = new HashMap<>();

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

    //    data = (HashMap<String,String>) intent.getSerializableExtra("data");
        Intent intent = getIntent();
        System.out.println(intent.getExtras());
        ArrayList<String> categoryList = intent.getStringArrayListExtra("categoryList");
        ArrayList<Integer> amountList = intent.getIntegerArrayListExtra("amountList");
        System.out.println("받아온 값 / categoryList : " + categoryList);
        System.out.println("받아온 값 / amountList : " + amountList);

        final TabHost host = findViewById(R.id.tab_host);
        host.setup();

        TabHost.TabSpec page1 = host.newTabSpec("분석");
        page1.setContent(R.id.analysis);
        page1.setIndicator("분석");
        host.addTab(page1);

        TabHost.TabSpec page2 = host.newTabSpec("예산");
        page2.setContent(R.id.budget);
        page2.setIndicator("예산");
        host.addTab(page2);

        //firebase get auth & database;
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        ledgers = new ArrayList<>();


     //   callDetailData();

        pieChart = findViewById(R.id.pie_chart);
        barChart = findViewById(R.id.bar_chart);
        setupBarChart(12, 50);

     //   setupPieChart();

    }

    @NonNull
    private String getUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser.getUid();
    }

    protected void setupPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();

       if(datas.size() != 0) {
            for (Map.Entry<String, String> d : datas.entrySet()) {
                System.out.println("value : " + d.getValue() + "key :" + d.getKey());
                pieEntries.add(new PieEntry(Integer.parseInt(d.getValue()), d.getKey()));
                System.out.println(pieEntries);
            }
        } else {
            System.out.println("데이터 없음");
        }

//        for(int i = 0 ; i < rainfall.length ; i++) {
//            pieEntries.add(new PieEntry(rainfall[i], monthName[i]));
//            System.out.println(pieEntries);
//        }


        PieDataSet dataSet = new PieDataSet(pieEntries, "Outgoing Pattern");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.99f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        pieChart.setData(data);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.invalidate();
    }

    protected void setupBarChart(int count, int range) {
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dataSnapshot.getValue();
        for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("ledger").getChildren()) {
            LedgerDto ledger = fileSnapshot.getValue(LedgerDto.class);

            ledgers.add(ledger);

            boolean check = false;

            if(ledger.getInOut().equals("지출")) {
                String category = ledger.getCategory();
                category = null == category ? "미분류" : category;
                for (Map.Entry<String, String> data : datas.entrySet()) {
                    if (data.getKey().equals(category)) {
                        data.setValue(String.valueOf(Integer.parseInt(data.getValue())+(Integer.parseInt(ledger.getAmount()))));
                        check = true;
                        break;
                    }
                }

                if (!check) {
                    datas.put(category, ledger.getAmount());
                }
            }
        }

        setupPieChart();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}