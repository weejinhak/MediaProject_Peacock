package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 8..
 */

public class Asset {

    private String nickname;
    private String balance;

    public Asset() {
        this.nickname = "";
        this.balance = "";
    }

    public Asset(String nickname, String balance) {
        this.nickname = nickname;
        this.balance = balance;
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

    public String getBalance() { return balance;  }

    public void setBalance(String balance) {
        this.balance = balance;
    }

}
