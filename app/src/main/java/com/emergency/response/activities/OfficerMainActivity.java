package com.emergency.response.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.emergency.response.R;
import com.emergency.response.fragments.OfficerDashboardFragment;
import com.emergency.response.fragments.OfficerNotificationsFragment;
import com.emergency.response.fragments.OfficerProfileFragment;
import com.emergency.response.fragments.OfficerTasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OfficerMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final OfficerDashboardFragment dashboardFragment = new OfficerDashboardFragment();
    private final OfficerTasksFragment tasksFragment = new OfficerTasksFragment();
    private final OfficerNotificationsFragment notificationsFragment = new OfficerNotificationsFragment();
    private final OfficerProfileFragment profileFragment = new OfficerProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_officer);

        if (savedInstanceState == null) {
            loadFragment(dashboardFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_off_dashboard) {
                loadFragment(dashboardFragment);
                return true;
            } else if (itemId == R.id.nav_off_tasks) {
                loadFragment(tasksFragment);
                return true;
            } else if (itemId == R.id.nav_off_notifications) {
                loadFragment(notificationsFragment);
                return true;
            } else if (itemId == R.id.nav_off_profile) {
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
        transaction.replace(R.id.officer_fragment_container, fragment);
        transaction.commit();
    }
}
