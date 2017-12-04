package com.peac.cock.peacock_project.projectDto;

public class Card extends Asset {

    private String bank;
    private String nickname;
    private int balance;

    public Card() {
        this.bank = "";
        this.nickname = "";
        this.balance = 0;
    }

    public Card(String bank, String nickname, int balance) {
        this.bank = bank;
        this.nickname = nickname;
        this.balance = balance;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return bank + ":" + nickname;
    }
}
