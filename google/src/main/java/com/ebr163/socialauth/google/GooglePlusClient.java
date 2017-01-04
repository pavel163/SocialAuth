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

    public GooglePlusClient(Activity activity, Fragment fragment, String clientId) {
        this(activity, clientId);
        this.fragment = fragment;
    }

    public GooglePlusClient(Activity activity, String clientId) {
        this.activity = activity;
        this.clientId = clientId;
    }

    public void getProfile(GooglePlusProfileLoadedListener googlePlusProfileLoadedListener) {
        this.googlePlusProfileLoadedListener = googlePlusProfileLoadedListener;
        if (mGoogleApiClient == null || mGoogleApiClient.isConnected()) {
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
        } else {
            mGoogleApiClient.disconnect();
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
        free();
    }

    void free() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) activity);
            mGoogleApiClient.disconnect();
        }
    }

    public void destroy(){
        fragment = null;
        activity = null;
    }
}
