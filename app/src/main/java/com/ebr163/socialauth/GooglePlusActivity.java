package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ebr163.socialauth.google.GooglePlusClient;
import com.ebr163.socialauth.google.GooglePlusProfile;
import com.google.android.gms.common.api.Status;

public class GooglePlusActivity extends AppCompatActivity implements View.OnClickListener,
        GooglePlusClient.GooglePlusProfileLoadedListener,
        GooglePlusClient.GooglePlusLogOutListener {

    private GooglePlusClient googlePlusClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googlePlusClient = new GooglePlusClient(this);
        googlePlusClient.setGooglePlusProfileLoadedListener(this);
        googlePlusClient.setGooglePlusLogOutListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googlePlusClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_plus:
                googlePlusClient.getProfile();
                break;
            case R.id.google_plus_logout:
                googlePlusClient.logOut();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogOutGooglePlus(Status status) {

    }

    @Override
    public void onGooglePlusProfileLoaded(GooglePlusProfile googlePlusProfile) {

    }

    @Override
    public void onErrorGooglePlusProfileLoaded(Exception exeption) {

    }
}
