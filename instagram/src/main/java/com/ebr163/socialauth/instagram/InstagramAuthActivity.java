package com.ebr163.socialauth.instagram;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ebr163.socialauth.instagram.rest.InstagramConfig;
import com.ebr163.socialauth.instagram.utils.InstagramUtils;

public class InstagramAuthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_auth);
        WebView authWebView = (WebView) findViewById(R.id.authWebView);
        authWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (InstagramUtils.checkIsAuthDone(url)) {
                    setResult(RESULT_OK, InstagramUtils.getResultIntent(url));
                    finish();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        authWebView.loadUrl(InstagramConfig.getInstance().getAuthorizationUrl());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
