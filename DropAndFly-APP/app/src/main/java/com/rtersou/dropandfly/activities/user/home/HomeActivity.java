package com.rtersou.dropandfly.activities.user.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;
import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.user.searching.SearchingActivity;

import static com.rtersou.dropandfly.helper.Helper.CURRENT_USER;

public class HomeActivity extends AppCompatActivity {

    private EditText address;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        initFileds();
        initListeners();
    }

    private void getUser() {
        Intent intent = getIntent();
        intent.getSerializableExtra(CURRENT_USER);
    }

    private void initFileds() {
        address = findViewById(R.id.user_home_address);
    }

    private void initListeners() {
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SearchingActivity = new Intent(HomeActivity.this, SearchingActivity.class);
                startActivity(SearchingActivity);
            }
        });
    }
}
