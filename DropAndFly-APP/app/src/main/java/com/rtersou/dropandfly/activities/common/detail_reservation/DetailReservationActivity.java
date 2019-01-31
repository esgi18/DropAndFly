package com.rtersou.dropandfly.activities.common.detail_reservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.models.Reservation;

public class DetailReservationActivity extends AppCompatActivity {

    TextView address1;
    TextView address2;
    TextView date_start;
    TextView h_start;
    TextView date_end;
    TextView h_end;
    TextView luggages;
    TextView price;
    Reservation reservation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reservation);

        initFileds();
        getReservation();
        setReservation();
    }


    private void initFileds() {
        address1 = findViewById(R.id.det_address1);
        address2 = findViewById(R.id.det_address2);
        date_start = findViewById(R.id.det_date_depose);
        h_start = findViewById(R.id.det_heure_depose);
        date_end = findViewById(R.id.det_date_retrait);
        h_end = findViewById(R.id.det_heure_retrait);
        luggages = findViewById(R.id.det_nb_luggages);
        price    = findViewById(R.id.det_prix_calc);
    }

    private void getReservation(){
        Intent intent = getIntent();
        reservation    = (Reservation) intent.getSerializableExtra("reservation");
    }

    private void setReservation(){
        date_start.setText(reservation.getDate_start());
        h_start.setText(reservation.getH_start());

        date_end.setText(reservation.getDate_end());
        h_end.setText(reservation.getH_end());

        luggages.setText(Integer.toString(reservation.getNb_luggage()));

        price.setText("Prix : " + Integer.toString(reservation.getPrice()) + "€");
    }
}
