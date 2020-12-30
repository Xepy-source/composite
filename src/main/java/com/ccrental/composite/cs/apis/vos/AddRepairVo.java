package com.ccrental.composite.cs.apis.vos;

public class AddRepairVo {
    private final String name;
    private final String address;
    private final String contact;
    private final String code;
    private final String coordinate_x;
    private final String coordinate_y;
    private final boolean isNormalization;

    public AddRepairVo(String name, String address, String contact, String code, String coordinate_x, String coordinate_y) {
        if(name.equals("") || address.equals("") || contact.equals("") || code.equals("") || coordinate_x.equals("") || coordinate_y.equals("")) {
            this.name = null;
            this.address = null;
            this.contact = null;
            this.code = null;
            this.coordinate_x = null;
            this.coordinate_y = null;
            this.isNormalization = false;
        } else {
            this.name = name;
            this.address = address;
            this.contact = contact;
            this.code = code;
            this.coordinate_x = coordinate_x;
            this.coordinate_y = coordinate_y;
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

    public String getCoordinate_x() {
        return coordinate_x;
    }

    public String getCoordinate_y() {
        return coordinate_y;
    }

    public boolean isNormalization() {
        return isNormalization;
    }
}
