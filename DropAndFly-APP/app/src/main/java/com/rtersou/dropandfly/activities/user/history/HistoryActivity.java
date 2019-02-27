package com.rtersou.dropandfly.activities.user.history;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.rtersou.dropandfly.activities.firebase.FirebaseFragment;
import com.rtersou.dropandfly.activities.firebase.MyFirebaseRecyclerViewAdapter;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements ReservationItemFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    Spinner spinner;
    static int itemSelected;
    ReservationItemFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_user);

        initFields();
        initListeners();
    }

    private void initFields(){
        fragment = new ReservationItemFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_reservation, fragment);
        ft.commit();
        spinner = findViewById(R.id.user_hist_spinner);
        fragment = (ReservationItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_reservation);
    }

    private void initListeners(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        itemSelected = pos;
        setFragment();
    }

    public void setFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_reservation, new ReservationItemFragment() );
        fragmentTransaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onListFragmentInteraction(Reservation item) {
        //Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
