package com.emergency.response.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.emergency.response.R;
import com.emergency.response.fragments.HomeFragment;
import com.emergency.response.fragments.MyReportsFragment;
import com.emergency.response.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final HomeFragment homeFragment = new HomeFragment();
    private final MyReportsFragment myReportsFragment = new MyReportsFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(homeFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.nav_report) {
                Intent intent = new Intent(MainActivity.this, ReportEmergencyActivity.class);
                startActivity(intent);
                return false; // keep current active tab
            } else if (itemId == R.id.nav_my_reports) {
                loadFragment(myReportsFragment);
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(profileFragment);
                return true;
            }
            return false;
        });
    }

    public void navigateToTab(int tabId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(tabId);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
