package com.kewal.acute;

import java.io.Serializable;

public class Supervisor implements Serializable{

    String superId;
    String superName;
    String superCity;
    String superAddress;
    String superMobNo;
    String superBirthDate;

    public Supervisor(){};

    public Supervisor(String superId, String superName, String superCity, String superAddress, String superMobNo, String superBirthDate) {
        this.superId = superId;
        this.superName = superName;
        this.superCity = superCity;
        this.superAddress = superAddress;
        this.superMobNo = superMobNo;
        this.superBirthDate = superBirthDate;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public String getSuperCity() {
        return superCity;
    }

    public void setSuperCity(String superCity) {
        this.superCity = superCity;
    }

    public String getSuperAddress() {
        return superAddress;
    }

    public void setSuperAddress(String superAddress) {
        this.superAddress = superAddress;
    }

    public String getSuperMobNo() {
        return superMobNo;
    }

    public void setSuperMobNo(String superMobNo) {
        this.superMobNo = superMobNo;
    }

    public String getSuperBirthDate() {
        return superBirthDate;
    }

    public void setSuperBirthDate(String superBirthDate) {
        this.superBirthDate = superBirthDate;
    }
}
