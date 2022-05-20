package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


       Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}