package com.rtersou.dropandfly.activities.user.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.location.LocationListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.user.searching.SearchingActivity;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private static final String TAG = "MapsActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    private EditText searchView;

    FirebaseFirestore db;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private GoogleMap mMap;


    public ArrayList<Shop> shops;
    HashMap<Marker, Shop> markers = new HashMap<>();

    private void initListeners() {
        searchView = findViewById(R.id.search_widget);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navSearch();
            }
        });
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( isGooglePlayServicesAvailable() ) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        initDb();
        initListeners();
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        setUserId();
        mapFragment.getMapAsync(this);

    }


    private void initDb() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        db.setFirestoreSettings(settings);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))).title("It's Me!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 12.0f));
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    private void getAllShop(){
        db.collection("shops")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Shop> shops = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(Helper.DB_EVENT_GET, document.getId() + " => " + document.getData());
                                Map<String, Object> map = document.getData();

                                shops.add(new Shop(document.getId(), map.get("address_city").toString(), map.get("address_country").toString(), map.get("address_cp").toString(), map.get("address_number").toString(), map.get("address_street").toString(), map.get("lat").toString(), map.get("lng").toString(), map.get("name").toString(), Integer.parseInt(map.get("places").toString()), "0"));

                            }
                            setShops(shops);
                            if( shops.size() > 0 ) {
                                createShopsMarkers();
                            }
                        } else {
                            System.out.println("zapokdazokdazpodjaz");
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void setShops(ArrayList<Shop> shops) {
        this.shops = shops;
        System.out.print(shops);
    }

    public void createShopsMarkers() {
        for( Shop s : shops ) {
            LatLng position = new LatLng(Double.parseDouble(s.getLat()), Double.parseDouble(s.getLng()));
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(position).title(s.getName()));
            markers.put(marker, s);

        }
        updateUI();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        createLocationRequest();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback(), null);
        Log.d(TAG, "Location update started ..............: ");
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Shop shop = markers.get(marker);
        navReservation(shop);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        getAllShop();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        updateUI();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
       // mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }


    public void navReservation(Shop shop) {
        Intent NewReservationActivity = new Intent(MapsActivity.this, com.rtersou.dropandfly.activities.user.searching.SearchingActivity.class);
        NewReservationActivity.putExtra("shop", shop);
        startActivity(NewReservationActivity);
    }


    private void navSearch() {
        Intent NewSearchActivity = new Intent(MapsActivity.this, com.rtersou.dropandfly.activities.user.searching.SearchingActivity.class);
        //NewReservationActivity.putExtra("shop", shop);
        startActivity(NewSearchActivity);
    }

    private void setUserId(){
        db.collection("users")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Helper.userEmail = document.getId();
                            }
                        } else {
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
