package com.fullsail.android.dvp5.pocketchef.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fullsail.android.dvp5.pocketchef.LadderAdapter;
import com.fullsail.android.dvp5.pocketchef.R;
import com.fullsail.android.dvp5.pocketchef.RecipeCard;
import com.fullsail.android.dvp5.pocketchef.utilities.FirebaseHelper;

import java.util.ArrayList;

public class CardListFragment extends Fragment {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_FAVRECIPES";
    private Context mContext;
    private BroadcastReceiver mReceiver;
    public static ArrayList<RecipeCard> mCards = new ArrayList<>();

    public CardListFragment() { super(R.layout.fragment_cardlist); }

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = requireContext();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        mReceiver = new UpdateReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);

        FirebaseHelper.readRecipeCards(mContext);
    }

    private void showRecyclerViewLadder(@NonNull View view) {
        RecyclerView rv = view.findViewById(R.id.recycleView_recipes);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rv.setLayoutManager(manager);
        LadderAdapter adapter = new LadderAdapter(mContext, mCards, (LadderAdapter.OnCardClickListener) mContext);
        rv.setAdapter(adapter);
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                unRegister();
                showRecyclerViewLadder(requireView());
            }
        }
    }
}
