package com.cotrack.helpers;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.ui.IconGenerator;

public class CustomClusterItem implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public CustomClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public CustomClusterItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}