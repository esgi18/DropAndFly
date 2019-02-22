package com.rtersou.dropandfly.activities.user.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseUser;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.loading.LoadingActivity;
import com.rtersou.dropandfly.activities.user.searching.SearchingActivity;
import com.rtersou.dropandfly.helper.FirestoreHelper;
import com.rtersou.dropandfly.helper.Helper;

import static com.rtersou.dropandfly.helper.Helper.CURRENT_USER;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText address;
    private TextView disconnect_btn;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        Intent NewMapsActivity = new Intent(HomeActivity.this, com.rtersou.dropandfly.activities.user.home.MapsActivity.class);
        startActivity(NewMapsActivity);
        HomeActivity.this.finish();
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);*/


        //initFileds();
        initListeners();
    }

    private void getUser() {
        Intent intent = getIntent();
        intent.getSerializableExtra(CURRENT_USER);
    }
    /*
    private void initFileds() {
        address = findViewById(R.id.user_home_address);
        disconnect_btn = findViewById(R.id.disconnect_btn);
    }
    */
    private void initListeners() {
/*        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SearchingActivity = new Intent(HomeActivity.this, SearchingActivity.class);
                startActivity(SearchingActivity);
            }
        });

        disconnect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHelper.disconnect();
                Intent LoadingActivity = new Intent(HomeActivity.this, LoadingActivity.class);
                startActivity(LoadingActivity);
            }
        });*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
