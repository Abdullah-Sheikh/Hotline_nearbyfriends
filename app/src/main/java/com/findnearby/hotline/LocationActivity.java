package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;

public class LocationActivity extends AppCompatActivity {

    GifImageView LoadingGif;
    ImageView StartLocationUpdates , StopLocationUpdates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        LoadingGif = (GifImageView) findViewById(R.id.loading_gif);
        StartLocationUpdates = (ImageView) findViewById(R.id.start_location_updates);
        StopLocationUpdates = (ImageView) findViewById(R.id.stop_location_updates);
        Drawable drawable = LoadingGif.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        }


        StartLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    StartLocationUpdates.setVisibility(View.GONE);
                    LoadingGif.setVisibility(View.VISIBLE);
                    Drawable drawable = LoadingGif.getDrawable();
                    if (drawable instanceof Animatable) {
                        ((Animatable) drawable).start();
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            LoadingGif.setVisibility(View.GONE);
                            StopLocationUpdates.setVisibility(View.VISIBLE);
                           // startLocationService();
                        }
                    }, 1000);

                }

        });
        StopLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StopLocationUpdates.setVisibility(View.GONE);
                LoadingGif.setVisibility(View.VISIBLE);
                Drawable drawable = LoadingGif.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        LoadingGif.setVisibility(View.GONE);
                        StartLocationUpdates.setVisibility(View.VISIBLE);
                       // stopLocationService();
                    }
                }, 1000);
            }
        });

    }




    public void movetoMain(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}