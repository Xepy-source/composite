package com.ccrental.composite.cs.apis.vos;

public class AddBranchVo {
    private final String name;
    private final String address;
    private final String contact;
    private final String code;
    private final String operation;
    private final String coordinate_x;
    private final String coordinate_y;
    private final String approach_bus;
    private final String approach_subway;
    private final boolean isNormalization;

    public AddBranchVo(String name, String address, String contact, String code, String operation, String coordinate_x, String coordinate_y, String approach_bus, String approach_subway) {
        if (name.equals("") || address.equals("") || contact.equals("") || code.equals("") || operation.equals("") || coordinate_x.equals("") || coordinate_y.equals("")) {
            this.name = null;
            this.address = null;
            this.contact = null;
            this.code = null;
            this.operation = null;
            this.coordinate_x = null;
            this.coordinate_y = null;
            this.approach_bus = null;
            this.approach_subway = null;
            this.isNormalization = false;
        } else {
            this.name = name;
            this.address = address;
            this.contact = contact;
            this.code = code;
            this.operation = operation;
            this.coordinate_x = coordinate_x;
            this.coordinate_y = coordinate_y;
            this.approach_bus = approach_bus;
            this.approach_subway = approach_subway;
            this.isNormalization = true;
        }
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

    public String getCode() {
        return code;
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

    public boolean isNormalization() {
        return isNormalization;
    }
}
