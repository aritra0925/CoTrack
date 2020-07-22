package com.cotrack.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cotrack.R;
import com.cotrack.helpers.Session;
import com.cotrack.receivers.Restarter;
import com.cotrack.services.LoginService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    Context context;
    LoginService mLoginService;
    Intent mServiceIntent;
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    public String userCookie;
    public String userTypeCookie;
    Session session;
    boolean isService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoginService = new LoginService();
        mServiceIntent = new Intent(this, mLoginService.getClass());
        if (!isMyServiceRunning(mLoginService.getClass())) {
            startService(mServiceIntent);
        }
        context = this;
        session = new Session(this);
        if(savedInstanceState!=null) {
            // App has been closed by the OS before and is now being restored

            if(fileExists(this, COOKIE_FILE_NAME)){
                Properties properties = getProperties();
                if(properties.containsKey(USER_COOKIE) && (properties.getProperty(USER_COOKIE) != null) && !properties.getProperty(USER_COOKIE).isEmpty()){
                    userCookie = properties.getProperty(USER_COOKIE);
                    session.setUserName(userCookie);
                }
                if(properties.containsKey(USER_TYPE_COOKIE) && (properties.getProperty(USER_TYPE_COOKIE) != null) && !properties.getProperty(USER_TYPE_COOKIE).isEmpty()){
                    userTypeCookie = properties.getProperty(USER_TYPE_COOKIE);
                    if(userTypeCookie.equalsIgnoreCase("service")){
                        isService = true;
                    }
                    session.setUserType(userTypeCookie);
                }
                if(userCookie!=null && userTypeCookie!=null) {
                    launchActivity();
                }
                return;
            } else {
                System.out.println("Properties file is not present as of now");
            }
        } else {
            if(fileExists(this, COOKIE_FILE_NAME)){
                Properties properties = getProperties();
                if(properties.containsKey(USER_COOKIE) && (properties.getProperty(USER_COOKIE) != null) && !properties.getProperty(USER_COOKIE).isEmpty()){
                    userCookie = properties.getProperty(USER_COOKIE);
                    session.setUserName(userCookie);
                }
                if(properties.containsKey(USER_TYPE_COOKIE) && (properties.getProperty(USER_TYPE_COOKIE) != null) && !properties.getProperty(USER_TYPE_COOKIE).isEmpty()){
                    userTypeCookie = properties.getProperty(USER_TYPE_COOKIE);
                    session.setUserType(userTypeCookie);
                    if(userTypeCookie.equalsIgnoreCase("service")){
                        isService = true;
                    }
                }
                if(userCookie!=null && userTypeCookie!=null) {
                    launchActivity();
                }
                return;
            } else {
                System.out.println("Properties file is not present as of now");
            }
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void launchActivity(){
        Intent intent = null;
        if (isService) {
            intent = new Intent(this, ServiceNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("service_user", userCookie); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
        } else {
            intent = new Intent(this, NavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("regular_user", userCookie); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent

        }
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    public Properties getProperties(){
        Properties props = new Properties();
        try {
            FileInputStream fin= openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }

    public Properties writeProperties(String key, String value){
        Properties props = new Properties();
        try {
            File file = this.getFileStreamPath(COOKIE_FILE_NAME);
            if(!file.exists()){
                file.createNewFile();
            }
            FileInputStream  fin= openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
            props.put(key, value);
            FileOutputStream fOut = openFileOutput(COOKIE_FILE_NAME,Context.MODE_PRIVATE);
            props.store(fOut, "Cookie Data");
            System.out.println("Properties was written successfully");
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }
    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
