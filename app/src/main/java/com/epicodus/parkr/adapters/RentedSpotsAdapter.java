package com.epicodus.parkr.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.parkr.R;
import com.epicodus.parkr.models.Spot;
import com.epicodus.parkr.ui.RentedSpotDetailActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RentedSpotsAdapter extends RecyclerView.Adapter<RentedSpotsAdapter.RentedSpotViewHolder> {
    private ArrayList<Spot> mSpots = new ArrayList<>();
    private Context mContext;

    public RentedSpotsAdapter(Context context, ArrayList<Spot> spots){
        mSpots = spots;
        mContext = context;
    }

    @Override
    public RentedSpotsAdapter.RentedSpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rented_spot_list_item, parent, false);
        RentedSpotViewHolder viewHolder = new RentedSpotViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RentedSpotsAdapter.RentedSpotViewHolder holder, int position) {
        holder.bindSpot(mSpots.get(position));
    }

    @Override
    public int getItemCount() {
        return mSpots.size();
    }

    public class RentedSpotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.rentedSpotAddressDisplay) TextView mRentedSpotAddressDisplay;
        @Bind(R.id.rentedSpotEndDateDisplay) TextView mRentedSpotEndDateDisplay;
        @Bind(R.id.rentedSpotEndTimeDisplay) TextView mRentedSpotEndTimeDisplay;

        private Context mContext;

        public RentedSpotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindSpot(Spot spot){
            mRentedSpotAddressDisplay.setText(spot.getAddress());
            mRentedSpotEndDateDisplay.setText(spot.getEndDate());
            mRentedSpotEndTimeDisplay.setText(spot.getEndTime());
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, RentedSpotDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("spots", Parcels.wrap(mSpots));
            mContext.startActivity(intent);
        }
    }
}
