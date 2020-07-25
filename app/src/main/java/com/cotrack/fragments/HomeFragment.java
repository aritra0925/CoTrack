package com.cotrack.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.cotrack.utils.JSONUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class HomeFragment extends Fragment {

    public static final String COVID_19_URL = "https://api.covid19api.com/summary";

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
        doGetCovidSummary(view.getContext());
        return view;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMapFragment == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = ((SupportMapFragment) getParentFragmentManager().findFragmentById(R.id.map));
            // Check if we were successful in obtaining the map.
            if (mMapFragment != null) {
                setUpMap();
            }
        }

    }

    private void setUpMap() {
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
    }

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
}