package com.ccrental.composite.user.apis.vos;

public class FindPasswordVo {
    private final String email;
    private final String name;
    private final String contact;

    public FindPasswordVo(String email, String name, String contact) {
        this.email = email;
        this.name = name;
        this.contact = contact;
    }

    public String getContact() {return contact;}

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
