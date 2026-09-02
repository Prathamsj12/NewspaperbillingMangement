package com.example.data.model;

import java.io.Serializable;
//used to to take the data for the trasaction data transaction between the fronm t end and the ui
//above modle is used for  the data transfer te from the repositary to view modle
//Modle class is used to  to  data transfer

public class Area implements Serializable {
    private String id;
    private String code;
    private String name;
    private String zone;
    private String city;
    private String pincode;
    private String landmark;

    public Area() {

    }

    public Area(String id, String code, String name, String zone, String city, String pincode, String landmark) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.zone = zone;
        this.city = city;
        this.pincode = pincode;
        this.landmark = landmark;
    }

    public String getId() { return id;
    }
    public void setId(String id) { this.id = id;
    }

    public String getCode() { return code; }
    public void setCode(String code) {

    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }

    public String getDisplayName() {
        return name + " - " + code;
    }
}
