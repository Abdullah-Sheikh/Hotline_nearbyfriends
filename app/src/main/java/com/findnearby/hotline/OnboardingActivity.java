package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
    }

    public void movetoLogin(View view) {
        Intent intent = new Intent(OnboardingActivity.this , LoginActivity.class);
        startActivity(intent);
    }

    public void movetoSignup(View view) {
        Intent intent = new Intent(OnboardingActivity.this , SignupActivity.class);
        startActivity(intent);
    }
}