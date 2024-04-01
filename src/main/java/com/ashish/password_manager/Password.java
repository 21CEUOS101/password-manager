package com.ashish.password_manager;
public class Password {
    String website;
    String username;
    String password;

    public Password(String website, String username, String password) {
        this.website = website;
        this.username = username;
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

