package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 13..
 */

public class Cash extends Asset{

    private String nickname;
    private String balance;

    public Cash() {}

    public Cash(String nickname, String balance) {
        this.nickname = nickname;
        this.balance = balance;
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
        return "현금:" + nickname;
    }
}
