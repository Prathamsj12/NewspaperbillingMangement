package com.example.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.R;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

public class DeliveryDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_DELIVERY_ID = "delivery_id";
    public static final String EXTRA_CUSTOMER_NAME = "customer_name";
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_AREA = "area";
    public static final String EXTRA_ROUTE = "route";
    public static final String EXTRA_IS_DELIVERED = "is_delivered";

    private String deliveryId;
    private String customerName;
    private String address;
    private String phone;
    private String area;
    private String routeId;
    private boolean isDelivered;

    private TextView tvDetailCustomerName, tvDetailFullAddress;
    private TextView tvDetailAreaName, tvDetailRouteCode;
    private ImageView btnDetailCall, btnDetailDirections;
    private Button btnDetailDeliveredNo, btnDetailDeliveredYes;

    private AppRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        repository = AppRepository.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarAddCustomer);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();

        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        AppBarLayout appBarLayout = findViewById(R.id.appbardeliverdetails);

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

        extractIntentData();
        initViews();
        setupToolbar();
        setupClickListeners();
        updateDeliveryToggleUI();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            deliveryId = intent.getStringExtra(EXTRA_DELIVERY_ID);
            customerName = intent.getStringExtra(EXTRA_CUSTOMER_NAME);
            if (customerName == null) customerName = "Mauli Super Market";
            address = intent.getStringExtra(EXTRA_ADDRESS);
            if (address == null) address = "Shop No 12, Ground Floor, Sai Plaza Building, MG Road Extension, Pune 411001";
            phone = intent.getStringExtra(EXTRA_PHONE);
            if (phone == null) phone = "9999999999";
            area = intent.getStringExtra(EXTRA_AREA);
            if (area == null) area = "Morning East Route";
            routeId = intent.getStringExtra(EXTRA_ROUTE);
            if (routeId == null) routeId = "RT-01";
            isDelivered = intent.getBooleanExtra(EXTRA_IS_DELIVERED, true);
        }
    }

    private void initViews() {
        tvDetailCustomerName = findViewById(R.id.tvDetailCustomerName);
        tvDetailFullAddress = findViewById(R.id.tvDetailFullAddress);
        tvDetailAreaName = findViewById(R.id.tvDetailAreaName);
        tvDetailRouteCode = findViewById(R.id.tvDetailRouteCode);
        btnDetailCall = findViewById(R.id.btnDetailCall);
        btnDetailDirections = findViewById(R.id.btnDetailDirections);
        btnDetailDeliveredNo = findViewById(R.id.btnDetailDeliveredNo);
        btnDetailDeliveredYes = findViewById(R.id.btnDetailDeliveredYes);

        tvDetailCustomerName.setText(customerName);
        tvDetailFullAddress.setText(address);
        tvDetailAreaName.setText(area);
        tvDetailRouteCode.setText(routeId);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarDeliveryDetails);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        btnDetailCall.setOnClickListener(v -> {
            Toast.makeText(this, "Calling " + customerName + " (" + phone + ")", Toast.LENGTH_SHORT).show();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(dialIntent);
        });

        btnDetailDirections.setOnClickListener(v -> {
            Toast.makeText(this, "Opening directions to " + address, Toast.LENGTH_SHORT).show();
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(address)));
            startActivity(mapIntent);
        });

        btnDetailDeliveredYes.setOnClickListener(v -> {
            isDelivered = true;
            if (deliveryId != null) {
                repository.updateDeliveryStatus(deliveryId, true);
            }
            updateDeliveryToggleUI();
            Toast.makeText(this, "Marked as Delivered!", Toast.LENGTH_SHORT).show();
        });

        btnDetailDeliveredNo.setOnClickListener(v -> {
            isDelivered = false;
            if (deliveryId != null) {
                repository.updateDeliveryStatus(deliveryId, false);
            }
            updateDeliveryToggleUI();
            Toast.makeText(this, "Marked as Pending", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateDeliveryToggleUI() {
        if (isDelivered) {
            btnDetailDeliveredYes.setBackgroundResource(R.drawable.bg_navy_button);
            btnDetailDeliveredYes.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnDetailDeliveredNo.setBackgroundResource(android.R.color.transparent);
            btnDetailDeliveredNo.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else {
            btnDetailDeliveredNo.setBackgroundResource(R.drawable.bg_navy_button);
            btnDetailDeliveredNo.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnDetailDeliveredYes.setBackgroundResource(android.R.color.transparent);
            btnDetailDeliveredYes.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }
}
