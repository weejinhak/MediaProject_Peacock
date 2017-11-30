package com.peac.cock.peacock_project.projectDto;

/**
 * Created by dahye on 2017-12-01.
 */

public class CategoryBudgetChart {

    String categoryName;
    String extraAmount;
    int percentage;
    String budgetNOutgoing;

    public CategoryBudgetChart() {   }

    public CategoryBudgetChart(String categoryName, String extraAmount, int percentage, String budgetNOutgoing) {
        this.categoryName = categoryName;
        this.extraAmount = extraAmount;
        this.percentage = percentage;
        this.budgetNOutgoing = budgetNOutgoing;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(String extraAmount) {
        this.extraAmount = extraAmount;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getBudgetNOutgoing() {
        return budgetNOutgoing;
    }

    public void setBudgetNOutgoing(String budgetNOutgoing) {
        this.budgetNOutgoing = budgetNOutgoing;
    }

    @Override
    public String toString() {
        return "CategoryBudgetChart{" +
                "categoryName='" + categoryName + '\'' +
                ", extraAmount='" + extraAmount + '\'' +
                ", percentage=" + percentage +
                ", budgetNOutgoing='" + budgetNOutgoing + '\'' +
                '}';
    }
}
