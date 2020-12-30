package com.ccrental.composite.user.apis.vos;

import com.ccrental.composite.common.utillity.Sha512;

public class UserRegisterVo {
    private final String user_email;
    private final String user_password;
    private final String hashedPassword;
    private final String user_name;
    private final String user_contact;
    private final String user_address; //주소
    private final String user_license_type; //운전면허증,주민등록번호
    private final String user_license_number; //번호판
    private final String user_license_issue_date; //발급날짜

    public UserRegisterVo(String user_email, String user_password, String user_name, String user_contact, String user_address, String user_license_type, String user_license_number, String user_license_issue_date) {
        this.user_email = user_email;
        this.user_password = user_password;
        this.hashedPassword = Sha512.hash(this.user_password);
        this.user_name = user_name;
        this.user_contact = user_contact;
        this.user_address = user_address;
        this.user_license_type = user_license_type;
        this.user_license_number = user_license_number;
        this.user_license_issue_date = user_license_issue_date;
    }

    public String getUser_email() { return user_email; }

    public String getUser_password() {
        return user_password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getUser_license_type() {
        return user_license_type;
    }

    public String getUser_license_number() {
        return user_license_number;
    }

    public String getUser_license_issue_date() {
        return user_license_issue_date;
    }
}
