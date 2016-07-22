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

import com.epicodus.parkr.Constants;
import com.epicodus.parkr.R;
import com.epicodus.parkr.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog mAuthProgressDialog;
    private DatabaseReference mSpecificUserReference;
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    String password;
    String confirmPassword;
    @Bind(R.id.signupHeadline) TextView mHeadline;
    @Bind(R.id.submitSignUpButton) Button mSubmitSignUpButton;
    @Bind(R.id.newEmailEditText) EditText mNewEmailEditText;
    @Bind(R.id.newPasswordEditText) EditText mNewPasswordEditText;
    @Bind(R.id.newConfirmPasswordEditText) EditText mNewConfirmPasswordEditText;
    @Bind(R.id.newFullNameEditText) EditText mNewFullNameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        createAuthProgressDialog();

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
        name = mNewFullNameEditText.getText().toString().trim();
        email = mNewEmailEditText.getText().toString().trim();
        password = mNewPasswordEditText.getText().toString().trim();
        confirmPassword = mNewConfirmPasswordEditText.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if(!validEmail|| !validName || !validPassword) return;

        mAuthProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuthProgressDialog.dismiss();
                            String uid = task.getResult().getUser().getUid();
                            mSpecificUserReference = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_CHILD_USER).child(uid);
                            User newUser = new User(uid, name, password, email);
                            saveUserToFirebase(newUser);


                            Intent intent = new Intent(SignUpActivity.this, AccountActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            mAuthProgressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "WHY", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mNewEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNewFullNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mNewPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mNewPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }


    private void saveUserToFirebase(User user){
        mSpecificUserReference.setValue(user);
    }

}
