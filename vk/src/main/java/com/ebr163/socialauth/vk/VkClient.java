package com.ebr163.socialauth.vk;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.ebr163.socialauth.vk.model.VkCity;
import com.ebr163.socialauth.vk.model.VkProfile;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ergashev on 07.01.2017.
 */
public class VkClient {

    private static final String[] SCOPE = new String[]{"photos", "offline"};
    private static final String PARAM_PROFILE_FIELDS = "sex,bdate,city,photo_max_orig";

    private Fragment fragment;
    private Activity activity;
    private VkProfileLoadedListener vkProfileLoadedListener;
    private VkLogOutListener vkLogOutListener;

    public interface VkProfileLoadedListener {
        void onVkProfileLoaded(VkProfile vkProfile);

        void onErrorVkProfileLoaded(VKError error);
    }

    public interface VkLogOutListener {
        void logOutVk();
    }

    public VkClient(Fragment fragment) {
        this.fragment = fragment;
    }

    public VkClient(Activity activity) {
        this.activity = activity;
    }

    public void setVkProfileLoadedListener(VkProfileLoadedListener vkProfileLoadedListener) {
        this.vkProfileLoadedListener = vkProfileLoadedListener;
    }

    public void setVkLogOutListener(VkLogOutListener vkLogOutListener) {
        this.vkLogOutListener = vkLogOutListener;
    }

    public void getProfile() {
        if (!VKSdk.isLoggedIn()) {
            authorize();
        } else {
            loadProfile(VKAccessToken.currentToken().accessToken);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(final VKAccessToken res) {
                loadProfile(res.accessToken);
            }

            @Override
            public void onError(VKError error) {
                vkProfileLoadedListener.onErrorVkProfileLoaded(error);
            }
        })) {

        }
    }

    private void authorize() {
        if (fragment != null) {
            VKSdk.login(fragment, SCOPE);
        } else if (activity != null) {
            VKSdk.login(activity, SCOPE);
        }
    }

    private void loadProfile(final String accessToken) {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, PARAM_PROFILE_FIELDS));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                VkProfile profile = new VkProfile();
                profile.accessToken = accessToken;
                try {
                    JSONObject model = ((VKList) response.parsedModel).get(0).fields;
                    profile.id = model.getInt("id");
                    profile.firstName = model.getString("first_name");
                    profile.lastName = model.getString("last_name");
                    profile.gender = model.getInt("sex");
                    profile.birthday = model.getString("bdate");
                    profile.photo = model.getString("photo_max_orig");
                    if (model.has("city")) {
                        VkCity vkCity = new VkCity();
                        vkCity.id = model.getJSONObject("city").getInt("id");
                        vkCity.title = model.getJSONObject("city").getString("title");
                        profile.city = vkCity;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    vkProfileLoadedListener.onErrorVkProfileLoaded(null);
                }
                vkProfileLoadedListener.onVkProfileLoaded(profile);
            }

            @Override
            public void onError(VKError error) {
                vkProfileLoadedListener.onErrorVkProfileLoaded(error);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            }
        });
    }

    public void logOut() {
        VKSdk.logout();
        vkLogOutListener.logOutVk();
    }
}
