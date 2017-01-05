package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ebr163.socialauth.google.GooglePlusClient;
import com.ebr163.socialauth.google.GooglePlusProfile;
import com.ebr163.socialauth.instagram.InstagramClient;
import com.ebr163.socialauth.instagram.model.InstagramProfile;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GooglePlusClient googlePlusClient;
    InstagramClient instagramClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googlePlusClient = new GooglePlusClient(this, getString(R.string.googleClientId));
        instagramClient = new InstagramClient(this, getString(R.string.instagramRedirectUri), getString(R.string.instagramClientId));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_plus:
                googlePlusClient.getProfile(new GooglePlusClient.GooglePlusProfileLoadedListener() {
                    @Override
                    public void onProfileLoaded(GooglePlusProfile googlePlusProfile) {
                        Toast.makeText(MainActivity.this, googlePlusProfile.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.google_plus_logout:
                googlePlusClient.logOut(new GooglePlusClient.GooglePlusLogOutListener() {
                    @Override
                    public void logOut(Status status) {
                        Toast.makeText(MainActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.instagram:
                instagramClient.getProfile(new InstagramClient.InstagramProfileLoadedListener<InstagramProfile>() {
                    @Override
                    public void onProfileLoaded(InstagramProfile instagramProfile) {
                        Toast.makeText(MainActivity.this, instagramProfile.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googlePlusClient.onActivityResult(requestCode, resultCode, data);
        instagramClient.onActivityResult(requestCode, resultCode, data);
    }
}
