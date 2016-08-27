package com.epicodus.parkr.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.R;
import com.epicodus.parkr.models.Spot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentedSpotDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mUserReference;
    private DatabaseReference mSpotReference;
    private String uid;
    private ArrayList<Spot> mSpots = new ArrayList<>();
    private String spotID;
    @Bind(R.id.spotDetailAddress) TextView mSpotDetailAddress;
    @Bind(R.id.spotDetailDescription) TextView mSpotDetailDescription;
    @Bind(R.id.spotDetailStartDate) TextView mSpotDetailStartDate;
    @Bind(R.id.spotDetailStartTime) TextView mSpotDetailStartTime;
    @Bind(R.id.spotDetailEndDate) TextView mSpotDetailEndDate;
    @Bind(R.id.spotDetailEndTime) TextView mSpotDetailEndTime;
    @Bind(R.id.removeSpotButton) Button mRemoveSpotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USERS).child(uid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_spot_detail);
        ButterKnife.bind(this);
        mRemoveSpotButton.setOnClickListener(this);

        mSpots = Parcels.unwrap(getIntent().getParcelableExtra("spots"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        Spot currentSpot = mSpots.get(startingPosition);
        mSpotDetailAddress.setText("Address: " + currentSpot.getAddress());
        mSpotDetailDescription.setText("Description: " + currentSpot.getDescription());
        mSpotDetailStartDate.setText("Start Date:" + currentSpot.getStartDate());
        mSpotDetailStartTime.setText("Start Time: " + currentSpot.getStartTime());
        mSpotDetailEndDate.setText("End Date: " + currentSpot.getEndDate());
        mSpotDetailEndTime.setText("End Time " + currentSpot.getEndTime());
        spotID = currentSpot.getSpotID();

    }

    @Override
    public void onClick(View view) {

        //REMOVES SPOT FROM USER
        if (view == mRemoveSpotButton){
            mUserReference.child("rentedSpots").child(spotID).removeValue();
            mSpotReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_SPOTS).child(spotID);
            mSpotReference.child("renterID").removeValue();
            mSpotReference.child("isCurrentlyRented").setValue(false);
            Toast.makeText(RentedSpotDetailActivity.this, "Spot has been removed.", Toast.LENGTH_SHORT).show();
            Intent accountIntent = new Intent(RentedSpotDetailActivity.this, AccountActivity.class);
            startActivity(accountIntent);
        }
    }
}
