package com.epicodus.parkr.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epicodus.parkr.R;
import com.epicodus.parkr.models.Spot;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RentedSpotDetailActivity extends AppCompatActivity {
    private ArrayList<Spot> mSpots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rented_spot_detail);
        ButterKnife.bind(this);

        mSpots = Parcels.unwrap(getIntent().getParcelableExtra("spots"));
        int startingPosition = getIntent().getIntExtra("position", 0);

    }
}
