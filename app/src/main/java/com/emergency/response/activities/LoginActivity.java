package com.emergency.response.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private MaterialCardView cardRoleCitizen, cardRoleAuthority, cardRoleOfficer;
    private ImageView ivRoleCitizen, ivRoleAuthority, ivRoleOfficer;
    private TextView tvRoleCitizen, tvRoleAuthority, tvRoleOfficer;
    private TextView tvActiveRoleBanner;

    private TextInputLayout tilEmailOrPhone, tilPassword;
    private TextInputEditText etEmailOrPhone, etPassword;
    private CheckBox cbRememberMe;
    private MaterialButton btnLoginSubmit;
    private TextView tvForgotPassword, tvGoToRegister;
    private MaterialButton btnQuickCitizen, btnQuickAuthority, btnQuickOfficer;

    private EmergencyDataRepository.UserRole selectedRole = EmergencyDataRepository.UserRole.CITIZEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
        selectRole(EmergencyDataRepository.UserRole.CITIZEN);
    }

    private void initViews() {
        cardRoleCitizen = findViewById(R.id.cardRoleCitizen);
        cardRoleAuthority = findViewById(R.id.cardRoleAuthority);
        cardRoleOfficer = findViewById(R.id.cardRoleOfficer);

        ivRoleCitizen = findViewById(R.id.ivRoleCitizen);
        ivRoleAuthority = findViewById(R.id.ivRoleAuthority);
        ivRoleOfficer = findViewById(R.id.ivRoleOfficer);

        tvRoleCitizen = findViewById(R.id.tvRoleCitizen);
        tvRoleAuthority = findViewById(R.id.tvRoleAuthority);
        tvRoleOfficer = findViewById(R.id.tvRoleOfficer);

        tvActiveRoleBanner = findViewById(R.id.tvActiveRoleBanner);

        tilEmailOrPhone = findViewById(R.id.tilEmailOrPhone);
        tilPassword = findViewById(R.id.tilPassword);
        etEmailOrPhone = findViewById(R.id.etEmailOrPhone);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLoginSubmit = findViewById(R.id.btnLoginSubmit);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnQuickCitizen = findViewById(R.id.btnQuickCitizen);
        btnQuickAuthority = findViewById(R.id.btnQuickAuthority);
        btnQuickOfficer = findViewById(R.id.btnQuickOfficer);
    }

    private void setupListeners() {
        cardRoleCitizen.setOnClickListener(v -> selectRole(EmergencyDataRepository.UserRole.CITIZEN));
        cardRoleAuthority.setOnClickListener(v -> selectRole(EmergencyDataRepository.UserRole.AUTHORITY));
        cardRoleOfficer.setOnClickListener(v -> selectRole(EmergencyDataRepository.UserRole.OFFICER));

        btnQuickCitizen.setOnClickListener(v -> selectRole(EmergencyDataRepository.UserRole.CITIZEN));
        btnQuickAuthority.setOnClickListener(v -> selectRole(EmergencyDataRepository.UserRole.AUTHORITY));
        btnQuickOfficer.setOnClickListener(v -> selectRole(EmergencyDataRepository.UserRole.OFFICER));

        btnLoginSubmit.setOnClickListener(v -> performLogin());
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void selectRole(EmergencyDataRepository.UserRole role) {
        this.selectedRole = role;

        // Reset all cards
        resetCardStyle(cardRoleCitizen, ivRoleCitizen, tvRoleCitizen, R.drawable.ic_role_citizen);
        resetCardStyle(cardRoleAuthority, ivRoleAuthority, tvRoleAuthority, R.drawable.ic_role_authority);
        resetCardStyle(cardRoleOfficer, ivRoleOfficer, tvRoleOfficer, R.drawable.ic_role_officer);

        if (role == EmergencyDataRepository.UserRole.CITIZEN) {
            highlightCard(cardRoleCitizen, ivRoleCitizen, tvRoleCitizen, R.color.role_citizen_tint);
            tvActiveRoleBanner.setText("Signing in as Citizen Responder (Demo)");
            btnLoginSubmit.setText("Sign In as Citizen");
            etEmailOrPhone.setText("sahil.sharma@example.com");
            etPassword.setText("Citizen@2026");
            tvGoToRegister.setVisibility(TextView.VISIBLE);
        } else if (role == EmergencyDataRepository.UserRole.AUTHORITY) {
            highlightCard(cardRoleAuthority, ivRoleAuthority, tvRoleAuthority, R.color.role_authority_tint);
            tvActiveRoleBanner.setText("Signing in as Emergency Operations Authority (Demo)");
            btnLoginSubmit.setText("Sign In as Authority");
            etEmailOrPhone.setText("control.room@emergency.gov.in");
            etPassword.setText("Authority@2026");
            tvGoToRegister.setVisibility(TextView.GONE);
        } else if (role == EmergencyDataRepository.UserRole.OFFICER) {
            highlightCard(cardRoleOfficer, ivRoleOfficer, tvRoleOfficer, R.color.role_officer_tint);
            tvActiveRoleBanner.setText("Signing in as Field Response Officer (Demo)");
            btnLoginSubmit.setText("Sign In as Officer");
            etEmailOrPhone.setText("rajesh.kumar@police.gov.in");
            etPassword.setText("Officer@2026");
            tvGoToRegister.setVisibility(TextView.GONE);
        }
    }

    private void resetCardStyle(MaterialCardView card, ImageView icon, TextView text, int iconRes) {
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.bg_surface));
        card.setStrokeColor(ContextCompat.getColor(this, R.color.card_stroke));
        card.setStrokeWidth(dpToPx(1));
        card.setCardElevation(dpToPx(1));
        icon.setColorFilter(ContextCompat.getColor(this, R.color.text_secondary));
        text.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
    }

    private void highlightCard(MaterialCardView card, ImageView icon, TextView text, int tintRes) {
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.role_selected_bg));
        card.setStrokeColor(ContextCompat.getColor(this, tintRes));
        card.setStrokeWidth(dpToPx(2));
        card.setCardElevation(dpToPx(3));
        icon.setColorFilter(ContextCompat.getColor(this, tintRes));
        text.setTextColor(ContextCompat.getColor(this, R.color.navy_primary));
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void performLogin() {
        String input = etEmailOrPhone.getText() != null ? etEmailOrPhone.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        tilEmailOrPhone.setError(null);
        tilPassword.setError(null);

        boolean isValid = true;

        if (!ValidationUtils.isNotEmpty(input)) {
            tilEmailOrPhone.setError("Please enter your registered email or mobile number");
            isValid = false;
        }

        if (!ValidationUtils.isNotEmpty(password)) {
            tilPassword.setError("Please enter your password");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Password must contain at least 6 characters");
            isValid = false;
        }

        if (isValid) {
            EmergencyDataRepository.getInstance().setCurrentRole(selectedRole);

            if (selectedRole == EmergencyDataRepository.UserRole.CITIZEN) {
                Toast.makeText(this, "Authenticated successfully as Citizen Responder", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (selectedRole == EmergencyDataRepository.UserRole.AUTHORITY) {
                Toast.makeText(this, "Authenticated successfully as Operations Authority", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, AuthorityMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (selectedRole == EmergencyDataRepository.UserRole.OFFICER) {
                Toast.makeText(this, "Authenticated successfully as Field Response Officer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, OfficerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
    }

    private void showForgotPasswordDialog() {
        final EditText inputEmail = new EditText(this);
        inputEmail.setHint("Enter your registered email address");
        inputEmail.setText(etEmailOrPhone.getText() != null ? etEmailOrPhone.getText().toString() : "sahil.sharma@example.com");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your email address to receive an instant secure password reset link.")
                .setView(inputEmail)
                .setPositiveButton("Send Reset Link", (d, which) -> {
                    String email = inputEmail.getText().toString().trim();
                    if (ValidationUtils.isValidEmail(email)) {
                        Toast.makeText(LoginActivity.this, "Password reset instructions sent to " + email, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please provide a valid email address", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }
}
