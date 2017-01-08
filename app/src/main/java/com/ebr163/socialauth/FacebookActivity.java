package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ebr163.socialauth.facebook.FacebookClient;
import com.ebr163.socialauth.facebook.model.FacebookProfile;
import com.facebook.FacebookException;

public class FacebookActivity extends AppCompatActivity implements View.OnClickListener,
        FacebookClient.FacebookProfileLoadedListener,
        FacebookClient.FacebookLogOutListener {

    private FacebookClient facebookClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        facebookClient = new FacebookClient(this);
        facebookClient.setFacebookProfileLoadedListener(this);
        facebookClient.setFacebookLogOutListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook:
                facebookClient.getProfile();
                break;
            case R.id.facebook_logout:
                facebookClient.logOut();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogOutFacebook() {

    }

    @Override
    public void onFacebookProfileLoaded(FacebookProfile facebookProfile) {

    }

    @Override
    public void onErrorFacebookProfileLoaded(FacebookException exception) {

    }
}
