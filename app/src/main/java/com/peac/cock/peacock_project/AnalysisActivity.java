package com.peac.cock.peacock_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.peac.cock.peacock_project.projectAdapter.BackPressCloseHandler;
import com.peac.cock.peacock_project.projectAdapter.ListTab1Adapter;
import com.peac.cock.peacock_project.projectDto.CategoryBudget;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectDto.MessageItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity implements ValueEventListener{

    private PieChart pieChart;

    private TextView categoryBudgetRegisterText;
    private Button categoryBudgetRegisterButton;

    private ListView ledgerListViewPerCategory;
    private ListTab1Adapter analysisListAdapter;

    private Date date = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM");
    private int selectedDate = Integer.parseInt(sdf.format(date));

    private int[] entireBudget = new int[2];
    private HashMap<String, String> categoryBudgetSet = new HashMap<>();
    private HashMap<String, String> categoryOutgoing = new HashMap<>();

    private HashMap<String, ArrayList<MessageItem>> ledgerLists; // 카테고리, 해당 카테고리 내역 리스트

    private Intent intent;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis);
        backPressCloseHandler = new BackPressCloseHandler(this);

        intent = getIntent();


        pieChart = findViewById(R.id.pie_chart);

        categoryBudgetRegisterText = findViewById(R.id.category_budget_register_text);
        categoryBudgetRegisterButton = findViewById(R.id.category_budget_register_button);

        ledgerLists = new HashMap<>();
        ledgerListViewPerCategory = findViewById(R.id.analysis_layout_detail_list);

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

        // set a chart value selected listener
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(AnalysisActivity.this, categoryOutgoing.keySet().toArray()[(int)h.getX()].toString() , Toast.LENGTH_SHORT).show();
                if(analysisListAdapter == null) {
                    analysisListAdapter = new ListTab1Adapter(AnalysisActivity.this.getApplicationContext(), R.layout.activity_analysis_list_item, ledgerLists.get(categoryOutgoing.keySet().toArray()[(int)h.getX()].toString()));
                } else {
                    analysisListAdapter.setMessageItems(ledgerLists.get(categoryOutgoing.keySet().toArray()[(int)h.getX()].toString()));
                }
                ledgerListViewPerCategory.setAdapter(analysisListAdapter);

            }

            @Override
            public void onNothingSelected() {
                //categoryOutgoing.get(e) + " = " + e.getData() + "%"

            }
        });
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
        pieChart.setCenterText("총 지출\n"+entireBudget[1]);
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
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
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

        entireBudget[0] = Integer.parseInt(dataSnapshot.child("users").child(getUid()).child("budget").getValue(String.class));
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

                if(ledger.getDate().substring(0,2).equals(String.valueOf(selectedDate))) {
                    System.out.println(ledger.getAmount());
                    entireBudget[1] += Double.parseDouble(ledger.getAmount());
                    Log.d("entire", String.valueOf(entireBudget[1]));

                    String msgContent = ledger.getContent();
                    String[] msgContentToken = msgContent.split("\\s");

                    String categoryName = ledger.getCategory().getCateImageString();

                    String msgKey = fileSnapshot.getKey();

                    //메시지 read 해올 때 카테고리 id 초기값으로  2131230977 줘야함.
                    MessageItem messageItem = new MessageItem(ledger.getCategory().getCateImageId(), msgContentToken[0], ledger.getDate(),
                            Integer.parseInt(ledger.getAmount()), msgKey);

                    if (ledgerLists.get(categoryName) == null) {
                        System.out.println(categoryName);
                        ArrayList<MessageItem> msgItems = new ArrayList<>();
                        ledgerLists.put(categoryName, msgItems);
                    }
                    ledgerLists.get(categoryName).add(messageItem);

                }
            }


        }

        setPieChartData();
        setBarChartData();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}