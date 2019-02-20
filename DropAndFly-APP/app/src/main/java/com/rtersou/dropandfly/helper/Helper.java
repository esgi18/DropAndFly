package com.rtersou.dropandfly.helper;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.rtersou.dropandfly.activities.common.connection.ConnectionActivity;

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

    public static FirebaseUser user;

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
