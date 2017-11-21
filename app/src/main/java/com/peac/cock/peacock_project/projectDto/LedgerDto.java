package com.peac.cock.peacock_project.projectDto;

/**
 * Created by wee on 2017. 11. 8..
 */

public class LedgerDto {

    private String seq;
    //String email;
    private String inOut;
    private String date;
    private String time;
    private String content;
    private String category;
    private String amount;
    private Asset asset;
    private String memo;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "LedgerDto{" +
                "seq='" + seq + '\'' +
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
