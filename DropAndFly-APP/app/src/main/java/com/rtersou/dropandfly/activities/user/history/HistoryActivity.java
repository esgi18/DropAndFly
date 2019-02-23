package com.rtersou.dropandfly.activities.user.history;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.Adapaters.UserLineAdapter;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.detail_reservation.DetailReservationActivity;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    ArrayList<Reservation> reservationsStarted = new ArrayList<>();
    ArrayList<Reservation> reservationsWait = new ArrayList<>();
    ArrayList<Reservation> reservationsFinish = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_user);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        loadFinish();
        loadStarted();
        loadWait();
    }

    private void getReservations(){
        db.collection("reservation")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getEmail())
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

                                switch (reservation.getStatut()){
                                    case 0 :
                                        reservationsWait.add(reservation);
                                        break;
                                    case 1 :
                                        reservationsStarted.add(reservation);
                                        break;
                                    case 2 :
                                        reservationsFinish.add(reservation);
                                        break;
                                    default :
                                        break;
                                }
                            }
                        } else {
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void loadWait() {

        ListView listView = findViewById(R.id.user_hist_list_view_en_attente);

        UserLineAdapter adapter = new UserLineAdapter(HistoryActivity.this, R.layout.activity_history_user_line, reservationsWait);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(HistoryActivity.this, DetailReservationActivity.class);
                CasseActivity.putExtra("reservation",reservationsStarted.get(position));
                startActivity(CasseActivity);
            }
        });
    }

    private void loadStarted() {

        ListView listView = findViewById(R.id.user_hist_list_view_en_cours);

        UserLineAdapter adapter = new UserLineAdapter(HistoryActivity.this, R.layout.activity_history_user_line, reservationsStarted);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(HistoryActivity.this, DetailReservationActivity.class);
                CasseActivity.putExtra("reservation",reservationsStarted.get(position));
                startActivity(CasseActivity);
            }
        });
    }

    private void loadFinish() {

        ListView listView = findViewById(R.id.user_hist_list_view_passé);

        UserLineAdapter adapter = new UserLineAdapter(HistoryActivity.this, R.layout.activity_history_user_line, reservationsFinish);
        listView.setAdapter(adapter);


        // Ecoute des clicks sur les lignes
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent CasseActivity = new Intent(HistoryActivity.this, DetailReservationActivity.class);
                CasseActivity.putExtra("reservation",reservationsFinish.get(position));
                startActivity(CasseActivity);
            }
        });
    }
}
