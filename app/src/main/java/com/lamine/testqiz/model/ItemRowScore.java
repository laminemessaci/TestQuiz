package com.lamine.testqiz.model;

public class ItemRowScore {

    private String mPseudo;
    private String mScore;
    private String mDate;

    public ItemRowScore(String pseudo, String score, String date) {
        mPseudo = pseudo;
        mScore = score;
        mDate = date;
    }

    public String getPseudo() {
        return mPseudo;
    }

    public String getScore() {
        return mScore;
    }


    public String getDate() {
        return mDate;
    }

}
