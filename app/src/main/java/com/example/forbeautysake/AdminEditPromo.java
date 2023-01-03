package com.example.forbeautysake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.forbeautysake.nav_fragment.FeedFragment;
import com.example.forbeautysake.utils.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminEditPromo extends AppCompatActivity{
    // define variables
    EditText promotionName;
    EditText promotionLink;

    Button cancelPromo;
    Button updatePromo;

    SharedPreferences sp;

    // define the name of shared preferences and key
    String SP_NAME = "mypref";
    String KEY_PROMOTIONID = "promotionid";

    //define variables
    private String promotionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_promo);

        // find components by id according to the defined variable
        promotionName = findViewById(R.id.promotionName);
        promotionLink = findViewById(R.id.promotionLink);

        cancelPromo = findViewById(R.id.btn_cancelPromo);
        updatePromo = findViewById(R.id.btn_updatePromo);

        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //check availability of sp
        final String promotionid = sp.getString(KEY_PROMOTIONID, null);

        // get promotionKey from intent
        Bundle extras = getIntent().getExtras();
        promotionKey = extras.getString("promotionKey");

        showPromotionData(promotionKey);

        //set on click listener for cancel button
        cancelPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set on click listener for update button
        updatePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String promotion_name = promotionName.getText().toString();
                String promotion_link = promotionLink.getText().toString();

                if (!promotion_name.isEmpty() & !promotion_link.isEmpty()){
                    updatePromo();
                }else if (promotion_name.isEmpty() & promotion_link.isEmpty()){
                    Toast.makeText(AdminEditPromo.this, "You cannot update an empty promotion!", Toast.LENGTH_SHORT).show();
                }else if (promotion_name.isEmpty()){
                    promotionName.setError("This cannot be empty!");
                }
                else if (promotion_link.isEmpty()){
                    promotionLink.setError("This cannot be empty!");
                }

            }
        });
    }

    public void updatePromo() {

        //update values of the review to database
        updatePromotionData(promotionKey);
        // make toast for display a text that review succesfully updated
        Toast.makeText(AdminEditPromo.this, "Promotion Updated", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void showPromotionData(String promotionid){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("table_promotion");
        Query promoData = ref.orderByChild(promotionid);

        promoData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String promotionNameFromDB = snapshot.child(promotionid).child("row_promotionName").getValue(String.class);
                String promotionLinkFromDB = snapshot.child(promotionid).child("row_promotionLink").getValue(String.class);

                promotionName.setText(promotionNameFromDB);
                promotionLink.setText(promotionLinkFromDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updatePromotionData (String promotionid){

        String promotionNameChanged = promotionName.getText().toString();
        String promotionLinkChanged = promotionLink.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("table_promotion");
        ref.child(promotionid).child("row_promotionName").setValue(promotionNameChanged);
        ref.child(promotionid).child("row_promotionLink").setValue(promotionLinkChanged);

    }
}
