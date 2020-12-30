package com.ccrental.composite.user.apis.vos;

import com.ccrental.composite.common.utillity.Sha512;

import javax.servlet.http.HttpServletRequest;

public class UserLoginVo {
    private final String email;
    private final String password;
    private final String hashedPassword;
    private final HttpServletRequest request;

    public UserLoginVo(String email, String password, HttpServletRequest request) {
        this.email = email;
        this.password = password;
        this.request = request;
        this.hashedPassword = Sha512.hash(this.password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}