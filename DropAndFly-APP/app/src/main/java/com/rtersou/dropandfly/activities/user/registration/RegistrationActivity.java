package com.rtersou.dropandfly.activities.user.registration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rtersou.dropandfly.R;

public class RegistrationActivity extends AppCompatActivity {

    EditText passwordEdit;
    EditText confpwdEdit;
    EditText emailEdit;
    EditText lastnameEdit;
    EditText firstnameEdit;
    Button registrationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
        
    }
}
