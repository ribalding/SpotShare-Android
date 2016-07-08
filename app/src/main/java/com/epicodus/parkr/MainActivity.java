package com.epicodus.parkr;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.headline)TextView mHeadline;
    @Bind(R.id.headline2) TextView mHeadline2;
    @Bind(R.id.signUpButton)Button mSignUpButton;
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.userNameInput) EditText mUserNameInput;
    @Bind(R.id.passwordInput) EditText mPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);
        mHeadline2.setTypeface(newFont);
        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == mSignUpButton){
            Intent signUpIntent = new Intent (MainActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }else if(v == mLoginButton){
            String userName = mUserNameInput.getText().toString();
            String password = mPasswordInput.getText().toString();
            Intent loginIntent = new Intent (MainActivity.this, AccountActivity.class);
            loginIntent.putExtra("userName", userName);
            loginIntent.putExtra("password", password);
            startActivity(loginIntent);
        }
    }
}
