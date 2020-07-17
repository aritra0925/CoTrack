package com.cotrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.cotrack.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_preview);
        //VideoView view = (VideoView) findViewById(R.id.videoView);
        // initiate a video view
        //view.setVideoURI(uri);
        //view.start();
        //clickOnUserLogin();
    }
    /*private void clickOnUserLogin(){
        Button b1 = findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserLogin.class));
            }
        });

    }*/
}