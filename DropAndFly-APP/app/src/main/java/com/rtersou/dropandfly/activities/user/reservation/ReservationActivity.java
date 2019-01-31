package com.rtersou.dropandfly.activities.user.reservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.models.Shop;

public class ReservationActivity extends AppCompatActivity {

    TextView name;
    TextView address1;
    TextView address2;
    EditText date_start;
    EditText date_end;
    EditText h_start;
    EditText h_end;
    EditText luggages;
    Button reservation;

    Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        initFields();
        initListeners();
        setAddress();
    }

    private void initFields() {

        name        = findViewById(R.id.res_name);
        address1    = findViewById(R.id.res_address1);
        address2    = findViewById(R.id.res_address2);
        date_start  = findViewById(R.id.res_date_depose);
        h_start     = findViewById(R.id.res_heure_depose);
        date_end    = findViewById(R.id.res_date_retrait);
        h_end       = findViewById(R.id.res_heure_retrait);
        luggages    = findViewById(R.id.res_nb_luggages);
        reservation = findViewById(R.id.res_btn);
    }

    private void initListeners() {
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reservation();
            }
        });
    }

    private void setAddress() {
        Intent intent = getIntent();
        shop = (Shop) intent.getSerializableExtra("shop");
        name.setText(shop.getName());
        address1.setText(shop.getAddress_number() + " " + shop.getAddress_street());
        address2.setText(shop.getAddress_city() + ", " + shop.getAddress_cp());
    }

    private void Reservation() {

    }
}
