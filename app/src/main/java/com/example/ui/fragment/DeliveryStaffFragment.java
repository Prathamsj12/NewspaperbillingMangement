package com.example.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.DeliveryStaff;
import com.example.ui.activity.AddDeliveryStaffActivity;
import com.example.ui.adapter.DeliveryStaffAdapter;
import com.example.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeliveryStaffFragment extends Fragment implements DeliveryStaffAdapter.OnStaffActionListener {

    private MainViewModel viewModel;
    private DeliveryStaffAdapter adapter;
    private EditText etSearchStaff;
    private TextView tvTotalStaffCount, tvActiveStaffCount;
    private List<DeliveryStaff> allStaff = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_staff, container, false);

        etSearchStaff = view.findViewById(R.id.etSearchStaff);
        RecyclerView rvStaff = view.findViewById(R.id.rvStaff);
        View btnNewStaff = view.findViewById(R.id.btnNewStaff);
        tvTotalStaffCount = view.findViewById(R.id.tvTotalStaffCount);
        tvActiveStaffCount = view.findViewById(R.id.tvActiveStaffCount);

        rvStaff.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DeliveryStaffAdapter(this);
        rvStaff.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getStaff().observe(getViewLifecycleOwner(), staff -> {
            allStaff = staff != null ? staff : new ArrayList<>();
            if (tvTotalStaffCount != null) {
                tvTotalStaffCount.setText(String.valueOf(allStaff.size()));
            }
            if (tvActiveStaffCount != null) {
                int activeCount = 0;
                for (DeliveryStaff s : allStaff) {
                    if (s.isActive()) activeCount++;
                }
                tvActiveStaffCount.setText(String.valueOf(activeCount > 0 ? activeCount : allStaff.size()));
            }
            filterStaff(etSearchStaff.getText().toString());
        });

        etSearchStaff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStaff(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (btnNewStaff != null) {
            btnNewStaff.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddDeliveryStaffActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }

    private void filterStaff(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setStaffList(allStaff);
            return;
        }
        String q = query.toLowerCase().trim();
        List<DeliveryStaff> filtered = new ArrayList<>();
        for (DeliveryStaff s : allStaff) {
            if ((s.getFullName() != null && s.getFullName().toLowerCase().contains(q)) ||
                (s.getStaffCode() != null && s.getStaffCode().toLowerCase().contains(q)) ||
                (s.getMobileNumber() != null && s.getMobileNumber().contains(q))) {
                filtered.add(s);
            }
        }
        adapter.setStaffList(filtered);
    }

    @Override
    public void onEdit(DeliveryStaff staff) {
        Intent intent = new Intent(getContext(), AddDeliveryStaffActivity.class);
        intent.putExtra("EXTRA_STAFF", staff);
        startActivity(intent);
    }

    @Override
    public void onDelete(DeliveryStaff staff) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Staff Member")
                .setMessage("Are you sure you want to delete " + staff.getFullName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteStaff(staff);
                    Toast.makeText(getContext(), "Staff member deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onView(DeliveryStaff staff) {
        new AlertDialog.Builder(requireContext())
                .setTitle(staff.getFullName() + " (" + staff.getStaffCode() + ")")
                .setMessage("Phone: " + staff.getMobileNumber() + "\n" +
                        "Address: " + staff.getResidentialAddress() + "\n" +
                        "Vehicle: " + staff.getVehicleDetails() + "\n" +
                        "Status: " + (staff.isActive() ? "Active On Duty" : "Inactive") + "\n\n" +
                        "Note: Area and Route are assigned in Route Management.")
                .setPositiveButton("Close", null)
                .show();
    }
}
