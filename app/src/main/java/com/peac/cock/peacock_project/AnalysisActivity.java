package com.peac.cock.peacock_project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectDto.Asset;
import com.peac.cock.peacock_project.projectDto.Card;
import com.peac.cock.peacock_project.projectDto.Cash;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity {
    private float rainfall[] = {98.8f, 123.8f, 161.6f, 24.2f, 52f, 58.2f, 35.4f, 13.3f, 78.4f, 203.4f, 240.2f, 159.7f};
    private String monthName[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private PieChart pieChart;
    private BarChart barChart;

    private ArrayList<LedgerDto> ledgers;
    private HashMap<String, String> datas;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private String uid;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        final TabHost host = (TabHost) findViewById(R.id.tab_host);
        host.setup();

        barChart = (BarChart) findViewById(R.id.bar_chart);
        setupBarChart(12, 50);

        TabHost.TabSpec page1 = host.newTabSpec("분석");
        page1.setContent(R.id.analysis);
        page1.setIndicator("분석");
        host.addTab(page1);

        TabHost.TabSpec page2 = host.newTabSpec("예산");
        page2.setContent(R.id.budget);
        page2.setIndicator("예산");
        host.addTab(page2);

        //firebase get auth & database;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        pieChart = (PieChart) findViewById(R.id.pie_chart);

        ledgers = new ArrayList<>();
        datas = new HashMap<>();

        callDetailData();

        setupPieChart();
    }

    private void callDetailData() {

        databaseReference = mDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue();
                for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(uid).child("ledger").getChildren()) {
                    LedgerDto ledger = fileSnapshot.getValue(LedgerDto.class);
                    /*ledger.setSeq(fileSnapshot.child("seq").getValue(String.class));
                    ledger.setAmount(fileSnapshot.child("amount").getValue(String.class));
                    ledger.setAsset(fileSnapshot.child("asset").getValue(Asset.class));
                    ledger.setCategory(fileSnapshot.child("category").getValue(String.class));
                    ledger.setContent(fileSnapshot.child("content").getValue(String.class));
                    ledger.setDate(fileSnapshot.child("date").getValue(String.class));
                    ledger.setTime(fileSnapshot.child("time").getValue(String.class));
                    ledger.setInOut(fileSnapshot.child("inOut").getValue(String.class));
                    ledger.setMemo(fileSnapshot.child("memo").getValue(String.class));*/
                    System.out.println(ledger);

                    ledgers.add(ledger);

                    boolean check = false;

                    if(ledger.getInOut().equals("지출") && datas != null) {
                        for (Map.Entry<String, String> data : datas.entrySet()) {
                            if (data.getKey().equals(ledger.getCategory())) {
                                data.setValue(String.valueOf(Integer.parseInt(data.getValue())+(Integer.parseInt(ledger.getAmount()))));
                                check = true;
                                break;
                            }
                        }

                        if (!check) {
                            datas.put(ledger.getCategory(), ledger.getAmount());
                        }
                    }

                    System.out.println(datas);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void setupPieChart() {
        System.out.println("들어옴");
        List<PieEntry> pieEntries = new ArrayList<>();

        if(datas != null) {
            for (Map.Entry<String, String> data : datas.entrySet()) {
                System.out.println("value : " + data.getValue() + "key :" + data.getKey());
                pieEntries.add(new PieEntry(Integer.parseInt(data.getValue()), data.getKey()));
                System.out.println(pieEntries);
            }
        } else {
            System.out.println("데이터 없음");
        }

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

}