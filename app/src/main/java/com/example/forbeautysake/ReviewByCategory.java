package com.example.forbeautysake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forbeautysake.Adapter.RVAdapter;
import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.utils.DBHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static java.security.AccessController.getContext;

public class ReviewByCategory extends AppCompatActivity {
    //define variables
    RecyclerView RV_reviewCat;
    RVAdapter adapter;
    TextView selectedCategory;

    //DBHelper dbHelper;
    DatabaseReference db;
    ArrayList <reviewModel> listReview;

    SharedPreferences sp;

    // define the name of shared preferences and key
    String SP_NAME = "mypref";
    String KEY_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_by_category);

        //get shared preferences
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //get category from sp
        String category = sp.getString(KEY_CATEGORY, null);

        // find components by id according to the defined variable
        RV_reviewCat = findViewById(R.id.rv_reviewCat);
        selectedCategory = findViewById(R.id.selectedCategory);

        //set text category
        selectedCategory.setText(category);

        //initiate dbHelper class
        db = FirebaseDatabase.getInstance().getReference("table_review");
        Query storyData = db.orderByChild("row_category").equalTo(category);


        //run method in line 75 to store data in array
        //storeDataInArray(category);

        displayData(storyData);

        //add and set adapter for recycler view
        adapter = new RVAdapter(this, listReview);
        RV_reviewCat.setAdapter(adapter);
        RV_reviewCat.setLayoutManager(new LinearLayoutManager(this));


    }

    void displayData(Query storyData) {

        storyData.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                if(snapshot.hasChildren()){
                                                    RV_reviewCat.setVisibility(View.VISIBLE);
                                                    //noStoriesLabel.setVisibility(View.GONE);
                                                    listReview.clear();
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        reviewModel helper = dataSnapshot.getValue(reviewModel.class);
                                                        helper.setKey(dataSnapshot.getKey());
                                                        listReview.add(helper);


                                                    }
                                                }else{
                                                    //noStoriesLabel.setVisibility(View.VISIBLE);
                                                    RV_reviewCat.setVisibility(View.GONE);
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

//    public void storeDataInArray(String category){
//
//        //get review category from database
//        Cursor cr = db.getReview(category);
//        //if the data based on selected category = 0
//        if(cr.getCount() == 0){
//            //make toast for tell the user there is no data
//            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
//        }else{
//            while(cr.moveToNext()){
//                //get data to display review
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
