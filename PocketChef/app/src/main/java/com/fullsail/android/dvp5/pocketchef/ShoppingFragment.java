package com.fullsail.android.dvp5.pocketchef;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class ShoppingFragment extends Fragment {
    public ShoppingFragment() { super(R.layout.fragment_shopping); }

    public static ShoppingFragment newInstance() {
        Bundle args = new Bundle();

        ShoppingFragment fragment = new ShoppingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
