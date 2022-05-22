package com.findnearby.hotline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdt , passwordEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt =(EditText) findViewById(R.id.login_email);
        passwordEdt = (EditText) findViewById(R.id.login_password);
    }

    public void backtoonBoarding(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void movetoHome(View view) {
        Intent intent = new Intent (LoginActivity.this , MainActivity.class);
        startActivity(intent);
    }

    public void movetoSignnupPage(View view) {
        Intent  intent= new Intent(LoginActivity.this , SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void forgetPassword(View view) {
    }
}