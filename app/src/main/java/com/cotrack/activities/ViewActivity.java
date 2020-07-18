package com.cotrack.activities;

import android.os.Bundle;

import com.cotrack.R;

public class ViewActivity extends UserLogin {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
    }

}
