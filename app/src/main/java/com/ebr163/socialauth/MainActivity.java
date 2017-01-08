package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ebr163.socialauth.facebook.FacebookClient;
import com.ebr163.socialauth.facebook.model.FacebookProfile;
import com.ebr163.socialauth.google.GooglePlusClient;
import com.ebr163.socialauth.google.GooglePlusProfile;
import com.ebr163.socialauth.instagram.InstagramClient;
import com.ebr163.socialauth.instagram.model.InstagramProfile;
import com.ebr163.socialauth.twitter.TwitterClient;
import com.ebr163.socialauth.twitter.TwitterProfile;
import com.ebr163.socialauth.vk.VkClient;
import com.ebr163.socialauth.vk.model.VkProfile;
import com.facebook.FacebookException;
import com.google.android.gms.common.api.Status;
import com.vk.sdk.api.VKError;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        VkClient.VkProfileLoadedListener, VkClient.VkLogOutListener,
        FacebookClient.FacebookProfileLoadedListener, FacebookClient.FacebookLogOutListener,
        GooglePlusClient.GooglePlusProfileLoadedListener, GooglePlusClient.GooglePlusLogOutListener,
        InstagramClient.InstagramProfileLoadedListener, InstagramClient.InstagramLogOutListener,
        TwitterClient.TwitterProfileLoadedListener, TwitterClient.TwitterLogOutListener {

    GooglePlusClient googlePlusClient;
    InstagramClient instagramClient;
    FacebookClient facebookClient;
    VkClient vkClient;
    TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googlePlusClient = new GooglePlusClient(this);
        googlePlusClient.setGooglePlusProfileLoadedListener(this);
        googlePlusClient.setGooglePlusLogOutListener(this);

        instagramClient = new InstagramClient(this);
        instagramClient.setInstagramProfileLoadedListener(this);
        instagramClient.setInstagramLogOutListener(this);

        facebookClient = new FacebookClient(this);
        facebookClient.setFacebookProfileLoadedListener(this);
        facebookClient.setFacebookLogOutListener(this);

        vkClient = new VkClient(this);
        vkClient.setVkLogOutListener(this);
        vkClient.setVkProfileLoadedListener(this);

        twitterClient = new TwitterClient(this);
        twitterClient.setTwitterProfileLoadedListener(this);
        twitterClient.setTwitterLogOutListener(this);
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
            case R.id.instagram:
                instagramClient.getProfile();
                break;
            case R.id.instagram_logout:
                instagramClient.logOut();
                break;
            case R.id.facebook:
                facebookClient.getProfile();
                break;
            case R.id.facebook_logout:
                facebookClient.logOut();
                break;
            case R.id.vk:
                vkClient.getProfile();
                break;
            case R.id.vk_logout:
                vkClient.logOut();
                break;
            case R.id.twitter:
                twitterClient.getProfile();
                break;
            case R.id.twitter_logout:
                twitterClient.logOut();
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
        facebookClient.onActivityResult(requestCode, resultCode, data);
        vkClient.onActivityResult(requestCode, resultCode, data);
        twitterClient.onActivityResult(requestCode, resultCode, data);
    }

    private void toNextScreen() {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        finish();
    }

    @Override
    public void onLogOutVk() {
        Toast.makeText(MainActivity.this, "vk logout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVkProfileLoaded(VkProfile vkProfile) {
        Toast.makeText(MainActivity.this, "vk", Toast.LENGTH_SHORT).show();
        toNextScreen();
    }

    @Override
    public void onErrorVkProfileLoaded(VKError error) {
    }

    @Override
    public void onLogOutFacebook() {
        Toast.makeText(MainActivity.this, "facebook logout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFacebookProfileLoaded(FacebookProfile facebookProfile) {
        Toast.makeText(MainActivity.this, "facebook", Toast.LENGTH_SHORT).show();
        toNextScreen();
    }

    @Override
    public void onErrorFacebookProfileLoaded(FacebookException exception) {
    }

    @Override
    public void onLogOutGooglePlus(Status status) {
        Toast.makeText(MainActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGooglePlusProfileLoaded(GooglePlusProfile googlePlusProfile) {
        Toast.makeText(MainActivity.this, googlePlusProfile.toString(), Toast.LENGTH_SHORT).show();
        toNextScreen();
    }

    @Override
    public void onErrorGooglePlusProfileLoaded(Exception exeption) {
    }

    @Override
    public void onLogOutInstagram() {
        Toast.makeText(MainActivity.this, "instagram logout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInstagramProfileLoaded(InstagramProfile instagramProfile) {
        Toast.makeText(MainActivity.this, instagramProfile.toString(), Toast.LENGTH_SHORT).show();
        toNextScreen();
    }

    @Override
    public void onErrorInstagramProfileLoaded(Exception exception) {
    }

    @Override
    public void onLogOutTwitter() {
        Toast.makeText(MainActivity.this, "twitter logout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTwitterProfileLoaded(TwitterProfile twitterProfile) {
        Toast.makeText(MainActivity.this, "twitter", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorTwitterProfileLoaded(Exception exception) {
    }
}
