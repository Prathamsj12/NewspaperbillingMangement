package com.example.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.example.data.model.BillingRecord;
import com.example.data.repository.AppRepository;
import com.google.android.material.appbar.AppBarLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_BILLING_ID = "billing_id";
    public static final String EXTRA_CUSTOMER_NAME = "customer_name";
    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_PREVIOUS_DUE = "previous_due";
    public static final String EXTRA_TOTAL_DUE = "total_due";
    public static final String EXTRA_AREA = "area";
    public static final String EXTRA_ROUTE = "route";
    public static final String EXTRA_MONTH = "month";
    public static final String EXTRA_IS_PAID = "is_paid";

    private String billingId;
    private String customerName;
    private double currentMonthAmount = 410.00;
    private double previousBalance = 50.00;
    private double totalDue = 460.00;
    private String area = "Viman Nagar Sec 1";
    private String routeId = "RT-01";
    private String monthYear = "October 2023";
    private boolean isPaid = false;

    private TextView tvPaymentToolbarTitle;
    private ImageView ivPaymentOverflow;
    private TextView tvSummaryCurrentMonth, tvSummaryPreviousDue, tvSummaryTotalDue;
    private TextView tvDeliveryStatusMonth;
    private RadioGroup rgPaymentOption;
    private RadioButton rbFullPayment, rbPartialPayment;
    private EditText etPartialAmount;
    private RelativeLayout rlPaymentDate;
    private TextView tvPaymentDateValue;
    private Button btnSavePayment;
    private Button btnPaymentShare, btnPaymentPdf, btnPaymentImage;
    private LinearLayout llDaysCalendarContainer;

    private AppRepository repository;
    private final Calendar selectedDateCalendar = Calendar.getInstance();
    private int selectedDayNumber = 25;
    private int selectedActionIndex = 0; // 0 = Share, 1 = PDF, 2 = Image

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        repository = AppRepository.getInstance();



        EdgeToEdge.enable(this);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);


        AppBarLayout appBarLayout = findViewById(R.id.appbarpaymanet);

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
        setupDaysCalendar();
        setupPaymentForm();
        setupActionButtons();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            billingId = intent.getStringExtra(EXTRA_BILLING_ID);
            customerName = intent.getStringExtra(EXTRA_CUSTOMER_NAME);
            if (customerName == null || customerName.isEmpty()) customerName = "Rajesh Sharma";

            currentMonthAmount = intent.getDoubleExtra(EXTRA_AMOUNT, 410.00);
            previousBalance = intent.getDoubleExtra(EXTRA_PREVIOUS_DUE, 50.00);
            totalDue = intent.getDoubleExtra(EXTRA_TOTAL_DUE, currentMonthAmount + previousBalance);
            area = intent.getStringExtra(EXTRA_AREA);
            if (area == null) area = "Viman Nagar Sec 1";
            routeId = intent.getStringExtra(EXTRA_ROUTE);
            if (routeId == null) routeId = "RT-01";
            monthYear = intent.getStringExtra(EXTRA_MONTH);
            if (monthYear == null) monthYear = "October 2023";
            isPaid = intent.getBooleanExtra(EXTRA_IS_PAID, false);
        }
    }

    private void initViews() {
//        tvPaymentToolbarTitle = findViewById(R.id.tvPaymentToolbarTitle);
//        ivPaymentOverflow = findViewById(R.id.ivPaymentOverflow);
        tvSummaryCurrentMonth = findViewById(R.id.tvSummaryCurrentMonth);
        tvSummaryPreviousDue = findViewById(R.id.tvSummaryPreviousDue);
        tvSummaryTotalDue = findViewById(R.id.tvSummaryTotalDue);
        tvDeliveryStatusMonth = findViewById(R.id.tvDeliveryStatusMonth);
        rgPaymentOption = findViewById(R.id.rgPaymentOption);
        rbFullPayment = findViewById(R.id.rbFullPayment);
        rbPartialPayment = findViewById(R.id.rbPartialPayment);
        etPartialAmount = findViewById(R.id.etPartialAmount);
        rlPaymentDate = findViewById(R.id.rlPaymentDate);
        tvPaymentDateValue = findViewById(R.id.tvPaymentDateValue);
        btnSavePayment = findViewById(R.id.btnSavePayment);
        btnPaymentShare = findViewById(R.id.btnPaymentShare);
        btnPaymentPdf = findViewById(R.id.btnPaymentPdf);
        btnPaymentImage = findViewById(R.id.btnPaymentImage);
        llDaysCalendarContainer = findViewById(R.id.llDaysCalendarContainer);

        tvPaymentToolbarTitle.setText("Payment Details - " + customerName);
        tvSummaryCurrentMonth.setText(String.format(Locale.US, "₹%.2f", currentMonthAmount));
        tvSummaryPreviousDue.setText(String.format(Locale.US, "₹%.2f", previousBalance));
        tvSummaryTotalDue.setText(String.format(Locale.US, "₹%.2f", totalDue));
        tvDeliveryStatusMonth.setText(monthYear);

        rbFullPayment.setText(String.format(Locale.US, "Mark as Paid (Full: ₹%.2f)", totalDue));

        String today = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date());
        tvPaymentDateValue.setText(today);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPaymentDetails);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        if (ivPaymentOverflow != null) {
            ivPaymentOverflow.setOnClickListener(v -> {
                Toast.makeText(this, "Bill options for " + customerName, Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupDaysCalendar() {
        llDaysCalendarContainer.removeAllViews();
        
        int startDay = 20;
        int totalDays = 11;
        String[] daysOfWeek = {"Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"};

        for (int offset = 0; offset < totalDays; offset++) {
            final int dayNum = startDay + offset;
            String dayName = daysOfWeek[offset % daysOfWeek.length];

            LinearLayout dayCard = new LinearLayout(this);
            dayCard.setOrientation(LinearLayout.VERTICAL);
            dayCard.setGravity(Gravity.CENTER);
            
            boolean isSelected = (dayNum == selectedDayNumber);
            dayCard.setBackgroundResource(isSelected ? R.drawable.bg_day_card_selected : R.drawable.bg_day_card_unselected);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(116, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 16, 0);
            dayCard.setLayoutParams(params);
            dayCard.setPadding(12, 16, 12, 16);

            TextView tvDayNum = new TextView(this);
            tvDayNum.setText(String.valueOf(dayNum));
            tvDayNum.setTextSize(13);
            tvDayNum.setTypeface(null, android.graphics.Typeface.BOLD);
            tvDayNum.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
            tvDayNum.setGravity(Gravity.CENTER);

            TextView tvDayName = new TextView(this);
            tvDayName.setText(dayName);
            tvDayName.setTextSize(11);
            tvDayName.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            tvDayName.setGravity(Gravity.CENTER);

            TextView tvStatusIcon = new TextView(this);
            boolean isSunday = dayName.equals("Sun");
            boolean isFuture = dayNum > 25;
            if (isFuture) {
                tvStatusIcon.setText("🕒");
                tvStatusIcon.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            } else if (isSunday) {
                tvStatusIcon.setText("⊗");
                tvStatusIcon.setTextColor(Color.parseColor("#DC2626"));
            } else {
                tvStatusIcon.setText("✓");
                tvStatusIcon.setTextColor(Color.parseColor("#0F2444"));
            }
            tvStatusIcon.setTextSize(13);
            tvStatusIcon.setTypeface(null, android.graphics.Typeface.BOLD);
            tvStatusIcon.setGravity(Gravity.CENTER);
            tvStatusIcon.setPadding(0, 2, 0, 2);

            TextView tvPaperCodes = new TextView(this);
            tvPaperCodes.setText((isSunday || isFuture) ? "-" : "TOI, ET");
            tvPaperCodes.setTextSize(9);
            tvPaperCodes.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            tvPaperCodes.setGravity(Gravity.CENTER);

            dayCard.addView(tvDayNum);
            dayCard.addView(tvDayName);
            dayCard.addView(tvStatusIcon);
            dayCard.addView(tvPaperCodes);

            dayCard.setOnClickListener(v -> {
                selectedDayNumber = dayNum;
                setupDaysCalendar();
            });

            llDaysCalendarContainer.addView(dayCard);
        }
    }

    private void setupPaymentForm() {
        rgPaymentOption.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPartialPayment) {
                etPartialAmount.setVisibility(View.VISIBLE);
            } else {
                etPartialAmount.setVisibility(View.GONE);
            }
        });

        rlPaymentDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    PaymentDetailsActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDateCalendar.set(year, month, dayOfMonth);
                        String formatted = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(selectedDateCalendar.getTime());
                        tvPaymentDateValue.setText(formatted);
                    },
                    selectedDateCalendar.get(Calendar.YEAR),
                    selectedDateCalendar.get(Calendar.MONTH),
                    selectedDateCalendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        btnSavePayment.setOnClickListener(v -> {
            double paidAmount = totalDue;
            if (rbPartialPayment.isChecked()) {
                String input = etPartialAmount.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(this, "Please enter a valid partial amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    paidAmount = Double.parseDouble(input);
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            boolean isFull = paidAmount >= totalDue;
            if (billingId != null) {
                repository.recordPayment(billingId, paidAmount, tvPaymentDateValue.getText().toString(), isFull);
            }

            Toast.makeText(this, "Payment of ₹" + paidAmount + " saved successfully for " + customerName + "!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void updateActionButtonStyles(int selectedIndex) {
        this.selectedActionIndex = selectedIndex;
        Button[] buttons = {btnPaymentShare, btnPaymentPdf, btnPaymentImage};
        int navyColor = ContextCompat.getColor(this, R.color.primary_navy);
        for (int i = 0; i < buttons.length; i++) {
            Button btn = buttons[i];
            if (i == selectedIndex) {
                // Selected: Dark blue background, white text, white icon
                btn.setBackgroundResource(R.drawable.bg_navy_button);
                btn.setTextColor(Color.WHITE);
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
            } else {
                // Unselected: White background with dark blue border, dark blue text, dark blue icon
                btn.setBackgroundResource(R.drawable.bg_outline_button);
                btn.setTextColor(navyColor);
                btn.setCompoundDrawableTintList(ColorStateList.valueOf(navyColor));
            }
        }
    }

    private void setupActionButtons() {
        updateActionButtonStyles(0); // Share selected by default

        btnPaymentShare.setOnClickListener(v -> {
            updateActionButtonStyles(0);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello " + customerName + ", your monthly newspaper billing summary for " + monthYear + " is " + String.format(Locale.US, "₹%.2f", totalDue) + ". Please make payment via UPI or Cash. Thank you!");
            startActivity(Intent.createChooser(shareIntent, "Share Billing Summary"));
        });

        btnPaymentPdf.setOnClickListener(v -> {
            updateActionButtonStyles(1);
            Toast.makeText(this, "Generating Bill PDF for " + customerName + "...", Toast.LENGTH_SHORT).show();
        });

        btnPaymentImage.setOnClickListener(v -> {
            updateActionButtonStyles(2);
            Toast.makeText(this, "Exporting Bill Card Image for " + customerName + "...", Toast.LENGTH_SHORT).show();
        });
    }
}

