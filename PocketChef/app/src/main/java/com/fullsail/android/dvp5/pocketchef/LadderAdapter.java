package com.fullsail.android.dvp5.pocketchef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LadderAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final ArrayList<RecipeCard> mCards;

    public LadderAdapter(@NonNull Context context, @NonNull ArrayList<RecipeCard> objects) {
        mContext = context;
        mCards = objects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.large_card_cell, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cvh = (CardViewHolder) holder;
        cvh.tv_title.setText(mCards.get(position).getTitle());
        cvh.tv_descipt.setText(mCards.get(position).getDes());
        Picasso.get().load(mCards.get(position).getImageLink()).placeholder(R.drawable.ic_launcher_foreground)
                .fit().centerInside().into(cvh.iv_cover);

        holder.itemView.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tv_title;
        TextView tv_descipt;
        ImageView iv_cover;

        public CardViewHolder(View _layout) {
            super(_layout);

            cardView = _layout.findViewById(R.id.cardView_large);
            tv_title = _layout.findViewById(R.id.textView_large_cardTitle);
            tv_descipt = _layout.findViewById(R.id.textView_large_cardDes);
            iv_cover = _layout.findViewById(R.id.imageView_large);
        }
    }
}
