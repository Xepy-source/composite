package com.ccrental.composite.cs.apis.vos;

public class GetRepairVo {
    private final String name;
    private final String address;
    private final String contact;
    private final String coordinate_x;
    private final String coordinate_y;

    public GetRepairVo(String name, String address, String contact, String coordinate_x, String coordinate_y) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getCoordinate_x() {
        return coordinate_x;
    }

    public String getCoordinate_y() {
        return coordinate_y;
    }
}