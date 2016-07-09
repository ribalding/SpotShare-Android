package com.epicodus.parkr;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Bind(R.id.signupHeadline) TextView mHeadline;
    @Bind(R.id.submitSignUpButton) Button mSubmitSignUpButton;
    @Bind(R.id.newEmailEditText) EditText mNewEmailEditText;
    @Bind(R.id.newPasswordEditText) EditText mNewPasswordEditText;
    @Bind(R.id.newConfirmPasswordEditText) EditText mNewConfirmPasswordEditText;
    @Bind(R.id.newFullNameEditText) EditText mNewFullNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        createAuthStateListener();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        mHeadline.setTypeface(newFont);

        mSubmitSignUpButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view == mSubmitSignUpButton){
            createNewUser();
        }
    }

    private void createNewUser() {
        final String name = mNewFullNameEditText.getText().toString().trim();
        final String email = mNewEmailEditText.getText().toString().trim();
        String password = mNewPasswordEditText.getText().toString().trim();
        String confirmPassword = mNewConfirmPasswordEditText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("blergh", "Authentication successful");
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, AccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
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
}
