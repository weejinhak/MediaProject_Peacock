package com.peac.cock.peacock_project.projectDto;

/**
 * Created by dahye on 2017-12-01.
 */

public class CategoryBudgetChart {

    String categoryName;
    String extraAmount;
    int percentage;
    String budget;
    String outgoing;

    public CategoryBudgetChart() {   }

    public CategoryBudgetChart(String categoryName, String extraAmount, int percentage, String budget, String outgoing) {
        this.categoryName = categoryName;
        this.extraAmount = extraAmount;
        this.percentage = percentage;
        this.budget = budget;
        this.outgoing = outgoing;
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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public String toString() {
        return "CategoryBudgetChart{" +
                "categoryName='" + categoryName + '\'' +
                ", extraAmount='" + extraAmount + '\'' +
                ", percentage=" + percentage +
                ", budget='" + budget + '\'' +
                ", outgoing='" + outgoing + '\'' +
                '}';
    }
}
