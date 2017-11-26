package com.peac.cock.peacock_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;

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
import com.peac.cock.peacock_project.projectDto.CategoryBudget;
import com.peac.cock.peacock_project.projectDto.LedgerDto;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

public class AnalysisActivity extends AppCompatActivity implements ValueEventListener{

    private PieChart pieChart;

    private TextView categoryBudgetRegisterText;
    private Button categoryBudgetRegisterButton;

    private int[] entireBudget = new int[2];
    private HashMap<String, String> categoryBudgetSet = new HashMap<>();
    private HashMap<String, String> categoryOutgoing = new HashMap<>();

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis);

        intent = getIntent();

        pieChart = findViewById(R.id.pie_chart);

        categoryBudgetRegisterText = findViewById(R.id.category_budget_register_text);
        categoryBudgetRegisterButton = findViewById(R.id.category_budget_register_button);

        final ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        final ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        final ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        final ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        detailGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getApplicationContext(), DetailTabActivity.class);
                startActivity(intent);
            }
        });

        assetGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(getApplicationContext(), AssetActivity.class);
                startActivity(intent);
            }
        });
        settingGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        analysisGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getApplicationContext(), AnalysisActivity.class);
                startActivity(intent);
            }
        });

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

        setupPieChart();
    }

    @NonNull
    private String getUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser.getUid();
    }

    protected void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
    }

    protected void setPieChartData() {
        List<PieEntry> pieEntries = new ArrayList<>();

        if(categoryOutgoing.size() != 0) {
            for (Map.Entry<String, String> c : categoryOutgoing.entrySet()) {
                pieEntries.add(new PieEntry(Integer.parseInt(c.getValue()), c.getKey()));
            }
        } else {
            System.out.println("데이터 없음");
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "지출 패턴");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    protected void setBarChartData() {
        LinearLayout layout = findViewById(R.id.linear_layout);

        int percentage = (int)(entireBudget[1]/entireBudget[0]*100);
        if (percentage > 100) {
            percentage = 100;
        }

        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgress(percentage);
        layout.addView(progressBar);
        System.out.println(percentage);

        if(categoryBudgetSet.size()==0) {
            categoryBudgetRegisterText.setVisibility(View.VISIBLE);
            categoryBudgetRegisterButton.setVisibility(View.VISIBLE);
            System.out.println("데이터 없음");
        } else {
            categoryBudgetRegisterText.setVisibility(View.GONE);
            categoryBudgetRegisterButton.setVisibility(View.GONE);
            System.out.println("데이터 있음");
            for(Map.Entry<String, String> categoryBudget : categoryBudgetSet.entrySet()) {
                int percent = Integer.parseInt(categoryOutgoing.get(categoryBudget.getKey()))/Integer.parseInt(categoryBudget.getValue())*100;
                if (percent > 100) {
                    percent = 100;
                }
                ProgressBar p = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
                p.setProgress(percent);
                layout.addView(p);
            }
        }
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

        /*entireBudget[0] = Double.parseDouble(dataSnapshot.child("users").child(getUid()).child("budget").getValue(String.class));
        System.out.println(entireBudget[0]);*/
        entireBudget[0] = 650000;
        entireBudget[1] = 0;

        for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("categoryBudget").getChildren()) {
            CategoryBudget categoryBudget = fileSnapshot.getValue(CategoryBudget.class);
            categoryBudgetSet.put(categoryBudget.getCategoryName(), categoryBudget.getBudget());
        }

        for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("ledger").getChildren()) {
            LedgerDto ledger = fileSnapshot.getValue(LedgerDto.class);

            boolean check = false;

            if(ledger.getInOut().equals("지출")) {
                String category = ledger.getCategory().getCateImageString();
                category = null == category ? "미분류" : category;
                for (Map.Entry<String, String> c : categoryOutgoing.entrySet()) {
                    if (c.getKey().equals(category)) {
                        c.setValue(String.valueOf(Integer.parseInt(c.getValue())+(Integer.parseInt(ledger.getAmount()))));
                        check = true;
                        break;
                    }
                }

                if (!check) {
                    categoryOutgoing.put(category, ledger.getAmount());
                }
                if(ledger.getDate().substring(0,2).equals("11")) {
                    entireBudget[1] += Double.parseDouble(ledger.getAmount());
                }
            }
        }
        System.out.println(entireBudget[1]);

        setPieChartData();
        setBarChartData();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}