package com.example.forbeautysake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.forbeautysake.model.reviewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.forbeautysake.model.promotionModel;

public class AdminAddPromo extends AppCompatActivity {

    DatabaseReference db;

    EditText promotionName;
    EditText promotionLink;
    Button postReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_promo);

        //initiate DBHelper class
        db = FirebaseDatabase.getInstance().getReference("table_promotion");

        // find components by id according to the defined variable
        promotionName = findViewById(R.id.promotionName);
        promotionLink = findViewById(R.id.promotionLink);
        postReview = findViewById(R.id.btn_postRev);

        postReview.setOnClickListener(v -> {
            promotionModel promotion = new promotionModel(promotionName.getText().toString(), promotionLink.getText().toString());
            db.push().setValue(promotion);
            finish();
        });
    }
}