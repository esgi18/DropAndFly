package com.rtersou.dropandfly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationController {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.KEY_ID,
            DatabaseHelper.KEY_DATE_START,
            DatabaseHelper.KEY_DATE_END,
            DatabaseHelper.KEY_H_START,
            DatabaseHelper.KEY_H_END,
            DatabaseHelper.KEY_NB_LUGGAGE,
            DatabaseHelper.KEY_PRICE,
            DatabaseHelper.KEY_USER_ID,
            DatabaseHelper.KEY_SHOP_ID};


    public ReservationController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public Reservation createReservation(Reservation reservation) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_DATE_START, reservation.getDate_start());
        values.put(DatabaseHelper.KEY_DATE_END, reservation.getDate_end());
        values.put(DatabaseHelper.KEY_H_START, reservation.getH_start());
        values.put(DatabaseHelper.KEY_H_END, reservation.getH_end());
        values.put(DatabaseHelper.KEY_NB_LUGGAGE, reservation.getNb_luggage());
        values.put(DatabaseHelper.KEY_PRICE, reservation.getPrice());
        values.put(DatabaseHelper.KEY_USER_ID, reservation.getUser_id());
        values.put(DatabaseHelper.KEY_SHOP_ID, reservation.getShop_id());

        long insertId = database.insert(DatabaseHelper.TABLE_RESERVATION, null,
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_RESERVATION,
                allColumns, DatabaseHelper.KEY_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Reservation newReservation = cursorToReservation(cursor);
        cursor.close();

        return newReservation;
    }



    public void deleteReservation(Reservation reservation) {
        String id = reservation.getId();

        database.delete(DatabaseHelper.TABLE_RESERVATION, DatabaseHelper.KEY_ID
                + " = " + id, null);
    }



    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<Reservation>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_RESERVATION,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Reservation reservation = cursorToReservation(cursor);
            reservations.add(reservation);
            cursor.moveToNext();
        }
        cursor.close();
        return reservations;
    }

    public Reservation getReservationById (long id) {

        String selectQuery =
                "SELECT  * FROM " + DatabaseHelper.TABLE_RESERVATION +
                        " WHERE "
                        + DatabaseHelper.KEY_ID + " = " + id;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Reservation reservation = new Reservation();
        reservation.setId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_ID)));
        reservation.setDate_start(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATE_START)));
        reservation.setDate_end(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATE_END)));
        reservation.setH_start(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_H_START)));
        reservation.setH_end(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_H_END)));
        reservation.setNb_luggage(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_NB_LUGGAGE)));
        reservation.setPrice(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_PRICE)));
        reservation.setShop_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_SHOP_ID)));
        reservation.setUser_id(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_ID)));

        cursor.close();

        return reservation;
    }



    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_RESERVATION, null, null);
    }



    private Reservation cursorToReservation(Cursor cursor) {
        Reservation reservation = new Reservation();
        reservation.setId(cursor.getString(0));
        reservation.setDate_start(cursor.getString(1));
        reservation.setDate_end(cursor.getString(2));
        reservation.setH_start(cursor.getString(3));
        reservation.setH_end(cursor.getString(4));
        reservation.setNb_luggage(cursor.getInt(5));
        reservation.setPrice(cursor.getInt(6));
        reservation.setShop_id(cursor.getString(7));
        reservation.setUser_id(cursor.getString(8));

        return reservation;
    }

}
