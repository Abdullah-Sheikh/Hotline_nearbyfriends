package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyEmailActivity extends AppCompatActivity {

    ImageView backBtn;
    CardView verifyEmailBtn;
    TextView verifyEmailText, goToHomeText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_verify_email);



        backBtn = (ImageView) findViewById(R.id.back_btn);
        verifyEmailBtn = (CardView) findViewById(R.id.verify_email_btn);
        verifyEmailText= (TextView) findViewById(R.id.verify_email_text);
        goToHomeText= (TextView) findViewById(R.id.go_to_home_text);

        firebaseAuth = FirebaseAuth.getInstance();




        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VerifyEmailActivity.this, "Verification Email Sent!",Toast.LENGTH_SHORT );
                        // FirebaseAuth.getInstance().signOut();
                        verifyEmailBtn.setVisibility(View.GONE);
                        verifyEmailText.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        goToHomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}