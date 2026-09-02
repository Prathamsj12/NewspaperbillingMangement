package com.example.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.DeliveryStaff;

import java.util.ArrayList;
import java.util.List;

public class DeliveryStaffAdapter extends RecyclerView.Adapter<DeliveryStaffAdapter.StaffViewHolder> {

    public interface OnStaffActionListener {
        void onEdit(DeliveryStaff staff);
        void onDelete(DeliveryStaff staff);
        void onView(DeliveryStaff staff);
    }

    private List<DeliveryStaff> staffList = new ArrayList<>();
    private final OnStaffActionListener listener;

    public DeliveryStaffAdapter(OnStaffActionListener listener) {
        this.listener = listener;
    }

    public void setStaffList(List<DeliveryStaff> staffList) {
        this.staffList = staffList != null ? staffList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_staff, parent, false);
        return new StaffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        DeliveryStaff staff = staffList.get(position);
        holder.tvStaffName.setText(staff.getFullName());
        holder.tvStaffCode.setText(staff.getStaffCode() != null ? staff.getStaffCode() : "DS-100");
        holder.tvStaffInitials.setText(staff.getInitials());
        holder.tvStaffPhone.setText(staff.getMobileNumber() != null ? staff.getMobileNumber() : "No Phone");
        holder.tvStaffAssignment.setText(staff.getAssignmentText());

        holder.btnViewStaff.setOnClickListener(v -> {
            if (listener != null) listener.onView(staff);
        });

        holder.btnEditStaff.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(staff);
        });

        holder.btnDeleteStaff.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(staff);
        });
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {
        TextView tvStaffInitials, tvStaffName, tvStaffCode, tvStaffPhone, tvStaffAssignment;
        ImageView btnViewStaff, btnEditStaff, btnDeleteStaff;

        public StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStaffInitials = itemView.findViewById(R.id.tvStaffInitials);
            tvStaffName = itemView.findViewById(R.id.tvStaffName);
            tvStaffCode = itemView.findViewById(R.id.tvStaffCode);
            tvStaffPhone = itemView.findViewById(R.id.tvStaffPhone);
            tvStaffAssignment = itemView.findViewById(R.id.tvStaffAssignment);
            btnViewStaff = itemView.findViewById(R.id.btnViewStaff);
            btnEditStaff = itemView.findViewById(R.id.btnEditStaff);
            btnDeleteStaff = itemView.findViewById(R.id.btnDeleteStaff);
        }
    }
}
