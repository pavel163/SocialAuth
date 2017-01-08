package com.ebr163.socialauth.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Ergashev on 08.01.2017.
 */

public class TwitterClient {

    private static final int TWITTER_AUTH_CODE = 100;
    private static final String TWITTER_CONSUMER_KEY = "twitter_consumer_key";
    private static final String TWITTER_CONSUMER_SECRET = "twitter_consumer_secret";
    private String accessToken;

    private Fragment fragment;
    private Activity activity;
    private TwitterProfileLoadedListener twitterProfileLoadedListener;
    private TwitterLogOutListener twitterLogOutListener;

    public interface TwitterProfileLoadedListener {
        void onTwitterProfileLoaded(TwitterProfile twitterProfile);

        void onErrorTwitterProfileLoaded(Exception exception);
    }

    public interface TwitterLogOutListener {
        void onLogOutTwitter();
    }

    public TwitterClient(Fragment fragment) {
        this(fragment.getActivity());
        this.fragment = fragment;
    }

    public TwitterClient(Activity activity) {
        this.activity = activity;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getStringResByName(TWITTER_CONSUMER_KEY),
                getStringResByName(TWITTER_CONSUMER_SECRET));
        Fabric.with(activity.getApplication(), new Twitter(authConfig));
    }

    public void setTwitterProfileLoadedListener(TwitterProfileLoadedListener twitterProfileLoadedListener) {
        this.twitterProfileLoadedListener = twitterProfileLoadedListener;
    }

    public void setTwitterLogOutListener(TwitterLogOutListener twitterLogOutListener) {
        this.twitterLogOutListener = twitterLogOutListener;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TWITTER_AUTH_CODE) {
                this.accessToken = data.getStringExtra(TwitterAuthActivity.KEY_ACCESS_TOKEN);
                loadUser();
            }
        } else {
            twitterProfileLoadedListener.onErrorTwitterProfileLoaded(new RuntimeException(data.getStringExtra(TwitterAuthActivity.CODE_EXCEPTION)));
        }
    }

    public void getProfile() {
        if (isSessionActive()) {
            loadUser();
            return;
        }
        if (fragment != null) {
            TwitterAuthActivity.authorize(fragment, TWITTER_AUTH_CODE);
        } else if (activity != null) {
            TwitterAuthActivity.authorize(activity, TWITTER_AUTH_CODE);
        }
    }

    private void loadUser() {
        getApiClient()
                .getAccountService()
                .verifyCredentials(true, false)
                .enqueue(new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        TwitterProfile twitterProfile = new TwitterProfile();
                        twitterProfile.id = response.body().id;
                        twitterProfile.name = response.body().name;
                        twitterProfile.screenName = response.body().screenName;
                        twitterProfile.photo = response.body().profileImageUrl;
                        twitterProfile.accessToken = accessToken;
                        twitterProfileLoadedListener.onTwitterProfileLoaded(twitterProfile);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        twitterProfileLoadedListener.onErrorTwitterProfileLoaded(new RuntimeException(t));
                    }
                });
    }

    private boolean isSessionActive() {
        return Twitter.getSessionManager().getActiveSession() != null;
    }

    private TwitterApiClient getApiClient() {
        return Twitter.getApiClient(Twitter.getSessionManager().getActiveSession());
    }

    private Context getContext() {
        if (fragment != null) return fragment.getActivity();
        else return activity;
    }

    private String getStringResByName(String aString) {
        Context context = getContext();
        int resId = context.getResources().getIdentifier(aString, "string", context.getPackageName());
        try {
            return context.getResources().getString(resId);
        } catch (Exception e) {
            twitterProfileLoadedListener.onErrorTwitterProfileLoaded(new Exception("Not find " + aString));
            return null;
        }
    }

    public void logOut() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        twitterLogOutListener.onLogOutTwitter();
    }
}
