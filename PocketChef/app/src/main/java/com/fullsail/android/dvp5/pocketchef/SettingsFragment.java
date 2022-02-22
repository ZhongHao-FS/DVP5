package com.fullsail.android.dvp5.pocketchef;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private SettingsControlListener listener;

    public interface SettingsControlListener {
        void onRecipes();
        void onShopping();
        void onSignOut();
    }

    public SettingsFragment() { super(R.layout.fragment_settings); }

    public static SettingsFragment newInstance(FirebaseUser user) {
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof SettingsControlListener) {
            listener = (SettingsControlListener) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button recipeButton = view.findViewById(R.id.button_favRecipes);
        recipeButton.setOnClickListener(this);
        Button shoppingButton = view.findViewById(R.id.button_shoppingList);
        shoppingButton.setOnClickListener(this);
        Button signOutButton = view.findViewById(R.id.button_signOut);
        signOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_favRecipes:
                listener.onRecipes();
            case R.id.button_shoppingList:
                listener.onShopping();
            case R.id.button_signOut:
                listener.onSignOut();
        }
    }
}
