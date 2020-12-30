package com.ccrental.composite.user.apis.vos;

public class PasswordVo {
    private final int userIndex;
    private final String userEmail;
    private final String userPassword;
    private final String userName;

    public PasswordVo(int userIndex, String userEmail, String userPassword, String userName) {
        this.userIndex = userIndex;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserName() {
        return userName;
    }
}
