package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 24..
 */

public class AssetItem {

    private String cardName; //카드이름
    private int balance;     //카드잔액

    public AssetItem(String cardName, int balance) {
        this.cardName = cardName;
        this.balance = balance;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
