package com.ebr163.socialauth.instagram.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class InstagramPreferences {
    private static final String PREFERENCES_NAME = "preferences_";
    private static final String INSTAGRAM_PREFERENCES_NAME = PREFERENCES_NAME + "instagram_";
    private static final String PREFERENCES_ACCESS_TOKEN = INSTAGRAM_PREFERENCES_NAME + "instagram_access_token";

    private InstagramPreferences() {
    }

    private static InstagramPreferences manager = new InstagramPreferences();

    public static InstagramPreferences getManager() {
        return manager;
    }

    private SharedPreferences getReader(Context context) {
        return context.getSharedPreferences(INSTAGRAM_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor(Context context) {
        return context.getSharedPreferences(INSTAGRAM_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
    }

    public String getInstagramAccessToken(Context context) {
        return getReader(context).getString(PREFERENCES_ACCESS_TOKEN, null);
    }

    public void setInstagramAccessToken(Context context, String accessToken) {
        getEditor(context).putString(PREFERENCES_ACCESS_TOKEN, accessToken).commit();
    }
}
