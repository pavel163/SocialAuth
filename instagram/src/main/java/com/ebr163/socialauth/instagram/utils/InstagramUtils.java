package com.ebr163.socialauth.instagram.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.ebr163.socialauth.instagram.InstagramAuthActivity;
import com.ebr163.socialauth.instagram.rest.InstagramConfig;

public class InstagramUtils {
    public static final String INSTAGRAM_FILED_ACCESS_TOKEN = "access_token";
    public static final String CODE_ACTION = "code_action";
    public static final int LOG_IN = 0;
    public static final int LOG_OUT = 1;

    public static void openAuthorizationActivity(Fragment fragment, int requestCode, int codeAction) {
        fragment.startActivityForResult(getAuthIntent(fragment.getActivity(), codeAction), requestCode);
    }

    public static void openAuthorizationActivity(Activity activity, int requestCode, int codeAction) {
        activity.startActivityForResult(getAuthIntent(activity, codeAction), requestCode);
    }

    public static boolean checkIsAuthDone(String url) {
        return url.startsWith(InstagramConfig.getInstance().getRedirectUri());
    }

    public static boolean checkIsLogOutDone(String url) {
        return url.equals("https://www.instagram.com/");
    }

    private static Intent getAuthIntent(Context context, int codeAction) {
        Intent intent = new Intent(context, InstagramAuthActivity.class);
        intent.putExtra(CODE_ACTION, codeAction);
        return intent;
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
