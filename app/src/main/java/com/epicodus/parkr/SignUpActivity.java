package com.epicodus.parkr;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.signupHeadline) TextView mHeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        Typeface newFont = Typeface.createFromAsset(getAssets(), "fonts/seas.ttf");
        mHeadline.setTypeface(newFont);
    }
}
