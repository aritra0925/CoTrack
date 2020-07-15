package com.cotrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.cotrack.R;

public class UserLogin extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        clickOnLoginButton();
    }
    private void clickOnLoginButton(){
        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(UserLogin.this, ProgressActivity.class));
                startActivity(new Intent(UserLogin.this, ViewActivity.class));
            }
        });
    }

}
