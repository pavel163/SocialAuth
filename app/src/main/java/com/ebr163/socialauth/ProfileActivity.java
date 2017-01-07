package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ebr163.socialauth.facebook.FacebookClient;
import com.ebr163.socialauth.google.GooglePlusClient;
import com.ebr163.socialauth.instagram.InstagramClient;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    GooglePlusClient googlePlusClient;
    InstagramClient instagramClient;
    FacebookClient facebookClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        googlePlusClient = new GooglePlusClient(this);
        instagramClient = new InstagramClient(this, getString(R.string.instagramRedirectUri), getString(R.string.instagramClientId));
        facebookClient = new FacebookClient(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_plus_logout:
                break;
            case R.id.instagram_logout:
                instagramClient.logOut(new InstagramClient.InstagramLogOutListener() {
                    @Override
                    public void onLogOut() {
                        signOut();
                    }
                });
                break;
            case R.id.facebook_logout:
                break;
            default:
                break;
        }
    }

    private void signOut() {
        Toast.makeText(ProfileActivity.this, "logout", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        finish();
    }
}