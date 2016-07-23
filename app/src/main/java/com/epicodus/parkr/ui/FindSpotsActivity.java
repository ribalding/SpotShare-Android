package com.epicodus.parkr.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.PermissionUtils;
import com.epicodus.parkr.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindSpotsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener,
        GoogleMap.OnMarkerClickListener{

    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mSpotReference;
    private DatabaseReference mUserReference;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean mPermissionDenied = false;
    private Location mLastLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String currentSpot;

    @Bind(R.id.addressDisplay) TextView mAddressDisplay;
    @Bind(R.id.descriptionDisplay) TextView mDescriptionDisplay;
    @Bind(R.id.startDateDisplay) TextView mStartDateDisplay;
    @Bind(R.id.startTimeDisplay) TextView mStartTimeDisplay;
    @Bind(R.id.endDateDisplay) TextView mEndDateDisplay;
    @Bind(R.id.endTimeDisplay) TextView mEndTimeDisplay;
    @Bind(R.id.getSpotButton) Button mGetSpotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpotReference = FirebaseDatabase.getInstance().getReference().child("spots");
        mUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_spots);

        ButterKnife.bind(this);

        mGetSpotButton.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.findSpotsMap);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }

        mSpotReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot spotSnapshot : dataSnapshot.getChildren()){
                    Double latitude = Double.parseDouble(spotSnapshot.child("latLng").child("latitude").getValue().toString());
                    Double longitude = Double.parseDouble(spotSnapshot.child("latLng").child("longitude").getValue().toString());
                    String description = spotSnapshot.child("description").getValue().toString();
                    LatLng newLatLng = new LatLng(latitude, longitude);
                    currentSpot = spotSnapshot.getKey();
                    Boolean isRented = Boolean.parseBoolean(spotSnapshot.child("isCurrentlyRented").getValue().toString());

                    if(isRented == true) {
                        mMap.addMarker(new MarkerOptions().position(newLatLng).title(description));
                    } else if (isRented == false){
                        mMap.addMarker(new MarkerOptions().position(newLatLng).title(description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng latLng) {
            }

        });

        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestFindSpotsPermission(FindSpotsActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.epicodus.parkr/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.epicodus.parkr/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View view) {

        if (view == mGetSpotButton){
            mSpotReference.child(currentSpot).child("renterID").setValue(uid);
            mSpotReference.child(currentSpot).child("isCurrentlyRented").setValue(true);
            DatabaseReference newSpotRef = mUserReference.child("rentedSpots").child(currentSpot);
            newSpotRef.setValue(currentSpot);
            Toast.makeText(FindSpotsActivity.this, "Spot Added to Your Rented Spots", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FindSpotsActivity.this, AccountActivity.class);
            startActivity(intent);

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        mSpotReference.child(currentSpot).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("description").getValue().toString();
                String startTime = dataSnapshot.child("startTime").getValue().toString();
                String startDate = dataSnapshot.child("startDate").getValue().toString();
                String endTime = dataSnapshot.child("endTime").getValue().toString();
                String endDate = dataSnapshot.child("endDate").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                mAddressDisplay.setText(address);
                mDescriptionDisplay.setText(description);
                mStartDateDisplay.setText(startDate);
                mStartTimeDisplay.setText(startTime);
                mEndDateDisplay.setText(endDate);
                mEndTimeDisplay.setText(endTime);
                currentSpot = dataSnapshot.getKey();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return false;
    }


}
