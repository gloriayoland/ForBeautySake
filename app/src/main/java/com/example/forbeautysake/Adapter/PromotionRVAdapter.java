package com.example.forbeautysake.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forbeautysake.R;
import com.example.forbeautysake.model.promotionModel;
import com.example.forbeautysake.model.reviewModel;

import java.util.ArrayList;

public class PromotionRVAdapter extends RecyclerView.Adapter<PromotionRVAdapter.PromotionAdapterViewHolder> {
    private Context context;
    private ArrayList <promotionModel> promotionList;

    public PromotionRVAdapter(Context context, ArrayList<promotionModel> promotionList) {
        this.context = context;
        this.promotionList = promotionList;

    }

    public class PromotionAdapterViewHolder extends RecyclerView.ViewHolder{

        //define variables
        TextView txt_promotionName, txt_promotionLink;

        public PromotionAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            // find components by id according to the defined variable
            txt_promotionName = itemView.findViewById(R.id.text_promotionName);
            txt_promotionLink = itemView.findViewById(R.id.text_promotionLink);
        }
    }

    @NonNull
    @Override
    public PromotionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // set layout for recycler view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_promotion_item, parent, false);

        return new PromotionRVAdapter.PromotionAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PromotionRVAdapter.PromotionAdapterViewHolder holder, int position) {

        promotionModel helper = promotionList.get(position);

        //set text
        holder.txt_promotionName.setText(helper.getRow_promotionName());
        holder.txt_promotionLink.setText(helper.getRow_promotionLink());
    }

    @Override
    public int getItemCount() {

        return promotionList.size();
    }

}
