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

public class EditReview extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // define variables
    EditText productName;
    EditText reviewDet;
    EditText productPrice;

    Button cancelReview;
    Button updateReview;

    Spinner category;

    public String username;
    private FirebaseAuth mAuth;

    SharedPreferences sp;

    // define the name of shared preferences and key
    String SP_NAME = "mypref";
    String KEY_REVIEWID = "reviewid";

    //define variables
    String id_review, categorySelected;
    private String reviewKey;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);

        // find components by id according to the defined variable
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        reviewDet = findViewById(R.id.reviewDet);

        cancelReview = findViewById(R.id.btn_cancelRev);
        updateReview = findViewById(R.id.btn_updateRev);

        category = findViewById(R.id.productCategory);

        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);

        //check availability of sp
        final String reviewid = sp.getString(KEY_REVIEWID, null);

        categorySelected = "Skincare";

        // get reviewKey from intent
        Bundle extras = getIntent().getExtras();
        reviewKey = extras.getString("reviewKey");

        //set adapter
        adapter = ArrayAdapter.createFromResource(this, R.array.productCategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(this);

        int p = selectSpinnerItembyValue(category, categorySelected);
        category.setSelection(p);

        showReviewData(reviewKey);

        //set on click listener for cancel button
        cancelReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //set on click listener for update button
        updateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_name = productName.getText().toString();
                String product_price = productPrice.getText().toString();
                String review_detail = reviewDet.getText().toString();

                if (!product_name.isEmpty() & !product_price.isEmpty() & !review_detail.isEmpty()){
                    updateReview();
                }else if (product_name.isEmpty() & product_price.isEmpty() & review_detail.isEmpty()){
                    Toast.makeText(EditReview.this, "You cannot update an empty review!", Toast.LENGTH_SHORT).show();
                }else if (product_name.isEmpty() & product_price.isEmpty()){
                    productName.setError("This cannot be empty!");
                    productPrice.setError("This cannot be empty!");
                }else if (product_name.isEmpty() & review_detail.isEmpty()){
                    productName.setError("This cannot be empty!");
                    reviewDet.setError("This cannot be empty");
                }else if (product_price.isEmpty() & review_detail.isEmpty()){
                    productPrice.setError("This cannot be empty!");
                    reviewDet.setError("This cannot be empty");
                }else if (product_name.isEmpty()){
                    productName.setError("This cannot be empty!");
                }else if (product_price.isEmpty()){
                    productPrice.setError("This cannot be empty!");
                }else if (review_detail.isEmpty()){
                    reviewDet.setError("This cannot be empty");
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
        Toast.makeText(this, "Please select the category", Toast.LENGTH_SHORT).show();
    }

    public void updateReview() {

        if (categorySelected.equals("Select Product Category")) {
            //alert dialog for select the product category
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning)
                    .setMessage("Please select the category of your product")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else {
            //update values of the review to database
            updateReviewData(reviewKey);
            // make toast for display a text that review succesfully updated
            Toast.makeText(EditReview.this, "Review Updated", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    public int selectSpinnerItembyValue(Spinner spn, String value){
        //method to get item selected from spinner
        ArrayAdapter adapter = (ArrayAdapter) spn.getAdapter();
        for (int position = 0; position < adapter.getCount();position++){
            if(value.equals(adapter.getItemId(position))){
                return position;

            }
        }
        return 0;
    }

    private void showReviewData(String reviewid){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("table_review");
        Query userData = ref.orderByChild(reviewid);

        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String productNameFromDB = snapshot.child(reviewid).child("row_namaProduk").getValue(String.class);
                String categoryFromDB = snapshot.child(reviewid).child("row_category").getValue(String.class);
                String productPriceFromDB = snapshot.child(reviewid).child("row_hargaProduk").getValue(String.class);
                String reviewDetFromDB = snapshot.child(reviewid).child("row_isiReview").getValue(String.class);

                productName.setText(productNameFromDB);
                category.setSelection(adapter.getPosition(categoryFromDB));
                productPrice.setText(productPriceFromDB);
                reviewDet.setText(reviewDetFromDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateReviewData (String reviewid){

        String productNameChanged = productName.getText().toString();
        String categoryChanged = categorySelected;
        String productPriceChanged = productPrice.getText().toString();
        String reviewDetChanged = reviewDet.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("table_review");
        ref.child(reviewid).child("row_namaProduk").setValue(productNameChanged);
        ref.child(reviewid).child("row_category").setValue(categoryChanged);
        ref.child(reviewid).child("row_hargaProduk").setValue(productPriceChanged);
        ref.child(reviewid).child("row_isiReview").setValue(reviewDetChanged);

    }
}
