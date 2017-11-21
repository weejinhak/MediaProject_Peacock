package com.peac.cock.peacock_project;

/**
 * Created by dmsru on 2017-11-14.
 */

public class ListViewRowItem {

    private String placeName;
    private int categoryPicId;
    private String purchase;
    private String accountType;

    public ListViewRowItem(String placeName, int categoryPicId, String purchase,
                           String accountType) {

        this.placeName = placeName;
        this.categoryPicId = categoryPicId;
        this.purchase = purchase;
        this.accountType = accountType;
    }

    public String getPlaceName() { return placeName; }

    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public int getCategoryPicId() { return categoryPicId;}

    public void setCategoryPicId(int categoryPicId) { this.categoryPicId = categoryPicId; }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
