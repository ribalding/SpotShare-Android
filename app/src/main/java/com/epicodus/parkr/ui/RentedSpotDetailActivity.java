package com.epicodus.parkr.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.parkr.R;
import com.epicodus.parkr.models.Spot;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentedSpotDetailActivity extends AppCompatActivity {
    private ArrayList<Spot> mSpots = new ArrayList<>();
    @Bind(R.id.spotDetailAddress) TextView mSpotDetailAddress;
    @Bind(R.id.spotDetailDescription) TextView mSpotDetailDescription;
    @Bind(R.id.spotDetailStartDate) TextView mSpotDetailStartDate;
    @Bind(R.id.spotDetailStartTime) TextView mSpotDetailStartTime;
    @Bind(R.id.spotDetailEndDate) TextView mSpotDetailEndDate;
    @Bind(R.id.spotDetailEndTime) TextView mSpotDetailEndTime;
    @Bind(R.id.removeSpotButton) Button mRemoveSpotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_spot_detail);
        ButterKnife.bind(this);

        mSpots = Parcels.unwrap(getIntent().getParcelableExtra("spots"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        Spot currentSpot = mSpots.get(startingPosition);
        mSpotDetailAddress.setText("Address: " + currentSpot.getAddress());
        mSpotDetailDescription.setText("Description: " + currentSpot.getDescription());
        mSpotDetailStartDate.setText("Start Date:" + currentSpot.getStartDate());
        mSpotDetailStartTime.setText("Start Time: " + currentSpot.getStartTime());
        mSpotDetailEndDate.setText("End Date: " + currentSpot.getEndDate());
        mSpotDetailEndTime.setText("End Time " + currentSpot.getEndTime());

    }
}
