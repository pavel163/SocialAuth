package com.ebr163.socialauth;

import android.content.Context;

import com.ebr163.socialauth.vk.VkApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Ergashev on 06.01.2017.
 */

public class BaseApplication extends VkApplication {

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        refWatcher = LeakCanary.install(this);


    }

    @Override
    protected void tokenIsInvalid() {

    }
}
