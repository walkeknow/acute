package com.kewal.acute;

import java.io.Serializable;

public class Employee implements Serializable{

    String empId;
    String empName;
    String empType;
    String cardNo;
    String cardType;
    String birthDate;
    String rating;
    String empCity;
    String comment;
    String photo;
    String bossId;

    public Employee(String empId, String empName, String empType, String cardNo, String cardType, String birthDate, String rating, String empCity, String comment, String photo, String bossId) {
        this.empId = empId;
        this.empName = empName;
        this.empType = empType;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.birthDate = birthDate;
        this.rating = rating;
        this.empCity = empCity;
        this.comment = comment;
        this.photo = photo;
        this.bossId = bossId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getEmpCity() {
        return empCity;
    }

    public void setEmpCity(String empCity) {
        this.empCity = empCity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBossId() {
        return bossId;
    }

    public void setBossId(String bossId) {
        this.bossId = bossId;
    }
}
