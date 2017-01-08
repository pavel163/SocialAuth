package com.ebr163.socialauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ebr163.socialauth.vk.VkClient;
import com.ebr163.socialauth.vk.model.VkProfile;
import com.vk.sdk.api.VKError;

public class VkActivity extends AppCompatActivity implements View.OnClickListener,
        VkClient.VkProfileLoadedListener,
        VkClient.VkLogOutListener {

    private VkClient vkClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vkClient = new VkClient(this);
        vkClient.setVkLogOutListener(this);
        vkClient.setVkProfileLoadedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vkClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vk:
                vkClient.getProfile();
                break;
            case R.id.vk_logout:
                vkClient.logOut();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogOutVk() {

    }

    @Override
    public void onVkProfileLoaded(VkProfile vkProfile) {

    }

    @Override
    public void onErrorVkProfileLoaded(VKError error) {

    }
}
