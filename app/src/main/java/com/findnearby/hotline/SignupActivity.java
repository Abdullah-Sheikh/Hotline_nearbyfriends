package com.findnearby.hotline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText fullNameEdt, emailEdt , passwordEdt , confirmPasswordEdt;
    FirebaseAuth firebaseAuth;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        LoadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

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


    private void check() {

        String fullName = fullNameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String confirmPassword = confirmPasswordEdt.getText().toString();


        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(this, "Full Name is Empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password is Empty", Toast.LENGTH_SHORT).show();
        }
       else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Confirm password is Empty", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword))
        {
            confirmPasswordEdt.setError("Password do not match");
        }

        else{
            LoadingBar.setTitle("Create Account");
            LoadingBar.setMessage("Please wait ! we are checking the credentials");
            LoadingBar.setCanceledOnTouchOutside(false);
            LoadingBar.show();
            //  StoreUserInfo( username, email, phone, password);
            createUser( fullName, email, password);
        }

    }

    void createUser(String fullName, String email, String password)
    {




        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                StoreUserInfo( fullName, email);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()){
                            new AlertDialog.Builder(SignupActivity.this)


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
        });
    }

    private void StoreUserInfo(String fullName, String email) {

        FirebaseDatabase database=  FirebaseDatabase.getInstance();
        final DatabaseReference RootRef;
        RootRef = database.getReference().child("Users");
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child(FirebaseAuth.getInstance().getUid()).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();

                    userdataMap.put("name", fullName);
                    userdataMap.put("email", email);
                    userdataMap.put("block", "no");
                    userdataMap.put("fbId", "empty");
                    userdataMap.put("instaId", "empty");
                    userdataMap.put("twitterId", "empty");
                    userdataMap.put("bio", "none");

                    RootRef.child(FirebaseAuth.getInstance().getUid()).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Congratulations, your account is created", Toast.LENGTH_SHORT).show();
                                        LoadingBar.dismiss();
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        LoadingBar.dismiss();
                                        Toast.makeText(SignupActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                                        //layout1.setVisibility(View.VISIBLE);
                                        // layout2.setVisibility(View.GONE);

                                    }
                                }
                            });


                } else {
                    Toast.makeText(SignupActivity.this, "This email " + FirebaseAuth.getInstance().getCurrentUser().getEmail().toString() + " Already exits. ", Toast.LENGTH_SHORT).show();
                    LoadingBar.dismiss();
                    Toast.makeText(SignupActivity.this, "Please try another Email ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}