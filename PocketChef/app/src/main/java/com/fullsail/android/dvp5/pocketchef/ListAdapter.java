package com.fullsail.android.dvp5.pocketchef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final ArrayList<ListItem> mList;

    public ListAdapter(@NonNull Context context, ArrayList<ListItem> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listitem_cell, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListViewHolder lvh = (ListViewHolder) holder;
        lvh.tv_item.setText(mList.get(position).getName());
        lvh.tv_quantity.setText(mList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item;
        TextView tv_quantity;

        public ListViewHolder(View _layout) {
            super(_layout);

            tv_item = _layout.findViewById(R.id.textView_itemName);
            tv_quantity = _layout.findViewById(R.id.textView_amount);
        }
    }
}
