package com.rtersou.dropandfly.activities.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.rtersou.dropandfly.R;
import com.rtersou.dropandfly.activities.firebase.dummy.DummyContent;

public class FirebaseActivity extends AppCompatActivity implements FirebaseFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }
}
