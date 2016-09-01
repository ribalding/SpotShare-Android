package com.epicodus.parkr.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.parkr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

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
        ButterKnife.bind(this);

        //Initialize Sign In Dialog
        createSignInProgressDialog();

        //Get Firebase Auth Instance
        mAuth = FirebaseAuth.getInstance();

        //Set Lobster Font
        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);
        mHeadline2.setTypeface(newFont);

        //Set Click Listeners
        mSignUpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        if(v == mSignUpButton){
            Intent signUpIntent = new Intent (LoginActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }else if(v == mLoginButton) {
            mSignInProgressDialog.show();
            login();
        }
    }

    private void login(){

        final String email = mEmailInput.getText().toString().trim();
        final String pass = mPasswordInput.getText().toString().trim();

        if(!isNotEmpty(email) || !isNotEmpty(pass)) return;

        mAuth.signInWithEmailAndPassword(email, mPasswordInput.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mSignInProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent loginIntent = new Intent (LoginActivity.this, AccountActivity.class);
                            startActivity(loginIntent);
                            finish();
                        } else if (!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    //Sign In Dialog
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
