package com.example.forbeautysake.feed_fragment;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.forbeautysake.Adapter.RVAdapter;
import com.example.forbeautysake.R;
import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.utils.DBHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

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

    ArrayList <reviewModel> listReview;
    DatabaseReference db;
    View myFragment;

    public PublicReviewFragment() {
        // Required empty public constructor
    }

    public static PublicReviewFragment getInstance() {
        return new PublicReviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFrag = inflater.inflate(R.layout.fragment_public_review, container, false);

        // find components by id according to the defined variable
        RV_review = myFrag.findViewById(R.id.rv_review);

        //initiate DBHelper class
        //dbHelper = new DBHelper(getContext());

        //make array list for each content
        product_name = new ArrayList<>();
        product_price = new ArrayList<>();
        review_detail = new ArrayList<>();
        username = new ArrayList<>();
        review_date = new ArrayList<>();
        product_category = new ArrayList<>();

        //storeDataInArray();

        listReview = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("table_review");

        //set adapter
        adapter = new RVAdapter(getContext(), listReview);
        RV_review.setAdapter(adapter);
        RV_review.setLayoutManager(new LinearLayoutManager(getContext()));

        displayData();

        return myFrag;
    }

    void displayData(){

        db.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull @NotNull DataSnapshot snapshot){
                                               if (snapshot.hasChildren()){
                                                   //noStoriesLabel.setVisibility(View.GONE);
                                                   RV_review.setVisibility(View.VISIBLE);
                                                   listReview.clear();
                                                   for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                       reviewModel helper = dataSnapshot.getValue(reviewModel.class);
                                                       helper.setKey(dataSnapshot.getKey());
                                                       listReview.add(helper);

                                                   }
                                               }else {
                                                   //noStoriesLabel.setVisibility(View.VISIBLE);
                                                   RV_review.setVisibility(View.GONE);
                                               }
                                               Collections.reverse(listReview);
                                               adapter.notifyDataSetChanged();
                                           }

                                           @Override
                                           public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                           }
                                       }
        );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
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