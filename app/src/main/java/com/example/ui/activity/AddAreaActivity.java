package com.example.ui.activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.R;
import com.example.data.model.Area;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

public class AddAreaActivity extends AppCompatActivity {

    private EditText etCode, etName, etZone, etCity, etPincode, etLandmark;

    private AppRepository repository;

    private FrameLayout loadingOverlay;
    private TextView tvLoadingMessage;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_area);

        // Initialize Repository
        repository = AppRepository.getInstance();

        // Initialize Views
        initViews();

        // Setup Toolbar
        setupToolbar();

        // Setup Edge to Edge
        setupEdgeToEdge();

        // Save Button Click
        setupSaveButton();
    }


    // --------------------------------------------------
    // Initialize All Views
    // --------------------------------------------------
    private void initViews() {

        etCode = findViewById(R.id.etAreaCode);
        etName = findViewById(R.id.etAreaName);
        etZone = findViewById(R.id.etAreaZone);
        etCity = findViewById(R.id.etAreaCity);
        etPincode = findViewById(R.id.etAreaPincode);
        etLandmark = findViewById(R.id.etAreaLandmark);

        btnSave = findViewById(R.id.btnSaveArea);

        loadingOverlay = findViewById(R.id.loadingOverlay);

        // IMPORTANT:
        // This ID must belong to TextView in XML
        tvLoadingMessage = findViewById(R.id.tvLoadingMessage);
    }


    // --------------------------------------------------
    // Toolbar Setup
    // --------------------------------------------------
    private void setupToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbarAddArea);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            getSupportActionBar()
                    .setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());
    }


    // --------------------------------------------------
    // Edge To Edge Setup
    // --------------------------------------------------
    private void setupEdgeToEdge() {

        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(
                getWindow(),
                false
        );

        AppBarLayout appBarLayout =
                findViewById(R.id.appbararea);


        // Status Bar Color
        getWindow().setStatusBarColor(
                getResources().getColor(
                        R.color.primary_navy
                )
        );


        // White Status Bar Icons
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.setAppearanceLightStatusBars(false);


        // Handle Status Bar Insets
        ViewCompat.setOnApplyWindowInsetsListener(
                appBarLayout,
                (view, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.statusBars()
                    );

                    view.setPadding(
                            view.getPaddingLeft(),
                            systemBars.top,
                            view.getPaddingRight(),
                            view.getPaddingBottom()
                    );

                    return insets;
                }
        );

        ViewCompat.requestApplyInsets(appBarLayout);
    }


    // --------------------------------------------------
    // Save Area Button
    // --------------------------------------------------
    private void setupSaveButton() {

        btnSave.setOnClickListener(v -> {

            // Get Input Data
            String code =
                    etCode.getText().toString().trim();

            String name =
                    etName.getText().toString().trim();

            String zone =
                    etZone.getText().toString().trim();

            String city =
                    etCity.getText().toString().trim();

            String pincode =
                    etPincode.getText().toString().trim();

            String landmark =
                    etLandmark.getText().toString().trim();


            // ------------------------------------------
            // Validate Required Fields FIRST
            // ------------------------------------------

            if (code.isEmpty()) {

                etCode.setError("Area Code is required");
                etCode.requestFocus();

                return;
            }


            if (name.isEmpty()) {

                etName.setError("Area Name is required");
                etName.requestFocus();

                return;
            }


            // ------------------------------------------
            // Show Loading AFTER Validation
            // ------------------------------------------

            showLoading(
                    true,
                    "Saving area, please wait..."
            );


            // Create Area Object
            Area area = new Area(

                    "area_" + System.currentTimeMillis(),

                    code,

                    name,

                    zone.isEmpty()
                            ? "General Zone"
                            : zone,

                    city.isEmpty()
                            ? "Pune"
                            : city,

                    pincode.isEmpty()
                            ? "411001"
                            : pincode,

                    landmark.isEmpty()
                            ? "Near Main Hub"
                            : landmark
            );


            // ------------------------------------------
            // Save Area
            // ------------------------------------------

            repository.addArea(area);


            // ------------------------------------------
            // Hide Loading
            // ------------------------------------------

            showLoading(false, "");


            // Show Success Message
            Toast.makeText(
                    AddAreaActivity.this,
                    "Area added successfully",
                    Toast.LENGTH_SHORT
            ).show();


            // Close Activity
            finish();
        });
    }


    // --------------------------------------------------
    // Show / Hide Loading
    // --------------------------------------------------
    private void showLoading(
            boolean isLoading,
            String message
    ) {

        if (isLoading) {

            // Set Loading Message
            tvLoadingMessage.setText(message);

            // Show Loading Overlay
            loadingOverlay.setVisibility(View.VISIBLE);

            // Disable Save Button
            btnSave.setEnabled(false);

        } else {

            // Hide Loading Overlay
            loadingOverlay.setVisibility(View.GONE);

            // Enable Save Button
            btnSave.setEnabled(true);
        }
    }
}
