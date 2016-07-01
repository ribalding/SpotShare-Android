package com.epicodus.parkr;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity {

    @Bind(R.id.userNameDisplay) TextView mUserNameDisplay;
    @Bind(R.id.headline) TextView mHeadline;
    @Bind(R.id.optionsList)ListView mOptionsList;
    String[] options = new String[]{"Find Parking Near Me", "My Saved Spots", "Post An Open Spot"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, options);
        mOptionsList.setAdapter(adapter);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/seas.ttf");
        mHeadline.setTypeface(newFont);

        Intent infoIntent = getIntent();
        String userName = infoIntent.getStringExtra("userName");
        mUserNameDisplay.setText(userName);
    }
}
