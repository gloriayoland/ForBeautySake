package com.example.forbeautysake.feed_fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.forbeautysake.Adapter.RVAdapterMyRev;
import com.example.forbeautysake.R;
import com.example.forbeautysake.utils.DBHelper;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyReviewFragment extends Fragment {

    //define variables
    RecyclerView RV_myreview;
    RVAdapterMyRev adapter;
    View myFrag;

    DBHelper dbHelper;
    ArrayList<String> product_name;
    ArrayList<String> product_price;
    ArrayList<String> review_detail;
    ArrayList<String> username;
    ArrayList<String> review_date;
    ArrayList<String> product_category;

    SharedPreferences sp;

    // define the name of shared preferences and key
    String SP_NAME = "mypref";
    String KEY_UNAME = "username";
    String name;

    public MyReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFrag = inflater.inflate(R.layout.fragment_my_review, container, false);

        //get shared preferences
        sp = getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //check availability of sp
        name = sp.getString(KEY_UNAME, null);

        // find components by id according to the defined variable
        RV_myreview = myFrag.findViewById(R.id.rv_review_my);

        //initiate DBHelper class
        dbHelper = new DBHelper(getContext());
        product_name = new ArrayList<>();
        product_price = new ArrayList<>();
        review_detail = new ArrayList<>();
        username = new ArrayList<>();
        review_date = new ArrayList<>();
        product_category = new ArrayList<>();

        storeDataInArray(name);

        //set adapter for my review fragment
        adapter = new RVAdapterMyRev(getContext(),product_name, product_category, product_price,
                review_detail , review_date, username);
        RV_myreview.setAdapter(adapter);
        RV_myreview.setLayoutManager(new LinearLayoutManager(getContext()));

        return myFrag;
    }


    public void storeDataInArray(String user_name){
        //get data from database
        Cursor cr = dbHelper.fetchMyReview(user_name);
        if(cr.getCount() == 0){

            //make toast for tell user there is no data
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }else{
            while(cr.moveToNext()){
                //add data to array list
                product_name.add(cr.getString(1));
                product_category.add(cr.getString(2));
                product_price.add(cr.getString(3));
                review_detail.add(cr.getString(4));
                username.add(cr.getString(5));
                review_date.add(cr.getString(6));
            }
        }
    }

}