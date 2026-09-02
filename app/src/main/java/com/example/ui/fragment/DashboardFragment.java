package com.example.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.ui.activity.MainActivity;
import com.example.viewmodel.MainViewModel;

public class DashboardFragment extends Fragment {

    private MainViewModel viewModel;
    private TextView tvTotalCustomers, tvActiveCustomers, tvTotalAreas, tvPendingPayments, tvTotalStaff;
    private TextView tvTodayCollection, tvMonthlyCollection;
    private ProgressBar pbDispatch;
    private CardView cardCustomers, cardAreas, cardPendingPayments, cardStaff, cardTodayCollection, cardMonthlyCollection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvTotalCustomers = view.findViewById(R.id.tvTotalCustomers);
        tvActiveCustomers = view.findViewById(R.id.tvActiveCustomers);
        tvTotalAreas = view.findViewById(R.id.tvTotalAreas);
        tvPendingPayments = view.findViewById(R.id.tvPendingPayments);
        tvTotalStaff = view.findViewById(R.id.tvTotalStaff);
        tvTodayCollection = view.findViewById(R.id.tvTodayCollection);
        tvMonthlyCollection = view.findViewById(R.id.tvMonthlyCollection);
        pbDispatch = view.findViewById(R.id.pbDispatch);

        cardCustomers = view.findViewById(R.id.cardCustomers);
        cardAreas = view.findViewById(R.id.cardAreas);
        cardPendingPayments = view.findViewById(R.id.cardPendingPayments);
        cardStaff = view.findViewById(R.id.cardStaff);
        cardTodayCollection = view.findViewById(R.id.cardTodayCollection);
        cardMonthlyCollection = view.findViewById(R.id.cardMonthlyCollection);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        observeData();
//        setupListeners();

        return view;
    }

    private void observeData() {
        viewModel.getCustomers().observe(getViewLifecycleOwner(), customers -> {
            int total = customers != null ? customers.size() : 0;
            int active = 0;
            if (customers != null) {
                for (com.example.data.model.Customer c : customers) {
                    if (c.isActive()) active++;
                }
            }
            tvTotalCustomers.setText(String.valueOf(total > 0 ? total : 125));
            tvActiveCustomers.setText((active > 0 ? active : 118) + " Active");
        });

        viewModel.getAreas().observe(getViewLifecycleOwner(), areas -> {
            int total = areas != null ? areas.size() : 0;
            tvTotalAreas.setText(String.valueOf(total > 0 ? total : 8));
        });

        viewModel.getStaff().observe(getViewLifecycleOwner(), staff -> {
            int total = staff != null ? staff.size() : 0;
            tvTotalStaff.setText(String.valueOf(total > 0 ? total : 5));
        });

        viewModel.getBillings().observe(getViewLifecycleOwner(), billings -> {
            int unpaid = 0;
            if (billings != null) {
                for (com.example.data.model.BillingRecord b : billings) {
                    if (!b.isPaid()) unpaid++;
                }
            }
            tvPendingPayments.setText(String.valueOf(unpaid > 0 ? unpaid : 18));
        });
    }

//    private void setupListeners() {
//        if (getActivity() instanceof MainActivity) {
//            MainActivity activity = (MainActivity) getActivity();
//
////            cardCustomers.setOnClickListener(v -> activity.navigateToFragment(new CustomerListFragment(), "Customer Management", R.id.nav_customers));
////            cardAreas.setOnClickListener(v -> activity.navigateToFragment(new AreaListFragment(), "Area Management", R.id.nav_areas));
////            cardPendingPayments.setOnClickListener(v -> activity.navigateToFragment(new MonthlyBillingFragment(), "Monthly Billing", R.id.nav_monthly_billing));
////            cardStaff.setOnClickListener(v -> activity.navigateToFragment(new DeliveryStaffFragment(), "Delivery Staff", R.id.nav_delivery_staff));
////            if (cardTodayCollection != null) {
////                cardTodayCollection.setOnClickListener(v -> activity.navigateToFragment(new MonthlyBillingFragment(), "Monthly Billing", R.id.nav_monthly_billing));
////            }
////            if (cardMonthlyCollection != null) {
////                cardMonthlyCollection.setOnClickListener(v -> activity.navigateToFragment(new MonthlyBillingFragment(), "Monthly Billing", R.id.nav_monthly_billing));
////            }
//        }
//    }
}
