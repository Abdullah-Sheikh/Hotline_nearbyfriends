package com.findnearby.hotline;

import static com.findnearby.hotline.PublicVariables.updateMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.findnearby.hotline.Model.People;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoViewActivity extends AppCompatActivity {

    ImageView LogoutBtn;

    private CircleImageView profileImage;
    private TextView userNametxt , userProfessiontxt , userEmailtxt , userBiotxt, userfbtxt, useristatxt, usertwittertxt;
    private Button nameEditCancel , nameEditUpdate , professionEditCancel , professionEditUpdate ,bioEditCancel , bioEditUpdate , userfbEditCancel , userfbEditUpdate ,userinstaEditCancel , userinstaEditUpdate ,usertwitterEditCancel , usertwitterEditUpdate;



    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;

    private ProgressDialog loadingBar;

    private DatabaseReference profileRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_view);


        profileRefrence = FirebaseDatabase.getInstance().getReference().child("Users");

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Users Images");


        loadingBar = new ProgressDialog(this);

        userNametxt = (TextView) findViewById(R.id.profile_name_text);
        userProfessiontxt = (TextView) findViewById(R.id.profile_profession_text);
        userEmailtxt = (TextView) findViewById(R.id.profile_email_text);
        userBiotxt = (TextView) findViewById(R.id.profile_bio_text);
        userfbtxt = (TextView) findViewById(R.id.profile_fbhandler_text);
        useristatxt = (TextView) findViewById(R.id.profile_instahandler_text);
        usertwittertxt = (TextView) findViewById(R.id.profile_t_handler_text);


        profileImage = (CircleImageView) findViewById(R.id.profile_image);


        nameEditCancel = (Button) findViewById(R.id.cancel_edit_name);
        nameEditUpdate = (Button) findViewById(R.id.update_edit_name);
        professionEditCancel = (Button) findViewById(R.id.cancel_edit_profession);
        professionEditUpdate = (Button) findViewById(R.id.update_profession);
        bioEditCancel = (Button) findViewById(R.id.cancel_edit_bio);
        bioEditUpdate = (Button) findViewById(R.id.update_bio);
        userfbEditCancel = (Button) findViewById(R.id.cancel_edit_fbhandler);
        userfbEditUpdate = (Button) findViewById(R.id.update_fbhandler);
        userinstaEditCancel = (Button) findViewById(R.id.cancel_edit_instahandler);
        userinstaEditUpdate = (Button) findViewById(R.id.update_instahandler);
        usertwitterEditCancel = (Button) findViewById(R.id.cancel_edit_t_handler);
        usertwitterEditUpdate = (Button) findViewById(R.id.update_t_handler);


    }





    private void getprofiledetails() {

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users");

        productsRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    People user = dataSnapshot.getValue(People.class);

                    userNametxt.setText(user.getName());
                    userEmailtxt.setText(user.getEmail());
                    userfbtxt.setText(user.getFbId());
                    useristatxt.setText(user.getInstaId());
                    usertwittertxt.setText(user.getTwitterId());
                    userProfessiontxt.setText(user.getProfession());
                    userBiotxt.setText(user.getBio());

                    Picasso.get().load(user.getImage()).into(profileImage);


                    progress.dismiss();


                }
                else {
                    Toast.makeText(PersonalInfoViewActivity.this, "Error.........", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PersonalInfoViewActivity.this, (CharSequence) databaseError, Toast.LENGTH_LONG).show();

            }
        });
    }











    @Override
    protected void onStart() {

        getprofiledetails();

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateMap =1;
        Intent intent = new Intent(PersonalInfoViewActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}