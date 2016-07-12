package com.epicodus.parkr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mSignInProgressDialog;

    @Bind(R.id.headline)TextView mHeadline;
    @Bind(R.id.headline2) TextView mHeadline2;
    @Bind(R.id.signUpButton)Button mSignUpButton;
    @Bind(R.id.loginButton) Button mLoginButton;
    @Bind(R.id.emailInput) EditText mEmailInput;
    @Bind(R.id.passwordInput) EditText mPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createSignInProgressDialog();
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);
        mHeadline2.setTypeface(newFont);

        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();

                    }
                }
            };
    }

    @Override
    public void onClick(View v){
        if(v == mSignUpButton){
            Intent signUpIntent = new Intent (MainActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }else if(v == mLoginButton){
            login();
        }
    }

    private void login(){

        final String email = mEmailInput.getText().toString();
        final String pass = mPasswordInput.getText().toString();

        if(!isNotEmpty(email) || !isNotEmpty(pass)) return;

        mSignInProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email, mPasswordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    mSignInProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent loginIntent = new Intent (MainActivity.this, AccountActivity.class);
                            startActivity(loginIntent);
                        } else if (!task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createSignInProgressDialog() {
        mSignInProgressDialog = new ProgressDialog(this);
        mSignInProgressDialog.setTitle("Loading...");
        mSignInProgressDialog.setMessage("Signing In...");
        mSignInProgressDialog.setCancelable(false);
    }

    private Boolean isNotEmpty(String email){
        if(email.equals("")){
            mEmailInput.setError("Blank Field");
            return false;
        }
     return true;
    }

}
