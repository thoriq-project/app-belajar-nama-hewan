package com.example.belajarnamahewan.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belajarnamahewan.Interface.ItemClickListener;
import com.example.belajarnamahewan.R;

public class HewanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageViewHewan;
    public TextView textViewHewan;
    public ItemClickListener listener;

    public HewanHolder(@NonNull View itemView) {
        super(itemView);

        imageViewHewan = itemView.findViewById(R.id.image_card_hewan);
        textViewHewan = itemView.findViewById(R.id.txt_card_hewan);

    }

    public void setItemClickListener(ItemClickListener listener){

        this.listener = listener;

    }

    @Override
    public void onClick(View v) {

        listener.onClick(v,getAdapterPosition(),false);

    }
}
