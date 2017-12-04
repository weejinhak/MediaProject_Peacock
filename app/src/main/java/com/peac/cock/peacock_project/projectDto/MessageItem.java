package com.peac.cock.peacock_project.projectDto;

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

    public String getMessageContent() {
        return messageContent;
    }

    public int getMessageBalance() {
        return messageBalance;
    }

    public String getMessageDate() {return messageDate;}

    public String getMessageKey() {return messageKey;}

    @Override
    public String toString() {
        return "MessageItem{" +
                "categoryId=" + categoryId +
                ", messageContent='" + messageContent + '\'' +
                ", messageBalance=" + messageBalance +
                ", messageDate='" + messageDate + '\'' +
                ", messageKey='" + messageKey + '\'' +
                '}';
    }
}
