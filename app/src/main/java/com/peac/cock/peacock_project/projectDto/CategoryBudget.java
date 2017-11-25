package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 15..
 */

public class CategoryBudget {

    private String categoryName;
    private String budget;

    public CategoryBudget() {

    }

    public CategoryBudget(String categoryName, String budget) {
        this.categoryName = categoryName;
        this.budget = budget;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
