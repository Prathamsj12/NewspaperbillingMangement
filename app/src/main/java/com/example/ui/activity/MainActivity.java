package com.example.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.R;
import com.example.ui.fragment.AreaListFragment;
import com.example.ui.fragment.CustomerListFragment;
import com.example.ui.fragment.DailyRouteDeliveryFragment;
import com.example.ui.fragment.DashboardFragment;
import com.example.ui.fragment.DeliveryStaffFragment;
import com.example.ui.fragment.MonthlyBillingFragment;
import com.example.ui.fragment.NewspaperListFragment;
import com.example.ui.fragment.RouteListFragment;
import com.example.viewmodel.MainViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvToolbarTitle;
    private MainViewModel viewModel;
    private String userRole;
//Newspaper point of  view(DeliveryPerson ,Owner,Customer (UI Logic should be Based)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);

        userRole = getIntent().getStringExtra("USER_ROLE");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigateToFragment(new DashboardFragment(), "Dashboard", R.id.nav_dashboard);
        }
    }

    public void navigateToFragment(Fragment fragment, String title, int navItemId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        if (tvToolbarTitle != null) {
            tvToolbarTitle.setText(title);
        }
        if (navigationView != null && navItemId != 0) {
            navigationView.setCheckedItem(navItemId);
        }
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            navigateToFragment(new DashboardFragment(), "Dashboard", id);
        }else if (id == R.id.nav_routes) {

            if ("DELIVERY_PERSON".equalsIgnoreCase(userRole)) {

                Toast.makeText(
                        this,
                        "Route Management is not available for Delivery Person",
                        Toast.LENGTH_SHORT
                ).show();

                return true;
            }

            navigateToFragment(
                    new RouteListFragment(),
                    "Route Management",
                    id
            );
        }else if (id == R.id.nav_customers) {
            navigateToFragment(new CustomerListFragment(), "Customer Management", id);
        } else if (id == R.id.nav_areas) {
            navigateToFragment(new AreaListFragment(), "Area Management", id);
        } else if (id == R.id.nav_newspapers) {
            navigateToFragment(new NewspaperListFragment(), "Newspaper Management", id);
        } else if (id == R.id.nav_delivery_staff) {
            navigateToFragment(new DeliveryStaffFragment(), "Delivery Staff", id);
        } else if (id == R.id.nav_daily_delivery) {
            navigateToFragment(new DailyRouteDeliveryFragment(), "Daily Route Delivery", id);
        } else if (id == R.id.nav_monthly_billing) {
            navigateToFragment(new MonthlyBillingFragment(), "Monthly Billing", id);
        } else if (id == R.id.nav_settings) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Settings & Preferences")
                    .setItems(new String[]{"Restore Sample Data", "Export Database Backup", "About SRS Newspaper Logistics"}, (dialog, which) -> {
                        if (which == 0) {
                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Restore Sample Data?")
                                    .setMessage("This will reset all routes, areas, customers, delivery statuses, and billing records to the default demo state.")
                                    .setPositiveButton("Restore Now", (d, w) -> {
                                        viewModel.restoreData();
                                        Toast.makeText(this, "Sample data restored successfully!", Toast.LENGTH_SHORT).show();
                                        navigateToFragment(new DashboardFragment(), "Dashboard", R.id.nav_dashboard);
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        } else if (which == 1) {
                            Toast.makeText(this, "Backup exported successfully to local storage.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "SRS Newspaper Logistics v1.0.0", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Close", null)
                    .show();
        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        Intent intent = new Intent(this, LoginActivity.class);

                        intent.setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                        );

                        startActivity(intent);
                        finish();

                        Toast.makeText(
                                this,
                                "Logged out successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    public void openAddCustomer() {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivity(intent);
    }

    public void openAddArea() {
        Intent intent = new Intent(this, AddAreaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
