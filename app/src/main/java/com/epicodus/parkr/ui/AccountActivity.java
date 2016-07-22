package com.epicodus.parkr.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.R;
import com.epicodus.parkr.models.Spot;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mSpecificUserReference;
    private DatabaseReference mAllSpotsReference;


    @Bind(R.id.userNameDisplay) TextView mUserNameDisplay;
    @Bind(R.id.headline) TextView mHeadline;
    @Bind(R.id.logOutButton) Button mLogOutButton;
    @Bind(R.id.postSpotButton) Button mPostSpotButton;
    @Bind(R.id.findSpotsButton) Button mFindSpotsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpecificUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);
        mAllSpotsReference = FirebaseDatabase.getInstance().getReference().child("spots");


        mSpecificUserReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("fullName").getValue().toString();
                 mUserNameDisplay.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSpecificUserReference.child("rentedSpots").addValueEventListener(new ValueEventListener() {
            ArrayList<Spot> mSpots = new ArrayList<>();
            ArrayList<String> spotKeys = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot spotSnapshot : dataSnapshot.getChildren()){
                    String spotKey = spotSnapshot.getValue().toString();
                    spotKeys.add(spotKey);
                }
                mAllSpotsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot innerDataSnapshot) {
                        for(DataSnapshot eachSpotSnapshot : innerDataSnapshot.getChildren()){
                            String compareSpotId = eachSpotSnapshot.getKey();
                            for(String eachSpotKey : spotKeys)
                                if(compareSpotId.equals(eachSpotKey)){
                                    String ownerId = eachSpotSnapshot.child("ownerID").getValue().toString();
                                    String address = eachSpotSnapshot.child("address").getValue().toString();
                                    String description = eachSpotSnapshot.child("description").getValue().toString();
                                    Double lat = Double.parseDouble(eachSpotSnapshot.child("latLng").child("latitude").getValue().toString());
                                    Double lng = Double.parseDouble(eachSpotSnapshot.child("latLng").child("longitude").getValue().toString());
                                    LatLng newLatLng = new LatLng(lat, lng);
                                    String startDate = eachSpotSnapshot.child("startDate").getValue().toString();
                                    String startTime = eachSpotSnapshot.child("startTime").getValue().toString();
                                    String endDate = eachSpotSnapshot.child("endDate").getValue().toString();
                                    String endTime = eachSpotSnapshot.child("endTime").getValue().toString();
                                    Spot newSpot = new Spot(eachSpotKey, ownerId, address, description, newLatLng, startDate, startTime, endDate, endTime);
                                    mSpots.add(newSpot);
                                }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);

        Intent infoIntent = getIntent();
        String userName = infoIntent.getStringExtra("user");
        mUserNameDisplay.setText(userName);

        mPostSpotButton.setOnClickListener(this);
        mLogOutButton.setOnClickListener(this);
        mFindSpotsButton.setOnClickListener(this);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view == mLogOutButton){
            logout();
        } else if (view == mPostSpotButton){
            Intent postSpotIntent = new Intent (AccountActivity.this, NewSpotActivity.class);
            startActivity(postSpotIntent);
        } else if (view == mFindSpotsButton){
            Intent findSpotsIntent = new Intent(AccountActivity.this, FindSpotsActivity.class);
            startActivity(findSpotsIntent);
        }
    }
}
