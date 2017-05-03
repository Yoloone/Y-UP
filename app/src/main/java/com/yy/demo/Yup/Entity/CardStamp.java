package com.yy.demo.Yup.Entity;

/**
 * Created by Administrator on 2017/4/10.
 */

public class CardStamp {
    private int cardStampImgID;
    private String cardStampName;

    public CardStamp(int cardStampImgID, String cardStampName) {
        this.cardStampImgID = cardStampImgID;
        this.cardStampName = cardStampName;
    }

    public int getCardStampImgID() {
        return cardStampImgID;
    }

    public void setCardStampImgID(int cardStampImgID) {
        this.cardStampImgID = cardStampImgID;
    }

    public String getCardStampName() {
        return cardStampName;
    }

    public void setCardStampName(String cardStampName) {
        this.cardStampName = cardStampName;
    }
}
