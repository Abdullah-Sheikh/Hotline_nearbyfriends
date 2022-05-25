package com.findnearby.hotline.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.findnearby.hotline.Model.People;
import com.findnearby.hotline.OnboardingActivity;
import com.findnearby.hotline.PersonalInfoActivity;
import com.findnearby.hotline.PersonalInfoViewActivity;
import com.findnearby.hotline.ProfileDetailsActivity;
import com.findnearby.hotline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    LinearLayout LogoutBtn;
    LinearLayout PersonalInfo , privacyPolicy , contentPolicy;
    TextView profileName , profileProfession , profileLocation;
    CircleImageView profileImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        PersonalInfo = (LinearLayout) view.findViewById(R.id.movetoprofileinfo);
        privacyPolicy = (LinearLayout) view.findViewById(R.id.privacy_policy);
        contentPolicy = (LinearLayout) view.findViewById(R.id.content_policy);
        LogoutBtn =(LinearLayout) view.findViewById(R.id.logoutApp);

        profileName = (TextView) view.findViewById(R.id.fragment_profile_name);
        profileProfession = (TextView) view.findViewById(R.id.fragment_profile_profession);
        profileLocation = (TextView) view.findViewById(R.id.fragment_profile_location);

        profileImg = (CircleImageView) view.findViewById(R.id.fragment_profile_img);

        LogoutBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
               Intent intent = new Intent(getContext(), OnboardingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Privacy Policy")
                        .setMessage(R.string.privacy_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.privacy_icon_black)
                        .show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setScroller(new Scroller(getContext()));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
            }
        });

        contentPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Content Policy")
                        .setMessage(R.string.privacy_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.contantpolicy_icon_black)
                        .show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setScroller(new Scroller(getContext()));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
            }
        });

        PersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), PersonalInfoViewActivity.class);
                startActivity(intent);


            }
        });

        return  view;
    }


    private void getprofiledetails() {

        ProgressDialog progress = new ProgressDialog(getContext());
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

                    profileName.setText(user.getName());
                    profileProfession.setText(user.getProfession());
                    if(!user.getLatitude().equals(0.0))
                    getAddress(user.getLatitude(),user.getLongitude());
                    //profileDetailsBio.setText(user.getBio());
                    if(!user.getImage().isEmpty())
                        Picasso.get().load(user.getImage()).into(profileImg);

                    progress.dismiss();


                }
                else {
                    Toast.makeText(getContext(), "Error.........", Toast.LENGTH_SHORT).show();
                    progress.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( getContext(), (CharSequence) databaseError, Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getprofiledetails();

    }

    public String getAddress(double lat, double lng) {
        String add = "";
        if(getContext()!= null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                Address obj = addresses.get(0);


                add = add + obj.getCountryName();
                add = add + ", " + obj.getAdminArea();
                add = add + ", " + obj.getSubAdminArea();
                add = add + ", " + obj.getLocality();
                add = add + ", " + obj.getSubThoroughfare();


                profileLocation.setText(add);
                //  Log.v("IGA", "Address" + add);
                // Toast.makeText(this, "Address=>" + add,
                // Toast.LENGTH_SHORT).show();

                // TennisAppActivity.showDialog(add);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                profileLocation.setText("Not found");
                return "Not found";
                //  Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        return  add;
    }

}