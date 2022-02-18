package com.fullsail.android.dvp5.pocketchef;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_SEARCH";
    public static String QUERY = "";
    private ArrayList<RecipeCard> mCards = new ArrayList<>();
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            QUERY = intent.getStringExtra(SearchManager.QUERY);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            mReceiver = new UpdateReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);

            if(NetworkUtility.isConnected(this)) {
                Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SearchWorker.class).setConstraints(constraints).build();
                WorkManager.getInstance(this).enqueue(workRequest);
            } else {
                Toast.makeText(this, "Network is not connected", Toast.LENGTH_SHORT).show();
            }
        }

        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation_result);
        bottomBar.setOnItemSelectedListener(this);
        bottomBar.setSelectedItemId(R.id.tab_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tab_home:
                return true;
            case R.id.tab_recipes:
                unRegister();
                return true;
            case R.id.tab_cart:
                unRegister();
                return true;
            case R.id.tab_settings:
                unRegister();
                return true;
        }

        return false;
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void showRecyclerViewLadder() {
        RecyclerView rv = findViewById(R.id.recycleView_result);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1);
        rv.setLayoutManager(manager);
        LadderAdapter adapter = new LadderAdapter(this, mCards);
        rv.setAdapter(adapter);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                mCards = (ArrayList<RecipeCard>) intent.getSerializableExtra("ExtraCards");
                showRecyclerViewLadder();
            }
        }
    }
}
