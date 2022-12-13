package com.example.forbeautysake.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public  static final String database_name = "db_login";
    public  static final String table_user = "table_user";
    public static final String table_review = "table_review";

    public  static final String row_idUser = "_idUser";
    public  static final String row_fullname = "Fullname";
    public  static final String row_email = "Email";
    public  static final String row_username = "Username";
    public  static final String row_password = "Password";

    public static final String row_idReview = "_idReview";
    public static final String row_namaProduk = "Nama_Produk";
    public static final String row_category = "Kategory_Produk";
    public static final String row_hargaProduk = "Harga_Produk";
    public static final String row_isiReview = "Isi_Review";
    public static final String row_tanggal = "Tanggal_Review";

    private SQLiteDatabase db;

    public DBHelper (Context context) {
        super(context,  database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        String queryUser = "CREATE TABLE " + table_user
                + "(" + row_idUser + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + row_fullname + " TEXT, "
                + row_email + " TEXT, "
                + row_username + " TEXT, "
                + row_password + " TEXT )";
        db.execSQL(queryUser);

        String queryReview  = "CREATE TABLE " + table_review +
                "(" + row_idReview + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + row_namaProduk + " TEXT, "
                + row_category + " TEXT, "
                + row_hargaProduk + " TEXT, "
                + row_isiReview + " TEXT, "
                + row_username + " TEXT, "
                + row_tanggal + " DATE, " +
                "FOREIGN KEY("+ row_username+") REFERENCES "+ table_user +"("+ row_username+") " +
                "ON DELETE CASCADE)";
        db.execSQL(queryReview);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL("DROP TABLE IF EXISTS " + table_user);
        db.execSQL("DROP TABLE IF EXISTS " + table_review);
        onCreate(db);
    }

    // Insert User
    public void  insertUser (ContentValues values) {
        db.insert(table_user, null, values);
    }

    // Insert Review
    public void  insertReview (ContentValues values) {
        db.insert(table_review, null, values);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + row_idUser + " FROM " +
                table_user + " WHERE " + row_username + "=? and " + row_password + "=?", new String[]
                {username, password});

        int count = cursor.getCount();

        if (count==1)
            return  true;
        else
            return false;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + row_idUser + " FROM " +
                table_user + " WHERE " + row_username + "=?", new String[]
                {username});

        int count = cursor.getCount();

        if (count==1)
            return  true;
        else
            return false;
    }

    public Cursor fetchAllProfileData(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table_user + " WHERE " + row_username +
                " =? ", new String[]
                {username});

        return res;
    }

    public boolean updateProfileData(String Fullname, String Email, String Password, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + table_user + " SET "
                        + row_fullname + " = (?), "
                        + row_email + " = (?), "
                        +  row_password + "=(?) " +
                        "WHERE " + row_idUser + " = (?)",
                new String[]{Fullname, Email, Password, id});


        return true;
    }

    public Cursor fetchAllReview() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table_review, null);

        return res;
    }

    public boolean updateReview(String idReview , String NamaProduk, String ProdukKategori,
                                String HargaProduk, String isiReview ) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + table_review + " SET "
                + row_namaProduk + " = (?), "
                + row_category + " = (?), "
                + row_hargaProduk +" = (?), "
                + row_isiReview +" = (?) " +
                " WHERE " + row_idReview + " = (?)",new String[]{NamaProduk,  ProdukKategori, HargaProduk, isiReview, idReview});

        return true;
    }

    public boolean deleteReview(String idReview) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " +table_review+" WHERE "+row_idReview+"=(?)",new String[]{idReview});

        return true;
    }

    public int getIdRev(String productName, String productCategory, String productPrice, String reviewDet, String username, String tanggal){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT _idReview FROM table_review WHERE Nama_Produk = ? AND Kategory_Produk = ? AND Harga_Produk = ? AND Isi_Review = ?  AND Username = ? AND Tanggal_Review = ?" ;
        Cursor res = db.rawQuery(sql, new String[]{productName, productCategory, productPrice, reviewDet, username, tanggal});
        res.moveToNext();
        int idRev = res.getInt(0);

        return idRev;
    }

    public Cursor fetchMyReview(String username) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table_review + " WHERE " + row_username +
                " = ?", new String[]
                {username});

        return res;
    }

    public Cursor getReview(String Kategory_Produk) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table_review + " WHERE " + row_category +
                " = ? ", new String[]
                {Kategory_Produk});

        return res;
    }

    public Cursor getReviewbyId(String idRev) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + table_review + " WHERE " + row_idReview +
                " = ? ", new String[]
                {idRev});

        return res;
    }

    public int getIdUser(String Username){
        int idUser = -1;
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT " + row_idUser +
                " FROM " + table_user + " WHERE " + row_username +
                "=(?)", new String[]{Username});

        if (res !=null && res.moveToFirst()){
            idUser = res.getInt(0);

        }

        return idUser;

    }

}
