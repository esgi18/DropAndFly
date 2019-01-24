package com.rtersou.dropandfly.models;

import java.sql.Date;

public class Reservation {

    private long id;
    private String date_start;
    private String date_end;
    private int nb_luggage;
    private int statut;
    private int user_id;
    private int shop_id;

    public Reservation(String date_start, String date_end, int nb_luggage, int statut, int user_id, int shop_id) {
        this.date_start = date_start;
        this.date_end = date_end;
        this.nb_luggage = nb_luggage;
        this.statut = statut;
        this.user_id = user_id;
        this.shop_id = shop_id;
    }

    public Reservation() {
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
}
