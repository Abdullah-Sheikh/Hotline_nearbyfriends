package com.findnearby.hotline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.findnearby.hotline.Model.People;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailsActivity extends AppCompatActivity {

    TextView profileDetailsName ,profileDetailsEmail , profileDetailsBio ;
    CircleImageView profileDetailsImage;
    String id ="";
    String fb , insta , twitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        profileDetailsName = (TextView) findViewById(R.id.profile_details_name);
        profileDetailsEmail = (TextView) findViewById(R.id.profile_details_email);
        profileDetailsBio = (TextView) findViewById(R.id.profile_details_bio);
        profileDetailsImage= (CircleImageView) findViewById(R.id.profile_image);

        id= getIntent().getStringExtra("id");

        if(id.equals(null))
        {
            finish();
        }

        getprofiledetails();

    }

    public void movetoMainActivity(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void ViewFb(View view) {
       String uri=fb;
       if(!fb.equals("")) {
           Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

           startActivity(intent);
       }
       else
       {
           Toast.makeText(this, "Facebook id not available", Toast.LENGTH_SHORT).show();
       }
    }

    public void ViewInsta(View view) {
        if(!insta.equals("")) {
            Uri uri = Uri.parse("http://instagram.com/_u/" + insta);
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/" + insta)));
            }
        }
        else
        {
            Toast.makeText(this, "Insta Id not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void Viewtwitter(View view) {
        if(!twitter.equals("")) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter)));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter)));
            }
        }
        else
        {
            Toast.makeText(this, "Twitter Id not available", Toast.LENGTH_SHORT).show();
        }

    }


    private void getprofiledetails() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users");

        productsRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    People user = dataSnapshot.getValue(People.class);

                    profileDetailsName.setText(user.getName());
                    profileDetailsEmail.setText(user.getEmail());
                    profileDetailsBio.setText(user.getBio());
                    fb = user.getFbId();
                    insta = user.getInstaId();
                    twitter = user.getTwitterId();
                    if(!user.getImage().isEmpty())
                    Picasso.get().load(user.getImage()).into(profileDetailsImage);

                    progress.dismiss();


                }
                else {
                    Toast.makeText(ProfileDetailsActivity.this, "Error.........", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( ProfileDetailsActivity.this, (CharSequence) databaseError, Toast.LENGTH_LONG).show();

            }
        });
    }
}