package com.epicodus.parkr.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.R;
import com.epicodus.parkr.adapters.MySpotsAdapter;
import com.epicodus.parkr.adapters.RentedSpotsAdapter;
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

public class MySpotsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mSpecificUserReference;
    private DatabaseReference mAllSpotsReference;
    private MySpotsAdapter mAdapter;
    private ArrayList<String> mSpotKeys = new ArrayList<>();
    private ArrayList<Spot> mSpots = new ArrayList<>();

    @Bind(R.id.mySpotsRecyclerView) RecyclerView mMySpotsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpecificUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);
        mAllSpotsReference = FirebaseDatabase.getInstance().getReference().child("spots");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spots);
        ButterKnife.bind(this);
        mSpecificUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSpotReference : dataSnapshot.child("ownedSpots").getChildren()){
                    String userSpotKey = userSpotReference.getKey();
                    mSpotKeys.add(userSpotKey);
                }
                mAllSpotsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot spotsDataSnapshot) {
                        for(DataSnapshot foundSpot : spotsDataSnapshot.getChildren()){
                            String foundSpotKey = foundSpot.getKey();
                            for(String compareSpotKey : mSpotKeys){
                                if(compareSpotKey.equals(foundSpotKey)){
                                    String ownerId = foundSpot.child("ownerID").getValue().toString();
                                    String address = foundSpot.child("address").getValue().toString();
                                    String description = foundSpot.child("description").getValue().toString();
                                    Double lat = Double.parseDouble(foundSpot.child("latLng").child("latitude").getValue().toString());
                                    Double lng = Double.parseDouble(foundSpot.child("latLng").child("longitude").getValue().toString());
                                    LatLng newLatLng = new LatLng(lat, lng);
                                    String startDate = foundSpot.child("startDate").getValue().toString();
                                    String startTime = foundSpot.child("startTime").getValue().toString();
                                    String endDate = foundSpot.child("endDate").getValue().toString();
                                    String endTime = foundSpot.child("endTime").getValue().toString();
                                    Spot newSpot = new Spot(foundSpotKey, ownerId, address, description, newLatLng, startDate, startTime, endDate, endTime);
                                    mSpots.add(newSpot);
                                }
                            }
                        }

                        mAdapter = new MySpotsAdapter(getApplicationContext(), mSpots);
                        mMySpotsRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MySpotsActivity.this);
                        mMySpotsRecyclerView.setLayoutManager(layoutManager);
                        mMySpotsRecyclerView.setHasFixedSize(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish();
            }
        });
    }
}
