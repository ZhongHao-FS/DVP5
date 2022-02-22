package com.fullsail.android.dvp5.pocketchef;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_RANDOM";
    private Context mContext;
    private ArrayList<RecipeCard> mCards = new ArrayList<>();
    private BroadcastReceiver mReceiver;

    public HomeFragment() { super(R.layout.fragment_home); }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();
        if (mContext != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            mReceiver = new UpdateReceiver();
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);

            if(NetworkUtility.isConnected(mContext)) {
                Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RandomWorker.class).setConstraints(constraints).build();
                WorkManager.getInstance(mContext).enqueue(workRequest);
            } else {
                Toast.makeText(mContext, "Network is not connected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mContext != null) {
            SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
            SearchView search = view.findViewById(R.id.searchView);
            search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(mContext, ResultsActivity.class)));
        }
    }

    private void showRecyclerViewGrid(@NonNull View view) {
        RecyclerView rv = view.findViewById(R.id.recycleView);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        rv.setLayoutManager(manager);
        GridAdapter adapter = new GridAdapter(requireContext(), mCards);
        rv.setAdapter(adapter);
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                mCards = (ArrayList<RecipeCard>) intent.getSerializableExtra("ExtraCards");
                unRegister();
                showRecyclerViewGrid(requireView());
            }
        }
    }
}
