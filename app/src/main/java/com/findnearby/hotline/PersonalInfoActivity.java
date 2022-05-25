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

public class PersonalInfoActivity extends AppCompatActivity {

    ImageView LogoutBtn;
    private Button updateImg , editImg;
    private CircleImageView profileImage;
    private TextView userNametxt , userProfessiontxt , userEmailtxt , userBiotxt, userfbtxt, userinstatxt, usertwittertxt;
    private EditText userNameedtxt , userProfessionedtxt,userBioedtxt , userfbedtxt , userinstaedtxt , usertwitteredtxt ;
    private ImageView editName , editProfession , editBio , editfb , editinsta , edittwitter;
    private Button nameEditCancel , nameEditUpdate , professionEditCancel , professionEditUpdate ,bioEditCancel , bioEditUpdate , userfbEditCancel , userfbEditUpdate ,userinstaEditCancel , userinstaEditUpdate ,usertwitterEditCancel , usertwitterEditUpdate;
    private CardView nameTextCard , nameEdtxtCard, professionTextCard , professionEdtxtCard,bioTextCard , bioEdtxtCard;
    private CardView fbTextCard , fbEdtxtCard ,instaTextCard , instaEdtxtCard , twitterTextCard , twitterEdtxtCard ;
    private String Name , Phone , Profession , Bio , fb , insta, twitter;

    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;

    private ProgressDialog loadingBar;

    private DatabaseReference profileRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);




        profileRefrence = FirebaseDatabase.getInstance().getReference().child("Users");

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Users Images");



        loadingBar = new ProgressDialog(this);

        userNametxt = (TextView) findViewById(R.id.profile_name_text);
        userProfessiontxt=(TextView)findViewById(R.id.profile_profession_text);
        userEmailtxt=(TextView)findViewById(R.id.profile_email_text);
        userBiotxt =(TextView)findViewById(R.id.profile_bio_text);
        userfbtxt=(TextView)findViewById(R.id.profile_fbhandler_text);
        userinstatxt=(TextView)findViewById(R.id.profile_instahandler_text);
        usertwittertxt=(TextView)findViewById(R.id.profile_t_handler_text);

        userNameedtxt=(EditText)findViewById(R.id.profile_name_edtxt);
        userProfessionedtxt=(EditText)findViewById(R.id.profile_profession_edtxt);
        userBioedtxt=(EditText)findViewById(R.id.profile_bio_edtxt);
        userfbedtxt =(EditText)findViewById(R.id.profile_fbhandler_edtxt);
        userinstaedtxt=(EditText)findViewById(R.id.profile_instahandler_edtxt);
        usertwitteredtxt=(EditText)findViewById(R.id.profile_t_handler_edtxt);

        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        editName = (ImageView) findViewById(R.id.edit_name);
        editProfession= (ImageView)findViewById(R.id.edit_profession);
        editBio =(ImageView) findViewById(R.id.edit_bio);
        editfb= (ImageView)findViewById(R.id.edit_fbhandler);
        editinsta = (ImageView)findViewById(R.id.edit_instahandler);
        edittwitter= (ImageView)findViewById(R.id.edit_t_handler);


        updateImg = (Button) findViewById(R.id.update_img);
        editImg = (Button) findViewById(R.id.editImage);

        nameEditCancel=(Button)findViewById(R.id.cancel_edit_name);
        nameEditUpdate=(Button)findViewById(R.id.update_edit_name);
        professionEditCancel=(Button)findViewById(R.id.cancel_edit_profession);
        professionEditUpdate=(Button)findViewById(R.id.update_profession);
        bioEditCancel=(Button)findViewById(R.id.cancel_edit_bio);
        bioEditUpdate=(Button)findViewById(R.id.update_bio);
        userfbEditCancel=(Button)findViewById(R.id.cancel_edit_fbhandler);
        userfbEditUpdate=(Button)findViewById(R.id.update_fbhandler);
        userinstaEditCancel=(Button)findViewById(R.id.cancel_edit_instahandler);
        userinstaEditUpdate=(Button)findViewById(R.id.update_instahandler);
        usertwitterEditCancel=(Button)findViewById(R.id.cancel_edit_t_handler);
        usertwitterEditUpdate=(Button)findViewById(R.id.update_t_handler);

        nameTextCard = (CardView)findViewById(R.id.name_text_card);
        nameEdtxtCard = (CardView)findViewById(R.id.name_edittext_card);
        professionTextCard = (CardView)findViewById(R.id.profession_text_card);
        professionEdtxtCard = (CardView)findViewById(R.id.profession_edittext_card);
        bioTextCard= (CardView)findViewById(R.id.bio_text_card);
        bioEdtxtCard= (CardView)findViewById(R.id.bio_edittext_card);
        fbTextCard= (CardView)findViewById(R.id.fbhandler_text_card);
        fbEdtxtCard= (CardView)findViewById(R.id.fbhandler_edittext_card);
        instaTextCard= (CardView)findViewById(R.id.instahandler_text_card);
        instaEdtxtCard= (CardView)findViewById(R.id.instahandler_edittext_card);
        twitterTextCard= (CardView)findViewById(R.id.t_handler_text_card);
        twitterEdtxtCard= (CardView)findViewById(R.id.t_handler_edittext_card);


        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
                editImg.setVisibility(View.GONE);
                updateImg.setVisibility(View.VISIBLE);


            }
        });

        updateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImg.setVisibility(View.GONE);
                editImg.setVisibility(View.VISIBLE);
                StoreItemInformation();

            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTextCard.setVisibility(View.GONE);
                nameEdtxtCard.setVisibility(View.VISIBLE);
            }
        });
        nameEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdtxtCard.setVisibility(View.GONE);
                nameTextCard.setVisibility(View.VISIBLE);

            }
        });

        professionEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Profession =  userProfessionedtxt.getText().toString();

                if (TextUtils.isEmpty(Profession))
                {
                    Toast.makeText(PersonalInfoActivity.this, "Write Profession", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatefun("Profession");
                }


            }
        });


        editProfession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professionTextCard.setVisibility(View.GONE);
                professionEdtxtCard.setVisibility(View.VISIBLE);
            }
        });
        professionEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                professionEdtxtCard.setVisibility(View.GONE);
                professionTextCard.setVisibility(View.VISIBLE);

            }
        });

        nameEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name = userNameedtxt.getText().toString();

                if (TextUtils.isEmpty(Name))
                {
                    Toast.makeText(PersonalInfoActivity.this, "Write Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatefun("Name");
                }


            }
        });



        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioTextCard.setVisibility(View.GONE);
                bioEdtxtCard.setVisibility(View.VISIBLE);
            }
        });
        bioEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bioEdtxtCard.setVisibility(View.GONE);
                bioTextCard.setVisibility(View.VISIBLE);

            }
        });

        bioEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bio = userBioedtxt.getText().toString();

                if (TextUtils.isEmpty(Bio))
                {
                    Toast.makeText(PersonalInfoActivity.this, "Write Bio", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatefun("Bio");
                }


            }
        });




        editfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbTextCard.setVisibility(View.GONE);
                fbEdtxtCard.setVisibility(View.VISIBLE);
            }
        });
        userfbEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbEdtxtCard.setVisibility(View.GONE);
                fbTextCard.setVisibility(View.VISIBLE);

            }
        });

        userfbEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fb = userfbedtxt.getText().toString();

                if (TextUtils.isEmpty(fb))
                {
                    Toast.makeText(PersonalInfoActivity.this, "Write Facebook url", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatefun("fbId");
                }


            }
        });


        editinsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instaTextCard.setVisibility(View.GONE);
                instaEdtxtCard.setVisibility(View.VISIBLE);
            }
        });
        userinstaEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instaEdtxtCard.setVisibility(View.GONE);
                instaTextCard.setVisibility(View.VISIBLE);

            }
        });

        userinstaEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insta = userinstaedtxt.getText().toString();

                if (TextUtils.isEmpty(insta))
                {
                    Toast.makeText(PersonalInfoActivity.this, "Write Instagram username", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatefun("instaId");
                }


            }
        });


        edittwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterTextCard.setVisibility(View.GONE);
                twitterEdtxtCard.setVisibility(View.VISIBLE);
            }
        });
        usertwitterEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterEdtxtCard.setVisibility(View.GONE);
                twitterTextCard.setVisibility(View.VISIBLE);

            }
        });

        usertwitterEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                twitter = usertwitteredtxt.getText().toString();

                if (TextUtils.isEmpty(twitter))
                {
                    Toast.makeText(PersonalInfoActivity.this, "Write twitter username", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatefun("twitterId");
                }


            }
        });

    }



    private void updatefun(String data) {


        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while we are updating loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        HashMap<String, Object> productMap = new HashMap<>();
        if(data=="Name") {
            productMap.put("name",Name );
        }
        else if(data=="Phone") {
            productMap.put("phone", Phone);
        }
        else if(data=="Image") {
            productMap.put("image", downloadImageUrl);
        }
        else if(data=="Profession") {
            productMap.put("profession", Profession);
        }
        else if(data=="Bio") {
            productMap.put("bio", Bio);
        }
        else if(data=="fbId") {
            productMap.put("fbId", Bio);
        }
        else if(data=="fbId") {
            productMap.put("fbId", Bio);
        }
        else if(data=="instaId") {
            productMap.put("instaId", Bio);
        }
        else if(data=="twitterId") {
            productMap.put("twitterId", Bio);
        }


        profileRefrence.child(FirebaseAuth.getInstance().getUid()).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();
                            startActivity(getIntent());

                            progress.dismiss();
                            Toast.makeText(PersonalInfoActivity.this, data + "   updated successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progress.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(PersonalInfoActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                    userNameedtxt.setText(user.getName());
                    userProfessiontxt.setText(user.getProfession());
                    userProfessionedtxt.setText(user.getProfession());

                    userEmailtxt.setText(user.getEmail());

                    userBiotxt.setText(user.getBio());
                    userBioedtxt.setText(user.getBio());

                    userfbtxt.setText(user.getFbId());
                    userfbedtxt.setText(user.getFbId());

                    userinstatxt.setText(user.getInstaId());
                    userinstaedtxt.setText(user.getInstaId());

                    usertwittertxt.setText(user.getTwitterId());
                    usertwitteredtxt.setText(user.getTwitterId());



                    if(!user.getImage().equals(""))
                    Picasso.get().load(user.getImage()).into(profileImage);


                    progress.dismiss();


                }
                else {
                    Toast.makeText(PersonalInfoActivity.this, "Error.........", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PersonalInfoActivity.this, (CharSequence) databaseError, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            profileImage.setImageURI(ImageUri);
        }
    }







    private void StoreItemInformation()
    {

        loadingBar.setMessage("loading wait....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        productRandomKey = "User-"+String.valueOf(  new Random().nextInt(12000) + 600)+"-IMG-"+  String.valueOf(  new Random().nextInt(12000) + 600);


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".png");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(PersonalInfoActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(PersonalInfoActivity.this, "Item Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(PersonalInfoActivity.this, "got the item image Url Successfully...", Toast.LENGTH_SHORT).show();

                          //  SaveItemInfoToDatabase();
                            updatefun("Image");
                        }
                    }
                });
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
        Intent intent = new Intent(PersonalInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}