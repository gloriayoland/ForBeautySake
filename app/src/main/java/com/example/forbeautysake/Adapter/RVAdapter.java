package com.example.forbeautysake.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forbeautysake.model.reviewModel;
import com.example.forbeautysake.model.userModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.forbeautysake.R;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVviewHolder> {

    //define variables
    private Context context;
    private ArrayList product_name, product_category, product_price, review_detail, review_date, username;
    ArrayList <reviewModel> listReview;

    //constructor
    public RVAdapter(Context context, ArrayList<reviewModel> listReview) {
        this.context = context;
        this.listReview = listReview;

    }

    public RVAdapter(Context context, ArrayList product_name, ArrayList product_category,
                     ArrayList product_price, ArrayList review_detail, ArrayList review_date,
                     ArrayList username){
        this.context = context;
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_price = product_price;
        this.review_date = review_date;
        this.review_detail = review_detail;
        this.username = username;
    }

    public class RVviewHolder extends RecyclerView.ViewHolder{

        //define variables
        TextView txt_productName, txt_productCategory, txt_productPrice, txt_reviewDet,
                txt_reviewDate, txt_username;

        public RVviewHolder(@NonNull View itemView) {
            super(itemView);

            // find components by id according to the defined variable
            txt_productName = itemView.findViewById(R.id.text_productName);
            txt_productCategory = itemView.findViewById(R.id.text_productCategory);
            txt_productPrice = itemView.findViewById(R.id.text_productPrice);
            txt_reviewDet = itemView.findViewById(R.id.text_reviewDet);
            txt_reviewDate = itemView.findViewById(R.id.text_reviewDate);
            txt_username = itemView.findViewById(R.id.text_username);

        }
    }


    @NonNull
    @Override
    public RVviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set layout for recycler view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_review_item, parent, false);

        return new RVviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVviewHolder holder, int position) {

        reviewModel helper = listReview.get(position);

        //set text
        holder.txt_productName.setText(helper.getRow_namaProduk());
        holder.txt_productCategory.setText(helper.getRow_category());
        holder.txt_productPrice.setText(helper.getRow_hargaProduk());
        holder.txt_reviewDet.setText(helper.getRow_isiReview());
        holder.txt_reviewDate.setText(helper.getRow_tanggal());
        holder.txt_username.setText(helper.getRow_username());
    }

    @Override
    public int getItemCount() {
        return listReview.size();
    }




}
