package com.fullsail.android.dvp5.pocketchef.fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fullsail.android.dvp5.pocketchef.LadderAdapter;
import com.fullsail.android.dvp5.pocketchef.R;
import com.fullsail.android.dvp5.pocketchef.RecipeCard;
import com.fullsail.android.dvp5.pocketchef.ResultsActivity;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment.TAG";
    private ArrayList<RecipeCard> mCards = new ArrayList<>();

    public SearchFragment() { super(R.layout.fragment_search); }

    public static SearchFragment newInstance(ArrayList<RecipeCard> cards) {
        Bundle args = new Bundle();
        args.putSerializable(TAG, cards);

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCards = (ArrayList<RecipeCard>) getArguments().getSerializable(TAG);
        showRecyclerViewLadder(view);

        Context context = getContext();
        if (context != null) {
            SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
            SearchView search = view.findViewById(R.id.searchView_search);
            search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(context, ResultsActivity.class)));
        }
    }

    private void showRecyclerViewLadder(@NonNull View view) {
        RecyclerView rv = view.findViewById(R.id.recycleView_search);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        rv.setLayoutManager(manager);
        LadderAdapter adapter = new LadderAdapter(requireContext(), mCards);
        rv.setAdapter(adapter);
    }
}
