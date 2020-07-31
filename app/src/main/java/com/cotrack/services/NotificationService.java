package com.cotrack.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cotrack.R;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.models.OrderDetails;
import com.cotrack.models.Test;
import com.cotrack.receivers.NotificationRestarter;
import com.cotrack.receivers.Restarter;
import com.cotrack.utils.CloudantOrderUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private static final String TAG = NotificationService.class.getSimpleName();

    final String NOTIFICATION_CHANNEL_ID = "com.cotrack";
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    Notification notification;
    NotificationManager manager;
    NotificationCompat.Builder notificationBuilder;
    Service service;

    public int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        service = this;
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notification = notificationBuilder.setOngoing(true)
                .setContentTitle("CoTrack")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String userName = getProperties().getProperty(USER_COOKIE);
        Log.d("Notification Service", "Checking update requests");
        new DBConnect().execute(userName);
        startForeground(1, notification);
        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, NotificationRestarter.class);
        this.sendBroadcast(broadcastIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class DBConnect extends AsyncTask<String, Void, List<OrderDataHolder>> {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected List<OrderDataHolder> doInBackground(String[] objects) {
            List<OrderDataHolder> orderDataHolderList = OrderDataHolder.getAllServiceSpecificDetails().get(objects[0]);
            List<OrderDataHolder> requestedUpdates = new ArrayList<>();
            for (OrderDataHolder holder : orderDataHolderList) {
                if (holder.getOrder_status().equalsIgnoreCase("Update Requested")) {
                    requestedUpdates.add(holder);
                }
            }
            return requestedUpdates;
        }

        protected void onPostExecute(List<OrderDataHolder> feed) {
            if (!feed.isEmpty()) {
                for (OrderDataHolder holder : feed) {
                    notificationBuilder = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID);
                    notification = notificationBuilder.setOngoing(true)
                            .setContentTitle("CoTrack")
                            .setContentText("User " + holder.getUser_id() + " has requested update for order: " + holder.get_id())
                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .setCategory(Notification.CATEGORY_SERVICE)
                            .setSmallIcon(R.drawable.logo)
                            .build();
                    manager.notify(0, notificationBuilder.build());
                }
            }
            Log.d("Notification Service", "Update requested for # of users: " + feed.size());

        }
    }


    public Properties getProperties() {
        Properties props = new Properties();
        try {
            FileInputStream fin = openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.d("File Error", "Error reading properties file");
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file");
        }
        return props;
    }
}
