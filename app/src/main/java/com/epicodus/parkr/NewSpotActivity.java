package com.epicodus.parkr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class NewSpotActivity extends FragmentActivity
        implements OnMapReadyCallback,
        OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        ConnectionCallbacks,
        OnConnectionFailedListener,
        View.OnClickListener {

    private DatabaseReference mSpotReference;
    private DatabaseReference mUserReference;
    private String uid;
    private FirebaseAuth mAuth;
    private boolean mPermissionDenied = false;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location mLastLocation;
    private boolean markerCheck = true;
    private LatLng position;


    @Bind(R.id.addSpotButton) Button mAddSpotButton;
    @Bind(R.id.startDate) EditText mStartDate;
    @Bind(R.id.endDate) EditText mEndDate;
    @Bind(R.id.startTime) EditText mStartTime;
    @Bind(R.id.endTime) EditText mEndTime;
    @Bind(R.id.spotDescriptionEditText) EditText mSpotDescriptionEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpotReference = FirebaseDatabase.getInstance().getReference().child("spots");
        mUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_spot);

        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAddSpotButton.setOnClickListener(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng latLng) {
                if(markerCheck) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Your Spot").draggable(true));
                    position = latLng;
                    markerCheck = false;
                }
            }
        });

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(NewSpotActivity.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
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
    public void onConnected(Bundle connectionHint) {
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        if(view == mAddSpotButton){
            String description = mSpotDescriptionEditText.getText().toString();
            String startDate = mStartDate.getText().toString();
            String startTime = mStartTime.getText().toString();
            String endDate = mEndDate.getText().toString();
            String endTime = mEndTime.getText().toString();
            addSpot(uid, description, position, startDate, startTime, endDate, endTime);
        }
    }

    public void addSpot(String ownerId, String description,LatLng spot, String startDate, String startTime, String endDate, String endTime){
        Spot newSpot = new Spot(ownerId, description, spot, startDate, startTime, endDate, endTime);
        DatabaseReference pushRef = mSpotReference.push();
        String pushId = pushRef.getKey();
        pushRef.setValue(newSpot);
        Toast.makeText(NewSpotActivity.this, "New Spot Added Successfully", Toast.LENGTH_SHORT).show();
    }




}
