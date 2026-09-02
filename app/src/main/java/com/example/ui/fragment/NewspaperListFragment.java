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
import com.example.data.model.Newspaper;
import com.example.ui.activity.AddNewspaperActivity;
import com.example.ui.adapter.NewspaperAdapter;
import com.example.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NewspaperListFragment extends Fragment implements NewspaperAdapter.OnNewspaperActionListener {

    private MainViewModel viewModel;
    private NewspaperAdapter adapter;
    private EditText etSearchNewspaper;
    private List<Newspaper> allNewspapers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newspaper_list, container, false);

        etSearchNewspaper = view.findViewById(R.id.etSearchNewspaper);
        RecyclerView rvNewspapers = view.findViewById(R.id.rvNewspapers);
        ExtendedFloatingActionButton fabAddNewspaper = view.findViewById(R.id.fabAddNewspaper);

        rvNewspapers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewspaperAdapter(this);
        rvNewspapers.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.getNewspapers().observe(getViewLifecycleOwner(), newspapers -> {
            allNewspapers = newspapers != null ? newspapers : new ArrayList<>();
            filterNewspapers(etSearchNewspaper.getText().toString());
        });

        etSearchNewspaper.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNewspapers(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddNewspaper.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddNewspaperActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void filterNewspapers(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setNewspapers(allNewspapers);
            return;
        }
        String q = query.toLowerCase().trim();
        List<Newspaper> filtered = new ArrayList<>();
        for (Newspaper n : allNewspapers) {
            if ((n.getName() != null && n.getName().toLowerCase().contains(q)) ||
                (n.getCode() != null && n.getCode().toLowerCase().contains(q)) ||
                (n.getLanguage() != null && n.getLanguage().toLowerCase().contains(q))) {
                filtered.add(n);
            }
        }
        adapter.setNewspapers(filtered);
    }

    @Override
    public void onEdit(Newspaper newspaper) {
        Intent intent = new Intent(getContext(), AddNewspaperActivity.class);
        intent.putExtra("EXTRA_NEWSPAPER", newspaper);
        startActivity(intent);
    }

    @Override
    public void onDelete(Newspaper newspaper) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Newspaper")
                .setMessage("Are you sure you want to delete " + newspaper.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteNewspaper(newspaper);
                    Toast.makeText(getContext(), "Newspaper deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
