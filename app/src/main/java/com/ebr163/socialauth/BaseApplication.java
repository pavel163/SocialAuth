package com.ebr163.socialauth;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Ergashev on 06.01.2017.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
