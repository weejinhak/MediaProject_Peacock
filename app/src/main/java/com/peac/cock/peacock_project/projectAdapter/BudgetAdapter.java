package com.peac.cock.peacock_project.projectAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.peac.cock.peacock_project.R;
import com.peac.cock.peacock_project.projectDto.CategoryBudgetChart;

import java.util.List;

public class BudgetAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<CategoryBudgetChart> categoryBudgetCharts;
    private int layout;


    public BudgetAdapter(Context context, int layout, List<CategoryBudgetChart> categoryBudgetCharts) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categoryBudgetCharts = categoryBudgetCharts;
        this.layout = layout;
    }

    public void setCategoryBudgetCharts(List<CategoryBudgetChart> categoryBudgetCharts) {
        if(categoryBudgetCharts != null) {
            this.categoryBudgetCharts = categoryBudgetCharts;
        }
    }

    @Override
    public int getCount() {
        if(categoryBudgetCharts != null) {
            return categoryBudgetCharts.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return categoryBudgetCharts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        CategoryBudgetChart categoryBudgetChart = categoryBudgetCharts.get(position);

        TextView category = convertView.findViewById(R.id.category_budget_layout_category_text);
        category.setText(categoryBudgetChart.getCategoryName());

        TextView categoryMonthExtraAmount = convertView.findViewById(R.id.category_budget_layout_month_extra_amount);
        categoryMonthExtraAmount.setText(categoryBudgetChart.getExtraAmount());

        ProgressBar categoryProgressbar = convertView.findViewById(R.id.category_budget_layout_progressbar);
        categoryProgressbar.setProgress(categoryBudgetChart.getPercentage());

        TextView categoryPBudgetPercentage = convertView.findViewById(R.id.category_budget_layout_percentage);
        String text = categoryBudgetChart.getPercentage() + "%";
        categoryPBudgetPercentage.setText(text);

        TextView categoryBudgetResultText = convertView.findViewById(R.id.category_budget_layout_budget_result_text);
        categoryBudgetResultText.setText(categoryBudgetChart.getBudgetNOutgoing());

        return convertView;
    }

}
