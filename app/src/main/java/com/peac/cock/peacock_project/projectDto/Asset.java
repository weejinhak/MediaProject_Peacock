package com.peac.cock.peacock_project.projectDto;

public class Asset {

    private String nickname;
    private int balance;

    public Asset() {
        this.nickname = "";
        this.balance = 0;
    }

    public Asset(String nickname){
        this.nickname=nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBalance() { return balance;  }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}
