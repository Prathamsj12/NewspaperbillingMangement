package com.example.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.Area;
import com.example.data.model.BillingRecord;
import com.example.data.model.Route;
import com.example.ui.activity.MainActivity;
import com.example.ui.activity.PaymentDetailsActivity;
import com.example.ui.adapter.MonthlyBillingAdapter;
import com.example.viewmodel.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthlyBillingFragment extends Fragment implements MonthlyBillingAdapter.OnBillingActionListener {

    private MainViewModel viewModel;
    private MonthlyBillingAdapter adapter;
    private Spinner spBillingRouteFilter, spBillingAreaFilter;
    private RelativeLayout rlSelectMonth;
    private TextView tvSelectedMonth;
    private EditText etSearchBilling;
    private List<BillingRecord> allBillings = new ArrayList<>();
    private String selectedRoute = "All Routes";
    private String selectedArea = "All Areas";
    private String searchQuery = "";
    private final Calendar selectedMonthCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_billing, container, false);

        spBillingRouteFilter = view.findViewById(R.id.spBillingRouteFilter);
        spBillingAreaFilter = view.findViewById(R.id.spBillingAreaFilter);
        rlSelectMonth = view.findViewById(R.id.rlSelectMonth);
        tvSelectedMonth = view.findViewById(R.id.tvSelectedMonth);
        etSearchBilling = view.findViewById(R.id.etSearchBilling);
        RecyclerView rvMonthlyBillings = view.findViewById(R.id.rvMonthlyBillings);

        rvMonthlyBillings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MonthlyBillingAdapter(this);
        rvMonthlyBillings.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getBillings().observe(getViewLifecycleOwner(), billings -> {
            allBillings = billings != null ? billings : new ArrayList<>();
            applyFilter();
        });

        setupMonthSelector();
        setupSearch();
        setupSpinners();

        return view;
    }

    private void setupMonthSelector() {
        if (rlSelectMonth != null) {
            rlSelectMonth.setOnClickListener(v -> {
                DatePickerDialog dialog = new DatePickerDialog(
                        requireContext(),
                        (view, year, month, dayOfMonth) -> {
                            selectedMonthCalendar.set(Calendar.YEAR, year);
                            selectedMonthCalendar.set(Calendar.MONTH, month);
                            String formatted = new SimpleDateFormat("MMMM yyyy", Locale.US).format(selectedMonthCalendar.getTime());
                            tvSelectedMonth.setText(formatted);
                            applyFilter();
                        },
                        selectedMonthCalendar.get(Calendar.YEAR),
                        selectedMonthCalendar.get(Calendar.MONTH),
                        1
                );
                dialog.show();
            });
        }
    }

    private void setupSearch() {
        if (etSearchBilling != null) {
            etSearchBilling.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchQuery = s != null ? s.toString().trim().toLowerCase() : "";
                    applyFilter();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupSpinners() {
        viewModel.getRoutes().observe(getViewLifecycleOwner(), routes -> {
            List<String> routeNames = new ArrayList<>();
            routeNames.add("All Routes");
            if (routes != null) {
                for (Route r : routes) {
                    routeNames.add(r.getCode() + " - " + r.getName());
                }
            }
            if (getContext() != null) {
                ArrayAdapter<String> routeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, routeNames);
                spBillingRouteFilter.setAdapter(routeAdapter);
            }
        });

        viewModel.getAreas().observe(getViewLifecycleOwner(), areas -> {
            List<String> areaNames = new ArrayList<>();
            areaNames.add("All Areas");
            if (areas != null) {
                for (Area a : areas) {
                    areaNames.add(a.getName());
                }
            }
            if (getContext() != null) {
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, areaNames);
                spBillingAreaFilter.setAdapter(areaAdapter);
            }
        });

        spBillingRouteFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoute = parent.getItemAtPosition(position).toString();
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spBillingAreaFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedArea = parent.getItemAtPosition(position).toString();
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applyFilter() {
        List<BillingRecord> filtered = new ArrayList<>();
        for (BillingRecord br : allBillings) {
            boolean routeMatch = selectedRoute.equals("All Routes") ||
                    (br.getRouteId() != null && selectedRoute.contains(br.getRouteId()));
            boolean areaMatch = selectedArea.equals("All Areas") ||
                    (br.getArea() != null && br.getArea().equalsIgnoreCase(selectedArea));

            boolean searchMatch = searchQuery.isEmpty() ||
                    (br.getCustomerName() != null && br.getCustomerName().toLowerCase().contains(searchQuery)) ||
                    (br.getCustomerId() != null && br.getCustomerId().toLowerCase().contains(searchQuery));

            if (routeMatch && areaMatch && searchMatch) {
                filtered.add(br);
            }
        }
        adapter.setBillings(filtered);
    }

    @Override
    public void onCollectCash(BillingRecord record) {
        onClick(record);
    }

    @Override
    public void onSendUpi(BillingRecord record) {
        Toast.makeText(getContext(), "Bill sent successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewPdf(BillingRecord record) {
        Toast.makeText(getContext(), "Generating PDF bill for " + record.getCustomerName() + "...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewImage(BillingRecord record) {
        Toast.makeText(getContext(), "Exporting Bill Card Image for " + record.getCustomerName() + "...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(BillingRecord record) {
        Intent intent = new Intent(requireContext(), PaymentDetailsActivity.class);
        intent.putExtra(PaymentDetailsActivity.EXTRA_BILLING_ID, record.getId());
        intent.putExtra(PaymentDetailsActivity.EXTRA_CUSTOMER_NAME, record.getCustomerName());
        intent.putExtra(PaymentDetailsActivity.EXTRA_AMOUNT, record.getCurrentMonthAmount());
        intent.putExtra(PaymentDetailsActivity.EXTRA_PREVIOUS_DUE, record.getPreviousBalance());
        intent.putExtra(PaymentDetailsActivity.EXTRA_TOTAL_DUE, record.getTotalDue());
        intent.putExtra(PaymentDetailsActivity.EXTRA_AREA, record.getArea());
        intent.putExtra(PaymentDetailsActivity.EXTRA_ROUTE, record.getRouteId());
        intent.putExtra(PaymentDetailsActivity.EXTRA_MONTH, record.getMonthYear());
        intent.putExtra(PaymentDetailsActivity.EXTRA_IS_PAID, record.isPaid());
        startActivity(intent);
    }
}

