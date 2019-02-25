package com.rtersou.dropandfly.activities.common.loading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.helper.FirestoreHelper;
import com.rtersou.dropandfly.helper.Helper;

import java.util.Arrays;
import java.util.List;

import static com.rtersou.dropandfly.helper.Helper.isMerchant;

public class LoadingActivity extends AppCompatActivity {

    FirebaseFirestore db;

    FirebaseAuth mAuth;

    FirestoreHelper firestoreHelper;

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onStart() {
        super.onStart();
/*
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if( currentUser != null ) {
            Log.i("LOG_USER_CONNECTED", currentUser.getDisplayName());
            navHome();
        } else {
            Log.i("LOG_USER_NOT_CONNECTED", "Connexion failed");
        }
*/
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firestoreHelper = new FirestoreHelper();

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


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Helper.userEmail = currentUser.getEmail();
                Helper.isMerchant = false;
                // ...
                //
                if( response.isNewUser() ) {
                    //return user;
                    //firestoreHelper.addData("users", currentUser);
                    db.collection("users")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    navHome();
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

    private void getShopId(){
        db.collection("shops")
                .whereEqualTo("user_id", Helper.userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("shop", MODE_PRIVATE);
                                sharedPreferences
                                        .edit()
                                        .putString("shop_id", document.getId())
                                        .putInt("places", Integer.parseInt(document.get("places").toString()))
                                        .apply();
                            }
                            Intent NewHomeActivity = new Intent(LoadingActivity.this, com.rtersou.dropandfly.activities.merchant.home.HomeActivity.class);
                            startActivity(NewHomeActivity);
                            LoadingActivity.this.finish();
                        } else {
                            Log.w(Helper.DB_EVENT_GET, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void navHome() {
        if(isMerchant){
            getShopId();
        }
        else {
            Intent NewHomeActivity = new Intent(LoadingActivity.this, com.rtersou.dropandfly.activities.user.history.HistoryActivity.class);
            startActivity(NewHomeActivity);
            LoadingActivity.this.finish();
        }
    }


}
