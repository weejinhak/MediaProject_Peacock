package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 8..
 */

public class Card extends Asset{

    private String bank;
    private String nickname;
    private String balance;

    public Card() {
        this.bank = "";
        this.nickname = "";
        this.balance = "";
    }

    public Card(String bank, String nickname, String balance) {
        this.bank = bank;
        this.nickname = nickname;
        this.balance = balance;
    }

    public Card(String bank, String nickname) {
        this.bank = bank;
        this.nickname = nickname;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBalance() { return balance;  }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return bank + ":" + nickname;
    }
}
