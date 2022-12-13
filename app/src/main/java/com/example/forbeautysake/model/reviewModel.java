package com.example.forbeautysake.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class reviewModel implements Parcelable {

    //define variables
    @Exclude
    String key;
    String row_namaProduk;
    String row_category;
    String row_hargaProduk;
    String row_isiReview;
    String row_tanggal;
    String row_username;
    String row_userid;

    public reviewModel() {
    }

    //get constructor for review model class
    public reviewModel(String row_namaProduk, String row_category, String row_hargaProduk,
                       String row_isiReview, String row_tanggal, String row_username,String row_userid) {

        this.row_namaProduk = row_namaProduk;
        this.row_category = row_category;
        this.row_hargaProduk = row_hargaProduk;
        this.row_isiReview = row_isiReview;
        this.row_tanggal = row_tanggal;
        this.row_username = row_username;
        this.row_userid = row_userid;
    }

    protected reviewModel(Parcel in) {
        row_namaProduk = in.readString();
        row_category = in.readString();
        row_hargaProduk = in.readString();
        row_isiReview = in.readString();
        row_tanggal = in.readString();
    }

    //setter and getter for review model class
    public static final Creator<reviewModel> CREATOR = new Creator<reviewModel>() {
        @Override
        public reviewModel createFromParcel(Parcel in) {
            return new reviewModel(in);
        }

        @Override
        public reviewModel[] newArray(int size) {
            return new reviewModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRow_namaProduk() {
        return row_namaProduk;
    }

    public void setRow_namaProduk(String row_namaProduk) {
        this.row_namaProduk = row_namaProduk;
    }

    public String getRow_category() {
        return row_category;
    }

    public void setRow_category(String row_category) {
        this.row_category = row_category;
    }

    public String getRow_hargaProduk() {
        return row_hargaProduk;
    }

    public void setRow_hargaProduk(String row_hargaProduk) {
        this.row_hargaProduk = row_hargaProduk;
    }

    public String getRow_isiReview() {
        return row_isiReview;
    }

    public void setRow_isiReview(String row_isiReview) {
        this.row_isiReview = row_isiReview;
    }

    public String getRow_tanggal() {
        return row_tanggal;
    }

    public void setRow_tanggal(String row_tanggal) {
        this.row_tanggal = row_tanggal;
    }

    public String getRow_username() {
        return row_username;
    }

    public void setRow_username(String row_username) {
        this.row_username = row_username;
    }

    public String getRow_userid() {
        return row_userid;
    }

    public void setRow_userid(String row_userid) {
        this.row_userid = row_userid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(row_namaProduk);
        parcel.writeString(row_category);
        parcel.writeString(row_hargaProduk);
        parcel.writeString(row_isiReview);
        parcel.writeString(row_tanggal);
    }
}