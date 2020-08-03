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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cotrack.R;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.models.LatLangDetails;
import com.cotrack.models.LocationDetails;
import com.cotrack.models.Test;
import com.cotrack.receivers.Restarter;
import com.cotrack.utils.CloudantLocationUtils;
import com.cotrack.utils.CommonUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    float disance;
    private static final String TAG = LocationService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    FusedLocationProviderClient mFusedLocationClient;
    final String NOTIFICATION_CHANNEL_ID = "com.cotrack";
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    Location location;
    NotificationCompat.Builder notificationBuilder;
    NotificationManager manager;
    Notification notification;
    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";
    public static final long LOCATION_INTERVAL = 10000;
    public static final long FASTEST_LOCATION_INTERVAL = 1000;
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
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
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
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_STICKY;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                new DBConnect().execute(location);
            }
        });
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
            notification = new Notification();
        }
        startForeground(1, notification);
        Log.d("Location Service", "Foreground Started");
        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        // LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        Log.d(TAG, "Connected to Google API");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "Location changed: " + location.getLatitude() + " : " + location.getLongitude());

        if (location != null) {
            Log.d(TAG, "== location != null");
            new DBConnect().execute(location);
            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }
    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_LATITUDE, lat);
        intent.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * @param provider
     * @param status
     * @param extras
     * @deprecated
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

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

    class DBConnect extends AsyncTask<Location, Void, LocationMap> {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected LocationMap doInBackground(Location[] objects) {
            Location location = objects[0];
            LocationMap locationMap = null;
            if(getProperties().getProperty(USER_TYPE_COOKIE).equalsIgnoreCase("SERVICE")){
                return locationMap;
            }
            try {
                String latitude = Double.toString(location.getLatitude());
                String longitude = Double.toString(location.getLongitude());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy_hh:mm:ss");
                Date date = new Date();
                String currentDateString = dateFormat.format(date);
                String userId = getProperties().getProperty(USER_COOKIE);
                if (!CloudantLocationUtils.checkEntry("user_id", userId)) {
                    LocationDetails locationDetails = getNewLocationDetailsDetails(userId, latitude, longitude, currentDateString);
                    CloudantLocationUtils.insertDocument(locationDetails);
                    Log.d("Location Service", "Inserted Document for location: " + latitude + ", " + longitude + " and user: " + userId);

                } else {
                    LocationDetails locationDetails = addLocationDetails(userId, latitude, longitude, currentDateString);
                    CloudantLocationUtils.updateDocument(locationDetails);
                    Log.d("Location Service", "Updated Document for location : " + latitude + ", " + longitude);
                }
                boolean status = validateRisk(location, date, dateFormat, userId);
                Log.d("Location Service", "Threat status: " + status);
                locationMap = new LocationMap(location, status);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
            return locationMap;
        }

        protected void onPostExecute(LocationMap feed) {
            if(getProperties().getProperty(USER_TYPE_COOKIE).equalsIgnoreCase("SERVICE")){
                return;
            }
            if (feed != null) {
                if (feed.status) {
                    Log.d("Location Service", "Inside Covid + result");
                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    notificationBuilder = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID);
                    notification = notificationBuilder.setOngoing(true)
                            .setContentTitle("CoTrack")
                            .setContentText("You are at Risk " + "(latitude: " + feed.locationDetails.getLongitude() + ", longitude: "
                                    + feed.locationDetails.getLongitude() + ") is ~" + disance + " meters away. Please be safe.")
                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .setCategory(Notification.CATEGORY_SERVICE)
                            .setSmallIcon(R.drawable.logo)
                            .setSound(defaultSoundUri)
                            .setColor(getResources().getColor(R.color.primary_darker))
                            .build();
                    manager.notify(0, notificationBuilder.build());
                } else {
                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    notificationBuilder = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID);
                    notification = notificationBuilder.setOngoing(true)
                            .setContentTitle("CoTrack")
                            .setContentText("Your location " + "(latitude: " + feed.locationDetails.getLongitude() + ", longitude: "
                                    + feed.locationDetails.getLongitude() + ") has been updated")
                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .setSound(defaultSoundUri)
                            .setCategory(Notification.CATEGORY_SERVICE)
                            .setSmallIcon(R.drawable.logo)
                            .build();
                    manager.notify(0, notificationBuilder.build());
                }
            }
            Log.d("Notification Service", "Update requested for # of users: ");

        }
    }

    public LocationDetails getNewLocationDetailsDetails(String user_id, String latitude, String longitude, String date) {
        LocationDetails locationDetails = new LocationDetails();
        String _id = "location:L" + CommonUtils.generateRandomDigits(8);


        locationDetails.set_id(_id);
        LatLangDetails latLangDetails = new LatLangDetails();
        latLangDetails.setLatitude(latitude);
        latLangDetails.setLongitude(longitude);
        latLangDetails.setUpload_time(date);
        List<LatLangDetails> latLangDetailsList = new ArrayList<>();
        latLangDetailsList.add(latLangDetails);
        locationDetails.setUser_id(user_id);
        locationDetails.setLatLang_details(latLangDetailsList);
        locationDetails.setCovid_test_status("NOT TAKEN");
        List<OrderDataHolder> orderDataHolder = OrderDataHolder.getAllUserSpecificDetails().get(user_id);
        if (orderDataHolder != null) {
            for (OrderDataHolder dataHolder : orderDataHolder) {
                if (dataHolder.getService_type().equalsIgnoreCase("PATHOLOGY")) {
                    if (dataHolder.getTests() != null
                            && dataHolder.getTests().getTest_type().contains("Covid")
                            && dataHolder.getTests().getTest_status().equalsIgnoreCase("POSITIVE")) {
                        locationDetails.setCovid_test_status("POSITIVE");
                    }
                }
            }
        }
        return locationDetails;

    }

    public LocationDetails addLocationDetails(String user_id, String latitude, String longitude, String date) {
        LocationDetails locationDetails = CloudantLocationUtils.cloudantGetWithPrimaryKey("user_id", user_id).getDocs().get(0);
        List<LatLangDetails> latLangDetailsList = locationDetails.getLatLang_details();
        if (latLangDetailsList == null) {
            latLangDetailsList = new ArrayList<>();
        }
        LatLangDetails latLangDetails = new LatLangDetails();
        latLangDetails.setLatitude(latitude);
        latLangDetails.setLongitude(longitude);
        latLangDetails.setUpload_time(date);
        latLangDetailsList.add(latLangDetails);

        locationDetails.setLatLang_details(latLangDetailsList);
        locationDetails.setCovid_test_status("NOT TAKEN");
        List<OrderDataHolder> orderDataHolder = OrderDataHolder.getAllUserSpecificDetails().get(user_id);
        if(orderDataHolder != null) {
            for (OrderDataHolder dataHolder : orderDataHolder) {
                if (dataHolder.getService_type().equalsIgnoreCase("PATHOLOGY")) {
                    if (dataHolder.getTests() != null
                            && dataHolder.getTests().getTest_type().contains("Covid")
                            && dataHolder.getTests().getTest_status().equalsIgnoreCase("POSITIVE")) {
                        locationDetails.setCovid_test_status("POSITIVE");
                    }
                }
            }
        }
        return locationDetails;

    }

    public boolean validateRisk(Location currentLocation, Date date, SimpleDateFormat dateFormat, String userId) {
        boolean flag = false;
        List<LocationDetails> locationDetailsList = CloudantLocationUtils.getAllData().getDocs();
        for (LocationDetails locationDetails : locationDetailsList) {
            for (LatLangDetails latLangDetails : locationDetails.getLatLang_details()) {
                double lat = Double.parseDouble(latLangDetails.getLatitude());
                double lng = Double.parseDouble(latLangDetails.getLongitude());
                Date trackingDate = null;
                try {
                    trackingDate = dateFormat.parse(latLangDetails.getUpload_time());
                } catch (Exception ex) {
                    return false;
                }
                disance = distance(currentLocation.getLatitude(), currentLocation.getLongitude(), lat, lng);
                Log.d("Location Service","Location User LAT: " + currentLocation.getLatitude());
                Log.d("Location Service","Location User LONG: " + currentLocation.getLongitude());
                Log.d("Location Service","Location User Threat LAT: " + lat);
                Log.d("Location Service","Location User Threat LONG: " + lng);
                Log.d("Location Service","Location User ID: " + userId);
                Log.d("Location Service","Location distance: " + disance);
                Log.d("Location Service","Location Difference: " + getDaysDifference(date, trackingDate) );
                if (disance < 20 && !locationDetails.getUser_id().equalsIgnoreCase(userId) && (getDaysDifference(date, trackingDate) < 30)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public long getDaysDifference(Date newerDate, Date olderDate) {
        long diff = newerDate.getTime() - olderDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    class LocationMap {
        public Location locationDetails;
        public boolean status;

        LocationMap(Location locationDetails, boolean status) {
            this.locationDetails = locationDetails;
            this.status = status;
        }

    }

}
