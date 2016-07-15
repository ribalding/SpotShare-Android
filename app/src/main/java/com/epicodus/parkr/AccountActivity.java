package com.epicodus.parkr;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mSpecificUserReference;

    @Bind(R.id.userNameDisplay) TextView mUserNameDisplay;
    @Bind(R.id.headline) TextView mHeadline;
    @Bind(R.id.logOutButton) Button mLogOutButton;
    @Bind(R.id.postSpotButton) Button mPostSpotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSpecificUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);

        mSpecificUserReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("fullName").getValue().toString();
                 mUserNameDisplay.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);

        Intent infoIntent = getIntent();
        String userName = infoIntent.getStringExtra("user");
        mUserNameDisplay.setText(userName);

        mPostSpotButton.setOnClickListener(this);
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
        } else if (view == mPostSpotButton){
            Intent postSpotIntent = new Intent (AccountActivity.this, NewSpotActivity.class);
            startActivity(postSpotIntent);
        }
    }
}
