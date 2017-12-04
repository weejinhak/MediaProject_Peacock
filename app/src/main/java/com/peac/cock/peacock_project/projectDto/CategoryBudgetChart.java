package com.peac.cock.peacock_project.projectDto;

public class CategoryBudgetChart {

    String categoryName;
    String extraAmount;
    int percentage;
    String budgetNOutgoing;

    public CategoryBudgetChart() {  }

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
