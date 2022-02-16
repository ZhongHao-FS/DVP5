package com.fullsail.android.dvp5.pocketchef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                HomeFragment fragment = HomeFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
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


}