package com.example.ui.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.data.model.Newspaper;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;
//Daily Activity when user  updated the data on te regular basis
public class AddNewspaperActivity extends AppCompatActivity {

    private EditText etCode, etName, etPublisher, etPrice;
    private Spinner spLanguage;
    private SwitchCompat swStatus;
    private AppRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newspaper);

        repository = AppRepository.getInstance();

        // --------------------------------------------------
        // Edge-to-Edge setup
        // --------------------------------------------------

        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // --------------------------------------------------
        // Toolbar setup
        // --------------------------------------------------

        Toolbar toolbar = findViewById(R.id.toolbarAddNewspaper);
        AppBarLayout appBarLayout = findViewById(R.id.appnewspaper);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // --------------------------------------------------
        // Status bar setup
        // --------------------------------------------------

        getWindow().setStatusBarColor(
                getResources().getColor(R.color.primary_navy)
        );

        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );

        // White icons on navy status bar
        controller.setAppearanceLightStatusBars(false);

        // --------------------------------------------------
        // Handle AppBarLayout from TOP
        // --------------------------------------------------

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

        // --------------------------------------------------
        // Form fields
        // --------------------------------------------------

        etCode = findViewById(R.id.etNewspaperCode);
        etName = findViewById(R.id.etNewspaperName);
        etPublisher = findViewById(R.id.etNewspaperPublisher);
        etPrice = findViewById(R.id.etNewspaperPrice);
        spLanguage = findViewById(R.id.spNewspaperLanguage);
        swStatus = findViewById(R.id.swNewspaperStatus);
        Button btnSave = findViewById(R.id.btnSaveNewspaper);

        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        "English",
                        "Hindi",
                        "Marathi",
                        "Gujarati",
                        "Tamil",
                        "Bengali",
                        "Kannada"
                }
        );

        spLanguage.setAdapter(langAdapter);

        btnSave.setOnClickListener(v -> {

            String code = etCode.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String publisher = etPublisher.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            boolean active = swStatus.isChecked();

            if (code.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(
                        this,
                        "Please fill required fields (*)",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            double price = 5.00;

            try {
                price = Double.parseDouble(priceStr);
            } catch (Exception ignored) {
            }

            String language = spLanguage.getSelectedItem() != null
                    ? spLanguage.getSelectedItem().toString()
                    : "English";

            Newspaper np = new Newspaper(
                    "np_" + System.currentTimeMillis(),
                    code,
                    name,
                    language,
                    publisher.isEmpty()
                            ? "National Media Group"
                            : publisher,
                    price,
                    active
            );

            repository.addNewspaper(np);

            Toast.makeText(
                    this,
                    "Publication added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }
}
