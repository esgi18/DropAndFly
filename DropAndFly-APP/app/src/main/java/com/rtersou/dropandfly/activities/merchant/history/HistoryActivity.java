package com.rtersou.dropandfly.activities.merchant.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.Adapaters.MerchantLineAdapter;
import com.rtersou.dropandfly.Adapaters.UserLineAdapter;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.detail_reservation.DetailReservationActivity;
import com.rtersou.dropandfly.activities.merchant.home.HomeActivity;
import com.rtersou.dropandfly.database.ReservationController;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private Button home;
    ArrayList<Reservation> reservationsFinish = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_merchant);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        initFields();
        initListeners();
        getReservations();
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

    private void getReservations(){
        reservationsFinish.clear();
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("shop", MODE_PRIVATE);
        db.collection("reservations")
                .whereEqualTo("shop_id", sharedPreferences.getString("shop_id",""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Reservation reservation = new Reservation(
                                        document.getId(),
                                        document.get("date_start").toString(),
                                        document.get("date_end").toString(),
                                        document.get("h_start").toString(),
                                        document.get("h_end").toString(),
                                        Integer.parseInt(document.get("nb_luggage").toString()),
                                        Integer.parseInt(document.get("statut").toString()),
                                        Integer.parseInt(document.get("price").toString()),
                                        document.get("user_id").toString(),
                                        document.get("shop_id").toString()
                                );
                                if(reservation.getStatut() == 3){
                                    reservationsFinish.add(reservation);
                                }
                            }
                            loadHistory();
                        } else {
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void loadHistory() {

        ListView listView = findViewById(R.id.mer_hist_list_view);

        UserLineAdapter adapter = new UserLineAdapter(HistoryActivity.this, R.layout.activity_history_merchant_line, reservationsFinish);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent DetailReservationActivity = new Intent(HistoryActivity.this, DetailReservationActivity.class);
                DetailReservationActivity.putExtra("reservation",reservationsFinish.get(position));
                startActivity(DetailReservationActivity);
            }
        });
    }
}
