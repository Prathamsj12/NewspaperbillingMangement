package com.example.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.R;
import com.example.data.model.Customer;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Locale;

public class CustomerDetailsActivity extends AppCompatActivity {

    private String customerId;
    private Customer customer;
    private AppRepository repository;

    private TextView tvCustomerInitials, tvCustomerName, tvCustomerType, tvCustomerId, tvActiveSince;
    private TextView tvBalance, tvStatusBadge;
    private TextView tvCustomerPhone, tvCustomerEmail, tvCustomerAddress;
    private TextView tvArea, tvRoute, tvSequence, tvInstructions;
    private TextView tvBillingCycle, tvDays, btnSwitchCycle;
    private LinearLayout llSubscriptionContainer;
    private Button btnToolbarEditCustomer, btnEditSubscriptions, btnCollectBill;
    private ImageView btnCall;
    private TextView btnDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        repository = AppRepository.getInstance();

        if (getIntent().hasExtra("EXTRA_CUSTOMER")) {
            customer = (Customer) getIntent().getSerializableExtra("EXTRA_CUSTOMER");
            if (customer != null) customerId = customer.getId();
        } else if (getIntent().hasExtra("EXTRA_CUSTOMER_ID")) {
            customerId = getIntent().getStringExtra("EXTRA_CUSTOMER_ID");
        } else {
            customerId = getIntent().getStringExtra("customer_id");
        }

        Toolbar toolbar = findViewById(R.id.toolbarCustomerDetails);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();

        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        AppBarLayout appBarLayout = findViewById(R.id.appbarcustomer);

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

        initViews();
        loadCustomerData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (customerId != null) {
            loadCustomerData();
        }
    }

    private void initViews() {
        tvCustomerInitials = findViewById(R.id.tvCustomerInitials);
        tvCustomerName = findViewById(R.id.tvDetailsCustomerName);
        tvCustomerType = findViewById(R.id.tvDetailsCustomerType);
        tvCustomerId = findViewById(R.id.tvDetailsCustomerId);
        tvActiveSince = findViewById(R.id.tvDetailsActiveSince);
        tvBalance = findViewById(R.id.tvDetailsBalance);
        tvStatusBadge = findViewById(R.id.tvDetailsStatusBadge);

        tvCustomerPhone = findViewById(R.id.tvDetailsCustomerPhone);
        tvCustomerEmail = findViewById(R.id.tvDetailsCustomerEmail);
        tvCustomerAddress = findViewById(R.id.tvDetailsCustomerAddress);
        btnCall = findViewById(R.id.btnDetailsCall);
        btnDirections = findViewById(R.id.btnDetailsDirections);

        tvArea = findViewById(R.id.tvDetailsArea);
        tvRoute = findViewById(R.id.tvDetailsRoute);
        tvSequence = findViewById(R.id.tvDetailsSequence);
        tvInstructions = findViewById(R.id.tvDetailsInstructions);

        tvBillingCycle = findViewById(R.id.tvDetailsBillingCycle);
        btnSwitchCycle = findViewById(R.id.btnSwitchCycle);
        tvDays = findViewById(R.id.tvDetailsDays);
        btnEditSubscriptions = findViewById(R.id.btnEditSubscriptions);
        btnToolbarEditCustomer = findViewById(R.id.btnToolbarEditCustomer);
        llSubscriptionContainer = findViewById(R.id.llSubscriptionContainer);
        btnCollectBill = findViewById(R.id.btnDetailsCollectBill);

        btnCall.setOnClickListener(v -> {
            if (customer != null && customer.getPhone() != null && !customer.getPhone().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + customer.getPhone()));
                startActivity(intent);
            }
        });

        btnDirections.setOnClickListener(v -> {
            if (customer != null && customer.getAddress() != null) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(customer.getAddress()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                try {
                    startActivity(mapIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "Opening Address: " + customer.getAddress(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        View.OnClickListener editCustomerListener = v -> {
            if (customer != null) {
                Intent intent = new Intent(CustomerDetailsActivity.this, AddCustomerActivity.class);
                intent.putExtra("EXTRA_CUSTOMER", customer);
                startActivity(intent);
            }
        };

        if (btnToolbarEditCustomer != null) {
            btnToolbarEditCustomer.setOnClickListener(editCustomerListener);
        }
        if (btnEditSubscriptions != null) {
            btnEditSubscriptions.setOnClickListener(editCustomerListener);
        }

        btnSwitchCycle.setOnClickListener(v -> {
            if (customer != null) {
                String current = customer.getBillingCycle();
                if ("Monthly Fixed".equalsIgnoreCase(current)) {
                    customer.setBillingCycle("Per Day");
                    tvBillingCycle.setText("Per Day");
                    btnSwitchCycle.setText("Switch to Fixed Monthly");
                } else {
                    customer.setBillingCycle("Monthly Fixed");
                    tvBillingCycle.setText("Monthly Fixed");
                    btnSwitchCycle.setText("Switch to Per Day");
                }
                repository.updateCustomer(customer);
                Toast.makeText(this, "Billing cycle updated to " + customer.getBillingCycle(), Toast.LENGTH_SHORT).show();
            }
        });

        btnCollectBill.setOnClickListener(v -> {
            if (customer == null) return;
            EditText etAmount = new EditText(this);
            etAmount.setHint("Amount (e.g. " + customer.getBalance() + ")");
            etAmount.setText(String.format(Locale.US, "%.2f", customer.getBalance()));
            etAmount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

            new AlertDialog.Builder(this)
                    .setTitle("Record Payment for " + customer.getName())
                    .setMessage("Current Outstanding Balance: " + customer.getFormattedBalance())
                    .setView(etAmount)
                    .setPositiveButton("Confirm Paid", (dialog, which) -> {
                        String input = etAmount.getText().toString().trim();
                        double amount = customer.getBalance();
                        if (!input.isEmpty()) {
                            try {
                                amount = Double.parseDouble(input);
                            } catch (Exception ignored) {}
                        }
                        customer.setBalance(Math.max(0, customer.getBalance() - amount));
                        tvBalance.setText(customer.getFormattedBalance());
                        tvStatusBadge.setText("PAID TODAY");
                        repository.updateCustomer(customer);
                        Toast.makeText(this, "Payment of ₹" + String.format(Locale.US, "%.2f", amount) + " recorded!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void loadCustomerData() {
        if (customerId != null) {
            Customer found = repository.getCustomerById(customerId);
            if (found != null) customer = found;
        }

        if (customer == null) {
            Toast.makeText(this, "Customer profile not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvCustomerInitials.setText(customer.getInitials());
        tvCustomerName.setText(customer.getName());

        boolean isRes = customer.isResidential();
        tvCustomerType.setText(isRes ? "RESIDENTIAL" : "COMMERCIAL");
        if (isRes) {
            tvCustomerType.setBackgroundResource(R.drawable.bg_badge_residential);
            tvCustomerType.setTextColor(Color.parseColor("#3730A3"));
        } else {
            tvCustomerType.setBackgroundResource(R.drawable.bg_badge_commercial);
            tvCustomerType.setTextColor(Color.parseColor("#9D174D"));
        }

        tvCustomerId.setText("ID: CUST-" + (customer.getId().length() > 6 ? customer.getId().substring(customer.getId().length() - 6) : "99201A").toUpperCase());
        tvActiveSince.setText("Active since " + (customer.getActiveSince() != null ? customer.getActiveSince() : "Oct 2021"));
        tvBalance.setText(customer.getFormattedBalance());

        if (customer.getBalance() == 0) {
            tvStatusBadge.setText("PAID IN FULL");
            tvStatusBadge.setBackgroundResource(R.drawable.bg_dark_navy_pill);
        } else {
            tvStatusBadge.setText("PAID YESTERDAY");
            tvStatusBadge.setBackgroundResource(R.drawable.bg_dark_navy_pill);
        }

        tvCustomerPhone.setText(customer.getPhone());
        tvCustomerEmail.setText(customer.getEmail());
        tvCustomerAddress.setText(customer.getAddress());

        tvArea.setText(customer.getArea() != null && !customer.getArea().isEmpty() ? customer.getArea() : "Northwest District - Sector 4");
        tvRoute.setText(customer.getZone() != null && !customer.getZone().isEmpty() ? customer.getZone() + " (Morning)" : "Route 42-A (Morning)");
        tvSequence.setText("#" + (customer.getDropSequence() != null && !customer.getDropSequence().isEmpty() ? customer.getDropSequence() : "45"));
        tvInstructions.setText("\"" + (customer.getDeliveryInstructions() != null && !customer.getDeliveryInstructions().isEmpty() ? customer.getDeliveryInstructions() : "Please leave on the side porch, not the front door. Beware of dog.") + "\"");

        String cycle = customer.getBillingCycle() != null ? customer.getBillingCycle() : "Monthly Fixed";
        tvBillingCycle.setText(cycle);
        btnSwitchCycle.setText(cycle.equalsIgnoreCase("Monthly Fixed") ? "Switch to Per Day" : "Switch to Fixed Monthly");
        tvDays.setText(customer.getDeliveryDays() != null && !customer.getDeliveryDays().isEmpty() ? customer.getDeliveryDays() : "Mon, Tue, Wed, Thu, Fri, Sat, Sun");

        renderSubscriptions();
    }

    private void renderSubscriptions() {
        llSubscriptionContainer.removeAllViews();
        if (customer.getSubscriptions() == null || customer.getSubscriptions().isEmpty()) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_customer_subscription_card, llSubscriptionContainer, false);
            TextView tvName = cardView.findViewById(R.id.tvSubPaperName);
            TextView tvDays = cardView.findViewById(R.id.tvSubDeliveryDays);
            TextView tvBilling = cardView.findViewById(R.id.tvSubBillingType);
            TextView tvDate = cardView.findViewById(R.id.tvSubStartDate);
            TextView tvStatus = cardView.findViewById(R.id.tvSubStatus);

            tvName.setText("Daily Bugle");
            tvDays.setText("Mon, Tue, Wed, Thu, Fri, Sat, Sun");
            tvBilling.setText("Monthly Fixed");
            tvDate.setText("2021-10-15");
            tvStatus.setText("ACTIVE");

            cardView.findViewById(R.id.btnEditSubCard).setOnClickListener(v -> {
                Intent intent = new Intent(this, AddCustomerActivity.class);
                intent.putExtra("EXTRA_CUSTOMER", customer);
                startActivity(intent);
            });

            llSubscriptionContainer.addView(cardView);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Customer.SubscriptionItem sub : customer.getSubscriptions()) {
            View cardView = inflater.inflate(R.layout.item_customer_subscription_card, llSubscriptionContainer, false);

            TextView tvName = cardView.findViewById(R.id.tvSubPaperName);
            TextView tvDays = cardView.findViewById(R.id.tvSubDeliveryDays);
            TextView tvBilling = cardView.findViewById(R.id.tvSubBillingType);
            TextView tvDate = cardView.findViewById(R.id.tvSubStartDate);
            TextView tvStatus = cardView.findViewById(R.id.tvSubStatus);
            ImageView btnEdit = cardView.findViewById(R.id.btnEditSubCard);

            tvName.setText(sub.getPaperName() != null ? sub.getPaperName() : sub.getName());
            tvDays.setText(sub.getDeliveryDays() != null ? sub.getDeliveryDays() : "Mon, Tue, Wed, Thu, Fri, Sat, Sun");
            tvBilling.setText(sub.getBillingType() != null ? sub.getBillingType() : "Monthly Fixed");
            tvDate.setText(sub.getStartDate() != null ? sub.getStartDate() : "2021-10-15");
            tvStatus.setText(sub.isActive() ? "ACTIVE" : "PAUSED");

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddCustomerActivity.class);
                intent.putExtra("EXTRA_CUSTOMER", customer);
                startActivity(intent);
            });

            llSubscriptionContainer.addView(cardView);
        }
    }
}
