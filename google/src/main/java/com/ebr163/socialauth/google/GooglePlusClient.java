package com.ebr163.socialauth.google;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class GooglePlusClient {

    private static final String TAG = "GooglePlus";

    private static final int RC_SIGN_IN = 8;
    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private Fragment fragment;
    private GooglePlusProfileLoadedListener googlePlusProfileLoadedListener;
    private String clientId;

    public interface GooglePlusProfileLoadedListener {
        void onProfileLoaded(GooglePlusProfile googlePlusProfile);
    }

    public interface GooglePlusLogOutListener {
        void logOut(Status status);
    }

    public GooglePlusClient(Activity activity, Fragment fragment, String clientId) {
        this(activity, clientId);
        this.fragment = fragment;
        init();
    }

    public GooglePlusClient(Activity activity, String clientId) {
        this.activity = activity;
        this.clientId = clientId;
        init();
    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this.activity)
                .enableAutoManage((FragmentActivity) activity,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void getProfile(GooglePlusProfileLoadedListener googlePlusProfileLoadedListener) {
        this.googlePlusProfileLoadedListener = googlePlusProfileLoadedListener;
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            return;
        }

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        if (fragment == null) {
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (googlePlusProfileLoadedListener != null) {
                String avatar = null;
                if (account != null && account.getPhotoUrl() != null) {
                    avatar = account.getPhotoUrl().toString();
                }
                if (account != null) {
                    googlePlusProfileLoadedListener.onProfileLoaded(new GooglePlusProfile(
                            account.getDisplayName(),
                            account.getIdToken(),
                            avatar,
                            account.getEmail(),
                            account.getId()));
                }
            }
        } else {
            Log.i(TAG, "onActivityResult: " + result.getStatus().isSuccess() + " " + result.getStatus().getStatusMessage() + " " + result.getStatus().getStatusCode());
        }
    }

    private void free() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) activity);
            mGoogleApiClient.disconnect();
        }
    }

    public void logOut(final GooglePlusLogOutListener googlePlusLogOutListener) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            googlePlusLogOutListener.logOut(status);
                            free();
                        }
                    });
        }
    }

    public void onDestroy() {
        fragment = null;
        activity = null;
    }
}
