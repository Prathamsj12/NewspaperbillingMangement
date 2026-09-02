package com.example.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.data.model.Area;
import com.example.data.model.BillingRecord;
import com.example.data.model.Customer;
import com.example.data.model.DeliveryRecord;
import com.example.data.model.DeliveryStaff;
import com.example.data.model.Newspaper;
import com.example.data.model.Route;
import com.example.data.repository.AppRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
    private final AppRepository repository;

    public MainViewModel() {
        this.repository = AppRepository.getInstance();
    }

    public LiveData<List<Area>> getAreas() { return repository.getAreas(); }
    public void addArea(Area area) { repository.addArea(area); }
    public void deleteArea(Area area) { repository.deleteArea(area); }

    public LiveData<List<Route>> getRoutes() { return repository.getRoutes(); }
    public void addRoute(Route route) { repository.addRoute(route); }
    public void deleteRoute(Route route) { repository.deleteRoute(route); }

    public LiveData<List<DeliveryStaff>> getStaff() { return repository.getStaff(); }
    public void addStaff(DeliveryStaff staff) { repository.addStaff(staff); }
    public void deleteStaff(DeliveryStaff staff) { repository.deleteStaff(staff); }

    public LiveData<List<Newspaper>> getNewspapers() { return repository.getNewspapers(); }
    public void addNewspaper(Newspaper newspaper) { repository.addNewspaper(newspaper); }
    public void deleteNewspaper(Newspaper newspaper) { repository.deleteNewspaper(newspaper); }

    public LiveData<List<Customer>> getCustomers() { return repository.getCustomers(); }
    public void addCustomer(Customer customer) { repository.addCustomer(customer); }
    public void deleteCustomer(Customer customer) { repository.deleteCustomer(customer); }
    public Customer getCustomerById(String id) { return repository.getCustomerById(id); }

    public LiveData<List<DeliveryRecord>> getDeliveries() { return repository.getDeliveries();
    }
    public void updateDeliveryStatus(String id, boolean delivered) { repository.updateDeliveryStatus(id, delivered);
    }

    public LiveData<List<BillingRecord>> getBillings() { return repository.getBillings();
    }
    public void recordPayment(String billingId, double amount, String date, boolean isFull) {
        repository.recordPayment(billingId, amount, date, isFull);
    }
    public BillingRecord getBillingById(String id) { return repository.getBillingById(id); }
    public void restoreData() { repository.restoreInitialData(); }
}
