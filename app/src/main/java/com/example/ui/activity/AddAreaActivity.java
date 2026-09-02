package com.example.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_area);

        repository = AppRepository.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbarAddArea);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());


        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        AppBarLayout appBarLayout = findViewById(R.id.appbararea);

// Status bar color
        getWindow().setStatusBarColor(
                getResources().getColor(R.color.primary_navy)
        );

// White status bar icons
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.setAppearanceLightStatusBars(false);

// Handle status bar inset
        ViewCompat.setOnApplyWindowInsetsListener(appBarLayout, (view, insets) -> {

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
        });

        ViewCompat.requestApplyInsets(appBarLayout);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etCode = findViewById(R.id.etAreaCode);
        etName = findViewById(R.id.etAreaName);
        etZone = findViewById(R.id.etAreaZone);
        etCity = findViewById(R.id.etAreaCity);
        etPincode = findViewById(R.id.etAreaPincode);
        etLandmark = findViewById(R.id.etAreaLandmark);
        Button btnSave = findViewById(R.id.btnSaveArea);

        btnSave.setOnClickListener(v -> {
            String code = etCode.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String zone = etZone.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String pincode = etPincode.getText().toString().trim();
            String landmark = etLandmark.getText().toString().trim();

            if (code.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please enter Area Code and Name", Toast.LENGTH_SHORT).show();
                return;
            }

            Area area = new Area(
                    "area_" + System.currentTimeMillis(),
                    code, name,
                    zone.isEmpty() ? "General Zone" : zone,
                    city.isEmpty() ? "Pune" : city,
                    pincode.isEmpty() ? "411001" : pincode,
                    landmark.isEmpty() ? "Near Main Hub" : landmark
            );

            repository.addArea(area);
            Toast.makeText(this, "Area added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
