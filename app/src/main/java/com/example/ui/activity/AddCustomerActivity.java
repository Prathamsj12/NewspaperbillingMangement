package com.example.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.data.model.Customer;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText etCustomerName, etCustomerPhone, etCustomerAddress, etCustomerSequence;
    private Button btnTypeResidential, btnTypeCommercial;
    private Spinner spZone, spArea;
    private LinearLayout layoutPaperCardsContainer;
    private LinearLayout btnAddAnotherPaper;
    private Button btnSaveCustomer;

    private boolean isResidential = true;//true   by default true
    private AppRepository repository;
    private Customer existingCustomer;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private final List<View> paperCardViews = new ArrayList<>();
    private final List<String> paperNames = Arrays.asList(
            "The Times of India ($15.00/mo)",
            "The Economic Times ($18.00/mo)",
            "Daily Bugle ($15.00/mo)",
            "Sunday Times ($12.00/mo)",
            "The Indian Express ($14.00/mo)",
            "Financial Express ($16.00/mo)",
            "Maharashtra Times ($12.00/mo)"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        repository = AppRepository.getInstance();
        ToolbarAppbar();//fixing of the toolbar inside the oncreate methods
        btnAddAnotherPaper.setOnClickListener(v -> addPaperCard(null));

        if (getIntent().hasExtra("EXTRA_CUSTOMER")) {
            existingCustomer = (Customer) getIntent().getSerializableExtra("EXTRA_CUSTOMER");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Customer");
            }
            populateCustomerData();
        } else {
            // Add first initial paper subscription card
            addPaperCard(null);
        }

        btnSaveCustomer.setOnClickListener(v -> saveCustomer());
    }
    private void ToolbarAppbar(){

        Toolbar toolbar = findViewById(R.id.toolbarAddCustomer);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();
        setupCustomerTypeToggle();
        setupZoneAndAreaSpinners();
        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        AppBarLayout appBarLayout = findViewById(R.id.appbar);

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
    }

    private void initViews() {
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etCustomerAddress = findViewById(R.id.etCustomerAddress);
        etCustomerSequence = findViewById(R.id.etCustomerSequence);
        btnTypeResidential = findViewById(R.id.btnTypeResidential);
        btnTypeCommercial = findViewById(R.id.btnTypeCommercial);
        spZone = findViewById(R.id.spCustomerZone);
        spArea = findViewById(R.id.spCustomerArea);
        layoutPaperCardsContainer = findViewById(R.id.layoutPaperCardsContainer);
        btnAddAnotherPaper = findViewById(R.id.btnAddAnotherPaper);
        btnSaveCustomer = findViewById(R.id.btnSaveCustomer);
    }

    private void setupCustomerTypeToggle() {
        btnTypeResidential.setOnClickListener(v -> setCustomerType(true));
        btnTypeCommercial.setOnClickListener(v -> setCustomerType(false));
    }

    private void setCustomerType(boolean residential) {
        isResidential = residential;
        if (residential) {
            btnTypeResidential.setBackgroundResource(R.drawable.bg_segment_selected);
            btnTypeResidential.setTextColor(getResources().getColor(R.color.white, null));
            btnTypeCommercial.setBackgroundResource(R.drawable.bg_segment_unselected);
            btnTypeCommercial.setTextColor(getResources().getColor(R.color.text_secondary, null));
        } else {
            btnTypeCommercial.setBackgroundResource(R.drawable.bg_segment_selected);
            btnTypeCommercial.setTextColor(getResources().getColor(R.color.white, null));
            btnTypeResidential.setBackgroundResource(R.drawable.bg_segment_unselected);
            btnTypeResidential.setTextColor(getResources().getColor(R.color.text_secondary, null));
        }
    }

    private void setupZoneAndAreaSpinners() {
        List<String> zones = Arrays.asList("East Zone", "West Zone", "North Zone", "South Zone", "Central Zone");
        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, zones);
        spZone.setAdapter(zoneAdapter);

        List<Area> areas = repository.getAreas().getValue();
        List<String> areaNames = new ArrayList<>();
        if (areas != null && !areas.isEmpty()) {
            for (Area a : areas) {
                areaNames.add(a.getName());
            }
        } else {
            areaNames.add("Viman Nagar - SEC1");
            areaNames.add("Kalyani Nagar - PH2");
            areaNames.add("Koregaon Park - LN4");
            areaNames.add("Northwest District - Sector 4");
        }

        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, areaNames);
        spArea.setAdapter(areaAdapter);
    }

    private void addPaperCard(Customer.SubscriptionItem existingSub) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_paper_subscription_entry, layoutPaperCardsContainer, false);

        TextView tvTitle = cardView.findViewById(R.id.tvPaperCardTitle);
        ImageView btnRemove = cardView.findViewById(R.id.btnRemovePaperCard);
        Spinner spPaper = cardView.findViewById(R.id.spSelectPaper);
        EditText etStartDate = cardView.findViewById(R.id.etPaperStartDate);
        Button btnPerDay = cardView.findViewById(R.id.btnBillingPerDay);
        Button btnMonthly = cardView.findViewById(R.id.btnBillingMonthly);
        LinearLayout layoutDays = cardView.findViewById(R.id.layoutDeliveryDaysContainer);

        ArrayAdapter<String> paperAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paperNames);
        spPaper.setAdapter(paperAdapter);

        etStartDate.setText(dateFormat.format(Calendar.getInstance().getTime()));
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));

        // State holder on tag
        final boolean[] isPerDay = {true};

        btnPerDay.setOnClickListener(v -> {
            isPerDay[0] = true;
            btnPerDay.setBackgroundResource(R.drawable.bg_segment_selected);
            btnPerDay.setTextColor(getResources().getColor(R.color.white, null));
            btnMonthly.setBackgroundResource(R.drawable.bg_segment_unselected);
            btnMonthly.setTextColor(getResources().getColor(R.color.text_secondary, null));
            layoutDays.setVisibility(View.VISIBLE);
        });

        btnMonthly.setOnClickListener(v -> {
            isPerDay[0] = false;
            btnMonthly.setBackgroundResource(R.drawable.bg_segment_selected);
            btnMonthly.setTextColor(getResources().getColor(R.color.white, null));
            btnPerDay.setBackgroundResource(R.drawable.bg_segment_unselected);
            btnPerDay.setTextColor(getResources().getColor(R.color.text_secondary, null));
            layoutDays.setVisibility(View.GONE);
        });

        if (existingSub != null) {
            etStartDate.setText(existingSub.getStartDate());
            if (existingSub.getBillingType() != null && existingSub.getBillingType().equalsIgnoreCase("Monthly Fixed")) {
                btnMonthly.performClick();
            } else {
                btnPerDay.performClick();
            }
            for (int i = 0; i < paperNames.size(); i++) {
                if (paperNames.get(i).contains(existingSub.getPaperName())) {
                    spPaper.setSelection(i);
                    break;
                }
            }
        }

        btnRemove.setOnClickListener(v -> {
            layoutPaperCardsContainer.removeView(cardView);
            paperCardViews.remove(cardView);
            updateCardTitlesAndRemoveButtons();
        });

        paperCardViews.add(cardView);
        layoutPaperCardsContainer.addView(cardView);
        updateCardTitlesAndRemoveButtons();
    }

    private void updateCardTitlesAndRemoveButtons() {
        for (int i = 0; i < paperCardViews.size(); i++) {
            View card = paperCardViews.get(i);
            TextView tvTitle = card.findViewById(R.id.tvPaperCardTitle);
            ImageView btnRemove = card.findViewById(R.id.btnRemovePaperCard);

            tvTitle.setText("Paper Subscription #" + (i + 1));
            btnRemove.setVisibility(paperCardViews.size() > 1 ? View.VISIBLE : View.GONE);
        }
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

    private void populateCustomerData() {
//        receving the data  from the user side
        etCustomerName.setText(existingCustomer.getName());
        etCustomerPhone.setText(existingCustomer.getPhone());
        etCustomerAddress.setText(existingCustomer.getAddress());
        etCustomerSequence.setText(existingCustomer.getDropSequence());
        setCustomerType(existingCustomer.isResidential());

        layoutPaperCardsContainer.removeAllViews();
        paperCardViews.clear();

        List<Customer.SubscriptionItem> subs = existingCustomer.getSubscriptions();
        if (subs != null && !subs.isEmpty()) {
            for (Customer.SubscriptionItem item : subs) {
                addPaperCard(item);
            }
        } else {
            addPaperCard(null);
        }
    }

    private void saveCustomer() {
        String name = etCustomerName.getText().toString().trim();
        String phone = etCustomerPhone.getText().toString().trim();
        String address = etCustomerAddress.getText().toString().trim();
        String seq = etCustomerSequence.getText().toString().trim();
        String zone = spZone.getSelectedItem() != null ? spZone.getSelectedItem().toString() : "East Zone";
        String area = spArea.getSelectedItem() != null ? spArea.getSelectedItem().toString() : "Viman Nagar - SEC1";

        if (name.isEmpty()) {
            etCustomerName.setError("Customer name is required");
            etCustomerName.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            etCustomerPhone.setError("Mobile number is required");
            etCustomerPhone.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            etCustomerAddress.setError("Address is required");
            etCustomerAddress.requestFocus();
            return;
        }
        if (seq.isEmpty()) {
            seq = "15";
        }

        List<Customer.SubscriptionItem> subscriptions = new ArrayList<>();
        double totalBalance = 0.0;
        StringBuilder overallDays = new StringBuilder();
        String primaryBillingType = "Monthly Fixed";

        for (int i = 0; i < paperCardViews.size(); i++) {
            View card = paperCardViews.get(i);
            Spinner spPaper = card.findViewById(R.id.spSelectPaper);
            EditText etStartDate = card.findViewById(R.id.etPaperStartDate);
            LinearLayout layoutDays = card.findViewById(R.id.layoutDeliveryDaysContainer);

            CheckBox cbMon = card.findViewById(R.id.cbDayMon);
            CheckBox cbTue = card.findViewById(R.id.cbDayTue);
            CheckBox cbWed = card.findViewById(R.id.cbDayWed);
            CheckBox cbThu = card.findViewById(R.id.cbDayThu);
            CheckBox cbFri = card.findViewById(R.id.cbDayFri);
            CheckBox cbSat = card.findViewById(R.id.cbDaySat);
            CheckBox cbSun = card.findViewById(R.id.cbDaySun);

            String selectedPaperStr = spPaper.getSelectedItem() != null ? spPaper.getSelectedItem().toString() : "Daily Bugle ($15.00/mo)";
            String paperName = selectedPaperStr.contains("(") ? selectedPaperStr.substring(0, selectedPaperStr.indexOf("(")).trim() : selectedPaperStr;
            double price = 15.00;
            if (selectedPaperStr.contains("$")) {
                try {
                    String pStr = selectedPaperStr.substring(selectedPaperStr.indexOf("$") + 1);
                    if (pStr.contains("/")) pStr = pStr.substring(0, pStr.indexOf("/"));
                    price = Double.parseDouble(pStr.trim());
                } catch (Exception ignored) {}
            }

            boolean isMonthly = (layoutDays.getVisibility() == View.GONE);
            String billingType = isMonthly ? "Monthly Fixed" : "Per Day";
            if (i == 0) primaryBillingType = billingType;

            List<String> days = new ArrayList<>();
            if (isMonthly) {
                days = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
            } else {
                if (cbMon.isChecked()) days.add("Mon");
                if (cbTue.isChecked()) days.add("Tue");
                if (cbWed.isChecked()) days.add("Wed");
                if (cbThu.isChecked()) days.add("Thu");
                if (cbFri.isChecked()) days.add("Fri");
                if (cbSat.isChecked()) days.add("Sat");
                if (cbSun.isChecked()) days.add("Sun");
                if (days.isEmpty()) days.add("Mon");
            }

            StringBuilder daysSb = new StringBuilder();
            for (int d = 0; d < days.size(); d++) {
                if (d > 0) daysSb.append(", ");
                daysSb.append(days.get(d));
            }
            if (i == 0) overallDays = daysSb;

            String code = "PUB-" + (100 + i);
            String startDate = etStartDate.getText().toString().trim();
            if (startDate.isEmpty()) startDate = "10/01/2024";

            subscriptions.add(new Customer.SubscriptionItem(code, paperName, startDate, billingType, daysSb.toString(), price, true));
            totalBalance += price;
        }

        String id = existingCustomer != null ? existingCustomer.getId() : "cust_" + System.currentTimeMillis();
        Customer customer = new Customer(
                id,
                name,
                phone,
                name.toLowerCase().replace(" ", ".") + "@example.com",
                isResidential ? "RESIDENTIAL" : "COMMERCIAL",
                zone,
                area,
                address,
                seq,
                "Please leave on the side porch, not the front door. Beware of dog.",
                primaryBillingType,
                overallDays.toString(),
                "Oct 2021",
                totalBalance,
                true,
                true,
                subscriptions
        );

        if (existingCustomer != null) {
            repository.updateCustomer(customer);
            Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            repository.addCustomer(customer);
            Toast.makeText(this, "Customer saved successfully", Toast.LENGTH_SHORT).show();
        }

    }
}
