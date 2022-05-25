package com.findnearby.hotline;

import static com.findnearby.hotline.Constants.ACTION_START_LOCATION_SERVICE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import pl.droidsonroids.gif.GifImageView;

public class LocationActivity extends AppCompatActivity {

    GifImageView LoadingGif;
    TextView locationText;
    ImageView StartLocationUpdates , StopLocationUpdates;
    private static int REQUEST_CODE_LOCATION_PERMISSION =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        LoadingGif = (GifImageView) findViewById(R.id.loading_gif);
        StartLocationUpdates = (ImageView) findViewById(R.id.start_location_updates);
        StopLocationUpdates = (ImageView) findViewById(R.id.stop_location_updates);
        locationText = (TextView) findViewById(R.id.location_text);
        Drawable drawable = LoadingGif.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).stop();
        }


        StartLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            LocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {

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

                            startLocationService();
                        }
                    }, 1000);

                }
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
                        stopLocationService();
                    }
                }, 1000);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

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
                        Toast.makeText(LocationActivity.this,"Location Service initiaded ", Toast.LENGTH_LONG);
                        startLocationService();
                    }
                }, 1000);
            }else{
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning()
    {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager!= null){
            for(ActivityManager.RunningServiceInfo service:
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return  true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){

        Toast.makeText(this,"Location Service Method ", Toast.LENGTH_LONG);
        if(! isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(ACTION_START_LOCATION_SERVICE);
            startService(intent);
            locationText.setText("Started");
            Toast.makeText(this,"Location Service started ", Toast.LENGTH_LONG);
        }
    }

    private void stopLocationService(){

        if(isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"Location Service stoped ", Toast.LENGTH_LONG);
        }
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