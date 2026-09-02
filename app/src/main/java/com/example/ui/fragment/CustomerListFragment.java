package com.example.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.Customer;
import com.example.ui.activity.AddCustomerActivity;
import com.example.ui.activity.CustomerDetailsActivity;
import com.example.ui.adapter.CustomerAdapter;
import com.example.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CustomerListFragment extends Fragment implements CustomerAdapter.OnCustomerActionListener {

    private MainViewModel viewModel;
    private CustomerAdapter adapter;
    private EditText etSearchCustomer;
    private List<Customer> allCustomers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);

        etSearchCustomer = view.findViewById(R.id.etSearchCustomer);
        RecyclerView rvCustomers = view.findViewById(R.id.rvCustomers);
        View btnNewCustomer = view.findViewById(R.id.btnNewCustomer);
// Adaptor is used  Creted te adaptors
        rvCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomerAdapter(this);
        rvCustomers.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        //
        viewModel.getCustomers().observe(getViewLifecycleOwner(), customers -> {
            allCustomers = customers != null ? customers : new ArrayList<>();
            filterCustomers(etSearchCustomer.getText().toString());
        });

        etSearchCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCustomers(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (btnNewCustomer != null) {
            btnNewCustomer.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddCustomerActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }

    private void filterCustomers(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setCustomers(allCustomers);
            return;
        }
        String q = query.toLowerCase().trim();
        List<Customer> filtered = new ArrayList<>();
//        user can able to  search the customer

        //with the specific char user  Priorities the customer  from top to bottom approch
        for (Customer c : allCustomers) {
            if ((c.getName() != null && c.getName().toLowerCase().contains(q)) ||
                (c.getArea() != null && c.getArea().toLowerCase().contains(q)) ||
                (c.getMobileNumber() != null && c.getMobileNumber().contains(q))) {
                filtered.add(c);
            }
        }
        adapter.setCustomers(filtered);
    }

    @Override
    public void onClick(Customer customer) {
        Intent intent = new Intent(getContext(), CustomerDetailsActivity.class);
        intent.putExtra("EXTRA_CUSTOMER_ID", customer.getId());
        startActivity(intent);
    }

    @Override
    public void onEdit(Customer customer) {
        Intent intent = new Intent(getContext(), AddCustomerActivity.class);
        intent.putExtra("EXTRA_CUSTOMER", customer);
        startActivity(intent);
    }
//aleart box
    @Override
    public void onDelete(Customer customer) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Customer")
                .setMessage("Are you sure you want to delete " + customer.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteCustomer(customer);
                    Toast.makeText(getContext(), "Customer deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
//taking the response from  modle classs object
    @Override
    public void onToggleActive(Customer customer, boolean active) {
        Toast.makeText(getContext(), customer.getName() + " status updated to " + (active ? "Active" : "Inactive"), Toast.LENGTH_SHORT).show();
    }
}
