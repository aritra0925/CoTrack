package com.cotrack.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cotrack.R;

public class ProgressActivity extends UserLogin {

    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circular_progress);
        txtProgress = findViewById(R.id.textView3);
        progressBar = findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(pStatus);
                            txtProgress.setText(pStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();
    }
}
