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
import com.example.data.model.Route;
import com.example.ui.activity.AddRouteActivity;
import com.example.ui.adapter.RouteAdapter;
import com.example.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RouteListFragment extends Fragment implements RouteAdapter.OnRouteActionListener {

    private MainViewModel viewModel;
    private RouteAdapter adapter;
    private EditText etSearchRoute;
    private List<Route> allRoutes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_list, container, false);

        etSearchRoute = view.findViewById(R.id.etSearchRoute);
        RecyclerView rvRoutes = view.findViewById(R.id.rvRoutes);
        View btnNewRoute = view.findViewById(R.id.btnNewRoute);
        android.widget.TextView tvTotalRoutesCount = view.findViewById(R.id.tvTotalRoutesCount);
        android.widget.TextView tvActiveHawkersCount = view.findViewById(R.id.tvActiveHawkersCount);

        rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RouteAdapter(this);
        rvRoutes.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getRoutes().observe(getViewLifecycleOwner(), routes -> {
            allRoutes = routes != null ? routes : new ArrayList<>();
            if (tvTotalRoutesCount != null) {
                tvTotalRoutesCount.setText(String.valueOf(allRoutes.size()));
            }
            if (tvActiveHawkersCount != null) {
                int activeHawkers = 0;
                for (Route r : allRoutes) {
                    if (r.isActive() && r.getHawkerName() != null && !r.getHawkerName().equals("Unassigned")) {
                        activeHawkers++;
                    }
                }
                tvActiveHawkersCount.setText(String.valueOf(activeHawkers > 0 ? activeHawkers : allRoutes.size()));
            }
            filterRoutes(etSearchRoute.getText().toString());
        });

        etSearchRoute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRoutes(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (btnNewRoute != null) {
            btnNewRoute.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddRouteActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }

    private void filterRoutes(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setRoutes(allRoutes);
            return;
        }
        String q = query.toLowerCase().trim();
        List<Route> filtered = new ArrayList<>();
        for (Route r : allRoutes) {
            if ((r.getName() != null && r.getName().toLowerCase().contains(q)) ||
                (r.getCode() != null && r.getCode().toLowerCase().contains(q)) ||
                (r.getHawkerName() != null && r.getHawkerName().toLowerCase().contains(q)) ||
                (r.getAreasJoined() != null && r.getAreasJoined().toLowerCase().contains(q))) {
                filtered.add(r);
            }
        }
        adapter.setRoutes(filtered);
    }

    @Override
    public void onEdit(Route route) {
        Intent intent = new Intent(getContext(), AddRouteActivity.class);
        intent.putExtra("EXTRA_ROUTE", route);
        startActivity(intent);
    }

    @Override
    public void onDelete(Route route) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Route")
                .setMessage("Are you sure you want to delete " + route.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteRoute(route);
                    Toast.makeText(getContext(), "Route deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
