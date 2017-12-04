package com.peac.cock.peacock_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

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
import com.peac.cock.peacock_project.projectAdapter.BudgetAdapter;
import com.peac.cock.peacock_project.projectAdapter.ListTab1Adapter;
import com.peac.cock.peacock_project.projectDto.CategoryBudget;
import com.peac.cock.peacock_project.projectDto.CategoryBudgetChart;
import com.peac.cock.peacock_project.projectDto.LedgerDto;
import com.peac.cock.peacock_project.projectDto.MessageItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisActivity extends AppCompatActivity implements ValueEventListener{

    private PieChart pieChart;

    private TextView categoryBudgetRegisterText;

    private ListView ledgerListViewPerCategory;
    private ListTab1Adapter analysisListAdapter;
    private ListView budgetListViewPerCategory;
    private BudgetAdapter budgetListAdapter;
    private Button categoryBudgetRegisterButton;
    private FloatingActionButton categoryBudgetRegisterFloatingButton;

    private TextView budgetMonthExtraAmount;
    private TextView budgetMonthInfoAmount1;
    private TextView budgetMonthInfoAmount2;
    private ProgressBar budgetProgressbar;
    private TextView budgetPercentage;
    private TextView budgetResultText;

    private DecimalFormat df = new DecimalFormat("#,###");
    private Calendar today = Calendar.getInstance();

    private Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("MM");
    private int selectedDate = Integer.parseInt(sdf.format(date));

    private int[] entireBudget = new int[2];

    private List<CategoryBudgetChart> categoryBudgetSet;
    private HashMap<String, String> categoryOutgoing;
    private HashMap<String, ArrayList<MessageItem>> ledgerLists;

    private String currentTab = "analysis";

    private BackPressCloseHandler backPressCloseHandler;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis);

        backPressCloseHandler = new BackPressCloseHandler(this);

        intent = getIntent();

        if(intent.getStringExtra("budgetRegister") != null) {
            currentTab = "budget";
        }

        pieChart = findViewById(R.id.pie_chart);

        categoryBudgetRegisterText = findViewById(R.id.category_budget_register_text);
        categoryBudgetRegisterButton = findViewById(R.id.category_budget_register_button);
        categoryBudgetRegisterFloatingButton = findViewById(R.id.category_budget_register_floating_button);

        ledgerLists = new HashMap<>();

        ledgerListViewPerCategory = findViewById(R.id.analysis_layout_detail_list);
        budgetListViewPerCategory = findViewById(R.id.budget_layout_budget_list);

        categoryBudgetSet = new ArrayList<>();
        categoryOutgoing = new HashMap<>();

        budgetListAdapter = new BudgetAdapter(this, R.layout.activity_analysis_category_budget_item, categoryBudgetSet);

        final ImageButton assetGoButton = findViewById(R.id.main_layout_asset_go_button);
        final ImageButton analysisGoButton = findViewById(R.id.main_layout_analysis_go_button);
        final ImageButton detailGoButton = findViewById(R.id.main_layout_breakdown_go_button);
        final ImageButton settingGoButton = findViewById(R.id.main_layout_setting_go_button);

        budgetMonthExtraAmount = findViewById(R.id.budget_layout_month_extra_amount);
        budgetMonthInfoAmount1 = findViewById(R.id.budget_layout_month_info_amount1);
        budgetMonthInfoAmount2 = findViewById(R.id.budget_layout_month_info_amount2);
        budgetProgressbar = findViewById(R.id.budget_layout_progressbar);
        budgetPercentage = findViewById(R.id.budget_layout_percentage);
        budgetResultText = findViewById(R.id.budget_layout_budget_result_text);

        categoryBudgetRegisterFloatingButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), CategoryBudgetRegisterActivity.class);
            startActivity(intent);
        });

        categoryBudgetRegisterButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), CategoryBudgetRegisterActivity.class);
            startActivity(intent);
        });

        detailGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), DetailTabActivity.class);
            startActivity(intent);
        });

        assetGoButton.setOnClickListener(view -> {
            intent.setClass(getApplicationContext(), AssetActivity.class);
            startActivity(intent);
        });
        settingGoButton.setOnClickListener(v -> {
            intent.setClass(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
        });
        analysisGoButton.setOnClickListener(v -> {
            intent.setClass(getApplicationContext(), AnalysisActivity.class);
            startActivity(intent);
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

        if(currentTab.equals("analysis")) {
            host.setCurrentTab(0);
        } else {
            host.setCurrentTab(1);
        }

        setupPieChart();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;

                if(analysisListAdapter == null) {
                    analysisListAdapter = new ListTab1Adapter(AnalysisActivity.this.getApplicationContext(), R.layout.activity_analysis_list_item, ledgerLists.get(categoryOutgoing.keySet().toArray()[(int)h.getX()].toString()));
                } else {
                    analysisListAdapter.setMessageItems(ledgerLists.get(categoryOutgoing.keySet().toArray()[(int)h.getX()].toString()));
                }
                ledgerListViewPerCategory.setAdapter(analysisListAdapter);

            }

            @Override
            public void onNothingSelected() {  }
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
    }

    protected void setPieChartData() {
        List<PieEntry> pieEntries = new ArrayList<>();

        if(categoryOutgoing.size() != 0) {
            for (Map.Entry<String, String> c : categoryOutgoing.entrySet()) {
                pieEntries.add(new PieEntry(Integer.parseInt(c.getValue()), c.getKey()));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "지출 패턴");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setCenterText("총 지출\n"+String.valueOf(entireBudget[1]+"원"));
        pieChart.setCenterTextSize(18);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    protected void setBarChartData() {

        int percentage = entireBudget[1]*100/entireBudget[0];
        budgetProgressbar.setProgress(percentage);
        String percentageString = String.valueOf(percentage) + "%";
        budgetPercentage.setText(percentageString);


        String text;
        int delta = entireBudget[0] - entireBudget[1];
        if(delta >= 0) {
            text = df.format(delta) + "원 남음";
        } else {
            text = df.format(-delta) + "원 초과";
        }
        budgetMonthExtraAmount.setText(text);


        int maxDay = today.getActualMaximum(Calendar.DATE);
        int remainDay = maxDay - today.get(Calendar.DATE) + 1;
        String text2 = "예산을 맞추려면 하루에" + df.format(delta/remainDay) + "원씩 써야해요.";
        budgetMonthInfoAmount1.setText(text2);

        int expectedOutgoing = entireBudget[1]*30/today.get(Calendar.DATE);
        String text3 = "이렇게 쓴다면 이번달 지출은 " + df.format(expectedOutgoing) + "원으로 예상되네요.";
        budgetMonthInfoAmount2.setText(text3);

        String text4 = "예산 : " + df.format(entireBudget[0]) + "원\n지출 : " + df.format(entireBudget[1]) + "원";
        budgetResultText.setText(text4);

        if(categoryBudgetSet.size()==0) {
            categoryBudgetRegisterText.setVisibility(View.VISIBLE);
            categoryBudgetRegisterButton.setVisibility(View.VISIBLE);
            categoryBudgetRegisterFloatingButton.setVisibility(View.GONE);
        } else {
            categoryBudgetRegisterText.setVisibility(View.GONE);
            categoryBudgetRegisterButton.setVisibility(View.GONE);
            categoryBudgetRegisterFloatingButton.setVisibility(View.VISIBLE);
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
                    entireBudget[1] += Double.parseDouble(ledger.getAmount());

                    String msgContent = ledger.getContent();
                    String[] msgContentToken = msgContent.split("\\s");

                    String categoryName = ledger.getCategory().getCateImageString();

                    String msgKey = fileSnapshot.getKey();

                    MessageItem messageItem = new MessageItem(ledger.getCategory().getCateImageId(), msgContentToken[0], ledger.getDate(),
                            Integer.parseInt(ledger.getAmount()), msgKey);

                    if (ledgerLists.get(categoryName) == null) {
                        ledgerLists.put(categoryName, new ArrayList<>());
                    }
                    ledgerLists.get(categoryName).add(messageItem);

                }
            }
        }

        for(DataSnapshot fileSnapshot : dataSnapshot.child("users").child(getUid()).child("categoryBudget").getChildren()) {
            CategoryBudget categoryBudget = fileSnapshot.getValue(CategoryBudget.class);
            String text;
            CategoryBudgetChart categoryBudgetChart = new CategoryBudgetChart();

            int outgoing = Integer.parseInt(categoryOutgoing.get(categoryBudget.getCategory().getCateImageString()));
            int budget = Integer.parseInt(categoryBudget.getBudget());
            int percentage = outgoing*100/budget;

            categoryBudgetChart.setCategoryName(categoryBudget.getCategory().getCateImageString());
            categoryBudgetChart.setPercentage(percentage);

            int delta = budget - outgoing;
            if(delta >= 0) {
                text = df.format(delta) + "원 남음";
            } else {
                text = df.format(-delta) + "원 초과";
            }
            categoryBudgetChart.setExtraAmount(text);


            text = "예산 : " + df.format(budget) + "원\n지출 : " + df.format(outgoing) + "원";
            categoryBudgetChart.setBudgetNOutgoing(text);

            categoryBudgetSet.add(categoryBudgetChart);
        }

        budgetListAdapter.setCategoryBudgetCharts(categoryBudgetSet);
        budgetListViewPerCategory.setAdapter(budgetListAdapter);

        setPieChartData();
        setBarChartData();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {   }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}