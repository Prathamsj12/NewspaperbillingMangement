package com.example.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    public interface OnCustomerActionListener {
        void onClick(Customer customer);
        void onEdit(Customer customer);
        void onDelete(Customer customer);
        void onToggleActive(Customer customer, boolean active);
    }

    private List<Customer> customers = new ArrayList<>();//
    private final OnCustomerActionListener listener;

    public CustomerAdapter(OnCustomerActionListener listener) {
        this.listener = listener;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers != null ? customers : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customers.get(position);

        holder.tvCustomerName.setText(customer.getName());
        holder.tvCustomerType.setText(customer.getCustomerType() != null ? customer.getCustomerType() : "RESIDENTIAL");
        if ("COMMERCIAL".equalsIgnoreCase(customer.getCustomerType())) {
            holder.tvCustomerType.setBackgroundResource(R.drawable.bg_badge_commercial);
            holder.tvCustomerType.setTextColor(Color.parseColor("#6B21A8"));
        } else {
            holder.tvCustomerType.setBackgroundResource(R.drawable.bg_badge_residential);
            holder.tvCustomerType.setTextColor(Color.parseColor("#3730A3"));
        }

        holder.tvCustomerPhone.setText(customer.getMobileNumber() != null ? customer.getMobileNumber() : "No Phone");
        holder.tvCustomerArea.setText(customer.getArea() != null ? customer.getArea() : "Unassigned");
        holder.tvCustomerDeliveryDays.setText("Delivery: " + (customer.getDeliveryDays() != null ? customer.getDeliveryDays() : "Mon-Sun"));
        holder.tvCustomerBilling.setText("Billing: " + (customer.getBillingCycle() != null ? customer.getBillingCycle() : "Monthly Fixed"));

        holder.swCustomerActive.setOnCheckedChangeListener(null);
        holder.swCustomerActive.setChecked(customer.isActive());
        holder.swCustomerActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            customer.setActive(isChecked);
            if (listener != null) listener.onToggleActive(customer, isChecked);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(customer);
        });

        holder.btnEditCustomer.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(customer);
        });

        holder.btnDeleteCustomer.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(customer);
        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvCustomerType, tvCustomerPhone, tvCustomerArea, tvCustomerDeliveryDays, tvCustomerBilling;
        SwitchCompat swCustomerActive;
        Button btnEditCustomer, btnDeleteCustomer;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerType = itemView.findViewById(R.id.tvCustomerType);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvCustomerArea = itemView.findViewById(R.id.tvCustomerArea);
            tvCustomerDeliveryDays = itemView.findViewById(R.id.tvCustomerDeliveryDays);
            tvCustomerBilling = itemView.findViewById(R.id.tvCustomerBilling);
            swCustomerActive = itemView.findViewById(R.id.swCustomerActive);
            btnEditCustomer = itemView.findViewById(R.id.btnEditCustomer);
            btnDeleteCustomer = itemView.findViewById(R.id.btnDeleteCustomer);
        }
    }
}
