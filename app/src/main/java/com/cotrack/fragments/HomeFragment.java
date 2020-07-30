package com.cotrack.fragments;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cotrack.R;
import com.cotrack.models.Country;
import com.cotrack.models.Global;
import com.cotrack.services.LocationService;
import com.cotrack.utils.JSONUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class HomeFragment extends Fragment implements OnMapReadyCallback {


    List<LatLng> latLngList;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    final int REQUEST_CODE = 101;
    public static final String COVID_19_URL = "https://api.covid19api.com/summary";
    LocationService mLocationService;
    Intent mServiceIntent;
    RadioGroup radioGroup;
    RadioButton countryRadio;
    String DEFAULT_COUNTRY = "India";
    String country = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        if (instance == null) {
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
        radioGroup = (RadioGroup) view.findViewById(R.id.covidSelection);
        countryRadio = (RadioButton) view.findViewById(R.id.india);
        latLngList = new ArrayList<>();
        setUpMapIfNeeded();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        fetchLocation(view.getContext());
        loadFeedData(view.getContext());
        mLocationService = new LocationService();
        mServiceIntent = new Intent(view.getContext(), mLocationService.getClass());
        if (!isMyServiceRunning(mLocationService.getClass())) {
            System.out.println("Service is already running");
            getActivity().startService(new Intent(getActivity(), mLocationService.getClass()));
            System.out.println("Service started");
        } else {
            System.out.println("Service is already running");
        }
        // This overrides the radiogroup onCheckListener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!country.isEmpty()) {
                    DEFAULT_COUNTRY = country;
                    countryRadio.setText(DEFAULT_COUNTRY);
                }
                loadFeedData(view.getContext());
            }
        });
        return view;
    }


    private void loadMarkers(ClusterManager<ClusterItem> manager, GoogleMap map, LatLng center, int count,
                             double minDistance, double maxDistance) {
        map.clear();
        manager.clearItems();
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;
        CircleOptions circleOptions = new CircleOptions()
                .strokeWidth(4)
                .radius(maxDistance * 2)
                .center(center)
                .strokeColor(Color.parseColor(getResources().getString(R.color.primary_dark_transparent)))
                .fillColor(Color.parseColor(getResources().getString(R.color.primary_dark_transparent)));


        for (int i = 0; i < count; ++i) {
            double distance = minDistance + Math.random() * maxDistance;
            double heading = Math.random() * 360 - 180;

            LatLng position = SphericalUtil.computeOffset(center, distance, heading);
            latLngList.add(position);
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
        loadStatusData(view.getContext(), center, latLngList);
        country = getCurrentLocationCountry(view.getContext(), center);
        if (!country.isEmpty()) {
            DEFAULT_COUNTRY = country;
            countryRadio.setText(DEFAULT_COUNTRY);
            loadFeedData(view.getContext());
        }

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

    public void doGetCovidSummary(Context context, String countryName, boolean isCountry) {
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
                        TextView covidActiveCount = (TextView) getActivity().findViewById(R.id.covidActiveCount);
                        TextView covidDeceasedCount = (TextView) getActivity().findViewById(R.id.covidDeceasedCount);
                        TextView covidRecoveredCount = (TextView) getActivity().findViewById(R.id.covidRecoveredCount);
                        TextView covidTotalCount = (TextView) getActivity().findViewById(R.id.covidTotalCount);

                        if (isCountry) {
                            for (Country country : globalData.getCountryList()) {
                                if (country.getCountry().equalsIgnoreCase(countryName) || country.getCountry().contains(countryName) || countryName.contains(country.getCountry())) {
                                    covidActiveCount.setText(country.getNewConfirmed());
                                    covidDeceasedCount.setText(country.getTotalDeaths());
                                    covidRecoveredCount.setText(country.getTotalRecovered());
                                    covidTotalCount.setText(country.getTotalConfirmed());
                                }

                            }
                        } else {
                            covidActiveCount.setText(globalData.getGlobal().getNewConfirmed());
                            covidDeceasedCount.setText(globalData.getGlobal().getTotalDeaths());
                            covidRecoveredCount.setText(globalData.getGlobal().getTotalRecovered());
                            covidTotalCount.setText(globalData.getGlobal().getTotalConfirmed());
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
                    // Toast.makeText(context.getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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
        loadMarkers(clusterManager, googleMap, latLng, 10, 20, 50);
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

    public void loadFeedData(Context context) {
        boolean isCountry = false;
        radioGroup = (RadioGroup) view.findViewById(R.id.covidSelection);
        //RadioButton radioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
        if (countryRadio.isChecked()) {
            isCountry = true;
        }
        doGetCovidSummary(context, DEFAULT_COUNTRY, isCountry);
    }

    public void loadStatusData(Context context, LatLng centerLocation, List<LatLng> markers) {
        float minDistance = 500;
        for (LatLng latLng : markers) {
            float distance = distance(latLng.latitude, latLng.longitude, centerLocation.latitude, centerLocation.longitude);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        TextView status = view.findViewById(R.id.statusLocationBased);
        TextView distance = view.findViewById(R.id.riskDistance);
        MaterialCardView cardView = view.findViewById(R.id.statusCardView);
        if (minDistance < 20) {
            status.setText("At Risk");
            status.setTextColor(R.color.red);
        } else {
            status.setText("Safe");
        }
        distance.setText("~ " + (int) minDistance + " meters");
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

    public String getCurrentLocationCountry(Context context, LatLng latLng) {
        String countryName = "";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Log.e("Address Issue", "Error getting address", e);
        }
        if (addresses.size() > 0) {
            countryName = addresses.get(0).getCountryName();
        }
        return countryName;
    }
}