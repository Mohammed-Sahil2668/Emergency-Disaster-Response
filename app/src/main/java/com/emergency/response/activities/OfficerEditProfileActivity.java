package com.emergency.response.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.emergency.response.R;
import com.emergency.response.data.EmergencyDataRepository;
import com.emergency.response.models.Officer;
import com.emergency.response.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class OfficerEditProfileActivity extends AppCompatActivity {

    private ImageView btnBackOfficerEdit, ivOfficerEditPreview;
    private TextView tvOfficerPhotoStatus;
    private MaterialButton btnUploadOfficerPhoto, btnSaveOfficerProfile;
    private TextInputEditText etEditOfficerName, etEditOfficerPhone, etEditOfficerEmail, etEditOfficerExp;

    private Officer currentOfficer;
    private Uri selectedPhotoUri = null;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedPhotoUri = uri;
                    ivOfficerEditPreview.setImageURI(uri);
                    ivOfficerEditPreview.setColorFilter(null);
                    tvOfficerPhotoStatus.setText("Custom Profile Photo Selected");
                    Toast.makeText(this, "Profile photo loaded from device gallery", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_edit_profile);

        currentOfficer = EmergencyDataRepository.getInstance().getCurrentOfficerProfile();
        if (currentOfficer == null) {
            currentOfficer = EmergencyDataRepository.getInstance().getOfficerById("OFF-4092");
        }

        initViews();
        setupListeners();
        populateFields();
    }

    private void initViews() {
        btnBackOfficerEdit = findViewById(R.id.btnBackOfficerEdit);
        ivOfficerEditPreview = findViewById(R.id.ivOfficerEditPreview);
        tvOfficerPhotoStatus = findViewById(R.id.tvOfficerPhotoStatus);
        btnUploadOfficerPhoto = findViewById(R.id.btnUploadOfficerPhoto);
        btnSaveOfficerProfile = findViewById(R.id.btnSaveOfficerProfile);

        etEditOfficerName = findViewById(R.id.etEditOfficerName);
        etEditOfficerPhone = findViewById(R.id.etEditOfficerPhone);
        etEditOfficerEmail = findViewById(R.id.etEditOfficerEmail);
        etEditOfficerExp = findViewById(R.id.etEditOfficerExp);
    }

    private void setupListeners() {
        btnBackOfficerEdit.setOnClickListener(v -> finish());

        btnUploadOfficerPhoto.setOnClickListener(v -> {
            imagePickerLauncher.launch("image/*");
        });

        btnSaveOfficerProfile.setOnClickListener(v -> saveOfficerProfile());
    }

    private void populateFields() {
        if (currentOfficer != null) {
            etEditOfficerName.setText(currentOfficer.getName());
            etEditOfficerPhone.setText(currentOfficer.getPhone());
            etEditOfficerEmail.setText(currentOfficer.getEmail());
            etEditOfficerExp.setText(currentOfficer.getExperience());

            if (currentOfficer.getPhotoUri() != null) {
                try {
                    ivOfficerEditPreview.setImageURI(Uri.parse(currentOfficer.getPhotoUri()));
                    ivOfficerEditPreview.setColorFilter(null);
                    tvOfficerPhotoStatus.setText("Active Officer Photo");
                } catch (Exception e) {
                    ivOfficerEditPreview.setImageResource(R.drawable.ic_user_avatar);
                }
            }
        }
    }

    private void saveOfficerProfile() {
        String name = etEditOfficerName.getText() != null ? etEditOfficerName.getText().toString().trim() : "";
        String phone = etEditOfficerPhone.getText() != null ? etEditOfficerPhone.getText().toString().trim() : "";
        String email = etEditOfficerEmail.getText() != null ? etEditOfficerEmail.getText().toString().trim() : "";
        String exp = etEditOfficerExp.getText() != null ? etEditOfficerExp.getText().toString().trim() : "";

        if (!ValidationUtils.isNotEmpty(name)) {
            etEditOfficerName.setError("Name cannot be empty");
            return;
        }

        if (currentOfficer != null) {
            currentOfficer.setName(name);
            currentOfficer.setPhone(phone);
            currentOfficer.setEmail(email);
            currentOfficer.setExperience(exp);

            if (selectedPhotoUri != null) {
                currentOfficer.setPhotoUri(selectedPhotoUri.toString());
            }

            EmergencyDataRepository.getInstance().updateOfficer(currentOfficer);
            Toast.makeText(this, "Officer profile and photo successfully updated!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
