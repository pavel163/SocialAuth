package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ebr163.socialauth.twitter.TwitterClient;
import com.ebr163.socialauth.twitter.TwitterProfile;

public class TwitterActivity extends AppCompatActivity implements View.OnClickListener,
        TwitterClient.TwitterProfileLoadedListener,
        TwitterClient.TwitterLogOutListener {

    private TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twitterClient = new TwitterClient(this);
        twitterClient.setTwitterProfileLoadedListener(this);
        twitterClient.setTwitterLogOutListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterClient.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
    public void onLogOutTwitter() {
    }

    @Override
    public void onTwitterProfileLoaded(TwitterProfile twitterProfile) {
    }

    @Override
    public void onErrorTwitterProfileLoaded(Exception exception) {
    }
}
