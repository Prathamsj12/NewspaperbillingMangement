package com.example.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.data.model.DeliveryRecord;
import com.example.data.model.Route;
import com.example.ui.activity.DeliveryDetailsActivity;
import com.example.ui.activity.MainActivity;
import com.example.ui.adapter.DailyDeliveryAdapter;
import com.example.viewmodel.MainViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

//Daily Updated  need to less time compareitivily
//route and number of person is  caontyact Details
//User can able  to updated te daywise  delivery data
public class DailyRouteDeliveryFragment extends Fragment implements DailyDeliveryAdapter.OnDeliveryActionListener {

    private MainViewModel viewModel;
    private DailyDeliveryAdapter adapter;
    private Spinner spRouteFilter, spAreaFilter;
    private TextView tvTotalDeliveredMetric, tvPendingMetric;
    private EditText etSearchDelivery;
    private BottomNavigationView bottomNavDelivery;
    private List<DeliveryRecord> allDeliveries = new ArrayList<>();
    private String selectedRoute = "All Routes";
    private String selectedArea = "All Areas";
    private String searchQuery = "";
    private LinearLayout[] dateChips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_delivery, container, false);

        spRouteFilter = view.findViewById(R.id.spRouteFilter);
        spAreaFilter = view.findViewById(R.id.spAreaFilter);
        tvTotalDeliveredMetric = view.findViewById(R.id.tvTotalDeliveredMetric);
        tvPendingMetric = view.findViewById(R.id.tvPendingMetric);
        etSearchDelivery = view.findViewById(R.id.etSearchDelivery);
        bottomNavDelivery = view.findViewById(R.id.bottomNavDelivery);
        RecyclerView rvDeliveries = view.findViewById(R.id.rvDailyDeliveries);

        rvDeliveries.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DailyDeliveryAdapter(this);
        rvDeliveries.setAdapter(adapter);

        setupDateChips(view);
        setupSearch();
        setupBottomNav();

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        viewModel.getDeliveries().observe(getViewLifecycleOwner(), deliveries -> {
            allDeliveries = deliveries != null ? deliveries : new ArrayList<>();
            applyFilter();
        });

        setupSpinners();

        return view;
    }

    private void setupDateChips(View view) {
        dateChips = new LinearLayout[]{
                view.findViewById(R.id.dateChip1),
                view.findViewById(R.id.dateChip2),
                view.findViewById(R.id.dateChip3),
                view.findViewById(R.id.dateChip4),
                view.findViewById(R.id.dateChip5)
        };

        for (int i = 0; i < dateChips.length; i++) {
            final int index = i;
            if (dateChips[i] != null) {
                dateChips[i].setOnClickListener(v -> selectDateChip(index));
            }
        }
    }

    private void selectDateChip(int selectedIndex) {
        for (int i = 0; i < dateChips.length; i++) {
            if (dateChips[i] != null) {
                if (i == selectedIndex) {
                    dateChips[i].setBackgroundResource(R.drawable.bg_date_selected);
                    for (int j = 0; j < dateChips[i].getChildCount(); j++) {
                        View child = dateChips[i].getChildAt(j);
                        if (child instanceof TextView) {
                            ((TextView) child).setTextColor(android.graphics.Color.WHITE);
                        }
                    }
                } else {
                    dateChips[i].setBackgroundResource(R.drawable.bg_date_unselected);
                    if (dateChips[i].getChildCount() >= 2) {
                        View c1 = dateChips[i].getChildAt(0);
                        View c2 = dateChips[i].getChildAt(1);
                        if (c1 instanceof TextView) ((TextView) c1).setTextColor(android.graphics.Color.parseColor("#94A3B8"));
                        if (c2 instanceof TextView) ((TextView) c2).setTextColor(android.graphics.Color.parseColor("#0F172A"));
                    }
                }
            }
        }
        applyFilter();
    }

    private void setupSearch() {
        if (etSearchDelivery != null) {
            etSearchDelivery.addTextChangedListener(new TextWatcher() {
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

    private void setupBottomNav() {
        if (bottomNavDelivery != null) {
            bottomNavDelivery.setSelectedItemId(R.id.bottom_deliveries);
            bottomNavDelivery.setOnItemSelectedListener(item -> {
                if (!(getActivity() instanceof MainActivity)) return false;
                MainActivity main = (MainActivity) getActivity();
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_deliveries) {
                    return true;
                } else if (itemId == R.id.bottom_customers) {
                    main.navigateToFragment(new CustomerListFragment(), "Customer Management", R.id.nav_customers);
                    return true;
                } else if (itemId == R.id.bottom_areas) {
                    main.navigateToFragment(new AreaListFragment(), "Area Management", R.id.nav_areas);
                    return true;
                } else if (itemId == R.id.bottom_billing) {
                    main.navigateToFragment(new MonthlyBillingFragment(), "Monthly Billing", R.id.nav_monthly_billing);
                    return true;
                }
                return false;
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
                spRouteFilter.setAdapter(routeAdapter);
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
                spAreaFilter.setAdapter(areaAdapter);
            }
        });

        spRouteFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoute = parent.getItemAtPosition(position).toString();
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spAreaFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        List<DeliveryRecord> filtered = new ArrayList<>();
        int deliveredCount = 0;
        int totalCount = 0;

        for (DeliveryRecord dr : allDeliveries) {
            boolean routeMatch = selectedRoute.equals("All Routes") ||
                    (dr.getRouteId() != null && selectedRoute.contains(dr.getRouteId()));
            boolean areaMatch = selectedArea.equals("All Areas") ||
                    (dr.getArea() != null && dr.getArea().equalsIgnoreCase(selectedArea));

            boolean searchMatch = searchQuery.isEmpty() ||
                    (dr.getCustomerName() != null && dr.getCustomerName().toLowerCase().contains(searchQuery)) ||
                    (dr.getAddress() != null && dr.getAddress().toLowerCase().contains(searchQuery));

            if (routeMatch && areaMatch && searchMatch) {
                filtered.add(dr);
                totalCount++;
                if (dr.isDelivered()) {
                    deliveredCount++;
                }
            }
        }

        int pendingCount = totalCount - deliveredCount;
        if (tvTotalDeliveredMetric != null) {
            tvTotalDeliveredMetric.setText(deliveredCount + " / " + totalCount);
        }
        if (tvPendingMetric != null) {
            tvPendingMetric.setText(String.valueOf(pendingCount));
        }

        adapter.setRecords(filtered);
    }

    @Override
    public void onStatusChanged(DeliveryRecord record, boolean delivered) {
        viewModel.updateDeliveryStatus(record.getId(), delivered);
        applyFilter();
        Toast.makeText(getContext(), (delivered ? "Delivered to " : "Marked pending: ") + record.getCustomerName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(DeliveryRecord record) {
        Intent intent = new Intent(requireContext(), DeliveryDetailsActivity.class);
        intent.putExtra(DeliveryDetailsActivity.EXTRA_DELIVERY_ID, record.getId());
        intent.putExtra(DeliveryDetailsActivity.EXTRA_CUSTOMER_NAME, record.getCustomerName());
        intent.putExtra(DeliveryDetailsActivity.EXTRA_ADDRESS, record.getAddress());
        intent.putExtra(DeliveryDetailsActivity.EXTRA_PHONE, record.getPhone());
        intent.putExtra(DeliveryDetailsActivity.EXTRA_AREA, record.getArea());
        intent.putExtra(DeliveryDetailsActivity.EXTRA_ROUTE, record.getRouteId());
        intent.putExtra(DeliveryDetailsActivity.EXTRA_IS_DELIVERED, record.isDelivered());
        startActivity(intent);
    }
}

