package com.peac.cock.peacock_project.projectDto;

public class Category {

    private String cateImageString;
    private int  cateImageId;

    public Category() { }

    public Category(String cateImageString, int cateImageId) {
        this.cateImageString = cateImageString;
        this.cateImageId = cateImageId;
    }

    public String getCateImageString() {
        return cateImageString;
    }

    public void setCateImageString(String cateImageString) {
        this.cateImageString = cateImageString;
    }

    public int getCateImageId() {
        return cateImageId;
    }

    public void setCateImageId(int cateImageId) {
        this.cateImageId = cateImageId;
    }

    @Override
    public String toString() {
        return cateImageString;
    }
}
