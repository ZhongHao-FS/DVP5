package com.fullsail.android.dvp5.pocketchef;


import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_RANDOM";
    private ArrayList<RecipeCard> mCards = new ArrayList<>();

    public HomeFragment() { super(R.layout.fragment_home); }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();
        if (context != null) {
            SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
            SearchView search = view.findViewById(R.id.searchView);
            search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(context, ResultsActivity.class)));
            Log.i("Search", new ComponentName(context, ResultsActivity.class).toString());
        }

        showRecyclerViewGrid(view);
    }

    private void showRecyclerViewGrid(@NonNull View view) {
        RecyclerView rv = view.findViewById(R.id.recycleView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        rv.setLayoutManager(manager);
        GridAdapter adapter = new GridAdapter(requireContext(), mCards);
        rv.setAdapter(adapter);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                mCards = (ArrayList<RecipeCard>) intent.getSerializableExtra("ExtraCards");
                showRecyclerViewGrid(requireView());
            }
        }
    }
}
