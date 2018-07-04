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
    String cityId;
    String comment;
    String photo;
    String bossId;

    public Employee() {

    }

    public Employee(String empId, String empName, String empType, String cardNo, String cardType, String birthDate, String rating, String cityId, String comment, String photo, String bossId) {
        this.empId = empId;
        this.empName = empName;
        this.empType = empType;
        this.cardNo = cardNo;
        this.cardType = cardType;
        this.birthDate = birthDate;
        this.rating = rating;
        this.cityId = cityId;
        this.comment = comment;
        this.photo = photo;
        this.bossId = bossId;
    }

    public String getEmpType() {
        return empType;
    }

    public String getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getRating() {
        return rating;
    }

    public String getCityId() {
        return cityId;
    }

    public String getComment() {
        return comment;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBossId() {
        return bossId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setBossId(String bossId) {
        this.bossId = bossId;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }
}
