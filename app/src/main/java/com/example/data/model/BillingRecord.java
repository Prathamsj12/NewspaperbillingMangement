package com.example.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BillingRecord implements Serializable {
    private String id;
    private String customerId;
    private String customerName;
    private String area;
    private String routeId;
    private String month; // e.g. "June 2026", "October 2023"
    private double currentMonthAmount;
    private double previousBalance;
    private double totalDue;
    private boolean paid;
    private String paymentDate;
    private String paymentMode; // "Cash", "UPI", etc.
    private List<HistoryItem> history;

    public static class HistoryItem implements Serializable {
        private String month;
        private double amount;
        private String mode;

        public HistoryItem() {

        }
        public HistoryItem(String month, double amount, String mode) {
            this.month = month;
            this.amount = amount;
            this.mode = mode;
        }

        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }

        public String getDate() { return month; }
        public String getNotes() { return mode; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
    }

    public BillingRecord() {
        this.history = new ArrayList<>();
    }

    public BillingRecord(String id, String customerId, String customerName, String area, String routeId, String month, double currentMonthAmount, double previousBalance, double totalDue, boolean paid, String paymentDate, String paymentMode, List<HistoryItem> history) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.area = area;
        this.routeId = routeId;
        this.month = month;
        this.currentMonthAmount = currentMonthAmount;
        this.previousBalance = previousBalance;
        this.totalDue = totalDue;
        this.paid = paid;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.history = history != null ? history : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getMonthYear() { return month; }

    public double getCurrentMonthAmount() { return currentMonthAmount; }
    public void setCurrentMonthAmount(double currentMonthAmount) { this.currentMonthAmount = currentMonthAmount; }

    public double getPreviousBalance() { return previousBalance; }
    public void setPreviousBalance(double previousBalance) { this.previousBalance = previousBalance; }

    public double getTotalDue() { return totalDue; }
    public void setTotalDue(double totalDue) { this.totalDue = totalDue; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public List<HistoryItem> getHistory() { return history; }
    public void setHistory(List<HistoryItem> history) { this.history = history; }

    public String getFormattedAmount() {
        return "₹" + String.format(java.util.Locale.US, "%.2f", currentMonthAmount);
    }

    public String getFormattedTotalDue() {
        return "₹" + String.format(java.util.Locale.US, "%.2f", totalDue);
    }
}
