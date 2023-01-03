package com.example.forbeautysake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    Button postAddPromo;
    Button cancelAddPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_promo);

        //initiate DBHelper class
        db = FirebaseDatabase.getInstance().getReference("table_promotion");

        // find components by id according to the defined variable
        promotionName = findViewById(R.id.promotionName);
        promotionLink = findViewById(R.id.promotionLink);
        postAddPromo = findViewById(R.id.btn_postAddPromo);
        cancelAddPromo = findViewById(R.id.btn_cancelAddPromo);

        postAddPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promotionModel promotion = new promotionModel();
                String promotionname = promotionName.getText().toString();
                String promotionlink = promotionLink.getText().toString();

                if (!promotionname.isEmpty() & !promotionlink.isEmpty()){
                    db.push().setValue(promotion);
                    finish();
                }else{
                    Toast.makeText(AdminAddPromo.this, "You cannot post an empty promotion!", Toast.LENGTH_SHORT).show();
                    promotionName.setError("This cannot be empty!");
                    promotionLink.setError("This cannot be empty!");
                }
            }
        });

        cancelAddPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}