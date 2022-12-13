package com.example.forbeautysake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.utils.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewReview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // define variables
    EditText productName;
    EditText productPrice;
    EditText reviewDet;

    Button cancelReview;
    Button postReview;

    Spinner category;

    DatabaseReference db;
    private FirebaseAuth mAuth;

    //define variables
    String name, categorySelected;
    String username = "userdefault";
    Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);

        //initiate DBHelper class
        db = FirebaseDatabase.getInstance().getReference("table_review");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //get current date
        currentDate = new Date();
        SimpleDateFormat formDate = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formDate.format(currentDate);

        // find components by id according to the defined variable
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        reviewDet = findViewById(R.id.reviewDet);

        cancelReview = findViewById(R.id.btn_cancelRev);
        postReview = findViewById(R.id.btn_postRev);

        category = findViewById(R.id.productCategory);

        //set adapter for category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.productCategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(this);

        //set on click listener for cancel button
        cancelReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set on click listener for post button
        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storyContent = reviewDet.getText().toString();
                String productname = productName.getText().toString();
                String productprice = productPrice.getText().toString();

                if(!storyContent.isEmpty()){
                    storeReviewtoDB(productname, categorySelected, productprice,
                            storyContent, formattedDate, username, currentUser);

                    Toast.makeText(NewReview.this, "Review Posted", Toast.LENGTH_SHORT).show();
                    //after review has posted, it will go back to the page before
                    finish();
                }else{
                    Toast.makeText(NewReview.this, "You cannot post an empty review!", Toast.LENGTH_SHORT).show();
                    reviewDet.setError("This cannot be empty!");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySelected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void storeReviewtoDB(String row_namaProduk, String row_category, String row_hargaProduk,
                        String row_isiReview, String row_tanggal, String row_username, FirebaseUser user){
        String userid = user.getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("table_review");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("table_users").child(userid);
        ref.child("row_username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameFromDB = snapshot.getValue().toString();
                reviewModel helper = new reviewModel (row_namaProduk, row_category, row_hargaProduk,
                        row_isiReview, row_tanggal, usernameFromDB, userid);
                reference.push().setValue(helper);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void getUsernamefromDB(FirebaseUser user) {
        String userid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userid);

        ref.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String usernameFromDB = snapshot.getValue().toString();
                Log.i("info aje", usernameFromDB);
                username = usernameFromDB;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
