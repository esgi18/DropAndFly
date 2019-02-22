package com.rtersou.dropandfly.activities.common.loading;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.connection.ConnectionActivity;
import com.rtersou.dropandfly.helper.FirestoreHelper;
import com.rtersou.dropandfly.helper.Helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rtersou.dropandfly.helper.Helper.CURRENT_USER;

public class LoadingActivity extends AppCompatActivity {

    FirebaseFirestore db;

    FirebaseAuth mAuth;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onStart() {
        super.onStart();
/*
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            Helper.user = currentUser;
            navHome();
        } else {
            Helper.user = null;
        }*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        db = FirebaseFirestore.getInstance();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Helper.WEB_CLIENT_ID)
                .requestEmail()
                .build();
        // [END config_signin]

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                // ...
                if( response.isNewUser() ) {
                    //return user;
                    FirestoreHelper.addData(db, "users", currentUser);
                }
                navHome();
            } else {
                System.out.println("NOOOOO");
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void navHome() {
        Intent NewHomeActivity = new Intent(LoadingActivity.this, com.rtersou.dropandfly.activities.user.home.HomeActivity.class);
        startActivity(NewHomeActivity);
        LoadingActivity.this.finish();
    }


}
