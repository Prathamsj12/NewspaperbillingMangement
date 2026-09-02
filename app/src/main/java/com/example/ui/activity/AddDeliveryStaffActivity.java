package com.example.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.R;
import com.example.data.model.DeliveryStaff;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddDeliveryStaffActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etCode, etEmail, etMobile, etDob;
    private Spinner spGender, spEducation;

    private EditText etJoiningDate, etExperience, etAddress;

    private Spinner spDocType;
    private EditText etDocNumber;
    private LinearLayout layoutUploadFront, layoutUploadBack;
    private TextView tvUploadFrontText, tvUploadBackText;

    private EditText etBankName, etBranchName, etAccountNumber, etConfirmAccountNumber, etIfsc;
    private Spinner spAccountType;

    private Button btnSaveStaff;
    private AppRepository repository;
    private DeliveryStaff existingStaff;
//Buiness logic one time details Updated over the  Ui or when  delivery perons join that time Updated the data
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_staff);

        repository = AppRepository.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbarAddStaff);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();
        setupSpinners();
        setupDatePickers();
        setupUploads();
        setupToolbar();

        if (getIntent().hasExtra("EXTRA_STAFF")) {
            existingStaff = (DeliveryStaff) getIntent().getSerializableExtra("EXTRA_STAFF");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Delivery Person");
            }
            populateStaffData();
        }

        btnSaveStaff.setOnClickListener(v -> saveStaff());
    }

    private void setupToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbarAddStaff);
        AppBarLayout appBarLayout = findViewById(R.id.appBarDeliveryStaff);

        // Set Toolbar as ActionBar
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Toolbar back button
        toolbar.setNavigationOnClickListener(v -> finish());

        // Status bar color
        getWindow().setStatusBarColor(
                getResources().getColor(R.color.primary_navy)
        );

        // Status bar icon appearance
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(
                        getWindow(),
                        getWindow().getDecorView()
                );

        // false = white icons
        controller.setAppearanceLightStatusBars(false);

        // Handle status bar top inset
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

        // Request inset calculation
        ViewCompat.requestApplyInsets(appBarLayout);
    }
    private void initViews() {
        etFirstName = findViewById(R.id.etStaffFirstName);
        etMiddleName = findViewById(R.id.etStaffMiddleName);
        etLastName = findViewById(R.id.etStaffLastName);
        etCode = findViewById(R.id.etStaffCode);
        etEmail = findViewById(R.id.etStaffEmail);
        etMobile = findViewById(R.id.etStaffMobile);
        etDob = findViewById(R.id.etStaffDob);
        spGender = findViewById(R.id.spStaffGender);
        spEducation = findViewById(R.id.spStaffEducation);

        etJoiningDate = findViewById(R.id.etStaffJoiningDate);
        etExperience = findViewById(R.id.etStaffExperience);
        etAddress = findViewById(R.id.etStaffAddress);

        spDocType = findViewById(R.id.spStaffDocType);
        etDocNumber = findViewById(R.id.etStaffDocNumber);
        layoutUploadFront = findViewById(R.id.layoutUploadFront);
        layoutUploadBack = findViewById(R.id.layoutUploadBack);
        tvUploadFrontText = findViewById(R.id.tvUploadFrontText);
        tvUploadBackText = findViewById(R.id.tvUploadBackText);

        etBankName = findViewById(R.id.etStaffBankName);
        etBranchName = findViewById(R.id.etStaffBranchName);
        etAccountNumber = findViewById(R.id.etStaffAccountNumber);
        etConfirmAccountNumber = findViewById(R.id.etStaffConfirmAccountNumber);
        etIfsc = findViewById(R.id.etStaffIfsc);
        spAccountType = findViewById(R.id.spStaffAccountType);

        btnSaveStaff = findViewById(R.id.btnSaveStaff);
    }

    private void setupSpinners() {
        List<String> genders = Arrays.asList("Select Gender", "Male", "Female", "Other");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        spGender.setAdapter(genderAdapter);

        List<String> educations = Arrays.asList("Select Level", "High School", "Diploma", "Bachelor's Degree", "Master's Degree", "Other");
        ArrayAdapter<String> eduAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, educations);
        spEducation.setAdapter(eduAdapter);

        List<String> docTypes = Arrays.asList("Select ID Type", "Aadhaar Card", "Driving License", "Voter ID Card", "PAN Card", "Passport");
        ArrayAdapter<String> docAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, docTypes);
        spDocType.setAdapter(docAdapter);

        List<String> accTypes = Arrays.asList("Checking / Current", "Savings Account", "Salary Account");
        ArrayAdapter<String> accAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accTypes);
        spAccountType.setAdapter(accAdapter);
    }

    private void setupDatePickers() {
        etDob.setOnClickListener(v -> showDatePicker(etDob));
        etJoiningDate.setOnClickListener(v -> showDatePicker(etJoiningDate));
    }

    private void showDatePicker(EditText target) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    target.setText(dateFormat.format(c.getTime()));
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void setupUploads() {
        layoutUploadFront.setOnClickListener(v -> {
            tvUploadFrontText.setText("Document_Front.jpg (Attached ✓)");
            tvUploadFrontText.setTextColor(getResources().getColor(R.color.primary_navy, null));
            Toast.makeText(this, "Front document attached successfully", Toast.LENGTH_SHORT).show();
        });

        layoutUploadBack.setOnClickListener(v -> {
            tvUploadBackText.setText("Document_Back.jpg (Attached ✓)");
            tvUploadBackText.setTextColor(getResources().getColor(R.color.primary_navy, null));
            Toast.makeText(this, "Back document attached successfully", Toast.LENGTH_SHORT).show();
        });
    }
//passing the data from the modle or take tye response from the
    private void populateStaffData() {
        etFirstName.setText(existingStaff.getFirstName());
        etMiddleName.setText(existingStaff.getMiddleName());
        etLastName.setText(existingStaff.getLastName());
        etCode.setText(existingStaff.getStaffCode());
        etEmail.setText(existingStaff.getEmail());
        etMobile.setText(existingStaff.getMobileNumber());
        etDob.setText(existingStaff.getDob());
        etJoiningDate.setText(existingStaff.getJoiningDate());
        etExperience.setText(existingStaff.getExperience());
        etAddress.setText(existingStaff.getResidentialAddress());
        etDocNumber.setText(existingStaff.getDocumentNumber());
        etBankName.setText(existingStaff.getBankName());
        etBranchName.setText(existingStaff.getBranchName());
        etAccountNumber.setText(existingStaff.getAccountNumber());
        etConfirmAccountNumber.setText(existingStaff.getAccountNumber());
        etIfsc.setText(existingStaff.getIfscCode());
    }

    private void saveStaff() {
        String first = etFirstName.getText().toString().trim();
        String middle = etMiddleName.getText().toString().trim();
        String last = etLastName.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String joining = etJoiningDate.getText().toString().trim();
        String exp = etExperience.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String docType = spDocType.getSelectedItem() != null ? spDocType.getSelectedItem().toString() : "Aadhaar Card";
        String docNumber = etDocNumber.getText().toString().trim();
        String bank = etBankName.getText().toString().trim();
        String branch = etBranchName.getText().toString().trim();
        String accNum = etAccountNumber.getText().toString().trim();
        String confirmAcc = etConfirmAccountNumber.getText().toString().trim();
        String ifsc = etIfsc.getText().toString().trim();
        String accType = spAccountType.getSelectedItem() != null ? spAccountType.getSelectedItem().toString() : "Checking / Current";
// details mandatoary
        if (first.isEmpty()) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return;
        }
        if (last.isEmpty()) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return;
        }
        if (mobile.isEmpty()) {
            etMobile.setError("Mobile number is required");
            etMobile.requestFocus();
            return;
        }

        if (code.isEmpty()) {
            code = "DS-" + (int)(100 + Math.random() * 900);
        }

        if (!accNum.isEmpty() && !confirmAcc.isEmpty() && !accNum.equals(confirmAcc)) {
            etConfirmAccountNumber.setError("Account numbers do not match");
            etConfirmAccountNumber.requestFocus();
            return;
        }

        String id = existingStaff != null ? existingStaff.getId() : "staff_" + System.currentTimeMillis();
        DeliveryStaff staff = new DeliveryStaff();
        staff.setId(id);
        staff.setFirstName(first);
        staff.setMiddleName(middle);
        staff.setLastName(last);
        staff.setStaffCode(code);
        staff.setEmail(email.isEmpty() ? first.toLowerCase() + "." + last.toLowerCase() + "@newspaper.com" : email);
        staff.setMobileNumber(mobile);
        staff.setDob(dob.isEmpty() ? "01/01/1995" : dob);
        staff.setGender(spGender.getSelectedItem() != null ? spGender.getSelectedItem().toString() : "Male");
        staff.setEducation(spEducation.getSelectedItem() != null ? spEducation.getSelectedItem().toString() : "High School");
        staff.setJoiningDate(joining.isEmpty() ? "01/15/2024" : joining);
        staff.setExperience(exp);
        staff.setResidentialAddress(address.isEmpty() ? "City Center, Block A" : address);
        staff.setDocumentType(docType);
        staff.setDocumentNumber(docNumber);
        staff.setBankName(bank);
        staff.setBranchName(branch);
        staff.setAccountNumber(accNum);
        staff.setIfscCode(ifsc);
        staff.setAccountType(accType);
        staff.setActive(true);
        staff.setVehicleDetails("Bicycle / Motorcycle");
        if (existingStaff != null) {
            repository.updateStaff(staff);
            Toast.makeText(this, "Delivery Person updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            repository.addStaff(staff);
            Toast.makeText(this, "Delivery Person created successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
