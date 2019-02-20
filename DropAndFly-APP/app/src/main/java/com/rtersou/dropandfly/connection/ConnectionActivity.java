package com.rtersou.dropandfly.connection;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.firebase.FirebaseActivity;
import com.rtersou.dropandfly.registration.RegistrationActivity;

import java.util.List;

public class ConnectionActivity extends AppCompatActivity {

    EditText passwordEdit;
    EditText emailEdit;
    Button connectBtn;
    TextView forgotPwdText;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        initFileds();
        initListeners();
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

    }

    private void ForgotPwd() {

    }

    private void Signup() {

        Intent ListActivity = new Intent(this, FirebaseActivity.class);
        startActivity(ListActivity);
        ConnectionActivity.this.finish();

        /** @TODO : A remettre

        Intent RegistrationActivity = new Intent(this, RegistrationActivity.class);
        startActivity(RegistrationActivity);
        ConnectionActivity.this.finish();
         */
    }
}
