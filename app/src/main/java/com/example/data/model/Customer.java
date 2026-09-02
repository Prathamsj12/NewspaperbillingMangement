package com.example.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private String id;
    private String name;
    private String mobileNumber;
    private String email;
    private String customerType; // "RESIDENTIAL" or "COMMERCIAL"
    private String zone;
    private String area;
    private String address;
    private String dropSequence;
    private String deliveryInstructions;
    private String billingCycle; // "Monthly Fixed" or "Per Day"
    private String deliveryDays; // "Mon, Tue, Wed, Thu, Fri, Sat, Sun"
    private String activeSince;
    private double currentBalance;
    private boolean paidRecently;
    private boolean active;
    private List<SubscriptionItem> subscriptions;

    public static class SubscriptionItem implements Serializable {
        private String paperCode;
        private String paperName;
        private String startDate;
        private String billingType;
        private String deliveryDays;
        private double monthlyPrice;
        private boolean active;

        public SubscriptionItem() {
            this.active = true;
        }

        public SubscriptionItem(String paperCode, String paperName, String startDate, String billingType, String deliveryDays, double monthlyPrice, boolean active) {
            this.paperCode = paperCode;
            this.paperName = paperName;
            this.startDate = startDate;
            this.billingType = billingType;
            this.deliveryDays = deliveryDays;
            this.monthlyPrice = monthlyPrice;
            this.active = active;
        }

        public String getPaperCode() { return paperCode; }
        public void setPaperCode(String paperCode) { this.paperCode = paperCode; }

        public String getCode() { return paperCode; }
        public String getName() { return paperName; }
        public double getMonthlyRate() { return monthlyPrice; }

        public String getPaperName() { return paperName; }
        public void setPaperName(String paperName) { this.paperName = paperName; }

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getBillingType() { return billingType; }
        public void setBillingType(String billingType) { this.billingType = billingType; }

        public String getDeliveryDays() { return deliveryDays; }
        public void setDeliveryDays(String deliveryDays) { this.deliveryDays = deliveryDays; }

        public double getMonthlyPrice() { return monthlyPrice; }
        public void setMonthlyPrice(double monthlyPrice) { this.monthlyPrice = monthlyPrice; }

        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }

    public Customer() {
        this.subscriptions = new ArrayList<>();
        this.active = true;
        this.customerType = "RESIDENTIAL";
        this.billingCycle = "Per Day";
    }

    public Customer(String id, String name, String mobileNumber, String email, String customerType,
                    String zone, String area, String address, String dropSequence, String deliveryInstructions,
                    String billingCycle, String deliveryDays, String activeSince, double currentBalance,
                    boolean paidRecently, boolean active, List<SubscriptionItem> subscriptions) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.customerType = customerType;
        this.zone = zone;
        this.area = area;
        this.address = address;
        this.dropSequence = dropSequence;
        this.deliveryInstructions = deliveryInstructions;
        this.billingCycle = billingCycle;
        this.deliveryDays = deliveryDays;
        this.activeSince = activeSince;
        this.currentBalance = currentBalance;
        this.paidRecently = paidRecently;
        this.active = active;
        this.subscriptions = subscriptions != null ? subscriptions : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getPhone() { return mobileNumber; }
    public void setPhone(String phone) { this.mobileNumber = phone; }

    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }

    public String getType() { return customerType; }
    public void setType(String type) { this.customerType = type; }

    public String getDropSequence() { return dropSequence; }
    public void setDropSequence(String dropSequence) { this.dropSequence = dropSequence; }

    public String getSequenceNumber() { return dropSequence; }
    public void setSequenceNumber(String sequenceNumber) { this.dropSequence = sequenceNumber; }

    public double getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(double currentBalance) { this.currentBalance = currentBalance; }

    public double getBalance() { return currentBalance; }
    public void setBalance(double balance) { this.currentBalance = balance; }

    public String getFormattedBalance() {
        return "₹" + String.format(java.util.Locale.US, "%.2f", currentBalance);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }

    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }

    public String getDeliveryDays() { return deliveryDays; }
    public void setDeliveryDays(String deliveryDays) { this.deliveryDays = deliveryDays; }

    public String getActiveSince() { return activeSince; }
    public void setActiveSince(String activeSince) { this.activeSince = activeSince; }

    public String getRoute() { return zone; }
    public void setRoute(String route) { this.zone = route; }

    public boolean isPaidRecently() { return paidRecently; }
    public void setPaidRecently(boolean paidRecently) { this.paidRecently = paidRecently; }

    public boolean isResidential() {
        return customerType == null || customerType.equalsIgnoreCase("RESIDENTIAL");
    }

    public int getDeliverySequence() {
        try {
            return dropSequence != null ? Integer.parseInt(dropSequence) : 15;
        } catch (Exception e) {
            return 15;
        }
    }

    public boolean isActive() { return active;
    }
    public void setActive(boolean active) { this.active = active;
    }

    public List<SubscriptionItem> getSubscriptions() { return subscriptions;
    }
    public void setSubscriptions(List<SubscriptionItem> subscriptions) { this.subscriptions = subscriptions; }

    public String getInitials() {
        if (name == null || name.trim().isEmpty()) return "CU";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return parts[0].substring(0, 1).toUpperCase() + parts[1].substring(0, 1).toUpperCase();
        }
        return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
    }
}
