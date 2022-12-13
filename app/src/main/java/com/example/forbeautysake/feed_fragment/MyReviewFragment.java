package com.example.forbeautysake.feed_fragment;

import android.content.SharedPreferences;
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

import com.example.forbeautysake.Adapter.RVAdapterMyRev;
import com.example.forbeautysake.R;
import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.utils.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public class MyReviewFragment extends Fragment {

    //define variables
    RecyclerView RV_myreview;
    RVAdapterMyRev adapter;
    View myFrag;

    //DBHelper dbHelper;
    ArrayList<String> product_name;
    ArrayList<String> product_price;
    ArrayList<String> review_detail;
    ArrayList<String> username;
    ArrayList<String> review_date;
    ArrayList<String> product_category;

    ArrayList<reviewModel> listReview;

    private DatabaseReference database;
    private FirebaseAuth mAuth;

    public MyReviewFragment() {
    }

    public static MyReviewFragment getInstance() {
        return new MyReviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFrag = inflater.inflate(R.layout.fragment_my_review, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // find components by id according to the defined variable
        RV_myreview = myFrag.findViewById(R.id.rv_review_my);

        listReview = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("table_review");
        Query storyData = database.orderByChild("row_userid").equalTo(currentUser.getUid());

        product_name = new ArrayList<>();
        product_price = new ArrayList<>();
        review_detail = new ArrayList<>();
        username = new ArrayList<>();
        review_date = new ArrayList<>();
        product_category = new ArrayList<>();

        //storeDataInArray(name);

        //set adapter for my review fragment
        adapter = new RVAdapterMyRev(getContext(), listReview);
        RV_myreview.setAdapter(adapter);
        RV_myreview.setLayoutManager(new LinearLayoutManager(getContext()));

        displayData(storyData);

        return myFrag;
    }

    void displayData(Query storyData) {

        storyData.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                if(snapshot.hasChildren()){
                                                    RV_myreview.setVisibility(View.VISIBLE);
                                                    //noStoriesLabel.setVisibility(View.GONE);
                                                    listReview.clear();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        reviewModel helper = dataSnapshot.getValue(reviewModel.class);
                                                        helper.setKey(dataSnapshot.getKey());
                                                        listReview.add(helper);


                                                    }
                                                }else{
                                                    //noStoriesLabel.setVisibility(View.VISIBLE);
                                                    RV_myreview.setVisibility(View.GONE);
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

//    public void storeDataInArray(String user_name){
//        //get data from database
//        Cursor cr = dbHelper.fetchMyReview(user_name);
//        if(cr.getCount() == 0){
//
//            //make toast for tell user there is no data
//            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
//        }else{
//            while(cr.moveToNext()){
//                //add data to array list
//                product_name.add(cr.getString(1));
//                product_category.add(cr.getString(2));
//                product_price.add(cr.getString(3));
//                review_detail.add(cr.getString(4));
//                username.add(cr.getString(5));
//                review_date.add(cr.getString(6));
//            }
//        }
//    }

}