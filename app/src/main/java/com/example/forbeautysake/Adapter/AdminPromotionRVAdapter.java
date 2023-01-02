package com.example.forbeautysake.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forbeautysake.AdminEditPromo;
import com.example.forbeautysake.EditReview;
import com.example.forbeautysake.R;
import com.example.forbeautysake.model.promotionModel;
import com.example.forbeautysake.model.reviewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminPromotionRVAdapter extends RecyclerView.Adapter<AdminPromotionRVAdapter.AdminPromotionAdapterViewHolder> {
    private Context context;
    private ArrayList<String> promotion_name, promotion_link;
    private ArrayList <promotionModel> promotionList;

    public AdminPromotionRVAdapter(Context context, ArrayList<promotionModel> promotionList) {
        this.context = context;
        this.promotionList = promotionList;

    }

    public AdminPromotionRVAdapter(Context context, ArrayList promotion_name, ArrayList promotion_link){
        this.context = context;
        this.promotion_name = promotion_name;
        this.promotion_link = promotion_link;

    }

    public class AdminPromotionAdapterViewHolder extends RecyclerView.ViewHolder{

        //define variables
        TextView txt_promotionName, txt_promotionLink;
        Button deletePromo, editPromo;

        public AdminPromotionAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            // find components by id according to the defined variable
            txt_promotionName = itemView.findViewById(R.id.text_promotionName);
            txt_promotionLink = itemView.findViewById(R.id.text_promotionLink);

            deletePromo = itemView.findViewById(R.id.btn_deletePromo);
            editPromo = itemView.findViewById(R.id.btn_editPromo);
        }
    }

    @NonNull
    @Override
    public AdminPromotionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set layout for recycler view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_admin_promotion_item, parent, false);

        return new AdminPromotionRVAdapter.AdminPromotionAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AdminPromotionRVAdapter.AdminPromotionAdapterViewHolder holder, int position) {

        promotionModel helper = promotionList.get(position);

        //set text
        holder.txt_promotionName.setText(helper.getRow_promotionName());
        holder.txt_promotionLink.setText(helper.getRow_promotionLink());

        // set on click listener for delete button
        holder.deletePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //get review id from database

                // alert dialog for make sure are the user want to delete this review
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(false);
                builder.setMessage("Are you sure to delete this promotion?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference ref = db.getReference("table_promotion");
                        ref.child(helper.getKey()).removeValue()
                                .addOnSuccessListener(suc->{
                                    Toast.makeText(v.getContext(), "This promotion post has been successfully deleted", Toast.LENGTH_SHORT).show();
                                    notifyItemRemoved(position);
                                    promotionList.remove(helper);

                                }).addOnFailureListener(error->{
                            Toast.makeText(v.getContext(), "This promotion post has been failed delete", Toast.LENGTH_SHORT).show();
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
        holder.editPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("table_promotion");
                ref.child(helper.getKey()).setValue(helper);
                Intent intent = new Intent(v.getContext(), AdminEditPromo.class);
                intent.putExtra("promotionKey", helper.getKey());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

}
