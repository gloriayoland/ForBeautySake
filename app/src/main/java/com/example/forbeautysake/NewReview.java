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

    public String username;

    //SharedPreferences sp;
    //FirebaseDatabase db;
    private FirebaseAuth mAuth;
    //DBHelper dbHelper;

    // define the name of shared preferences and key

    //define variables
    String name, categorySelected;

    Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_review);

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

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
                String reviewContent = reviewDet.getText().toString();
                String product_Name = productName.getText().toString();
                String product_Price = productPrice.getText().toString();


                if(!reviewContent.isEmpty()){
                    storeReviewtoDB(product_Name, categorySelected, product_Price, reviewContent, formattedDate, currentUser);

                    Toast.makeText(NewReview.this, "Review Posted", Toast.LENGTH_SHORT).show();
                    //after review has posted, it will go back to the page before
                    finish();
                }else{
                    Toast.makeText(NewReview.this, "You cannot post an empty Review!", Toast.LENGTH_SHORT).show();
                    reviewDet.setError("This cannot be empty!");
                }



            }
        });
    }

    void storeReviewtoDB(String productName, String category, String productPrice, String reviewDet, String date, FirebaseUser user){
        String userid = user.getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("stories");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(userid);
        ref.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameFromDB = snapshot.getValue().toString();
                reviewModel helper = new reviewModel (productName, category, productPrice, reviewDet, date, userid, usernameFromDB);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySelected = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
