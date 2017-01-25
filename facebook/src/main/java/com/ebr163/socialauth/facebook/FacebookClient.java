package com.ebr163.socialauth.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ebr163.socialauth.facebook.model.FacebookProfile;
import com.ebr163.socialauth.facebook.model.Location;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ergashev on 06.01.2017.
 */
public class FacebookClient {

    private Collection<String> facebookPermissions = Arrays.asList("public_profile", "email", "user_birthday", "user_friends");

    private Activity activity;
    private Fragment fragment;
    private CallbackManager callbackManager;
    private FacebookProfileLoadedListener facebookProfileLoadedListener;
    private FacebookLogOutListener facebookLogOutListener;

    public interface FacebookProfileLoadedListener {
        void onFacebookProfileLoaded(FacebookProfile facebookProfile);

        void onErrorFacebookProfileLoaded(FacebookException exception);
    }

    public interface FacebookLogOutListener {
        void onLogOutFacebook();
    }

    public void setFacebookProfileLoadedListener(FacebookProfileLoadedListener facebookProfileLoadedListener) {
        this.facebookProfileLoadedListener = facebookProfileLoadedListener;
    }

    public void setFacebookLogOutListener(FacebookLogOutListener facebookLogOutListener) {
        this.facebookLogOutListener = facebookLogOutListener;
    }

    public FacebookClient(Activity activity) {
        this.activity = activity;
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                FacebookProfile facebookProfile = new FacebookProfile();
                                facebookProfile.accessToken = loginResult.getAccessToken().getToken();
                                try {
                                    facebookProfile.id = object.getString("id");
                                    facebookProfile.birthday = object.getString("birthday");
                                    facebookProfile.firstName = object.getString("first_name");
                                    facebookProfile.gender = object.getString("gender");
                                    facebookProfile.lastName = object.getString("last_name");
                                    facebookProfile.link = object.getString("link");
                                    facebookProfile.locale = object.getString("locale");
                                    facebookProfile.name = object.getString("name");
                                    facebookProfile.timezone = object.getString("timezone");

                                    if (object.has("location")) {
                                        Location location = new Location();
                                        location.id = object.getJSONObject("location").getString("id");
                                        location.name = object.getJSONObject("location").getString("name");
                                        facebookProfile.location = location;
                                    }

                                    if (object.has("picture")) {
                                        facebookProfile.picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                facebookProfileLoadedListener.onFacebookProfileLoaded(facebookProfile);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,birthday,first_name,gender,last_name,link,location,locale,name,timezone,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                facebookProfileLoadedListener.onErrorFacebookProfileLoaded(exception);
            }
        });
    }

    public FacebookClient(Fragment fragment) {
        this(fragment.getActivity());
        this.fragment = fragment;
    }

    public void setFacebookPermissions(List<String> permissions) {
        this.facebookPermissions = permissions;
    }

    public void getProfile() {
        if (fragment == null) {
            LoginManager.getInstance().logInWithReadPermissions(activity, facebookPermissions);
        } else {
            LoginManager.getInstance().logInWithReadPermissions(fragment, facebookPermissions);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logOut() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
                facebookLogOutListener.onLogOutFacebook();
            }
        }).executeAsync();
    }
}
