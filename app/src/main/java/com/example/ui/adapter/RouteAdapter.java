package com.example.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.data.model.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    public interface OnRouteActionListener {
        void onEdit(Route route);
        void onDelete(Route route);
    }

    private List<Route> routes = new ArrayList<>();
    private final OnRouteActionListener listener;

    public RouteAdapter(OnRouteActionListener listener) {
        this.listener = listener;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes != null ? routes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Route route = routes.get(position);

        holder.tvRouteTitle.setText(route.getFormattedTitle());
        holder.tvHawkerName.setText(route.getHawkerName() != null ? route.getHawkerName() : "Unassigned");
        holder.tvAreasIncluded.setText(route.getAreasJoined());

        if (route.isActive()) {
            holder.tvRouteBadge.setText("ACTIVE");
            holder.tvRouteBadge.setBackgroundResource(R.drawable.bg_badge_active);
            holder.tvRouteBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_active_text));
        } else {
            holder.tvRouteBadge.setText("INACTIVE");
            holder.tvRouteBadge.setBackgroundResource(R.drawable.bg_badge_inactive);
            holder.tvRouteBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_inactive_text));
        }

        holder.btnCallHawker.setOnClickListener(v -> {
            Toast.makeText(context, "Calling " + route.getHawkerName() + "...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9999999999"));
            context.startActivity(intent);
        });

        holder.btnEditRoute.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(route);
        });

        holder.btnDeleteRoute.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(route);
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteTitle, tvRouteBadge, tvHawkerName, tvAreasIncluded;
        ImageView btnCallHawker, btnEditRoute, btnDeleteRoute;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteTitle = itemView.findViewById(R.id.tvRouteTitle);
            tvRouteBadge = itemView.findViewById(R.id.tvRouteBadge);
            tvHawkerName = itemView.findViewById(R.id.tvHawkerName);
            tvAreasIncluded = itemView.findViewById(R.id.tvAreasIncluded);
            btnCallHawker = itemView.findViewById(R.id.btnCallHawker);
            btnEditRoute = itemView.findViewById(R.id.btnEditRoute);
            btnDeleteRoute = itemView.findViewById(R.id.btnDeleteRoute);
        }
    }
}
