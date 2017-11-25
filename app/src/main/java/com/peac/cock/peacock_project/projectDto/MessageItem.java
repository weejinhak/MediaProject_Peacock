package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 25..
 */

public class MessageItem {

    private int categoryId;
    private String messageContent;
    private int messageBalance;
    private String messageDate;
    private String messageKey;

    public MessageItem(int categoryId, String messageContent, String messageDate, int messageBalance, String messageKey) {
        this.categoryId = categoryId;
        this.messageContent = messageContent;
        this.messageDate = messageDate;
        this.messageBalance = messageBalance;
        this.messageKey=messageKey;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageBalance() {
        return messageBalance;
    }

    public void setMessageBalance(int messageBalance) {
        this.messageBalance = messageBalance;
    }

    public String getMessageDate() {return messageDate;}

    public void setMessageDate(String messageDate) {this.messageDate = messageDate;}

    public String getMessageKey() {return messageKey;}

    public void setMessageKey(String messageKey) {this.messageKey = messageKey;}
}
