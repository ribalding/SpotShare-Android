package com.epicodus.parkr.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.parkr.R;
import com.epicodus.parkr.models.Spot;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ryan on 7/25/2016.
 */
public class MySpotsAdapter extends RecyclerView.Adapter<MySpotsAdapter.MySpotsViewHolder>{
    private ArrayList<Spot> mSpots;
    private Context mContext;

    public MySpotsAdapter(Context context, ArrayList<Spot> spots){
        mSpots = spots;
        mContext = context;
    }

    @Override
    public MySpotsAdapter.MySpotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_spot_list_item, parent, false);
        MySpotsViewHolder viewHolder = new MySpotsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MySpotsAdapter.MySpotsViewHolder holder, int position) {
        holder.bindMySpots(mSpots.get(position));
    }

    @Override
    public int getItemCount() {
        return mSpots.size();
    }

    public class MySpotsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.mySpotsAddress) TextView mMySpotsAddress;
        @Bind(R.id.mySpotsCurrentlyRented) TextView mMySpotsCurrentlyRented;
        @Bind(R.id.mySpotsDescription) TextView mMySpotsDescription;

        public MySpotsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindMySpots(Spot spot){
            mMySpotsAddress.setText(spot.getAddress());
            if(spot.isCurrentlyRented()) {
                mMySpotsCurrentlyRented.setText("Currently Rented");
            } else {
                mMySpotsCurrentlyRented.setText("Not Currently Rented");
            }
            mMySpotsDescription.setText(spot.getDescription());
        }

        @Override
        public void onClick(View view) {

        }
    }
}
