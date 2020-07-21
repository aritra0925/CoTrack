package com.cotrack.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cotrack.R;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    Context context;
    boolean flag = false;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.INTERNET};
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            System.out.println("Checking permission request");
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(permission)) {
                        requestPermissions(new String[] {permission}, PERMISSION_ALL);
                    } else {
                        Toast.makeText(context, "App permission not working", Toast.LENGTH_LONG).show();
                        System.out.println("App permmission not working");
                        return false;
                    }

                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("Validating permission reuqest");
        if (requestCode == PERMISSION_ALL) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                switch (permission) {
                    case Manifest.permission.ACCESS_BACKGROUND_LOCATION:
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            boolean showRationale = shouldShowRequestPermissionRationale(permission);
                            if (!showRationale) {
                                // user also CHECKED "never ask again"
                                // you can either enable some fall back,
                                // disable features of your app
                                // or open another dialog explaining
                                // again the permission and directing to
                                // the app setting
                            }
                        }
                        break;
                    case Manifest.permission.INTERNET:
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            // user rejected the permission
                            boolean showRationale = shouldShowRequestPermissionRationale(permission);
                            if (!showRationale) {
                                // user also CHECKED "never ask again"
                                // you can either enable some fall back,
                                // disable features of your app
                                // or open another dialog explaining
                                // again the permission and directing to
                                // the app setting
                            }
                        }
                        break;
                }
            }
        }
    }
}
