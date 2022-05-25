package com.findnearby.hotline;

import static com.findnearby.hotline.PublicVariables.piCheck;
import static com.findnearby.hotline.PublicVariables.updateMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.findnearby.hotline.fragments.HomeFragment;
import com.findnearby.hotline.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        fragment = new HomeFragment();

        BottomNavigationView navigationView = findViewById(R.id.navigation_view_id);
        navigationView.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
              fragment).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;



                    switch (item.getItemId())
                    {
                        case R.id.home_id:

                            updateMap =1;
                            piCheck = true;
                            Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.home_drawable_icon);
                            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                            DrawableCompat.setTint(wrappedDrawable, Color.BLUE);
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.profile_id:
                            piCheck = false;
                            updateMap =0;
                           selectedFragment = null;
                            selectedFragment = new ProfileFragment();
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public void trackLocation(View view) {

        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
        startActivity(intent);
    }
}