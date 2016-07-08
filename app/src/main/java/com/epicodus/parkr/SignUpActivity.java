package com.epicodus.parkr;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mUserNameReference;
    @Bind(R.id.signupHeadline) TextView mHeadline;
    @Bind(R.id.submitSignUpButton) Button mSubmitSignUpButton;
    @Bind(R.id.newUserNameEditText) EditText mNewUserNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mUserNameReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_USER_NAME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);

        mSubmitSignUpButton.setOnClickListener(this);
    }

    public void saveUserNameToFireBase(String username){
        mUserNameReference.push().setValue(username);
    }

    @Override
    public void onClick(View view) {
        if(view == mSubmitSignUpButton){
            saveUserNameToFireBase(mNewUserNameEditText.getText().toString());
        }
    }
}
