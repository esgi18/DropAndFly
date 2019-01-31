package com.rtersou.dropandfly.models;

import java.io.Serializable;

public class Shop implements Serializable {

    private long id;
    private String address_city;
    private String address_country;
    private String address_cp;
    private String address_number;
    private String address_street;
    private String name;
    private int nb_luggage;
    private int user_id;

    public Shop() {
    }

    public Shop(String address_city, String address_country, String address_cp, String address_number, String address_street, String name, int nb_luggage, int user_id) {
        this.address_city = address_city;
        this.address_country = address_country;
        this.address_cp = address_cp;
        this.address_number = address_number;
        this.address_street = address_street;
        this.name = name;
        this.nb_luggage = nb_luggage;
        this.user_id = user_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_country() {
        return address_country;
    }

    public void setAddress_country(String address_country) {
        this.address_country = address_country;
    }

    public String getAddress_cp() {
        return address_cp;
    }

    public void setAddress_cp(String address_cp) {
        this.address_cp = address_cp;
    }

    public String getAddress_number() {
        return address_number;
    }

    public void setAddress_number(String address_number) {
        this.address_number = address_number;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNb_luggage() {
        return nb_luggage;
    }

    public void setNb_luggage(int nb_luggage) {
        this.nb_luggage = nb_luggage;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
