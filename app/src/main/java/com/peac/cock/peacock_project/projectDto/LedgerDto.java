package com.peac.cock.peacock_project.projectDto;

public class LedgerDto {

    private String inOut;
    private String date;
    private String time;
    private String content;
    private Category category;
    private String amount;
    private Asset asset;
    private String memo;

    public String getInOut() {
        return inOut;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "LedgerDto{" +
                ", inOut='" + inOut + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                ", asset=" + asset +
                ", memo='" + memo + '\'' +
                '}';
    }
}
