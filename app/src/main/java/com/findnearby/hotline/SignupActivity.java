package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText fullNameEdt, emailEdt , passwordEdt , confirmPasswordEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullNameEdt = (EditText) findViewById(R.id.signup_full_name);
        emailEdt =(EditText) findViewById(R.id.signup_email);
        passwordEdt = (EditText) findViewById(R.id.signup_password);
        confirmPasswordEdt = (EditText) findViewById(R.id.signup_confirm_password);
    }

    public void movetoonboarding(View view) {
        finish();
    }

    public void movetoLoginpage(View view) {
        Intent  intent= new Intent(SignupActivity.this , LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void Signup_btn(View view) {
    }
}