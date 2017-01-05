package com.ebr163.socialauth.instagram;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ebr163.socialauth.instagram.model.InstagramProfile;
import com.ebr163.socialauth.instagram.model.InstagramResponse;
import com.ebr163.socialauth.instagram.preferences.InstagramPreferences;
import com.ebr163.socialauth.instagram.rest.InstagramConfig;
import com.ebr163.socialauth.instagram.rest.InstagramRestClient;
import com.ebr163.socialauth.instagram.rest.services.InstagramService;
import com.ebr163.socialauth.instagram.utils.InstagramUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstagramClient {
    private static final int INSTAGRAM_AUTH_CODE = 300;
    private static final int INSTAGRAM_LOG_OUT_CODE = 301;

    private Fragment fragment;
    private Activity activity;
    private AuthorizationListener authListener;
    private LogOutListener logOutListener;

    public interface InstagramProfileLoadedListener<T> {
        void onProfileLoaded(T t);
    }

    public interface InstagramLogOutListener {
        void onLogOut();
    }

    private interface AuthorizationListener {
        void onAuthorized();
    }

    private interface LogOutListener {
        void onLogOut();
    }

    private InstagramClient(String redirectUri, String clientId) {
        InstagramConfig.getInstance().setClientId(clientId);
        InstagramConfig.getInstance().setRedirectUri(redirectUri);
    }

    public InstagramClient(Fragment fragment, String redirectUri, String clientId) {
        this(redirectUri, clientId);
        this.fragment = fragment;
    }

    public InstagramClient(Activity activity, String redirectUri, String clientId) {
        this(redirectUri, clientId);
        this.activity = activity;
    }

    public void getProfile(final InstagramProfileLoadedListener<InstagramProfile> listener) {
        authorize(new AuthorizationListener() {
            @Override
            public void onAuthorized() {
                new InstagramRestClient().getService(InstagramService.class)
                        .getProfile(getAccessToken()).enqueue(new Callback<InstagramResponse<InstagramProfile>>() {
                    @Override
                    public void onResponse(Call<InstagramResponse<InstagramProfile>> call, Response<InstagramResponse<InstagramProfile>> response) {
                        listener.onProfileLoaded(response.body().getData());
                    }

                    @Override
                    public void onFailure(Call<InstagramResponse<InstagramProfile>> call, Throwable t) {
                        Log.i("TAG", "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }

    private String getAccessToken() {
        return InstagramPreferences.getManager().getInstagramAccessToken(getContext());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INSTAGRAM_AUTH_CODE) {
                String token = data.getStringExtra(InstagramUtils.INSTAGRAM_FILED_ACCESS_TOKEN);
                InstagramPreferences.getManager().setInstagramAccessToken(getContext(), token);
                authListener.onAuthorized();
            } else if (requestCode == INSTAGRAM_LOG_OUT_CODE) {
                logOutListener.onLogOut();
            }
        }
    }

    private void authorize(AuthorizationListener listener) {
        authListener = listener;
        if (fragment != null) {
            InstagramUtils.openAuthorizationActivity(fragment, INSTAGRAM_AUTH_CODE, InstagramUtils.LOG_IN);
        } else if (activity != null) {
            InstagramUtils.openAuthorizationActivity(activity, INSTAGRAM_AUTH_CODE, InstagramUtils.LOG_IN);
        }
    }

    private Context getContext() {
        if (fragment != null) return fragment.getActivity();
        else return activity;
    }

    public void logOut(final InstagramLogOutListener instagramLogOutListener) {
        logOutListener = new LogOutListener() {
            @Override
            public void onLogOut() {
                instagramLogOutListener.onLogOut();
            }
        };

        if (fragment != null) {
            InstagramUtils.openAuthorizationActivity(fragment, INSTAGRAM_LOG_OUT_CODE, InstagramUtils.LOG_OUT);
        } else if (activity != null) {
            InstagramUtils.openAuthorizationActivity(activity, INSTAGRAM_LOG_OUT_CODE, InstagramUtils.LOG_OUT);
        }
    }
}
