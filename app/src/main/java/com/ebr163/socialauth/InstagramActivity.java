package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ebr163.socialauth.instagram.InstagramClient;
import com.ebr163.socialauth.instagram.model.InstagramProfile;

public class InstagramActivity extends AppCompatActivity implements View.OnClickListener,
        InstagramClient.InstagramProfileLoadedListener,
        InstagramClient.InstagramLogOutListener {

    private InstagramClient instagramClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instagramClient = new InstagramClient(this);
        instagramClient.setInstagramProfileLoadedListener(this);
        instagramClient.setInstagramLogOutListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        instagramClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.instagram:
                instagramClient.getProfile();
                break;
            case R.id.instagram_logout:
                instagramClient.logOut();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogOutInstagram() {

    }

    @Override
    public void onInstagramProfileLoaded(InstagramProfile instagramProfile) {

    }

    @Override
    public void onErrorInstagramProfileLoaded(Exception exception) {

    }
}
