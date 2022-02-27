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
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.fullsail.android.dvp5.pocketchef.fragments.CardListFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.DetailsFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.SearchFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.SettingsFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.ShoppingFragment;
import com.fullsail.android.dvp5.pocketchef.utilities.FirebaseHelper;
import com.fullsail.android.dvp5.pocketchef.utilities.NetworkUtility;
import com.fullsail.android.dvp5.pocketchef.utilities.SearchWorker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener,
        SettingsFragment.SettingsControlListener, LadderAdapter.OnCardClickListener, DetailsFragment.OnAuthRequired {
    public static final String BROADCAST_ACTION = "com.fullsail.android.dvp5.pocketchef.BROADCAST_ACTION_SEARCH";
    public static String QUERY = "";
    private ArrayList<RecipeCard> mCards = new ArrayList<>();
    private BroadcastReceiver mReceiver;
    private FirebaseHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        handleIntent(getIntent());
        mHelper = new FirebaseHelper();

        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation_result);
        bottomBar.setOnItemSelectedListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tab_home) {
            SearchFragment search = SearchFragment.newInstance(this, mCards);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, search).commit();
            return true;
        } else {
            if (FirebaseHelper.mUser == null) {
                mHelper.signIn(signInLauncher);
            }
            switch (item.getItemId()) {
                case R.id.tab_recipes:
                    CardListFragment recipes = CardListFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, recipes).commit();
                    return true;
                case R.id.tab_cart:
                    ShoppingFragment cart = ShoppingFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, cart).commit();
                    return true;
                case R.id.tab_settings:
                    SettingsFragment settings = SettingsFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, settings).commit();
                    return true;
            }
            return false;
        }
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        FirebaseHelper.mUser = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseHelper.mDatabase = FirebaseDatabase.getInstance().getReference();
                    } else {
                        Toast.makeText(ResultsActivity.this, "Email or password was incorrect. Please try again!", Toast.LENGTH_SHORT).show();
                        mHelper.signIn(signInLauncher);
                    }
                }
            }
    );

    @Override
    public void onSignInRequest() {
        mHelper.signIn(signInLauncher);
    }

    @Override
    public void onCardClick(int position) {
        DetailsFragment details = DetailsFragment.newInstance(mCards.get(position).getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, details).commit();
    }

    @Override
    public void onRecipes() {
        CardListFragment recipes = CardListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, recipes).commit();
    }

    @Override
    public void onShopping() {
        ShoppingFragment cart = ShoppingFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, cart).commit();
    }

    @Override
    public void onSignOut() {
        mHelper.signOut(this);
    }

    private void unRegister() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_ACTION)) {
                mCards = (ArrayList<RecipeCard>) intent.getSerializableExtra("ExtraCards");
                unRegister();
                SearchFragment search = SearchFragment.newInstance(ResultsActivity.this, mCards);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView_result, search).commit();
            }
        }
    }
}
