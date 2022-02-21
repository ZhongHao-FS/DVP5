package com.fullsail.android.dvp5.pocketchef;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;

public class ShoppingFragment extends Fragment {
    private static final String TAG = "ShoppingFragment.TAG";

    public ShoppingFragment() { super(R.layout.fragment_shopping); }

    public static ShoppingFragment newInstance(FirebaseUser user) {
        Bundle args = new Bundle();
        args.putParcelable(TAG, user);

        ShoppingFragment fragment = new ShoppingFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
