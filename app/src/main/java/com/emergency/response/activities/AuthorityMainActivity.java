package com.emergency.response.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.emergency.response.R;
import com.emergency.response.fragments.AuthorityDashboardFragment;
import com.emergency.response.fragments.AuthorityEmergenciesFragment;
import com.emergency.response.fragments.AuthorityNotificationsFragment;
import com.emergency.response.fragments.AuthorityProfileFragment;
import com.emergency.response.fragments.OfficerManagementFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AuthorityMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final AuthorityDashboardFragment dashboardFragment = new AuthorityDashboardFragment();
    private final AuthorityEmergenciesFragment emergenciesFragment = new AuthorityEmergenciesFragment();
    private final OfficerManagementFragment officersFragment = new OfficerManagementFragment();
    private final AuthorityNotificationsFragment notificationsFragment = new AuthorityNotificationsFragment();
    private final AuthorityProfileFragment profileFragment = new AuthorityProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_authority);

        if (savedInstanceState == null) {
            loadFragment(dashboardFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_auth_dashboard) {
                loadFragment(dashboardFragment);
                return true;
            } else if (itemId == R.id.nav_auth_emergencies) {
                loadFragment(emergenciesFragment);
                return true;
            } else if (itemId == R.id.nav_auth_officers) {
                loadFragment(officersFragment);
                return true;
            } else if (itemId == R.id.nav_auth_notifications) {
                loadFragment(notificationsFragment);
                return true;
            } else if (itemId == R.id.nav_auth_profile) {
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
        transaction.replace(R.id.authority_fragment_container, fragment);
        transaction.commit();
    }
}
