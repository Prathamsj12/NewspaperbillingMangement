package com.example.data.model;

import java.io.Serializable;
//Updates used to fo the data transfer
public class DeliveryStaff implements Serializable {
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String staffCode;
    private String email;
    private String mobileNumber;
    private String dob;
    private String gender;
    private String education;
    private String zone;
    private String route;
    private boolean active = true;
    private String vehicleDetails = "Bicycle / Bike";
    private String residentialAddress = "City Center";
    private String emergencyContact = "";

    private String joiningDate = "";
    private String experience = "";
    private String documentType = "Aadhaar Card";
    private String documentNumber = "";
    private String bankName = "";
    private String branchName = "";
    private String accountNumber = "";
    private String ifscCode = "";
    private String accountType = "Checking / Current";

    public DeliveryStaff() {}

    public DeliveryStaff(String id, String firstName, String middleName, String lastName, String staffCode,
                         String email, String mobileNumber, String dob, String gender, String education,
                         String zone, String route) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.staffCode = staffCode;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.dob = dob;
        this.gender = gender;
        this.education = education;
        this.zone = zone;
        this.route = route;
        this.active = true;
    }

    public DeliveryStaff(String id, String staffCode, String firstName, String middleName, String lastName,
                         String mobileNumber, String emergencyContact, String email, String residentialAddress,
                         String education, String gender, String route, String zone, String area,
                         String designation, String employmentType, String joiningDate, boolean active,
                         String vehicleDetails) {
        this.id = id;
        this.staffCode = staffCode;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.emergencyContact = emergencyContact;
        this.email = email;
        this.residentialAddress = residentialAddress;
        this.education = education;
        this.gender = gender;
        this.route = route;
        this.zone = zone;
        this.active = active;
        this.vehicleDetails = vehicleDetails;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getStaffCode() { return staffCode; }
    public void setStaffCode(String staffCode) { this.staffCode = staffCode; }

    public String getCode() { return staffCode; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }

    public String getResidentialAddress() { return residentialAddress; }
    public void setResidentialAddress(String residentialAddress) { this.residentialAddress = residentialAddress; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getJoiningDate() { return joiningDate; }
    public void setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getFullName() {
        return firstName + (lastName != null && !lastName.isEmpty() ? " " + lastName : "");
    }

    public String getInitials() {
        String f = firstName != null && !firstName.isEmpty() ? firstName.substring(0, 1).toUpperCase() : "D";
        String l = lastName != null && !lastName.isEmpty() ? lastName.substring(0, 1).toUpperCase() : "P";
        return f + l;
    }

    public String getAssignmentText() {
        if (vehicleDetails != null && !vehicleDetails.isEmpty() && !vehicleDetails.equals("None")) {
            return vehicleDetails;
        }
        if ((zone == null || zone.isEmpty()) && (route == null || route.isEmpty())) {
            return "Assigned in Route Management";
        }
        if (zone != null && route != null && !zone.isEmpty() && !route.isEmpty()) {
            return "Zone " + zone + ", Route " + route;
        }
        return zone != null && !zone.isEmpty() ? "Zone " + zone : "Route " + route;
    }
}
