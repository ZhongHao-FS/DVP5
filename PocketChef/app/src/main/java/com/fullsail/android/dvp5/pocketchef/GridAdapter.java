package com.fullsail.android.dvp5.pocketchef;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private static final long BASE_ID = 0x3001;
    private final Context mContext;
    private final ArrayList<RecipeCard> mCards;

    public GridAdapter(@NonNull Context context, @NonNull ArrayList<RecipeCard> objects) {
        mContext = context;
        mCards = objects;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Override
    public Object getItem(int i) {
        if (i >= 0 && i < mCards.size()) {
            return mCards.get(i);
        }

        return null;
    }

    @Override
    public long getItemId(int i) {
        return BASE_ID + i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
