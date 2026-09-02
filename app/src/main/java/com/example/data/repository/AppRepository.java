package com.example.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.data.model.Area;
import com.example.data.model.BillingRecord;
import com.example.data.model.Customer;
import com.example.data.model.DeliveryRecord;
import com.example.data.model.DeliveryStaff;
import com.example.data.model.Newspaper;
import com.example.data.model.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppRepository {
    private static AppRepository instance;

    private final MutableLiveData<List<Area>> areasLiveData = new MutableLiveData<>(new ArrayList<>());//mutable data is used to store  the data in the  when data recevied from the that time data is store in the
    private final MutableLiveData<List<Route>> routesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<DeliveryStaff>> staffLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Newspaper>> newspapersLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Customer>> customersLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<DeliveryRecord>> deliveriesLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<BillingRecord>> billingsLiveData = new MutableLiveData<>(new ArrayList<>());

    private final List<Area> areas = new ArrayList<>();
    private final List<Route> routes = new ArrayList<>();
    private final List<DeliveryStaff> staff = new ArrayList<>();
    private final List<Newspaper> newspapers = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<DeliveryRecord> deliveries = new ArrayList<>();
    private final List<BillingRecord> billings = new ArrayList<>();

    public static synchronized AppRepository getInstance() {
        if (instance == null) {
            instance = new AppRepository();
        }
        return instance;
    }

    private AppRepository() {
        populateInitialData();
    }

    private void populateInitialData() {
        // Areas
        areas.add(new Area("area_1", "VN-SEC1", "Viman Nagar - Sector 1", "Morning East", "Pune", "411014", "Near Symbiosis College"));
        areas.add(new Area("area_2", "EST2", "Kalyani Nagar - EST2", "Riverside North", "Pune", "411006", "Near Bishop School"));
        areas.add(new Area("area_3", "SEC3", "Koregaon Park - SEC3", "Central Loop", "Pune", "411001", "Lane 7 North Main"));
        areas.add(new Area("area_4", "SEC4", "Baner - High Street", "West Zone", "Pune", "411045", "Opposite Pancard Club"));
        areas.add(new Area("area_5", "SEC5", "Kothrud - DP Road", "South West", "Pune", "411038", "Near Karve Statue"));
        areas.add(new Area("area_6", "SEC6", "Hinjewadi - Phase 1", "IT Corridor", "Pune", "411057", "Near Wipro Circle"));
        areas.add(new Area("area_7", "SEC7", "Camp - MG Road", "Cantonment", "Pune", "411001", "Near Aurora Towers"));
        areas.add(new Area("area_8", "SEC8", "Shivajinagar - FC Road", "Central Deccan", "Pune", "411005", "Near Fergusson College"));
        areasLiveData.setValue(new ArrayList<>(areas));

        // Delivery Staff
        staff.add(new DeliveryStaff("staff_1", "Ramesh", "K", "Patil", "DS-101", "ramesh.patil@srsnews.com", "9999999999", "1992-05-14", "Male", "Higher Secondary", "B", "4A"));
        staff.add(new DeliveryStaff("staff_2", "Sunil", "M", "Kumar", "DS-102", "sunil.kumar@srsnews.com", "9999999999", "1994-08-22", "Male", "Graduate", "A", "1C"));
        staff.add(new DeliveryStaff("staff_3", "Amit", "", "Desai", "DS-103", "amit.desai@srsnews.com", "9999999999", "1990-11-03", "Male", "Secondary", "", ""));
        staff.add(new DeliveryStaff("staff_4", "James", "", "Wilson", "DS-104", "james.wilson@srsnews.com", "9999999999", "1988-02-18", "Male", "Higher Secondary", "East", "RT-01"));
        staff.add(new DeliveryStaff("staff_5", "Sarah", "", "Jenkins", "DS-105", "sarah.j@srsnews.com", "9999999999", "1995-09-12", "Female", "Graduate", "Central", "RT-02"));
        staffLiveData.setValue(new ArrayList<>(staff));

        // Routes
        routes.add(new Route("rt_1", "RT-01", "Morning East Route", "staff_4", "James Wilson", "Viman Nagar Depot, Pune",
                Arrays.asList("VN-SEC1", "EST2"), Arrays.asList("Oakridge", "Pine Valley", "Elm Street"), true));
        routes.add(new Route("rt_2", "RT-02", "Downtown Express", "staff_5", "Sarah Jenkins", "Central Hub, Camp, Pune",
                Arrays.asList("SEC3", "SEC7"), Arrays.asList("Financial District", "City Center"), true));
        routes.add(new Route("rt_3", "RT-03", "North Suburbs Night", "staff_1", "Ramesh Patil", "Kalyani Nagar Branch",
                Arrays.asList("EST2"), Arrays.asList("North Zone", "Lake Road"), false));
        routes.add(new Route("rt_4", "RT-04", "West Campus Loop", "staff_2", "Sunil Kumar", "Shivajinagar Station",
                Arrays.asList("SEC8"), Arrays.asList("University Road", "FC Road"), true));
        routesLiveData.setValue(new ArrayList<>(routes));

        // Newspapers
        newspapers.add(new Newspaper("np_1", "TOI-ENG", "Times of India", "English", "Bennett, Coleman & Co", 5.00, true));
        newspapers.add(new Newspaper("np_2", "TH-ENG", "The Hindu", "English", "Kasturi & Sons", 6.00, true));
        newspapers.add(new Newspaper("np_3", "DJ-HIN", "Dainik Jagran", "Hindi", "Jagran Prakashan Ltd", 4.00, true));
        newspapers.add(new Newspaper("np_4", "ET-ENG", "Economic Times", "English", "Bennett, Coleman & Co", 5.00, true));
        newspapers.add(new Newspaper("np_5", "SK-MAR", "Sakaal", "Marathi", "Sakal Media Group", 4.50, true));
        newspapers.add(new Newspaper("np_6", "IE-ENG", "Indian Express", "English", "Indian Express Group", 5.50, true));
        newspapersLiveData.setValue(new ArrayList<>(newspapers));

        // Customers
        Customer c1 = new Customer("cust_1", "Alice Cooper", "9999999999", "alice.cooper@example.com", "RESIDENTIAL",
                "Morning East", "Northwest District - Sector 4", "124 Maple Street, Unit B, Springfield, IL 62704",
                "45", "Please leave on the side porch, not the front door. Beware of dog.",
                "Monthly Fixed", "Mon, Tue, Wed, Thu, Fri, Sat, Sun", "Oct 2021", 45.00, true, true,
                Arrays.asList(
                        new Customer.SubscriptionItem("TOI-ENG", "Daily Bugle", "2021-10-15", "Monthly Fixed", "Mon, Tue, Wed, Thu, Fri, Sat, Sun", 210.00, true),
                        new Customer.SubscriptionItem("ST-ENG", "Sunday Times", "2022-03-01", "Monthly Fixed", "Sunday Only", 80.00, true),
                        new Customer.SubscriptionItem("LM-ENG", "Local Monthly", "2021-11-01", "Monthly Fixed", "1st Mon", 120.00, false)
                ));

        Customer c2 = new Customer("cust_2", "Eleanor Vance", "9999999999", "eleanor.vance@example.com", "RESIDENTIAL",
                "Northwest", "Northwest Heights", "742 Evergreen Terrace, Springfield", "12", "Drop in mailbox",
                "Per Day", "Mon-Sun", "Jan 2022", 0.0, true, true,
                Collections.singletonList(new Customer.SubscriptionItem("TOI-ENG", "Times of India", "2022-01-10", "Per Day", "Mon-Sun", 150.00, true)));

        Customer c3 = new Customer("cust_3", "Hillside Cafe", "9999999999", "contact@hillsidecafe.com", "COMMERCIAL",
                "Downtown", "Downtown Core", "45 Commercial St, Suite 101", "03", "Handover to cashier at counter",
                "Monthly Fixed", "Mon-Sat", "Aug 2020", 0.0, true, true,
                Arrays.asList(
                        new Customer.SubscriptionItem("TOI-ENG", "Times of India", "2020-08-01", "Monthly Fixed", "Mon-Sat", 150.00, true),
                        new Customer.SubscriptionItem("ET-ENG", "Economic Times", "2020-08-01", "Monthly Fixed", "Mon-Sat", 150.00, true)
                ));

        Customer c4 = new Customer("cust_4", "Arthur Pendelton", "9999999999", "arthur.p@example.com", "RESIDENTIAL",
                "Eastside", "Eastside Suburbs", "89 Oakridge Lane", "24", "Ring doorbell if rainy",
                "Per Day", "M, W, F", "Feb 2023", 110.0, false, false,
                Collections.singletonList(new Customer.SubscriptionItem("DJ-HIN", "Dainik Jagran", "2023-02-15", "Per Day", "M, W, F", 60.00, false)));

        Customer c5 = new Customer("cust_5", "City Library", "9999999999", "info@citylibrary.org", "COMMERCIAL",
                "Civic", "Civic Center", "100 Public Plaza, Main Entrance", "01", "Security desk reception",
                "Monthly Fixed", "Mon-Sun", "Jan 2019", 0.0, true, true,
                Arrays.asList(
                        new Customer.SubscriptionItem("TOI-ENG", "Times of India", "2019-01-01", "Monthly Fixed", "Mon-Sun", 150.00, true),
                        new Customer.SubscriptionItem("TH-ENG", "The Hindu", "2019-01-01", "Monthly Fixed", "Mon-Sun", 180.00, true)
                ));

        Customer c6 = new Customer("cust_6", "Mauli Super Market", "9999999999", "mauli.market@gmail.com", "COMMERCIAL",
                "Morning East", "Viman Nagar - Sector 1", "Shop No 12, Ground Floor, Sai Plaza Building, MG Road Extension, Pune 411001",
                "05", "Place inside glass sliding door before 6:30 AM",
                "Monthly Fixed", "Mon-Sun", "Mar 2022", 0.0, true, true,
                Arrays.asList(
                        new Customer.SubscriptionItem("TOI-ENG", "Times of India", "2022-03-01", "Monthly Fixed", "Mon-Sun", 150.00, true),
                        new Customer.SubscriptionItem("ET-ENG", "Economic Times", "2022-03-01", "Monthly Fixed", "Mon-Sun", 150.00, true)
                ));

        Customer c7 = new Customer("cust_7", "Sharma Residence", "9999999999", "rajesh.sharma@yahoo.com", "RESIDENTIAL",
                "Morning East", "Viman Nagar - Sector 1", "Villa 42, Green Park Avenue, Viman Nagar, Pune", "06",
                "Leave in porch wire basket", "Per Day", "Mon-Sun", "Nov 2021", 460.00, false, true,
                Arrays.asList(
                        new Customer.SubscriptionItem("TOI-ENG", "Times of India", "2021-11-01", "Monthly Fixed", "Mon-Sun", 210.00, true),
                        new Customer.SubscriptionItem("ET-ENG", "Economic Times", "2021-11-01", "Monthly Fixed", "Mon-Sun", 200.00, true)
                ));

        Customer c8 = new Customer("cust_8", "Dr. Mehta Clinic", "9999999999", "clinic.mehta@gmail.com", "COMMERCIAL",
                "Morning East", "Viman Nagar - Sector 1", "Ground Floor, City Hospital Wing, Viman Nagar", "07",
                "Drop at clinic reception front gate", "Monthly Fixed", "Mon-Sat", "Jun 2023", 0.0, true, true,
                Collections.singletonList(new Customer.SubscriptionItem("TOI-ENG", "Times of India", "2023-06-01", "Monthly Fixed", "Mon-Sat", 150.00, true)));

        customers.addAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8));
        customersLiveData.setValue(new ArrayList<>(customers));

        // Daily Deliveries
        deliveries.add(new DeliveryRecord("del_1", "cust_6", "Mauli Super Market", "Flat 101, Sunshine Heights, Sector 14", "9999999999", "Viman Nagar - Sector 1", "RT-01", "17 Mon", true,
                Arrays.asList(new DeliveryRecord.PaperItem("TOI", "Times of India", 1), new DeliveryRecord.PaperItem("ET", "Economic Times", 1))));
        deliveries.add(new DeliveryRecord("del_2", "cust_7", "Sharma Residence", "Villa 42, Green Park Avenue", "9999999999", "Viman Nagar - Sector 1", "RT-01", "17 Mon", true,
                Arrays.asList(new DeliveryRecord.PaperItem("TOI", "Times of India", 1), new DeliveryRecord.PaperItem("ET", "Economic Times", 1))));
        deliveries.add(new DeliveryRecord("del_3", "cust_8", "Dr. Mehta Clinic", "Ground Floor, City Hospital", "9999999999", "Viman Nagar - Sector 1", "RT-01", "17 Mon", true,
                Collections.singletonList(new DeliveryRecord.PaperItem("TOI", "Times of India", 1))));
        deliveries.add(new DeliveryRecord("del_4", "cust_1", "Alice Cooper", "124 Maple Street, Unit B", "9999999999", "Northwest District - Sector 4", "RT-01", "17 Mon", false,
                Arrays.asList(new DeliveryRecord.PaperItem("TOI", "Times of India", 1), new DeliveryRecord.PaperItem("ET", "Sunday Times", 1))));
        deliveries.add(new DeliveryRecord("del_5", "cust_3", "Hillside Cafe", "45 Commercial St, Suite 101", "9999999999", "Downtown Core", "RT-02", "17 Mon", false,
                Arrays.asList(new DeliveryRecord.PaperItem("TOI", "Times of India", 1), new DeliveryRecord.PaperItem("ET", "Economic Times", 1))));
        deliveriesLiveData.setValue(new ArrayList<>(deliveries));

        // Monthly Billings
        billings.add(new BillingRecord("bill_1", "cust_7", "Rajesh Sharma", "Viman Nagar Sec 1", "RT-01", "June 2026", 410.00, 50.00, 460.00, false, "10/25/2023", "Cash",
                Arrays.asList(new BillingRecord.HistoryItem("Sept 2023", 410.00, "Paid via Cash"), new BillingRecord.HistoryItem("Aug 2023", 410.00, "Paid via UPI"))));
        billings.add(new BillingRecord("bill_2", "cust_9", "Amit Verma", "Viman Nagar Sec 1", "RT-01", "June 2026", 280.00, 0.00, 280.00, true, "06/05/2026", "UPI",
                Arrays.asList(new BillingRecord.HistoryItem("May 2026", 280.00, "Paid via UPI"), new BillingRecord.HistoryItem("Apr 2026", 280.00, "Paid via UPI"))));
        billings.add(new BillingRecord("bill_3", "cust_6", "Mauli Super Market", "Viman Nagar Sec 1", "RT-01", "June 2026", 300.00, 0.00, 300.00, true, "06/02/2026", "Bank Transfer",
                Collections.singletonList(new BillingRecord.HistoryItem("May 2026", 300.00, "Paid via Bank Transfer"))));
        billings.add(new BillingRecord("bill_4", "cust_1", "Alice Cooper", "Northwest District - Sector 4", "RT-01", "June 2026", 410.00, 45.00, 455.00, false, "05/28/2026", "Cash",
                Collections.singletonList(new BillingRecord.HistoryItem("May 2026", 410.00, "Paid via Cash"))));
        billingsLiveData.setValue(new ArrayList<>(billings));
    }

    // Area CRUD
    public LiveData<List<Area>> getAreas() { return areasLiveData; }
    public void addArea(Area area) {
        if (area.getId() == null || area.getId().isEmpty()) {
            area.setId("area_" + (areas.size() + 1));
        }
        areas.add(0, area);
        areasLiveData.setValue(new ArrayList<>(areas));
    }
    public void deleteArea(Area area) {
        areas.removeIf(a -> a.getId().equals(area.getId()));
        areasLiveData.setValue(new ArrayList<>(areas));
    }

    // Route CRUD
    public LiveData<List<Route>> getRoutes() { return routesLiveData; }
    public void addRoute(Route route) {
        if (route.getId() == null || route.getId().isEmpty()) {
            route.setId("rt_" + (routes.size() + 1));
        }
        routes.add(0, route);
        routesLiveData.setValue(new ArrayList<>(routes));
    }
    public void updateRoute(Route route) {
        for (int i = 0; i < routes.size(); i++) {
            if (routes.get(i).getId().equals(route.getId())) {
                routes.set(i, route);
                routesLiveData.setValue(new ArrayList<>(routes));
                return;
            }
        }
        addRoute(route);
    }
    public void deleteRoute(Route route) {
        routes.removeIf(r -> r.getId().equals(route.getId()));
        routesLiveData.setValue(new ArrayList<>(routes));
    }

    // Staff CRUD
    public LiveData<List<DeliveryStaff>> getStaff() {
        return staffLiveData;
    }
    public LiveData<List<DeliveryStaff>> getatrtingending() {
        return staffLiveData;
    }
    public void addStaff(DeliveryStaff s) {
        if (s.getId() == null || s.getId().isEmpty()) {
            s.setId("staff_" + (staff.size() + 1));
        }
        staff.add(0, s);
        staffLiveData.setValue(new ArrayList<>(staff));
    }
    public void updateStaff(DeliveryStaff s) {
        for (int i = 0; i < staff.size(); i++) {
            if (staff.get(i).getId().equals(s.getId())) {
                staff.set(i, s);
                staffLiveData.setValue(new ArrayList<>(staff));
                return;
            }
        }
        addStaff(s);
    }
    public void deleteStaff(DeliveryStaff s) {
        staff.removeIf(item -> item.getId().equals(s.getId()));
        staffLiveData.setValue(new ArrayList<>(staff));
    }

    // Newspaper CRUD
    public LiveData<List<Newspaper>> getNewspapers() {
        return newspapersLiveData;
    }//
    public void addNewspaper(Newspaper np) {
        if (np.getId() == null || np.getId().isEmpty()) {
            np.setId("np_" + (newspapers.size() + 1));
        }
        newspapers.add(0, np);
        newspapersLiveData.setValue(new ArrayList<>(newspapers));
    }
    public void deleteNewspaper(Newspaper np) {
        newspapers.removeIf(n -> n.getId().equals(np.getId()));
        newspapersLiveData.setValue(new ArrayList<>(newspapers));
    }

    // Customer CRUD
    public LiveData<List<Customer>> getCustomers() { return customersLiveData; }
    public void addCustomer(Customer customer) {
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId("cust_" + (customers.size() + 1));
        }
        customers.add(0, customer);
        customersLiveData.setValue(new ArrayList<>(customers));
    }
    public void updateCustomer(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId().equals(customer.getId())) {
                customers.set(i, customer);
                customersLiveData.setValue(new ArrayList<>(customers));
                return;
            }
        }
        addCustomer(customer);
    }
    public void deleteCustomer(Customer customer) {
        customers.removeIf(c -> c.getId().equals(customer.getId()));
        customersLiveData.setValue(new ArrayList<>(customers));
    }
    public Customer getCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) return c;
        }
        return customers.isEmpty() ? null : customers.get(0);
    }

    // Daily Deliveries
    public LiveData<List<DeliveryRecord>> getDeliveries() { return deliveriesLiveData; }
    public void updateDeliveryStatus(String id, boolean delivered) {
        for (DeliveryRecord dr : deliveries) {
            if (dr.getId().equals(id)) {
                dr.setDelivered(delivered);
                break;
            }
        }
        deliveriesLiveData.setValue(new ArrayList<>(deliveries));
    }

    // Billing & Payments
    public LiveData<List<BillingRecord>> getBillings() { return billingsLiveData; }
    public void recordPayment(String billingId, double amount, String date, boolean isFull) {
        for (BillingRecord br : billings) {
            if (br.getId().equals(billingId)) {
                if (isFull) {
                    br.setPaid(true);
                    br.setPreviousBalance(0);
                    br.setCurrentMonthAmount(0);
                    br.setTotalDue(0);
                } else {
                    double remaining = Math.max(0, br.getTotalDue() - amount);
                    br.setTotalDue(remaining);
                    if (remaining == 0) {
                        br.setPaid(true);
                    }
                }
                br.setPaymentDate(date);
                br.getHistory().add(0, new BillingRecord.HistoryItem("Paid on " + date, amount, "Payment recorded"));
                break;
            }
        }
        billingsLiveData.setValue(new ArrayList<>(billings));
    }
    public void restoreInitialData() {
        areas.clear();
        routes.clear();
        staff.clear();
        newspapers.clear();
        customers.clear();
        deliveries.clear();
        billings.clear();
        populateInitialData();
    }

    public BillingRecord getBillingById(String id) {
        for (BillingRecord b : billings) {
            if (b.getId().equals(id)) return b;
        }
        return billings.isEmpty() ? null : billings.get(0);
    }
}
