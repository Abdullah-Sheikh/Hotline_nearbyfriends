package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (FirebaseAuth.getInstance().getCurrentUser() != null  ) {
            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(LaunchActivity.this, VerifyEmailActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }

        }
        else{
            Intent intent = new Intent(LaunchActivity.this, OnboardingActivity.class);
            startActivity(intent);

            finish();

        }
    }
}