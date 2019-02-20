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
import com.rtersou.dropandfly.helper.Helper;

import java.util.HashMap;
import java.util.Map;

import static com.rtersou.dropandfly.helper.Helper.CURRENT_USER;

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
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confPassword = confpwdEdit.getText().toString();
        String lastname = lastnameEdit.getText().toString();
        String firstname = firstnameEdit.getText().toString();

        // @TODO : Verif champs

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("REGISTRATION_EVENT", "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            Helper.user = currentUser;
                            navHome(currentUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("REGISTRATION_EVENT", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        // register user in db
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("firstname", firstname);
        user.put("lastname", lastname);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("REGISTRATION_EVENT", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("REGISTRATION_EVENT", "Error adding document", e);
                    }
                });

    }

    private void navHome(FirebaseUser user) {
        Intent NewHomeActivity = new Intent(RegistrationActivity.this, com.rtersou.dropandfly.activities.user.home.HomeActivity.class);
        NewHomeActivity.putExtra(CURRENT_USER, user);
        startActivity(NewHomeActivity);
        RegistrationActivity.this.finish();
    }
}
