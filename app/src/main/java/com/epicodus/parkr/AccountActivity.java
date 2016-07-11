package com.epicodus.parkr;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.userNameDisplay) TextView mUserNameDisplay;
    @Bind(R.id.headline) TextView mHeadline;
    @Bind(R.id.logOutButton) Button mLogOutButton;
    @Bind(R.id.findSpotsButton) Button mFindSpotsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);

        Intent infoIntent = getIntent();
        String userName = infoIntent.getStringExtra("user");
        mUserNameDisplay.setText(userName);

        mFindSpotsButton.setOnClickListener(this);
        mLogOutButton.setOnClickListener(this);
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
        } else if (view == mFindSpotsButton){
            Intent findSpotIntent = new Intent (AccountActivity.this, MapsActivity.class);
            startActivity(findSpotIntent);
        }
    }
}
