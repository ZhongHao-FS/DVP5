package com.fullsail.android.dvp5.pocketchef;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, SettingsFragment.SettingsControlListener {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        bottomBar.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            bottomBar.setSelectedItemId(R.id.tab_home);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tab_home) {
            HomeFragment fragment = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
            return true;
        } else if (mUser == null) {
            signIn();
            return true;
        } else {
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
    public void onRecipes() {
        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        onNavigationItemSelected(bottomBar.getMenu().getItem(R.id.tab_recipes));
    }

    @Override
    public void onShopping() {
        BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
        onNavigationItemSelected(bottomBar.getMenu().getItem(R.id.tab_cart));
    }

    @Override
    public void onSignOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
            mUser = null;
            BottomNavigationView bottomBar = findViewById(R.id.bottom_navigation);
            onNavigationItemSelected(bottomBar.getMenu().getItem(R.id.tab_settings));
        });
    }
}