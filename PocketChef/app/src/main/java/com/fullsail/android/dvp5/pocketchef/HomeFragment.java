package com.fullsail.android.dvp5.pocketchef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private final ArrayList<RecipeCard> mCards = new ArrayList<>();

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_home, container);
        showRecyclerViewGrid();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void showRecyclerViewGrid() {
        RecyclerView rv = requireView().findViewById(R.id.recycleView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(manager);
        GridAdapter adapter = new GridAdapter(requireContext(), mCards);
        rv.setAdapter(adapter);
    }

}
