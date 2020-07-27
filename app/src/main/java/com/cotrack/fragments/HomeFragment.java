package com.cotrack.fragments;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cotrack.R;
import com.cotrack.models.Country;
import com.cotrack.models.Countries;
import com.cotrack.models.Global;
import com.cotrack.services.LoginService;
import com.cotrack.utils.JSONUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    final int REQUEST_CODE = 101;
    public static final String COVID_19_URL = "https://api.covid19api.com/summary";
    LoginService mLoginService;
    Intent mServiceIntent;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        if(instance==null){
            instance = new HomeFragment();
        }
        return instance;
    }


    SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private static HomeFragment instance = null;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setUpMapIfNeeded();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        fetchLocation(view.getContext());
        doGetCovidSummary(view.getContext());
        mLoginService = new LoginService();
        mServiceIntent = new Intent(view.getContext(), mLoginService.getClass());
        if (!isMyServiceRunning(mLoginService.getClass())) {
            System.out.println("Service is already running");
            getActivity().startService(new Intent(getActivity(),mLoginService.getClass()));
            System.out.println("Service started");
        } else {
            System.out.println("Service is already running");
        }
        return view;
    }


    private void loadMarkers(ClusterManager<ClusterItem> manager, GoogleMap map, LatLng center, int count,
                             double minDistance, double maxDistance) {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;
        CircleOptions circleOptions= new CircleOptions()
                .strokeWidth(4)
                .radius(maxDistance*2)
                .center(center)
                .strokeColor(Color.parseColor(getResources().getString(R.color.primary_dark_transparent)))
                .fillColor(Color.parseColor(getResources().getString(R.color.primary_dark_transparent)));



        for (int i = 0; i < count; ++i) {
            double distance = minDistance + Math.random() * maxDistance;
            double heading = Math.random() * 360 - 180;

            LatLng position = SphericalUtil.computeOffset(center, distance, heading);

            ClusterItem marker = new ClusterItem() {
                @NonNull
                @Override
                public LatLng getPosition() {
                    return position;
                }

                @Nullable
                @Override
                public String getTitle() {
                    return "Covid 19 Marker";
                }

                @Nullable
                @Override
                public String getSnippet() {
                    return null;
                }
            };
            manager.addItem(marker);
            minLat = Math.min(minLat, position.latitude);
            minLon = Math.min(minLon, position.longitude);
            maxLat = Math.max(maxLat, position.latitude);
            maxLon = Math.max(maxLon, position.longitude);
        }
        map.addCircle(circleOptions);
        LatLng min = new LatLng(minLat, minLon);
        LatLng max = new LatLng(maxLat, maxLon);
        LatLngBounds bounds = new LatLngBounds(min, max);

        //map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 17));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMapFragment == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = ((SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.map));

        }

    }

    /*private void setUpMap() {
        mMapFragment.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                Log.d("Map Debug", "Inside Map Ready");
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });
    }*/

    public void doGetCovidSummary(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                COVID_19_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response: " + response.toString());
                        Log.e("Response", response.toString());
                        Global globalData = JSONUtils.mapJsonObject(response.toString(), Global.class);
                        Log.e("Global", globalData.getGlobal().getNewConfirmed());
                        TextView globalActiveCount = (TextView) getActivity().findViewById(R.id.globalActiveCount);
                        TextView globalConfirmedCount = (TextView) getActivity().findViewById(R.id.globalConfirmedCount);
                        TextView indiaActiveCount = (TextView) getActivity().findViewById(R.id.indiaActiveCount);
                        TextView indiaConfirmedCount = (TextView) getActivity().findViewById(R.id.indiaConfirmedCount);

                        globalActiveCount.setText(globalData.getGlobal().getNewConfirmed());
                        globalConfirmedCount.setText(globalData.getGlobal().getTotalConfirmed());
                        for(Country country:globalData.getCountryList()){
                            if(country.getCountry().equalsIgnoreCase("India")){
                                indiaActiveCount.setText(country.getNewConfirmed());
                                indiaConfirmedCount.setText(country.getTotalConfirmed());
                                if(Integer.parseInt(country.getNewConfirmed())>20000){
                                    indiaActiveCount.setTextColor(getResources().getColor(R.color.primary_dark));
                                }
                                Log.e("India", country.getNewConfirmed());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }

    private void fetchLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(context.getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(HomeFragment.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        /*MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);*/
        ClusterManager<ClusterItem> clusterManager = new ClusterManager<ClusterItem>(view.getContext(), googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        loadMarkers(clusterManager, googleMap, latLng, 10, 20,50);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation(getContext());
                }
                break;
        }
    }
}