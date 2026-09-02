package com.example.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.DeliveryRecord;

import java.util.ArrayList;
import java.util.List;

public class DailyDeliveryAdapter extends RecyclerView.Adapter<DailyDeliveryAdapter.DeliveryViewHolder> {

    public interface OnDeliveryActionListener {
        void onStatusChanged(DeliveryRecord record, boolean delivered);
        void onClick(DeliveryRecord record);
    }

    private List<DeliveryRecord> records = new ArrayList<>();
    private final OnDeliveryActionListener listener;

    public DailyDeliveryAdapter(OnDeliveryActionListener listener) {
        this.listener = listener;
    }

    public void setRecords(List<DeliveryRecord> records) {
        this.records = records != null ? records : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_delivery, parent, false);
        return new DeliveryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        DeliveryRecord record = records.get(position);

        holder.tvDeliveryCustomerName.setText(record.getCustomerName());
        holder.tvDeliveryAddress.setText(record.getAddress());

        if (record.isDelivered()) {
            holder.btnDeliveredYes.setBackgroundResource(R.drawable.bg_navy_button);
            holder.btnDeliveredYes.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btnDeliveredNo.setBackgroundResource(android.R.color.transparent);
            holder.btnDeliveredNo.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        } else {
            holder.btnDeliveredNo.setBackgroundResource(R.drawable.bg_navy_button);
            holder.btnDeliveredNo.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.btnDeliveredYes.setBackgroundResource(android.R.color.transparent);
            holder.btnDeliveredYes.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
        }

        holder.btnDeliveredYes.setOnClickListener(v -> {
            record.setDelivered(true);
            notifyItemChanged(holder.getAdapterPosition());
            if (listener != null) listener.onStatusChanged(record, true);
        });

        holder.btnDeliveredNo.setOnClickListener(v -> {
            record.setDelivered(false);
            notifyItemChanged(holder.getAdapterPosition());
            if (listener != null) listener.onStatusChanged(record, false);
        });

        holder.btnCallCustomer.setOnClickListener(v -> {
            String phone = record.getPhone() != null ? record.getPhone() : "9999999999";
            Toast.makeText(context, "Calling " + record.getCustomerName() + " (" + phone + ")", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
        });

        holder.btnDirectionsCustomer.setOnClickListener(v -> {
            Toast.makeText(context, "Opening Navigation to: " + record.getAddress(), Toast.LENGTH_SHORT).show();
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(record.getAddress())));
            context.startActivity(mapIntent);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(record);
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeliveryCustomerName, tvDeliveryAddress;
        ImageView btnCallCustomer, btnDirectionsCustomer;
        Button btnDeliveredNo, btnDeliveredYes;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDeliveryCustomerName = itemView.findViewById(R.id.tvDeliveryCustomerName);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            btnCallCustomer = itemView.findViewById(R.id.btnCallCustomer);
            btnDirectionsCustomer = itemView.findViewById(R.id.btnDirectionsCustomer);
            btnDeliveredNo = itemView.findViewById(R.id.btnDeliveredNo);
            btnDeliveredYes = itemView.findViewById(R.id.btnDeliveredYes);
        }
    }
}
