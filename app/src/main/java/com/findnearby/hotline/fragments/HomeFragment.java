package com.findnearby.hotline.fragments;

import static android.content.ContentValues.TAG;

import static com.findnearby.hotline.PublicVariables.piCheck;
import static com.findnearby.hotline.PublicVariables.updateMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.findnearby.hotline.Model.People;
import com.findnearby.hotline.PersonalInfoActivity;
import com.findnearby.hotline.ProfileDetailsActivity;
import com.findnearby.hotline.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements GoogleMap.OnMarkerClickListener {

    public List<People> peopleList = new ArrayList<>();
    double lat , lng;
    private Marker myMarker;
    public static int val =1;

    TextView peoples;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor


    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {





        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            if (getContext() != null) {
                if (updateMap == 1) {
                    updateMap = 0;
                    Map<String, String> mMarkerMap = new HashMap<>();
                    GoogleMap mMap = googleMap;

                    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = mFirebaseDatabase.getReference("Users");
                    databaseReference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                                People people = dataSnapshot.getValue(People.class);
                                peopleList.add(people);
                                if(getContext() != null) {
                                    for (int i = 0; i < peopleList.size(); i++) {


                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.profile_img, getActivity().getTheme());
                                        Bitmap b = bitmapdraw.getBitmap();


                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 84, 84, false);
                                        LatLng latLng = new LatLng(Double.parseDouble(childDataSnapshot.child("latitude").getValue().toString()), Double.parseDouble(childDataSnapshot.child("longitude").getValue().toString()));
                                        // if (mMap != null ) {
                                        if (mMap != null && !FirebaseAuth.getInstance().getUid().equals(childDataSnapshot.getKey())) {

                                            Marker marker = mMap.addMarker(new MarkerOptions()
                                                    .position(latLng).title(childDataSnapshot.child("name").getValue().toString())
                                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                            //  mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                            //Added:
                                            mMarkerMap.put(marker.getId(), childDataSnapshot.getKey());
                                        }
                                    }

                                    Log.v(TAG, "" + childDataSnapshot.getKey()); //displays the key for the node
                                    Log.v(TAG, "" + childDataSnapshot.child("name").getValue());   //gives the value for given keyname


                                    //  LatLng coordinate = new LatLng(Double.parseDouble(childDataSnapshot.child("latitude").getValue().toString()), Double.parseDouble(childDataSnapshot.child("longitude").getValue().toString()));
                                    //  googleMap.addMarker(new MarkerOptions().position(coordinate).title(childDataSnapshot.child("name").getValue().toString()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                    // googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));

                                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            String venueID = mMarkerMap.get(marker.getId());
                                            String venueName = marker.getTitle();
                                            Intent intent = new Intent(getContext(), ProfileDetailsActivity.class);
                                            intent.putExtra("id", venueID);
                                            // intent.putExtra(VENUE_ID, venueID);
                                            startActivity(intent);

                                            return false;
                                        }
                                    });
                                }


                            }
                            //  Toast.makeText(getContext(), "lIST" + peopleList.size(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });



                        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users");

                        productsRef.child(FirebaseAuth.getInstance().getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {
                                            People user = dataSnapshot.getValue(People.class);

                                            Log.d(TAG, "Data" + dataSnapshot.child("name").getValue() + user.getName());   //gives the value for given keyname

                                            lat = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                                            lng = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());


                                            if(getContext() !=null)
                                            {
                                            LatLng sydney = new LatLng(Double.parseDouble(dataSnapshot.child("latitude").getValue().toString()), Double.parseDouble(dataSnapshot.child("longitude").getValue().toString()));
                                            MarkerOptions markerOptions = new MarkerOptions();



                                            // Instantiates a new CircleOptions object and defines the center and radius

                                            if (user.getLatitude() != null) {
                                                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.blue_dot_icon, getContext().getTheme());
                                                Bitmap b = bitmapdraw.getBitmap();
                                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 104, 104, false);

                                                Marker marker = googleMap.addMarker(new MarkerOptions().position(sydney).title(getAddress(user.getLatitude(), user.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(b))
                                                        .anchor(0.5f, 0.5f));


                                                CircleOptions circleOptions = new CircleOptions();
                                                circleOptions.center(new LatLng(user.getLatitude(), user.getLongitude()));
                                                circleOptions.radius(700);
                                                circleOptions.strokeColor(Color.parseColor("#18A8D8"));
                                                circleOptions.fillColor(Color.parseColor("#F0FFFF"));
                                                circleOptions.strokeWidth(7);
                                                googleMap.addCircle(circleOptions);
                                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                                mMarkerMap.put(marker.getId(), dataSnapshot.getKey());


                                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                    @Override
                                                    public boolean onMarkerClick(Marker marker) {
                                                        String venueID = mMarkerMap.get(marker.getId());
                                                        String venueName = marker.getTitle();
                                                        Intent intent = new Intent(getContext(), ProfileDetailsActivity.class);
                                                        intent.putExtra("id", venueID);
                                                        // intent.putExtra(VENUE_ID, venueID);
                                                        startActivity(intent);

                                                        return false;
                                                    }
                                                });
                                            }

                                            }

                                        } else {
                                            Toast.makeText(getContext(), "Error.........", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getContext(), (CharSequence) databaseError, Toast.LENGTH_LONG).show();

                                    }
                                });



                    for (int i = 0; i < peopleList.size(); i++) {
                        People user = peopleList.get(i);


                        //Fetch information of college
                        peoples.setText(user.getLatitude().toString());
                        Toast.makeText(getContext(), "lat" + user.getLatitude(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "" + user.getLatitude());
                        Double latitude = user.getLatitude();
                        Double longitude = user.getLongitude();
                        String title = user.getName();


                        //Place Marker on College
                        LatLng coordinate = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(coordinate).title(title));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
                    }


                    //  Toast.makeText(getContext(), "lIST"+ peopleList.size(), Toast.LENGTH_SHORT).show();


                }

            }
        }
    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    DatabaseReference profileRefrence;
    TextView updateProfile;
    CircleImageView homeProfileImg;
    TextView homeCurrentLocation , homeProfileName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);


         peoples = (TextView) view.findViewById(R.id.peoples);
         updateProfile = (TextView) view.findViewById(R.id.updateprofile);
        homeCurrentLocation = (TextView) view.findViewById(R.id.home_current_location);
        homeProfileName = (TextView) view.findViewById(R.id.home_profile_name);
        homeProfileImg=(CircleImageView) view.findViewById(R.id.home_profile_img);

         updateProfile.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getContext(), PersonalInfoActivity.class);
                 startActivity(intent);
             }
         });


 /*


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.peoples_recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of CustomAdapter to send the reference and data to Adapter
        PeopleAdapter customAdapter = new PeopleAdapter(getContext());
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView


  */

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
       // updateMap =1;
        getprofiledetails();



      //  if(peopleList.size() !=0)



    }

    private void getprofiledetails() {

        if(updateMap==1) {

            ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();

            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Users");

            productsRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        People user = dataSnapshot.getValue(People.class);

                        homeProfileName.setText("Welcome Back " + user.getName() + " !");


                        if(updateMap==1)
                        getAddress(user.getLatitude(), user.getLongitude());

                        if (!user.getImage().isEmpty())
                            Picasso.get().load(user.getImage()).into(homeProfileImg);

                        lat = user.getLatitude();
                        lng = user.getLongitude();


                        progress.dismiss();


                    } else {
                        Toast.makeText(getContext(), "Error.........", Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), (CharSequence) databaseError, Toast.LENGTH_LONG).show();

                }
            });

        }
    }



    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    public String getAddress(double lat, double lng) {
        String add = "";

        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            if(!obj.equals(null)) {
                add = add + obj.getCountryName();

                add = add + ", " + obj.getAdminArea();
                add = add + ", " + obj.getSubAdminArea();
                add = add + ", " + obj.getLocality();
                add = add + ", " + obj.getSubThoroughfare();


                homeCurrentLocation.setText(add);

            }
            else
            {
                homeCurrentLocation.setText("Not found");
            }
          //  Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            homeCurrentLocation.setText("Not found");
            return "Not found";
          //  Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return  add;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {

            //uncomment below line in image name have spaces.
            //src = src.replaceAll(" ", "%20");

            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.d("vk21", e.toString());
            return null;
        }


    }




}