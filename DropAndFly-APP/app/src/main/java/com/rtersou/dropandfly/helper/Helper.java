package com.rtersou.dropandfly.helper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rtersou.dropandfly.activities.common.connection.ConnectionActivity;

import java.util.Map;

public class Helper {
    public static final String CURRENT_USER = "currentUser";
    public static final String NAV_USER_HOME = "nav_user_home";
    public static final String NAV_MERCHANT_HOME = "nav_merchant_home";
    public static final String NAV_CONNECTION = "nav_connection";
    public static final String NAV_REGISTER = "nav_register";
    public static final String NAV_SEARCHING = "nav_searching";
    public static final String NAV_USER_HISTORY = "nav_user_history";
    public static final String NAV_MERCHANT_HISTORY = "nav_merchant_history";
    public static final String NAV_DETAIL_RESERVATION = "nav_detail_reservation";
    public static final String NAV_LOADING = "nav_loading";
    public static final String NAV_RESERVATION = "nav_reservation";

    // LOGs
    public static final String LOG_EVENT_REGISTER = "LOG_EVENT-register";
    public static final String DB_EVENT_ADD = "Document added";
    public static final String DB_EVENT_MODIFIED = "Document modified";
    public static final String DB_EVENT_DELETE = "Document deleted";
    public static final String DB_EVENT_GET = "Getting documents";

    public static final String GOOGLE_API_KEY = "AIzaSyCSY3be92dQNox_brS8HV_0-yQwyH9Q_L4";

    public static final String WEB_CLIENT_ID = "1032255356100-3a1jc3kgmpgr6gdu4kdu44pujpaps0r7.apps.googleusercontent.com";

    public static FirebaseUser user;
    public static String userEmail;
    public static Boolean isMerchant;
/*
    public static void addUser(FirebaseFirestore db, FirebaseUser user) {
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Helper.LOG_EVENT_REGISTER, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Helper.LOG_EVENT_REGISTER, "Error adding document", e);
                    }
                });
    }
*/
    // Navigate between activities
    public static void nav(String activity, Class oClass) {
        switch (activity) {
            case NAV_USER_HOME :
                break;
            case NAV_MERCHANT_HOME :
                break;
            case NAV_CONNECTION :
                break;
            case NAV_REGISTER :
                break;
            case NAV_SEARCHING :
                break;
            case NAV_USER_HISTORY :
                break;
            case NAV_DETAIL_RESERVATION :
                break;
            case NAV_MERCHANT_HISTORY :
                break;
            case NAV_LOADING :
                break;
            case NAV_RESERVATION :
                break;
        }
    }

}
