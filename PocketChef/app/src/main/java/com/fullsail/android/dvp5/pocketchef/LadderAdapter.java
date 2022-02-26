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
    private final OnCardClickListener mListener;

    public LadderAdapter(@NonNull Context context, @NonNull ArrayList<RecipeCard> objects, OnCardClickListener listener) {
        mContext = context;
        mCards = objects;
        mListener = listener;
    }

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.large_card_cell, parent, false);
        return new CardViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardViewHolder cvh = (CardViewHolder) holder;
        cvh.tv_title.setText(mCards.get(position).getTitle());
        cvh.tv_descipt.setText(mCards.get(position).getDes());
        Picasso.get().load(mCards.get(position).getImageLink()).placeholder(R.drawable.ic_placeholder_ladder)
                .fit().centerInside().into(cvh.iv_cover);
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OnCardClickListener listener;
        MaterialCardView cardView;
        TextView tv_title;
        TextView tv_descipt;
        ImageView iv_cover;

        public CardViewHolder(View _layout, OnCardClickListener _listener) {
            super(_layout);

            cardView = _layout.findViewById(R.id.cardView_large);
            tv_title = _layout.findViewById(R.id.textView_large_cardTitle);
            tv_descipt = _layout.findViewById(R.id.textView_large_cardDes);
            iv_cover = _layout.findViewById(R.id.imageView_large);
            listener = _listener;
            _layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onCardClick(getAdapterPosition());
        }
    }
}
