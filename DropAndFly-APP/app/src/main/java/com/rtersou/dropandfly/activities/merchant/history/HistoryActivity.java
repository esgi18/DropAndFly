package com.rtersou.dropandfly.activities.merchant.history;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.rtersou.dropandfly.Adapaters.MerchantLineAdapter;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.detail_reservation.DetailReservationActivity;
import com.rtersou.dropandfly.activities.merchant.home.HomeActivity;
import com.rtersou.dropandfly.database.ReservationController;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private Button home;
    private ReservationController reservationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_merchant);

        initFields();
        initListeners();
        loadHistory();
    }

    private void initFields(){
        home = findViewById(R.id.merc_hist_home);
    }

    private void initListeners(){

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent HomeActivity = new Intent(HistoryActivity.this, HomeActivity.class);
                startActivity(HomeActivity);
                HistoryActivity.this.finish();
            }
        });

    }




    private void loadHistory() {
        final ArrayList<Reservation> reservations = new ArrayList<>();
        ListView listView = findViewById(R.id.mer_hist_list_view);
        reservationController = new ReservationController(this);

        reservationController.open();

        reservations.addAll(reservationController.getAllReservations());
        MerchantLineAdapter adapter = new MerchantLineAdapter(HistoryActivity.this, R.layout.activity_history_merchant_line, reservations);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(HistoryActivity.this, DetailReservationActivity.class);
                CasseActivity.putExtra("reservation",reservations.get(position));
                startActivity(CasseActivity);
            }
        });
        reservationController.close();
    }
}
