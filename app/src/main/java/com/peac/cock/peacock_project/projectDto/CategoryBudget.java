package com.peac.cock.peacock_project.projectDto;

public class CategoryBudget {

    private Category category;
    private String budget;

    public CategoryBudget() { }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "CategoryBudget{" +
                "category=" + category +
                ", budget='" + budget + '\'' +
                '}';
    }
}
