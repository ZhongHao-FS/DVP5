package com.fullsail.android.dvp5.pocketchef;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.fullsail.android.dvp5.pocketchef.fragments.CardListFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.DetailsFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.HomeFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.SettingsFragment;
import com.fullsail.android.dvp5.pocketchef.fragments.ShoppingFragment;
import com.fullsail.android.dvp5.pocketchef.utilities.FirebaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener,
        SettingsFragment.SettingsControlListener, LadderAdapter.OnCardClickListener, GridAdapter.OnSampleClickListener,
        DetailsFragment.OnAuthRequired {
    private FirebaseHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance());
        mHelper = new FirebaseHelper();
        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        bottomBar.setOnItemSelectedListener(this);

        if (savedInstanceState == null) {
            bottomBar.setSelectedItemId(R.id.tab_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tab_home) {
            HomeFragment fragment = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
            return true;
        } else {
            if (FirebaseHelper.mUser == null) {
                mHelper.signIn(signInLauncher);
            }
            switch (item.getItemId()) {
                case R.id.tab_recipes:
                    CardListFragment recipes = CardListFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, recipes).commit();
                    return true;
                case R.id.tab_cart:
                    ShoppingFragment cart = ShoppingFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, cart).commit();
                    return true;
                case R.id.tab_settings:
                    SettingsFragment settings = SettingsFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, settings).commit();
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
                        Toast.makeText(MainActivity.this, "Email or password was incorrect. Please try again!", Toast.LENGTH_SHORT).show();
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
    public void onRecipes() {
        CardListFragment recipes = CardListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, recipes).commit();
    }

    @Override
    public void onShopping() {
        ShoppingFragment cart = ShoppingFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, cart).commit();
    }

    @Override
    public void onSignOut() {
        mHelper.signOut(this);
    }

    @Override
    public void onSampleClick(int position) {
        DetailsFragment details = DetailsFragment.newInstance(HomeFragment.mCards.get(position).getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, details).commit();
    }

    @Override
    public void onCardClick(int position) {
        DetailsFragment details = DetailsFragment.newInstance(CardListFragment.mCards.get(position).getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, details).commit();
    }
}