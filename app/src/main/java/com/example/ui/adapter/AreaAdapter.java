package com.example.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.Area;

import java.util.ArrayList;
import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    public interface OnAreaActionListener {
        void onEdit(Area area);
        void onDelete(Area area);
        void onViewDetails(Area area);
    }

    private List<Area> areas = new ArrayList<>();
    private final OnAreaActionListener listener;

    public AreaAdapter(OnAreaActionListener listener) {
        this.listener = listener;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas != null ? areas : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.tvAreaTitle.setText(area.getName() + " - " + area.getCode());
        holder.tvAreaZone.setText("Zone: " + (area.getZone() != null ? area.getZone() : "Standard"));
        holder.tvAreaPincode.setText("Pincode: " + area.getPincode() + " | " + area.getCity());

        holder.btnEditArea.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(area);
        });

        holder.btnDeleteArea.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(area);
        });

        holder.btnViewDetails.setOnClickListener(v -> {
            if (listener != null) listener.onViewDetails(area);
        });
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    static class AreaViewHolder extends RecyclerView.ViewHolder {
        TextView tvAreaTitle, tvAreaZone, tvAreaPincode;
        ImageView btnEditArea, btnDeleteArea;
        Button btnViewDetails;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAreaTitle = itemView.findViewById(R.id.tvAreaTitle);
            tvAreaZone = itemView.findViewById(R.id.tvAreaZone);
            tvAreaPincode = itemView.findViewById(R.id.tvAreaPincode);
            btnEditArea = itemView.findViewById(R.id.btnEditArea);
            btnDeleteArea = itemView.findViewById(R.id.btnDeleteArea);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
