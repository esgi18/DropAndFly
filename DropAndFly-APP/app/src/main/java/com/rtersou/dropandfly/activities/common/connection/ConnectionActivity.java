package com.rtersou.dropandfly.activities.common.connection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.user.registration.RegistrationActivity;
import com.rtersou.dropandfly.database.ReservationController;
import com.rtersou.dropandfly.helper.Helper;
import com.rtersou.dropandfly.models.Reservation;
import com.rtersou.dropandfly.models.User;

import static com.rtersou.dropandfly.helper.Helper.CURRENT_USER;
import static com.rtersou.dropandfly.helper.Helper.NAV_USER_HOME;

public class ConnectionActivity extends AppCompatActivity {

    EditText passwordEdit;
    EditText emailEdit;
    Button connectBtn;
    TextView forgotPwdText;
    TextView signupText;

    // declare Firebase instance
    FirebaseAuth mAuth;

    private ReservationController reservationController;

    @Override
    protected void onStart() {
        super.onStart();

        /*
        @TODO : Fonction HELPER.checkConnection()

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            Helper.user = currentUser;
            navHome(currentUser);
        } else {
            Helper.user = null;
        }
        */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // Initialize firebaseAuth Instance
        mAuth = FirebaseAuth.getInstance();
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
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("CONNECT_EVENT", "signInWithEmail:Success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            navHome(currentUser);
                        } else {
                            Log.w("CONNECT_EVENT", "signInWithEmail:failure", task.getException());
                            Toast.makeText(ConnectionActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
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

    private void navHome(FirebaseUser user) {
        Intent NewHomeActivity = new Intent(ConnectionActivity.this, com.rtersou.dropandfly.activities.user.home.HomeActivity.class);
        NewHomeActivity.putExtra(CURRENT_USER, user);
        startActivity(NewHomeActivity);
        ConnectionActivity.this.finish();
    }
}
