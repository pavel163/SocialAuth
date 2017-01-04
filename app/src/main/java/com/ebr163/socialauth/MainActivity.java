package com.ebr163.socialauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ebr163.socialauth.google.GooglePlusClient;
import com.ebr163.socialauth.google.GooglePlusProfile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    GooglePlusClient googlePlusClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googlePlusClient = new GooglePlusClient(this, getString(R.string.googleClientId));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_plus:
                googlePlusClient.getProfile(new GooglePlusClient.GooglePlusResultCallback() {
                    @Override
                    public void onProfileLoaded(GooglePlusProfile googlePlusProfile) {
                        Toast.makeText(MainActivity.this, googlePlusProfile.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googlePlusClient.onActivityResult(requestCode, resultCode, data);
    }
}
