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
import com.example.data.model.Area;
import com.example.ui.activity.AddAreaActivity;
import com.example.ui.adapter.AreaAdapter;
import com.example.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AreaListFragment extends Fragment implements AreaAdapter.OnAreaActionListener {

    private MainViewModel viewModel;
    private AreaAdapter adapter;
    private EditText etSearchArea;
    private List<Area> allAreas = new ArrayList<>();

    @Nullable
    @Override
//Application development life cycle/
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area_list, container, false);

        etSearchArea = view.findViewById(R.id.etSearchArea);
        RecyclerView rvAreas = view.findViewById(R.id.rvAreas);
        ExtendedFloatingActionButton fabAddArea = view.findViewById(R.id.fabAddArea);

        rvAreas.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AreaAdapter(this);
        rvAreas.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getAreas().observe(getViewLifecycleOwner(), areas -> {
            allAreas = areas != null ? areas : new ArrayList<>();
            filterAreas(etSearchArea.getText().toString());
        });

        etSearchArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAreas(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddArea.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddAreaActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void filterAreas(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setAreas(allAreas);
            return;
        }
        String q = query.toLowerCase().trim();
        List<Area> filtered = new ArrayList<>();
        for (Area a : allAreas) {
            if ((a.getName() != null && a.getName().toLowerCase().contains(q)) ||
                (a.getCode() != null && a.getCode().toLowerCase().contains(q)) ||
                (a.getZone() != null && a.getZone().toLowerCase().contains(q))) {
                filtered.add(a);
            }
        }
        adapter.setAreas(filtered);
    }

    @Override
    public void onEdit(Area area) {
        Intent intent = new Intent(getContext(), AddAreaActivity.class);
        intent.putExtra("EXTRA_AREA", area);
        startActivity(intent);
    }

    @Override
    public void onDelete(Area area) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Area")
                .setMessage("Are you sure you want to delete " + area.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteArea(area);
                    Toast.makeText(getContext(), "Area deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onViewDetails(Area area) {
        new AlertDialog.Builder(requireContext())
                .setTitle(area.getName() + " (" + area.getCode() + ")")
                .setMessage("Zone: " + area.getZone() + "\nCity: " + area.getCity() + "\nPincode: " + area.getPincode() + "\nLandmark: " + (area.getLandmark() != null ? area.getLandmark() : "None"))
                .setPositiveButton("OK", null)
                .show();
    }
}
