package com.rtersou.dropandfly.activities.user.reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;
import com.rtersou.dropandfly.models.Shop;

public class ReservationActivity extends AppCompatActivity {

    TextView name;
    TextView address1;
    TextView address2;
    EditText jour_start;
    EditText jour_end;
    EditText h_start;
    EditText h_end;
    EditText mois_start;
    EditText mois_end;
    EditText min_start;
    EditText min_end;
    EditText luggages;
    Button reservation;

    Shop shop;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        initFields();
        initListeners();
        setAddress();
    }

    private void initFields() {

        name        = findViewById(R.id.res_name);
        address1    = findViewById(R.id.res_address1);
        address2    = findViewById(R.id.res_address2);
        jour_start  = findViewById(R.id.res_jour_depose);
        h_start     = findViewById(R.id.res_heure_depose);
        jour_end    = findViewById(R.id.res_jour_retrait);
        h_end       = findViewById(R.id.res_heure_retrait);
        mois_start  = findViewById(R.id.res_mois_depose);
        min_start   = findViewById(R.id.res_min_depose);
        mois_end    = findViewById(R.id.res_mois_retrait);
        min_end     = findViewById(R.id.res_min_retrait);
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
        if(verifFields()){
            addReservation();
            ReservationActivity.this.finish();
        }
    }

    private Boolean verifFields(){
        int jour_s  = Integer.parseInt(jour_start.getText().toString());
        int h_s     = Integer.parseInt(h_start.getText().toString());
        int jour_e  = Integer.parseInt(jour_end.getText().toString());
        int mois_s  = Integer.parseInt(mois_start.getText().toString());
        int h_e     = Integer.parseInt(h_end.getText().toString());
        int min_s   = Integer.parseInt(min_start.getText().toString());
        int mois_e  = Integer.parseInt(mois_end.getText().toString());
        int min_e   = Integer.parseInt(min_end.getText().toString());
        int nb_luggages = Integer.parseInt(luggages.getText().toString());

        int date_s = Integer.parseInt(
                mois_start.getText().toString() +
                        jour_start.getText().toString() +
                        h_start.getText().toString() +
                        min_start.getText().toString());

        int date_e = Integer.parseInt(
                mois_end.getText().toString() +
                        jour_end.getText().toString() +
                        h_end.getText().toString() +
                        min_end.getText().toString());


        //Erreur de date
        if(jour_s > 31 || jour_s < 1 ||
                jour_e > 31 || jour_e < 1 ||
                mois_s > 12 || mois_s < 1 ||
                mois_e > 12 || mois_e < 1 ||
                h_e > 24 || h_e < 0 ||
                h_s > 24 || h_s < 0 ||
                min_e > 60 || min_e < 0 ||
                min_s > 60 || min_s < 0){
            showError("Date incorrect");
            return false;
        }

        //Trop de baggages
        else if(nb_luggages > shop.getNb_luggage()){
            showError("Seulement " + shop.getNb_luggage() + " places disponibles chez " + shop.getName());

            return false;
        }

        //date depos aprés retrait
        else if(date_s > date_e){
            showError("La date de retrait est antérieur à celle de dépose");

            return false;
        }

        else {
            return true;
        }

    }

    private Reservation createReservation() {
        Reservation reservation = new Reservation(
                jour_start.getText().toString() + "/" + mois_start.getText().toString(),
                jour_end.getText().toString() + "/" + mois_end.getText().toString(),
                h_start.getText().toString() + ":" + min_start.getText().toString(),
                h_end.getText().toString() + ":" + min_end.getText().toString(),
                Integer.parseInt(luggages.getText().toString()),
                0,
                10,
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                shop.getId()
        );

        return reservation;
    }



    private void addReservation() {
        db = FirebaseFirestore.getInstance();
        db.collection("reservations")
                .add(createReservation())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Helper.DB_EVENT_ADD, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Helper.DB_EVENT_ADD, "Error adding document", e);
                    }
                });

    }

    private void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showOk(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ok");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
