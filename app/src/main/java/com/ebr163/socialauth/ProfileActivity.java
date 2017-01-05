package com.ebr163.socialauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ebr163.socialauth.google.GooglePlusClient;
import com.google.android.gms.common.api.Status;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    GooglePlusClient googlePlusClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        googlePlusClient = new GooglePlusClient(this, getString(R.string.googleClientId));
    }

    @Override
    public void onClick(View view) {
        googlePlusClient.logOut(new GooglePlusClient.GooglePlusLogOutListener() {
            @Override
            public void logOut(Status status) {
                Toast.makeText(ProfileActivity.this, "logout", Toast.LENGTH_SHORT).show();
            }
        });
    }
}