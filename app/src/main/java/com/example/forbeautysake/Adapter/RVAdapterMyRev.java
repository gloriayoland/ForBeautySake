package com.example.forbeautysake.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forbeautysake.EditReview;
import com.example.forbeautysake.R;
import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.model.userModel;
import com.example.forbeautysake.utils.DBHelper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RVAdapterMyRev extends RecyclerView.Adapter<RVAdapterMyRev.RVviewHolder> {

    //define variables
    private Context context;
    private ArrayList<String> product_name, product_category, product_price, review_detail,
            review_date, username;
    //private DBHelper dbHelper;
    ArrayList <reviewModel> listReview;

    public RVAdapterMyRev(Context context, ArrayList<reviewModel> listReview) {
        this.context = context;
        this.listReview = listReview;
    }

    //constructor
    public RVAdapterMyRev(Context context, ArrayList product_name, ArrayList product_category,
                          ArrayList product_price, ArrayList review_detail, ArrayList review_date,
                          ArrayList username){
        this.context = context;
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_price = product_price;
        this.review_date = review_date;
        this.review_detail = review_detail;
        this.username = username;

        //get data from database
        //dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public RVviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_review_item_my, parent, false);
        return new RVAdapterMyRev.RVviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RVviewHolder holder, final int position) {

        reviewModel helper = listReview.get(position);
        
        //set text for review layout
        holder.txt_productName.setText(helper.getRow_namaProduk());
        holder.txt_productCategory.setText(helper.getRow_category());
        holder.txt_productPrice.setText(helper.getRow_hargaProduk());
        holder.txt_reviewDet.setText(helper.getRow_isiReview());
        holder.txt_reviewDate.setText(helper.getRow_tanggal());
        holder.txt_username.setText(helper.getRow_username());

        // set on click listener for delete button
        holder.deleteRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //get review id from database
                
                // alert dialog for make sure are the user want to delete this review
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(false);
                builder.setMessage("Are you sure to delete this review?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference ref = db.getReference("table_review");
                        ref.child(helper.getKey()).removeValue()
                                .addOnSuccessListener(suc->{
                                    Toast.makeText(v.getContext(), "This post has been successfully deleted", Toast.LENGTH_SHORT).show();
                                    notifyItemRemoved(position);
                                    listReview.remove(helper);

                                }).addOnFailureListener(error->{
                            Toast.makeText(v.getContext(), "This post has been failed delete", Toast.LENGTH_SHORT).show();
                        });
                        
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //set on click listener edit button
        holder.editRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("table_review");
                ref.child(helper.getKey()).setValue(helper);
                Intent intent = new Intent(v.getContext(), EditReview.class);
                v.getContext().startActivity(intent);
//
//                //get id from database
//                int id = ref.getIdRev(product_name.get(position), product_category.get(position),
//                        product_price.get(position), review_detail.get(position), username.get(position),
//                        review_date.get(position));
//
//
//                //change page to edit review
//                Intent intent = new Intent(v.getContext(), EditReview.class);
//
//                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }

    public class RVviewHolder extends RecyclerView.ViewHolder{

        //define variables
        TextView txt_productName, txt_productCategory, txt_productPrice, txt_reviewDet, txt_reviewDate,
                txt_username;
        Button deleteRev, editRev;

        public RVviewHolder(@NonNull final View itemView) {
            super(itemView);

            txt_productName = itemView.findViewById(R.id.text_productName);
            txt_productCategory = itemView.findViewById(R.id.text_productCategory);
            txt_productPrice = itemView.findViewById(R.id.text_productPrice);
            txt_reviewDet = itemView.findViewById(R.id.text_reviewDet);
            txt_username = itemView.findViewById(R.id.text_username);
            txt_reviewDate = itemView.findViewById(R.id.text_reviewDate);

            deleteRev = itemView.findViewById(R.id.btn_deleteRev);
            editRev = itemView.findViewById(R.id.btn_editRev);

        }


    }
}
