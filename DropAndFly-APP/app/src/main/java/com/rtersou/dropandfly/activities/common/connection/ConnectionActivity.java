package com.rtersou.dropandfly.activities.common.connection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.user.registration.RegistrationActivity;
import com.rtersou.dropandfly.database.ReservationController;
import com.rtersou.dropandfly.models.Reservation;

public class ConnectionActivity extends AppCompatActivity {

    EditText passwordEdit;
    EditText emailEdit;
    Button connectBtn;
    TextView forgotPwdText;
    TextView signupText;

    private ReservationController reservationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        initFileds();
        initListeners();
        DebugDB.getAddressLog();
        testBDD();
    }


    private void initFileds() {
        passwordEdit = findViewById(R.id.con_password_edit);
        emailEdit = findViewById(R.id.con_email_edit);
        connectBtn = findViewById(R.id.con_connect_btn);
        forgotPwdText = findViewById(R.id.con_forgot_pwd_text);
        signupText = findViewById(R.id.con_signup_link_text);
    }

    private void initListeners() {
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connect();
            }
        });

        forgotPwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPwd();
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });
    }

    private void Connect() {
        Intent NewCasseActivity = new Intent(ConnectionActivity.this, com.rtersou.dropandfly.activities.user.home.HomeActivity.class);
        startActivity(NewCasseActivity);
        ConnectionActivity.this.finish();
    }

    private void ForgotPwd() {
        Intent NewCasseActivity = new Intent(ConnectionActivity.this, com.rtersou.dropandfly.activities.merchant.home.HomeActivity.class);
        startActivity(NewCasseActivity);
        ConnectionActivity.this.finish();
    }

    private void Signup() {


        Intent RegistrationActivity = new Intent(this, RegistrationActivity.class);
        startActivity(RegistrationActivity);
        ConnectionActivity.this.finish();

    }

    private void testBDD(){
        reservationController = new ReservationController(this);
        reservationController.open();

        Reservation reservation = new Reservation("21/01/2019","22/02/2019","10:30","19:45",3,1,18,1,1);


        reservationController.createReservation(reservation);
        reservationController.createReservation(reservation);
        reservationController.createReservation(reservation);
        reservationController.createReservation(reservation);
        reservationController.createReservation(reservation);


        reservationController.close();
    }
}
