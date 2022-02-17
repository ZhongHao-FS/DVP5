package com.fullsail.android.dvp5.pocketchef;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    private ArrayList<RecipeCard> mCards = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SearchWorker.class).setConstraints(constraints).build();
            WorkManager.getInstance(this).enqueue(workRequest);
        }

        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        bottomBar.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            bottomBar.setSelectedItemId(R.id.tab_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tab_home:
                return true;
            case R.id.tab_recipes:
                return true;
            case R.id.tab_cart:
                return true;
            case R.id.tab_settings:
                return true;
        }

        return false;
    }

    private void showRecyclerViewLadder() {
        RecyclerView rv = findViewById(R.id.recycleView_result);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
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
