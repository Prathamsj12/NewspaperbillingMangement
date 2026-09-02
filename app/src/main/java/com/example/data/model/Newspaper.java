package com.example.data.model;

import java.io.Serializable;

public class Newspaper implements Serializable {
    private String id;
    private String code;
    private String name;
    private String language;
    private String publisher;
    private double dailyRate;
    private boolean active;

    public Newspaper() {
        this.active = true;
    }

    public Newspaper(String id, String code, String name, String language, String publisher, double dailyRate, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.language = language;
        this.publisher = publisher;
        this.dailyRate = dailyRate;
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getFormattedPrice() {
        return "₹" + String.format(java.util.Locale.US, "%.2f", dailyRate) + " / Day";
    }
}
