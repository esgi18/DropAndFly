package com.rtersou.dropandfly.activities.common.detail_reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.merchant.home.HomeActivity;
import com.rtersou.dropandfly.activities.user.history.HistoryActivity;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;
import com.rtersou.dropandfly.models.Shop;

import java.util.HashMap;
import java.util.Map;

import static com.rtersou.dropandfly.helper.Helper.isMerchant;

public class DetailReservationActivity extends AppCompatActivity {

    TextView title;
    TextView address1;
    TextView address2;
    TextView date_start;
    TextView h_start;
    TextView date_end;
    TextView h_end;
    TextView luggages;
    TextView price;
    Button bAccept;
    Button bCancel;
    Reservation reservation;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reservation);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        initFileds();
        initListeners();
        getReservation();
        getShop();
        setReservation();
        setBtn();
    }



    private void initFileds() {
        title = findViewById(R.id.det2);
        address1 = findViewById(R.id.det_address1);
        address2 = findViewById(R.id.det_address2);
        date_start = findViewById(R.id.det_date_depose);
        h_start = findViewById(R.id.det_heure_depose);
        date_end = findViewById(R.id.det_date_retrait);
        h_end = findViewById(R.id.det_heure_retrait);
        luggages = findViewById(R.id.det_nb_luggages);
        price    = findViewById(R.id.det_prix_calc);
        bAccept = findViewById(R.id.det_accept_btn);
        bCancel = findViewById(R.id.det_cancel_btn);

        if(!isMerchant){
            title.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners(){
        bAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMerchant){
                    switch(reservation.getStatut()){
                        case 0 :
                            acceptReservation();
                            break;
                        case 1 :
                            break;
                        case 2 :
                            break;
                        default:
                            break;
                    }
                }
                else {
                    switch(reservation.getStatut()){
                        case 0 :
                            removeReservation();
                            break;
                        case 1 :
                            depotReservation();
                            break;
                        case 2 :
                            finishReservation();
                            break;
                        default:
                            break;
                    }
                }

            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMerchant){
                    switch(reservation.getStatut()){
                        case 0 :
                            cancelReservation();
                            break;
                        case 1 :
                            depotReservation();
                            break;
                        case 2 :
                            finishReservation();
                            break;
                        case 3 :
                            break;
                        case 4 :
                            break;
                        default:
                            break;
                    }
                }
                else {
                    switch(reservation.getStatut()){
                        case 0 :
                            DetailReservationActivity.this.finish();
                            break;
                        case 1 :
                            break;
                        case 2 :
                            break;
                        case 3 :
                            break;
                        case 4 :
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void getShop(){

        db.collection("shops").document(reservation.getShop_id())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                address1.setText(document.get("address_number").toString() + " "
                                        + document.get("address_street").toString());
                                address2.setText(document.get("address_city").toString() + ", "
                                        + document.get("address_cp").toString());
                            } else {
                            }
                        } else {
                        }
                    }
                });
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

    private void setBtn(){
        if(isMerchant){
            switch(reservation.getStatut()){
                case 0 :
                    bCancel.setEnabled(true);
                    bCancel.setVisibility(View.VISIBLE);
                    bCancel.setText("Refuser");

                    bAccept.setEnabled(true);
                    bAccept.setVisibility(View.VISIBLE);
                    bAccept.setText("Accepter");
                    break;
                case 1 :
                    break;
                case 2 :
                    break;
                case 3 :
                    break;
                case 4 :
                    break;
                default:
                    break;
            }
        }
        else {
            switch(reservation.getStatut()) {
                case 0:
                    bAccept.setEnabled(true);
                    bAccept.setVisibility(View.VISIBLE);
                    bAccept.setText("Annuler");

                    bCancel.setEnabled(true);
                    bCancel.setVisibility(View.VISIBLE);
                    bCancel.setText("Retour");
                    break;
                case 1:
                    bAccept.setEnabled(true);
                    bAccept.setVisibility(View.VISIBLE);
                    bAccept.setText("Déposer");

                    bCancel.setVisibility(View.VISIBLE);
                    bCancel.setText("Annuler");
                    break;
                case 2:
                    bAccept.setEnabled(true);
                    bAccept.setVisibility(View.VISIBLE);
                    bAccept.setText("Retirer");

                    bCancel.setVisibility(View.VISIBLE);
                    bCancel.setText("Annuler");
                    break;
                case 3 :
                    break;
                case 4 :
                    break;
                default:
                    break;
            }
        }
    }

    private void acceptReservation(){
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("shop", MODE_PRIVATE);

        reservation.setStatut(1);

        db.collection("reservations").document(reservation.getId())
                .update("statut",1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showOk("Réservation acceptée");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Erreur réservation en attente");
                    }
                });
    }

    private void depotReservation(){
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("shop", MODE_PRIVATE);

        reservation.setStatut(2);

        db.collection("reservations").document(reservation.getId())
                .update("statut",2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showOk("Consigne Déposée");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Erreur dêpot en attente");
                    }
                });

        db.collection("shops").document(sharedPreferences.getString("shop_id",""))
                .update("places",sharedPreferences.getInt("places",0) - reservation.getNb_luggage())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void cancelReservation(){
        Map<String, Object> resa = new HashMap<>();
        resa.put("statut", 4);

        reservation.setStatut(4);

        db.collection("reservations").document(reservation.getId())
                .update("statut",4)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showOk("Réservation refusée");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Erreur réservation en attente");
                    }
                });

    }

    private void removeReservation(){
        reservation.setStatut(4);

        db.collection("reservations").document(reservation.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showOk("Réservation annulée");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Erreur");
                    }
                });

    }

    private void finishReservation(){
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("shop", MODE_PRIVATE);

        reservation.setStatut(3);

        db.collection("reservations").document(reservation.getId())
                .update("statut",3)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showOk("Réservation terminée");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Erreur réservation en attente");
                    }
                });

        db.collection("shops").document(sharedPreferences.getString("shop_id",""))
                .update("places",sharedPreferences.getInt("places",0) + reservation.getNb_luggage())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
        builder.setTitle("Message");
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if(isMerchant){
                    Intent newIntent = new Intent(DetailReservationActivity.this, HomeActivity.class);
                    startActivity(newIntent);
                    DetailReservationActivity.this.finish();
                }
                else {
                    Intent newIntent = new Intent(DetailReservationActivity.this, HistoryActivity.class);
                    startActivity(newIntent);
                    DetailReservationActivity.this.finish();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
