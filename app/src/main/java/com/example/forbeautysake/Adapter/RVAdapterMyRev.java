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
import com.example.forbeautysake.utils.DBHelper;

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

        //set text for review layout
        holder.txt_productName.setText(String.valueOf(product_name.get(position)));
        holder.txt_productCategory.setText(String.valueOf(product_category.get(position)));
        holder.txt_productPrice.setText(String.valueOf(product_price.get(position)));
        holder.txt_reviewDet.setText(String.valueOf(review_detail.get(position)));
        holder.txt_reviewDate.setText(String.valueOf(review_date.get(position)));
        holder.txt_username.setText(String.valueOf(username.get(position)));

        // set on click listener for delete button
        holder.deleteRev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //get review id from database
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("stories");
                final int id = ref.getIdRev(product_name.get(position), product_category.get(position),
                        product_price.get(position), review_detail.get(position), username.get(position),
                        review_date.get(position));

                // alert dialog for make sure are the user want to delete this review
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(false);
                builder.setMessage("Are you sure to delete this review?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //delete review by id
                        boolean res = ref.deleteReview(String.valueOf(id));
                        if (res){

                            //make toast for tell the user that post has been successful deleted
                            Toast.makeText(v.getContext(), "This post has been successfully deleted", Toast.LENGTH_SHORT).show();

                            //remove data from array list
                            product_name.remove(position);
                            product_category.remove(position);
                            product_price.remove(position);
                            review_date.remove(position);
                            review_detail.remove(position);
                            username.remove(position);
                            notifyDataSetChanged();
                        }
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

                //get id from database
                int id = ref.getIdRev(product_name.get(position), product_category.get(position),
                        product_price.get(position), review_detail.get(position), username.get(position),
                        review_date.get(position));

                //put id to sp
                SharedPreferences.Editor editor = holder.sp.edit();
                editor.putInt(holder.KEY_REVIEWID, id);
                editor.commit();

                //change page to edit review
                Intent intent = new Intent(v.getContext(), EditReview.class);

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return product_name.size();
    }

    public class RVviewHolder extends RecyclerView.ViewHolder{

        //define variables
        TextView txt_productName, txt_productCategory, txt_productPrice, txt_reviewDet, txt_reviewDate,
                txt_username;
        Button deleteRev, editRev;

        // define the name of shared preferences and key
        String KEY_REVIEWID = "idRev";
        String SP_NAME = "mypref";
        SharedPreferences sp;

        public RVviewHolder(@NonNull final View itemView) {
            super(itemView);

            //get shared preferences
            sp = itemView.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);

            // find components by id according to the defined variable
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
