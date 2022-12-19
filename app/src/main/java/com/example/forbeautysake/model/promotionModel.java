package com.example.forbeautysake.model;

import com.google.firebase.database.Exclude;

public class promotionModel {

    //define variables
    @Exclude
    private String key;
    private String row_promotionName;
    private String row_promotionLink;

    public promotionModel() {

    }

    public promotionModel(String row_promotionName, String row_promotionLink) {
        this.key = key;
        this.row_promotionName = row_promotionName;
        this.row_promotionLink = row_promotionLink;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getRow_promotionName() {
        return row_promotionName;
    }

    public void setRow_promotionName(String row_promotionName) {
        this.row_promotionName = row_promotionName;
    }

    public String getRow_promotionLink() {
        return row_promotionLink;
    }

    public void setRow_promotionLink(String row_promotionLink) {
        this.row_promotionLink = row_promotionLink;
    }
}
