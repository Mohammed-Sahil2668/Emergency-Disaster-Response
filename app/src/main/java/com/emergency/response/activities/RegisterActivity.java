package com.emergency.response.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.UserProfile;
import com.emergency.response.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilMobile, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etMobile, etEmail, etPassword, etConfirmPassword;
    private CheckBox cbTerms;
    private MaterialButton btnRegisterSubmit;
    private TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();
    }

    private void initViews() {
        tilFullName = findViewById(R.id.tilFullName);
        tilMobile = findViewById(R.id.tilMobile);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etFullName = findViewById(R.id.etFullName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        cbTerms = findViewById(R.id.cbTerms);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Prepopulate with reasonable defaults for quick testing/demo
        etFullName.setText("Sahil Sharma");
        etMobile.setText("9876543210");
        etEmail.setText("sahil.sharma@example.com");
        etPassword.setText("Sahil@2026");
        etConfirmPassword.setText("Sahil@2026");
    }

    private void setupListeners() {
        btnRegisterSubmit.setOnClickListener(v -> performRegistration());

        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegistration() {
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String mobile = etMobile.getText() != null ? etMobile.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        tilFullName.setError(null);
        tilMobile.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(fullName)) {
            tilFullName.setError("Full name is required");
            isValid = false;
        }

        if (!ValidationUtils.isValidPhone(mobile)) {
            tilMobile.setError("Enter a valid 10-digit mobile number");
            isValid = false;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError("Enter a valid email address");
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(password) || password.length() < 6) {
            tilPassword.setError("Password must contain at least 6 characters");
            isValid = false;
        }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the genuine incident reporting declaration", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            // Update repository profile
            UserProfile profile = new UserProfile(
                    fullName,
                    email,
                    "+91 " + mobile,
                    "+91 98765 11223 (Emergency)",
                    "O+ Positive",
                    "#42, 12th Main Road, Indiranagar, Bengaluru, KA"
            );
            EmergencyDataRepository.getInstance().updateUserProfile(profile);

            Toast.makeText(this, "Registered successfully as Verified Citizen Responder!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
