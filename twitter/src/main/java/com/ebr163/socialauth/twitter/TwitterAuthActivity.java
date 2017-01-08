package com.ebr163.socialauth.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class TwitterAuthActivity extends AppCompatActivity {

    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String CODE_EXCEPTION = "code_exception";

    private TwitterAuthClient twitterClient;
    private boolean wasShown;

    public static void authorize(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(
                getIntent(fragment.getActivity()),
                requestCode
        );
        fragment.getActivity().overridePendingTransition(0, 0);
    }

    public static void authorize(Activity activity, int requestCode) {
        activity.startActivityForResult(
                getIntent(activity),
                requestCode
        );
        activity.overridePendingTransition(0, 0);
    }

    private static Intent getIntent(Context context) {
        return new Intent(context, TwitterAuthActivity.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!wasShown) {
            wasShown = true;
            twitterClient = new TwitterAuthClient();
            twitterClient.authorize(this,
                    new Callback<TwitterSession>() {
                        @Override
                        public void success(Result<TwitterSession> result) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(KEY_ACCESS_TOKEN, result.data.getAuthToken().toString());
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }

                        @Override
                        public void failure(TwitterException e) {
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra(CODE_EXCEPTION, e.getMessage());
                            setResult(RESULT_CANCELED, resultIntent);
                            finish();
                        }
                    }
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
