package com.emergency.response.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.emergency.response.R;
import com.google.android.material.button.MaterialButton;

public class OnboardingActivity extends AppCompatActivity {

    private static final String PREF_NAME = "emergency_prefs";
    private static final String KEY_ONBOARDED = "is_onboarded";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        MaterialButton btnGetStarted = findViewById(R.id.btnGetStarted);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        btnGetStarted.setOnClickListener(v -> {
            markOnboarded();
            Intent intent = new Intent(OnboardingActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            markOnboarded();
            Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void markOnboarded() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDED, true).apply();
    }
}
