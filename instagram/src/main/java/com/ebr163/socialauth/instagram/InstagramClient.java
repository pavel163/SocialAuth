package com.ebr163.socialauth.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

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
    private static final String INSTAGRAM_APP_ID = "instagram_app_id";
    private static final String INSTAGRAM_REDIRECT_URI = "instagram_redirect_uri";

    private Fragment fragment;
    private Activity activity;
    private AuthorizationListener authListener;
    private LogOutListener logOutListener;
    private InstagramLogOutListener instagramLogOutListener;
    private InstagramProfileLoadedListener instagramProfileLoadedListener;

    public interface InstagramProfileLoadedListener {
        void onInstagramProfileLoaded(InstagramProfile instagramProfile);

        void onErrorInstagramProfileLoaded(Exception exception);
    }

    public interface InstagramLogOutListener {
        void onLogOutInstagram();
    }

    private interface AuthorizationListener {
        void onAuthorized();
    }

    private interface LogOutListener {
        void onLogOut();
    }

    public InstagramClient(Fragment fragment) {
        this.fragment = fragment;
        init();
    }

    public InstagramClient(Activity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        InstagramConfig.getInstance().setClientId(getStringResByName(INSTAGRAM_APP_ID));
        InstagramConfig.getInstance().setRedirectUri(getStringResByName(INSTAGRAM_REDIRECT_URI));
    }

    public void setInstagramLogOutListener(InstagramLogOutListener instagramLogOutListener) {
        this.instagramLogOutListener = instagramLogOutListener;
    }

    public void setInstagramProfileLoadedListener(InstagramProfileLoadedListener instagramProfileLoadedListener) {
        this.instagramProfileLoadedListener = instagramProfileLoadedListener;
    }

    public void getProfile() {
        authorize(new AuthorizationListener() {
            @Override
            public void onAuthorized() {
                new InstagramRestClient().getService(InstagramService.class)
                        .getProfile(getAccessToken()).enqueue(new Callback<InstagramResponse<InstagramProfile>>() {
                    @Override
                    public void onResponse(Call<InstagramResponse<InstagramProfile>> call, Response<InstagramResponse<InstagramProfile>> response) {
                        instagramProfileLoadedListener.onInstagramProfileLoaded(response.body().getData());
                    }

                    @Override
                    public void onFailure(Call<InstagramResponse<InstagramProfile>> call, Throwable t) {
                        instagramProfileLoadedListener.onErrorInstagramProfileLoaded(new Exception(t));
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

    private String getStringResByName(String aString) {
        Context context = getContext();
        int resId = context.getResources().getIdentifier(aString, "string", context.getPackageName());
        try {
            return context.getResources().getString(resId);
        } catch (Exception e) {
            instagramProfileLoadedListener.onErrorInstagramProfileLoaded(new Exception("Not find " + aString));
            return null;
        }
    }

    public void logOut() {
        logOutListener = new LogOutListener() {
            @Override
            public void onLogOut() {
                instagramLogOutListener.onLogOutInstagram();
            }
        };

        if (fragment != null) {
            InstagramUtils.openAuthorizationActivity(fragment, INSTAGRAM_LOG_OUT_CODE, InstagramUtils.LOG_OUT);
        } else if (activity != null) {
            InstagramUtils.openAuthorizationActivity(activity, INSTAGRAM_LOG_OUT_CODE, InstagramUtils.LOG_OUT);
        }
    }
}
