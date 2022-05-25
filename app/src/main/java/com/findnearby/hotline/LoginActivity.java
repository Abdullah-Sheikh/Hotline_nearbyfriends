package com.findnearby.hotline;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdt , passwordEdt;
    FirebaseAuth firebaseAuth;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEdt =(EditText) findViewById(R.id.login_email);
        passwordEdt = (EditText) findViewById(R.id.login_password);
    }


    private void check()
    {
        if(TextUtils.isEmpty(emailEdt.getText().toString()))
        {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passwordEdt.getText().toString()))
        {
            Toast.makeText(this, "Password is Empty", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loginUser(emailEdt.getText().toString(),passwordEdt.getText().toString());
        }

    }

    void loginUser(String email, String password)
    {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {


                if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this, VerifyEmailActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                // Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(LoginActivity.this)


                                    .setTitle("Your Alert")
                                    .setMessage(e.getMessage().toString())
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Whatever...
                                        }
                                    }).show();
                        }
                    }
                });
            }
        }) ;

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null  )
        {
            if(! FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(LoginActivity.this, VerifyEmailActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }
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
        check();
    }

    public void movetoSignnupPage(View view) {
        Intent  intent= new Intent(LoginActivity.this , SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void forgetPassword(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Reset Password")
                .setIcon(R.drawable.ic_privacy)
                .setMessage("A reset link will be sent to your email.");

// Set up the input
        final EditText input = new EditText(LoginActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                email = input.getText().toString();
                resetPassword();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    void resetPassword()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }
}