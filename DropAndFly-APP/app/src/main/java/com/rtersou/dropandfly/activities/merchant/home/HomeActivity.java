package com.rtersou.dropandfly.activities.merchant.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.detail_reservation.DetailReservationActivity;
import com.rtersou.dropandfly.activities.merchant.history.HistoryActivity;
import com.rtersou.dropandfly.Adapaters.MerchantLineAdapter;
import com.rtersou.dropandfly.database.ReservationController;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Button history;
    private ReservationController reservationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_merchant);

        initFields();
        initListeners();
        loadEnAttente();
        loadEnCours();
    }

    private void initFields(){
        history = findViewById(R.id.merc_hist_history);
    }

    private void initListeners(){

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent HistoryActivity = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(HistoryActivity);
                HomeActivity.this.finish();
            }
        });

    }




    private void loadEnCours() {
        final ArrayList<Reservation> reservations = new ArrayList<>();
        ListView listView = findViewById(R.id.mer_home_list_view_en_cours);
        reservationController = new ReservationController(this);

        reservationController.open();

        reservations.addAll(reservationController.getAllReservations());
        MerchantLineAdapter adapter = new MerchantLineAdapter(HomeActivity.this, R.layout.activity_history_merchant_line, reservations);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(HomeActivity.this, DetailReservationActivity.class);
                CasseActivity.putExtra("reservation",reservations.get(position));
                startActivity(CasseActivity);
            }
        });
        reservationController.close();
    }

    private void loadEnAttente() {
        final ArrayList<Reservation> reservations = new ArrayList<>();
        ListView listView = findViewById(R.id.mer_home_list_view_en_attente);
        reservationController = new ReservationController(this);

        reservationController.open();

        reservations.addAll(reservationController.getAllReservations());
        MerchantLineAdapter adapter = new MerchantLineAdapter(HomeActivity.this, R.layout.activity_history_merchant_line, reservations);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(HomeActivity.this, DetailReservationActivity.class);
                CasseActivity.putExtra("reservation",reservations.get(position));
                startActivity(CasseActivity);
            }
        });
        reservationController.close();
    }
}
