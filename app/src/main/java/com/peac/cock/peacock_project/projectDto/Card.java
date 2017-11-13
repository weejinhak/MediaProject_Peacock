package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 8..
 */

public class Card {

    String bank;
    String nickName;
    String sum;

    public Card() {
        this.bank = "";
        this.nickName = "";
        this.sum = "";
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Card{" +
                "bank='" + bank + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sum='" + sum + '\'' +
                '}';
    }
}
