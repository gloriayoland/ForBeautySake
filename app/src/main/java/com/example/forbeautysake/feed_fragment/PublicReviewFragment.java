package com.example.forbeautysake.feed_fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.forbeautysake.Adapter.RVAdapter;
import com.example.forbeautysake.R;
import com.example.forbeautysake.utils.DBHelper;

import java.util.ArrayList;

public class PublicReviewFragment extends Fragment {

    //define variables
    RecyclerView RV_review;
    RVAdapter adapter;
    View myFrag;

    DBHelper dbHelper;
    ArrayList<String> product_name;
    ArrayList<String> product_price;
    ArrayList<String> review_detail;
    ArrayList<String> username;
    ArrayList<String> review_date;
    ArrayList<String> product_category;

    public PublicReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFrag = inflater.inflate(R.layout.fragment_public_review, container, false);

        // find components by id according to the defined variable
        RV_review = myFrag.findViewById(R.id.rv_review);

        //initiate DBHelper class
        dbHelper = new DBHelper(getContext());

        //make array list for each content
        product_name = new ArrayList<>();
        product_price = new ArrayList<>();
        review_detail = new ArrayList<>();
        username = new ArrayList<>();
        review_date = new ArrayList<>();
        product_category = new ArrayList<>();

        storeDataInArray();

        //set adapter
        adapter = new RVAdapter(getContext(),product_name, product_category, product_price,
                review_detail , review_date, username);
        RV_review.setAdapter(adapter);
        RV_review.setLayoutManager(new LinearLayoutManager(getContext()));

        return myFrag;
    }

    public void storeDataInArray(){
        //get all review from database
        Cursor cr = dbHelper.fetchAllReview();
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