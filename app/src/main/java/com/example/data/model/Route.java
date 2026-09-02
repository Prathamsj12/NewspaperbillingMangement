package com.example.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {
    private String id;
    private String code;
    private String name;
    private String hawkerId;
    private String hawkerName;
    private String startPoint;
    private List<String> areaCodes;
    private List<String> areaNames;
    private boolean active;

    public Route() {
        this.areaCodes = new ArrayList<>();
        this.areaNames = new ArrayList<>();
        this.active = true;
    }

    public Route(String id, String code, String name, String hawkerId, String hawkerName, String startPoint, List<String> areaCodes, List<String> areaNames, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.hawkerId = hawkerId;
        this.hawkerName = hawkerName;
        this.startPoint = startPoint;
        this.areaCodes = areaCodes != null ? areaCodes : new ArrayList<>();
        this.areaNames = areaNames != null ? areaNames : new ArrayList<>();
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHawkerId() { return hawkerId; }
    public void setHawkerId(String hawkerId) { this.hawkerId = hawkerId; }

    public String getHawkerName() { return hawkerName; }
    public void setHawkerName(String hawkerName) { this.hawkerName = hawkerName; }

    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }

    public String getStartLocation() { return startPoint; }
    public void setStartLocation(String startLocation) { this.startPoint = startLocation; }

    public List<String> getAreaCodes() { return areaCodes; }
    public void setAreaCodes(List<String> areaCodes) { this.areaCodes = areaCodes; }

    public List<String> getAreaNames() { return areaNames; }
    public void setAreaNames(List<String> areaNames) { this.areaNames = areaNames; }

    public List<String> getAreasCovered() { return areaNames; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getFormattedTitle() {
        return name + " (" + code + ")";
    }

    public String getAreasJoined() {
        if (areaNames == null || areaNames.isEmpty()) return "None";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < areaNames.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(areaNames.get(i));
        }
        return sb.toString();
    }
}
