package com.ccrental.composite.user.apis.vos;

public class EmailFindVo {
    private final String name;
    private final String contact;

    public EmailFindVo(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
