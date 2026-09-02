package com.example.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.BillingRecord;

import java.util.ArrayList;
import java.util.List;

public class MonthlyBillingAdapter extends RecyclerView.Adapter<MonthlyBillingAdapter.BillingViewHolder> {

    public interface OnBillingActionListener {
        void onCollectCash(BillingRecord record);
        void onSendUpi(BillingRecord record);
        void onViewPdf(BillingRecord record);
        void onViewImage(BillingRecord record);
        void onClick(BillingRecord record);
    }

    private List<BillingRecord> billings = new ArrayList<>();
    private final OnBillingActionListener listener;

    public MonthlyBillingAdapter(OnBillingActionListener listener) {
        this.listener = listener;
    }

    public void setBillings(List<BillingRecord> billings) {
        this.billings = billings != null ? billings : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monthly_billing, parent, false);
        return new BillingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        BillingRecord record = billings.get(position);

        holder.tvBillingCustomerName.setText(record.getCustomerName());
        holder.tvBillingArea.setText(record.getArea() != null ? record.getArea() : "Main Road");
        holder.tvTotalDueAmount.setText(record.getFormattedTotalDue());

        if (record.isPaid()) {
            holder.tvPaymentStatusBadge.setText("• Paid");
            holder.tvPaymentStatusBadge.setBackgroundResource(R.drawable.bg_badge_paid);
            holder.tvPaymentStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_paid_text));
        } else {
            holder.tvPaymentStatusBadge.setText("• Unpaid");
            holder.tvPaymentStatusBadge.setBackgroundResource(R.drawable.bg_badge_unpaid);
            holder.tvPaymentStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.status_unpaid_text));
        }

        holder.btnBillPdf.setOnClickListener(v -> {
            if (listener != null) listener.onViewPdf(record);
        });

        holder.btnBillImage.setOnClickListener(v -> {
            if (listener != null) listener.onViewImage(record);
        });

        holder.btnBillSend.setOnClickListener(v -> {
            if (listener != null) listener.onSendUpi(record);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(record);
        });
    }

    @Override
    public int getItemCount() {
        return billings.size();
    }

    static class BillingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillingCustomerName, tvPaymentStatusBadge, tvBillingArea, tvTotalDueAmount;
        LinearLayout btnBillPdf, btnBillImage, btnBillSend;

        public BillingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillingCustomerName = itemView.findViewById(R.id.tvBillingCustomerName);
            tvPaymentStatusBadge = itemView.findViewById(R.id.tvPaymentStatusBadge);
            tvBillingArea = itemView.findViewById(R.id.tvBillingArea);
            tvTotalDueAmount = itemView.findViewById(R.id.tvTotalDueAmount);
            btnBillPdf = itemView.findViewById(R.id.btnBillPdf);
            btnBillImage = itemView.findViewById(R.id.btnBillImage);
            btnBillSend = itemView.findViewById(R.id.btnBillSend);
        }
    }
}

