package com.ccrental.composite.cs.apis.vos;

public class GetBranchVo {
    private final String name;
    private final String address;
    private final String contact;
    private final String operation;
    private final String coordinate_x;
    private final String coordinate_y;
    private final String approach_bus;
    private final String approach_subway;

    public GetBranchVo(String name, String address, String contact, String operation, String coordinate_x, String coordinate_y, String approach_bus, String approach_subway) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.operation = operation;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.approach_bus = approach_bus;
        this.approach_subway = approach_subway;
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

    public String getOperation() {
        return operation;
    }

    public String getCoordinate_x() {
        return coordinate_x;
    }

    public String getCoordinate_y() {
        return coordinate_y;
    }

    public String getApproach_bus() {
        return approach_bus;
    }

    public String getApproach_subway() {
        return approach_subway;
    }
}
