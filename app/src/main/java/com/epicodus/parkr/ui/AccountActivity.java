package com.epicodus.parkr.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.R;
import com.epicodus.parkr.adapters.RentedSpotsAdapter;
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

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mSpecificUserReference;
    private DatabaseReference mAllSpotsReference;
    private RentedSpotsAdapter mAdapter;


    @Bind(R.id.rentedSpotRecyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.userNameDisplay) TextView mUserNameDisplay;
    @Bind(R.id.welcomeText) TextView mWelcomeText;
    @Bind(R.id.headline) TextView mHeadline;
    @Bind(R.id.logOutButton) Button mLogOutButton;
    @Bind(R.id.postSpotButton) Button mPostSpotButton;
    @Bind(R.id.findSpotsButton) Button mFindSpotsButton;
    @Bind(R.id.mySpotsButton) Button mMySpotsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //FIREBASE - SET UP DATABASE AND GET CURRENT LOGGED IN USER
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpecificUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(uid);
        mAllSpotsReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_SPOTS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //VALUE EVENT LISTENER FOR USER REFERENCE
        mSpecificUserReference.addValueEventListener(new ValueEventListener() {
            ArrayList<Spot> mSpots = new ArrayList<>();
            ArrayList<String> spotKeys = new ArrayList<>();
            Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //SET USER NAME AND FONTS
                String userName = dataSnapshot.child("fullName").getValue().toString();
                mUserNameDisplay.setText(userName);
                mUserNameDisplay.setTypeface(newFont);
                mWelcomeText.setTypeface(newFont);

                //GET ALL RENTED SPOT KEYS FOR THIS USER
                for(DataSnapshot spotKeySnapshot : dataSnapshot.child("rentedSpots").getChildren()){
                    String spotKey = spotKeySnapshot.getValue().toString();
                    spotKeys.add(spotKey);
                }

                //VALUE LISTENER FOR ALL SPOTS
                mAllSpotsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot AllSpotsDataSnapshot) {
                        mSpots.clear();

                        //CHECK USER'S RENTED SPOT KEYS AGAINST ALL SPOT KEYS
                        for(DataSnapshot eachSpotSnapshot : AllSpotsDataSnapshot.getChildren()){
                            String compareSpotId = eachSpotSnapshot.getKey();
                            for(String eachSpotKey : spotKeys) {
                                if (compareSpotId.equals(eachSpotKey)) {

                                    //IF KEYS MATCH, CREATE SPOT AND ADD IT TO SPOTS ARRAY
                                    Spot newSpot = eachSpotSnapshot.getValue(Spot.class);
                                    mSpots.add(newSpot);
                                }
                            }
                        }

                        //DISPLAY RENTED SPOTS WITH RENTED SPOT ADAPTER
                        mAdapter = new RentedSpotsAdapter(getApplicationContext(), mSpots);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AccountActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
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


        //SET BUTTERKNIFE
        ButterKnife.bind(this);

        //LOBSTER FONT
        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);

        //SET CLICK LISTENERS
        mPostSpotButton.setOnClickListener(this);
        mLogOutButton.setOnClickListener(this);
        mFindSpotsButton.setOnClickListener(this);
        mMySpotsButton.setOnClickListener(this);
    }

    //LOG OUT AND RETURN TO LOGIN ACTIVITY
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //CLICK LISTENERS
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
        } else if (view == mMySpotsButton){
            Intent mySpotsIntent = new Intent (AccountActivity.this, MySpotsActivity.class);
            startActivity(mySpotsIntent);
        }
    }

        //EMPTY OVERRIDDEN ONSAVEINSTANCESTATE (NECESSARY SO APP DOESNT CRASH)
        @Override
        public void onSaveInstanceState(Bundle saveInstanceState){}

}
