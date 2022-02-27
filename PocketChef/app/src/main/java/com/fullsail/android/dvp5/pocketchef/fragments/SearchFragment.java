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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fullsail.android.dvp5.pocketchef.LadderAdapter;
import com.fullsail.android.dvp5.pocketchef.R;
import com.fullsail.android.dvp5.pocketchef.RecipeCard;
import com.fullsail.android.dvp5.pocketchef.ResultsActivity;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment.TAG";
    @NonNull private final Context mContext;
    private ArrayList<RecipeCard> mCards = new ArrayList<>();

    public SearchFragment(@NonNull Context context) { super(R.layout.fragment_search);
        mContext = context;
    }

    public static SearchFragment newInstance(Context context, ArrayList<RecipeCard> cards) {
        Bundle args = new Bundle();
        args.putSerializable(TAG, cards);

        SearchFragment fragment = new SearchFragment(context);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCards = (ArrayList<RecipeCard>) getArguments().getSerializable(TAG);
        showRecyclerViewLadder(view);

        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        SearchView search = view.findViewById(R.id.searchView_search);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(mContext, ResultsActivity.class)));
    }

    private void showRecyclerViewLadder(@NonNull View view) {
        RecyclerView rv = view.findViewById(R.id.recycleView_search);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv.setLayoutManager(manager);
        LadderAdapter adapter = new LadderAdapter(mContext, mCards, (LadderAdapter.OnCardClickListener) mContext);
        rv.setAdapter(adapter);
    }
}
