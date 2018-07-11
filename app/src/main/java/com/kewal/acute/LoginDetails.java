package com.kewal.acute;

public class LoginDetails {
    String email_id;
    String password;
    String login_or_register;
    String login_through;
    String device_id;
    String device_mac;

    public LoginDetails(String email_id, String password, String login_or_register, String login_through, String device_id, String device_mac) {
        this.email_id = email_id;
        this.password = password;
        this.login_or_register = login_or_register;
        this.login_through = login_through;
        this.device_id = device_id;
        this.device_mac = device_mac;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_or_register() {
        return login_or_register;
    }

    public void setLogin_or_register(String login_or_register) {
        this.login_or_register = login_or_register;
    }

    public String getLogin_through() {
        return login_through;
    }

    public void setLogin_through(String login_through) {
        this.login_through = login_through;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_mac() {
        return device_mac;
    }

    public void setDevice_mac(String device_mac) {
        this.device_mac = device_mac;
    }
}
