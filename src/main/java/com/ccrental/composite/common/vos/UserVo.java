package com.ccrental.composite.common.vos;

public class UserVo {
    private final int index;
    private final String email;
    private final String password;
    private final String name;
    private final String contact;
    private final String address;
    private final String licenseType;
    private final String licenseNumber;
    private final String licenseIssueDate;
    private final int level;



    public UserVo(int index, String email, String password, String name, String contact, String address, String licenseType, String licenseNumber, String licenseIssueDate, int level) {
        this.index = index;
        this.email = email;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.licenseType = licenseType;
        this.licenseNumber = licenseNumber;
        this.licenseIssueDate = licenseIssueDate;
        this.level = level;
    }


    public int getIndex() {
        return index;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {  return password; }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public int getLevel() {
        return level;
    }
}