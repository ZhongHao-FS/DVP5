package com.fullsail.android.dvp5.pocketchef;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, SettingsFragment.SettingsControlListener {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_SEARCH";
    public static String QUERY = "";
    private ArrayList<RecipeCard> mCards = new ArrayList<>();
    private BroadcastReceiver mReceiver;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Bundle mInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        mInstanceState = savedInstanceState;

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

        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation_result);
        bottomBar.setOnItemSelectedListener(this);
        bottomBar.setSelectedItemId(R.id.tab_home);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tab_home) {
            onCreate(mInstanceState);
            return true;
        } else if (mUser == null) {
            signIn();
            return true;
        } else {
            unRegister();
            setContentView(R.layout.activity_main);

            switch (item.getItemId()) {
                case R.id.tab_recipes:
                    CardListFragment recipes = CardListFragment.newInstance(mUser);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, recipes).commit();
                    return true;
                case R.id.tab_cart:
                    ShoppingFragment cart = ShoppingFragment.newInstance(mUser);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, cart).commit();
                    return true;
                case R.id.tab_settings:
                    SettingsFragment settings = SettingsFragment.newInstance(mUser);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, settings).commit();
                    return true;
            }
            return false;
        }
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void signIn() {
        List<AuthUI.IdpConfig> provider = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(provider).build();
        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    mUser = onSignInResult(result);
                    BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
                    onNavigationItemSelected(bottomBar.getMenu().getItem(bottomBar.getSelectedItemId()));
                }
            }
    );

    private FirebaseUser onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            return mAuth.getCurrentUser();
        } else {
            Toast.makeText(this, "Email or password is incorrect, please sign in again!", Toast.LENGTH_SHORT).show();
            signIn();
            return null;
        }
    }

    @Override
    public void onSignOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
            mUser = null;
            BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
            onNavigationItemSelected(bottomBar.getMenu().getItem(R.id.tab_settings));
        });
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
