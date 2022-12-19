package com.example.forbeautysake.nav_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.forbeautysake.Adapter.PromotionRVAdapter;
import com.example.forbeautysake.Adapter.RVAdapter;
import com.example.forbeautysake.Adapter.SectionPagerAdapter;
import com.example.forbeautysake.R;
import com.example.forbeautysake.ReviewByCategory;
import com.example.forbeautysake.feed_fragment.MyReviewFragment;
import com.example.forbeautysake.feed_fragment.PublicReviewFragment;
import com.example.forbeautysake.model.promotionModel;
import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.utils.DBHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    // define variables
    Button Makeup, Skincare, bodyCare, beautyTools;
    RecyclerView rvPromotion;

    PromotionRVAdapter promotionAdapter;
    ArrayList<promotionModel> promotionList;



    View myFragment;

    SharedPreferences sp;

    // define the name of shared preferences and key
    String SP_NAME = "mypref";
    String KEY_CATEGORY = "category";

    DatabaseReference db;


    public HomeFragment (){

    }

    public static HomeFragment getInstance(){
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_home, container, false);

        // find components by id according to the defined variable
        Makeup = myFragment.findViewById(R.id.Makeup);
        Skincare = myFragment.findViewById(R.id.Skincare);
        bodyCare = myFragment.findViewById(R.id.bodyCare);
        beautyTools = myFragment.findViewById(R.id.beautyTools);
        rvPromotion = myFragment.findViewById(R.id.rv_promotion);

        //get shared preferences
        sp = getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //set on click listener make up category button
        Makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                String Category = "Make Up";
                editor.putString(KEY_CATEGORY, Category );
                editor.commit();
                Intent i = new Intent(getContext(), ReviewByCategory.class);
                startActivity(i);

            }
        });

        //set on click listener skincare category button
        Skincare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                String Category = "Skin Care";
                editor.putString(KEY_CATEGORY, Category );
                editor.commit();
                Intent i = new Intent(getContext(), ReviewByCategory.class);
                startActivity(i);

            }
        });

        //set on click listener body care category button
        bodyCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                String Category = "Body Care";
                editor.putString(KEY_CATEGORY, Category );
                editor.commit();
                Intent i = new Intent(getContext(), ReviewByCategory.class);
                startActivity(i);

            }
        });

        //set on click listener beauty tools category button
        beautyTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                String Category = "Beauty Tools";
                editor.putString(KEY_CATEGORY, Category );
                editor.commit();
                Intent i = new Intent(getContext(), ReviewByCategory.class);
                startActivity(i);

            }
        });

        promotionList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("table_promotion");


        setupPromotionRv();
        displayData();

        return myFragment;
    }

    private void setupPromotionRv() {
        promotionAdapter = new PromotionRVAdapter(getContext(), promotionList);
        rvPromotion.setAdapter(promotionAdapter);
        rvPromotion.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void displayData(){

        db.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull @NotNull DataSnapshot snapshot){
                                         if (snapshot.hasChildren()){
                                             //noStoriesLabel.setVisibility(View.GONE);
                                             rvPromotion.setVisibility(View.VISIBLE);
                                             promotionList.clear();
                                             for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                                 promotionModel helper = dataSnapshot.getValue(promotionModel.class);
                                                 helper.setKey(dataSnapshot.getKey());
                                                 promotionList.add(helper);

                                             }
                                         }else {
                                             //noStoriesLabel.setVisibility(View.VISIBLE);
                                             rvPromotion.setVisibility(View.GONE);
                                         }
                                         Collections.reverse(promotionList);
                                         promotionAdapter.notifyDataSetChanged();
                                     }

                                     @Override
                                     public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                     }
                                 }
        );
    }



}
