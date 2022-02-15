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

public class GridAdapter extends RecyclerView.Adapter {
    private static final long BASE_ID = 0x3001;
    private final Context mContext;
    private final ArrayList<RecipeCard> mCards;

    public GridAdapter(@NonNull Context context, @NonNull ArrayList<RecipeCard> objects) {
        mContext = context;
        mCards = objects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.material_card_cell, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cvh = (CardViewHolder) holder;
        cvh.tv_title.setText(mCards.get(position).getTitle());
        Picasso.get().load(mCards.get(position).getImageLink()).placeholder(R.drawable.ic_launcher_foreground)
                .fit().centerInside().into(cvh.iv_cover);

        holder.itemView.setOnClickListener(view -> {

        });
    }

    @Override
    public long getItemId(int i) {
        return BASE_ID + i;
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tv_title;
        ImageView iv_cover;

        public CardViewHolder(View _layout) {
            super(_layout);

            cardView = _layout.findViewById(R.id.cardView);
            tv_title = _layout.findViewById(R.id.textView_cardTitle);
            iv_cover = _layout.findViewById(R.id.imageView);
        }
    }
}
