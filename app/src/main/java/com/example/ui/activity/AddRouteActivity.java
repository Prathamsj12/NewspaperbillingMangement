package com.example.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.R;
import com.example.data.model.Area;
import com.example.data.model.DeliveryStaff;
import com.example.data.model.Route;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRouteActivity extends AppCompatActivity {

    private EditText etCode, etName, etStartLocation;
    private Spinner spStaff,startingorending;
    private SwitchCompat swStatus;
    private LinearLayout layoutAreaCheckboxes;
    private AppRepository repository;
    private Route existingRoute;
    private final List<CheckBox> areaCheckBoxes = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        repository = AppRepository.getInstance();
//Above code is used to updated te toolbvar as per the device
        toolbar();
        etCode = findViewById(R.id.etRouteCode);
        etName = findViewById(R.id.etRouteName);
        spStaff = findViewById(R.id.spRouteStaff);
        startingorending=findViewById(R.id.startingorendiing);
        etStartLocation = findViewById(R.id.etRouteStartLocation);
        layoutAreaCheckboxes = findViewById(R.id.layoutAreaCheckboxes);
        swStatus = findViewById(R.id.swRouteStatus);
        Button btnSave = findViewById(R.id.btnSaveRoute);

        if (getIntent().hasExtra("EXTRA_ROUTE")) {
            existingRoute = (Route) getIntent().getSerializableExtra("EXTRA_ROUTE");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Route");
            }
        }

        setupStaffSpinner();
        setupAreaCheckboxes();
        setupStartingEndingSpinner();

        if (existingRoute != null) {
            populateExistingData();
        }

        btnSave.setOnClickListener(v -> saveRoute());
    }
    private void toolbar(){
        Toolbar toolbar = findViewById(R.id.toolbarAddRoute);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());


        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        AppBarLayout appBarLayout = findViewById(R.id.appbarnewroute);

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
    }

    private void setupStaffSpinner() {
        List<String> staffNames = new ArrayList<>();
        staffNames.add("Select a DeliveryPerson...");
        if (repository.getStaff().getValue() != null) {
            for (DeliveryStaff s : repository.getStaff().getValue()) {
                staffNames.add(s.getFullName() + " (" + s.getCode() + ")");
            }
        }
        if (staffNames.size() == 1) {
            staffNames.add("James Wilson (DS-104)");
            staffNames.add("Sarah Jenkins (DS-105)");
            staffNames.add("Ramesh Patil (DS-101)");
        }
        ArrayAdapter<String> staffAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, staffNames);
        spStaff.setAdapter(staffAdapter);
    }
    private void setupStartingEndingSpinner() {

        List<String> locationNames = new ArrayList<>();

        // Default option
        locationNames.add("Select Starting → Ending Location...");

        // Starting Location → Ending Location
        locationNames.add("CIDCO → Railway Station");
        locationNames.add("Waluj → Chhatrapati Sambhajinagar");
        locationNames.add("Paithan → Kranti Chowk");

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                locationNames
        );

        startingorending.setAdapter(locationAdapter);
    }

    private void setupAreaCheckboxes() {
        layoutAreaCheckboxes.removeAllViews();
        areaCheckBoxes.clear();

        List<String> areaOptions = new ArrayList<>();
        if (repository.getAreas().getValue() != null && !repository.getAreas().getValue().isEmpty()) {
            for (Area a : repository.getAreas().getValue()) {
                areaOptions.add(a.getCode() + " (" + a.getName() + ")");
            }
        } else {
            areaOptions.add("VN-SEC1 (Viman Nagar Sector 1)");
            areaOptions.add("VN-SEC2 (Viman Nagar Sector 2)");
            areaOptions.add("KN-SEC1 (Kalyani Nagar Sector 1)");
            areaOptions.add("KN-SEC2 (Kalyani Nagar Sector 2)");
            areaOptions.add("KRG-MAIN (Koregaon Park Main)");
            areaOptions.add("MGR-EAST (Magarpatta East)");
        }

        for (String opt : areaOptions) {
            CheckBox cb = new CheckBox(this);
            cb.setText(opt);
            cb.setTextColor(getResources().getColor(R.color.text_primary));
            cb.setTextSize(13);
            cb.setPadding(8, 8, 8, 8);
            layoutAreaCheckboxes.addView(cb);
            areaCheckBoxes.add(cb);
        }
    }

    private void populateExistingData() {
        etCode.setText(existingRoute.getCode());
        etName.setText(existingRoute.getName());
        etStartLocation.setText(existingRoute.getStartLocation());
        swStatus.setChecked(existingRoute.isActive());

        // Select hawker
        if (existingRoute.getHawkerName() != null) {
            for (int i = 0; i < spStaff.getCount(); i++) {
                if (spStaff.getItemAtPosition(i).toString().contains(existingRoute.getHawkerName())) {
                    spStaff.setSelection(i);
                    break;
                }
            }
        }

        // Check areas
        if (existingRoute.getAreasCovered() != null) {
            for (CheckBox cb : areaCheckBoxes) {
                for (String area : existingRoute.getAreasCovered()) {
                    if (cb.getText().toString().toLowerCase().contains(area.toLowerCase())) {
                        cb.setChecked(true);
                    }
                }
            }
        }
    }

    private void saveRoute() {
        String code = etCode.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String startLoc = etStartLocation.getText().toString().trim();
        boolean active = swStatus.isChecked();

        if (code.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Please enter Route Code and Name", Toast.LENGTH_SHORT).show();
            return;
        }

        String staffSelection = spStaff.getSelectedItem() != null ? spStaff.getSelectedItem().toString() : "Unassigned";
        String staffName = "Unassigned";
        if (!staffSelection.startsWith("Select")) {
            int parenIdx = staffSelection.indexOf(" (");
            staffName = parenIdx != -1 ? staffSelection.substring(0, parenIdx) : staffSelection;
        }

        List<String> selectedAreas = new ArrayList<>();
        for (CheckBox cb : areaCheckBoxes) {
            if (cb.isChecked()) {
                String full = cb.getText().toString();
                int open = full.indexOf('(');
                int close = full.indexOf(')');
                if (open != -1 && close != -1) {
                    selectedAreas.add(full.substring(open + 1, close));
                } else {
                    selectedAreas.add(full);
                }
            }
        }

        if (selectedAreas.isEmpty()) {
            selectedAreas.add("Oakridge, Pine Valley");
        }

        String id = existingRoute != null ? existingRoute.getId() : "rt_" + System.currentTimeMillis();
        Route route = new Route(
                id,
                code, name, "staff_1", staffName,
                startLoc.isEmpty() ? "Distribution Hub" : startLoc,
                selectedAreas, Arrays.asList("Sequence 1", "Sequence 2"), active
        );

        if (existingRoute != null) {
            repository.updateRoute(route);
            Toast.makeText(this, "Route updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            repository.addRoute(route);
            Toast.makeText(this, "Route created successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
