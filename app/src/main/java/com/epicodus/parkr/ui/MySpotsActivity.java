package com.epicodus.parkr.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.R;
import com.epicodus.parkr.adapters.MySpotsAdapter;
import com.epicodus.parkr.models.Spot;
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

        //FIREBASE - SET UP DATABASE AND GET CURRENT LOGGED IN USER
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpecificUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(uid);
        mAllSpotsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_SPOTS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_spots);
        ButterKnife.bind(this);

        //VALUE LISTENER FOR SPECIFIC USER
        mSpecificUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSpotReference : dataSnapshot.child("ownedSpots").getChildren()){
                    String userSpotKey = userSpotReference.getKey();
                    mSpotKeys.add(userSpotKey);
                }

                //VALUE LISTENER FOR ALL SPOTS
                mAllSpotsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot spotsDataSnapshot) {
                        for(DataSnapshot foundSpot : spotsDataSnapshot.getChildren()){
                            String foundSpotKey = foundSpot.getKey();
                            for(String compareSpotKey : mSpotKeys){
                                if(compareSpotKey.equals(foundSpotKey)){
                                    Spot newSpot = foundSpot.getValue(Spot.class);
                                    mSpots.add(newSpot);
                                }
                            }
                        }

                        //DISPLAY RENTED SPOTS WITH RENTED SPOT ADAPTER
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
