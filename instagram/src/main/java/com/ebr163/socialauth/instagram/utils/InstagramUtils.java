package com.ebr163.socialauth.instagram.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.ebr163.socialauth.instagram.InstagramAuthActivity;
import com.ebr163.socialauth.instagram.rest.InstagramConfig;

public class InstagramUtils {
    public static final String INSTAGRAM_FILED_ACCESS_TOKEN = "access_token";

    public static void openAuthorizationActivity(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getAuthIntent(fragment.getActivity()), requestCode);
    }

    public static void openAuthorizationActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(getAuthIntent(activity), requestCode);
    }

    public static boolean checkIsAuthDone(String url) {
        return url.startsWith(InstagramConfig.getInstance().getRedirectUri());
    }

    private static Intent getAuthIntent(Context context) {
        return new Intent(context, InstagramAuthActivity.class);
    }

    public static Intent getResultIntent(String url) {
        String[] response_array = responseParse(url);
        Intent data = new Intent();
        data.putExtra(INSTAGRAM_FILED_ACCESS_TOKEN, response_array[2]);
        return data;
    }

    private static String[] responseParse(String url) {
        return url.split("[=#&]");
    }
}
