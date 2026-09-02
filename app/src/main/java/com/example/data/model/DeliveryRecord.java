package com.example.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeliveryRecord implements Serializable {
    private String id;
    private String customerId;
    private String customerName;
    private String address;
    private String phone;
    private String area;
    private String routeId;
    private String date;
    private boolean delivered;
    private List<PaperItem> papers;
    public static class PaperItem implements Serializable {
        private String code;
        private String name;
        private int quantity;

        public PaperItem() {}
        public PaperItem(String code, String name, int quantity) {
            this.code = code;
            this.name = name;
            this.quantity = quantity;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public DeliveryRecord() {
        this.papers = new ArrayList<>();
    }

    public DeliveryRecord(String id, String customerId, String customerName, String address, String phone, String area, String routeId, String date, boolean delivered, List<PaperItem> papers) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.area = area;
        this.routeId = routeId;
        this.date = date;
        this.delivered = delivered;
        this.papers = papers != null ? papers : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    public List<PaperItem> getPapers() { return papers; }
    public void setPapers(List<PaperItem> papers) { this.papers = papers; }
}
