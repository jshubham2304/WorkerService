package com.service.Worker.model;

public class Details {
    String name;
    String phone;
    String address;
    String date;
    String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Details(String name, String phone, String address, String date, String desc) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.desc = desc;
    }
}
