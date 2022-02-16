package com.fullsail.android.dvp5.pocketchef;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardListFragment extends Fragment {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION";
    private ArrayList<RecipeCard> mCards = new ArrayList<>();

    public CardListFragment() { super(R.layout.fragment_cardlist); }

    public static CardListFragment newInstance() {
        Bundle args = new Bundle();

        CardListFragment fragment = new CardListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showRecyclerViewLadder(view);
    }

    private void showRecyclerViewLadder(@NonNull View view) {
        RecyclerView rv = view.findViewById(R.id.recycleView_result);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        rv.setLayoutManager(manager);
        LadderAdapter adapter = new LadderAdapter(requireContext(), mCards);
        rv.setAdapter(adapter);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                mCards = (ArrayList<RecipeCard>) intent.getSerializableExtra("ExtraCards");
                showRecyclerViewLadder(requireView());
            }
        }
    }
}
