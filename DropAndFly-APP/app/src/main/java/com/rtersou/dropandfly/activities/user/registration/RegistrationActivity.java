package com.rtersou.dropandfly.activities.user.registration;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.common.connection.ConnectionActivity;
import com.rtersou.dropandfly.helper.FirestoreHelper;
import com.rtersou.dropandfly.helper.Helper;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import static com.rtersou.dropandfly.helper.Helper.CURRENT_USER;
import static com.rtersou.dropandfly.helper.Helper.NAV_CONNECTION;
import static com.rtersou.dropandfly.helper.Helper.NAV_MERCHANT_HOME;
import static com.rtersou.dropandfly.helper.Helper.NAV_USER_HOME;

public class RegistrationActivity extends AppCompatActivity {

    EditText passwordEdit;
    EditText confpwdEdit;
    EditText emailEdit;
    EditText lastnameEdit;
    EditText firstnameEdit;
    Button registrationBtn;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
        // getting firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initFileds();
        initListeners();
    }


    private void initFileds() {
        passwordEdit    = findViewById(R.id.reg_password_edit);
        confpwdEdit     = findViewById(R.id.reg_confirm_password_edit);
        emailEdit       = findViewById(R.id.reg_email_edit);
        lastnameEdit    = findViewById(R.id.reg_lastname_edit);
        firstnameEdit   = findViewById(R.id.reg_firstname_edit);
        registrationBtn = findViewById(R.id.reg_registration_btn);
    }

    private void initListeners() {
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
            }
        });

    }

    private void Registration(){

        final Map<String, Object> user = checkValidInput();

        if( user == null ) {
            // Champs incorrects
        } else {
            mAuth.createUserWithEmailAndPassword(user.get("email").toString(), user.get("password").toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(Helper.LOG_EVENT_REGISTER, "createUserWithEmail:success");
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                //Helper.addUser(db, currentUser);
                                //FirestoreHelper.addData(db, "users", currentUser);
                                nav(NAV_USER_HOME, currentUser);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("REGISTRATION_EVENT", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    private void nav(String activityNav, FirebaseUser user) {

        switch (activityNav) {
            case NAV_USER_HOME :
                Intent NewHomeActivity = new Intent(RegistrationActivity.this, com.rtersou.dropandfly.activities.user.home.HomeActivity.class);
                NewHomeActivity.putExtra(CURRENT_USER, user);
                startActivity(NewHomeActivity);
                break;
            case NAV_CONNECTION :
                Intent NewConnectionActivity = new Intent(RegistrationActivity.this, com.rtersou.dropandfly.activities.user.home.HomeActivity.class);
                NewConnectionActivity.putExtra(CURRENT_USER, user);
                startActivity(NewConnectionActivity);
                break;
        }


        RegistrationActivity.this.finish();
    }

    private Map<String, Object> checkValidInput() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confPassword = confpwdEdit.getText().toString();
        String lastname = lastnameEdit.getText().toString();
        String firstname = firstnameEdit.getText().toString();

        boolean err = false;

        // Vérif email
        if( email == null || !checkEmailUnique(email) || !checkEmailValid(email) ) {
            err = true;
        }

        // Vérif password
        if( password == null || !checkPassword(password) || !checkConfirmPassword(password, confPassword)) {
            err = true;
        }

        // Vérif lastname
        if( lastname == null || !checkLastname(lastname) ) {
            err = true;
        }

        // Vérif firstname
        if( firstname == null || !checkFirstname(firstname) ) {
            err = true;
        }

        if( err ) {
            return null;
        } else {
            Map<String, Object> user = new HashMap<>();
            user.put("email", email);
            user.put("firstname", firstname);
            user.put("lastname", lastname);
            return user;
        }
    }

    private boolean checkEmailValid(String email) {
        // @TODO : Verification validité email
        return true;
    }

    private boolean checkEmailUnique(String email) {
        // @TODO : Verification email unique
        return true;
    }

    private boolean checkPassword(String password) {
        // @TODO : Vérification validité password
        return true;
    }

    private boolean checkConfirmPassword(String password, String passwordConfirm) {
        // @TODO : Vérification password = passwordConfirm
        return true;
    }

    private boolean checkLastname(String lastname) {
        // @TODO : Vérification validité lastname
        return true;
    }

    private boolean checkFirstname(String firstname) {
        // @TODO : Vérification validité firstname
        return true;
    }
}
