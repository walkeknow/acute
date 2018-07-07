package com.kewal.acute;

import java.io.Serializable;

public class Supervisor implements Serializable {

    String id;
    String name;
    String address_1;
    String address_2;
    String address_3;
    String pin_code;
    String city;
    String mobile_number;
    String birth_date;

    public Supervisor() {
    }

    ;

    public Supervisor(String id, String name, String address_1, String address_2, String address_3, String pin_code, String city, String mobile_number, String birth_date) {
        this.id = id;
        this.name = name;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.address_3 = address_3;
        this.pin_code = pin_code;
        this.city = city;
        this.mobile_number = mobile_number;
        this.birth_date = birth_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_3() {
        return address_3;
    }

    public void setAddress_3(String address_3) {
        this.address_3 = address_3;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}