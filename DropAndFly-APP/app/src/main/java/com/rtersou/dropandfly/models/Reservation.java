package com.rtersou.dropandfly.models;

import java.io.Serializable;
import java.sql.Date;

public class Reservation implements Serializable {

    private long id;
    private String date_start;
    private String date_end;
    private String h_start;
    private String h_end;
    private int nb_luggage;
    private int statut;
    private int price;
    private int user_id;
    private int shop_id;


    public Reservation() {
    }

    public Reservation(String date_start, String date_end, String h_start, String h_end, int nb_luggage, int statut, int price, int user_id, int shop_id) {
        this.date_start = date_start;
        this.date_end = date_end;
        this.h_start = h_start;
        this.h_end = h_end;
        this.nb_luggage = nb_luggage;
        this.statut = statut;
        this.price = price;
        this.user_id = user_id;
        this.shop_id = shop_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNb_luggage() {
        return nb_luggage;
    }

    public void setNb_luggage(int nb_luggage) {
        this.nb_luggage = nb_luggage;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getH_start() {
        return h_start;
    }

    public void setH_start(String h_start) {
        this.h_start = h_start;
    }

    public String getH_end() {
        return h_end;
    }

    public void setH_end(String h_end) {
        this.h_end = h_end;
    }
}
