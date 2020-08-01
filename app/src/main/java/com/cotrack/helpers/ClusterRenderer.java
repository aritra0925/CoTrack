package com.cotrack.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cotrack.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class ClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

    private ImageView mImageView;
    private ImageView mClusterImageView;
    private int mDimension;
    private IconGenerator mClusterIconGenerator = null;
    private IconGenerator mIconGenerator = null;
    Context context;

    public ClusterRenderer(Context context, GoogleMap map,
                           ClusterManager<CustomClusterItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        mClusterIconGenerator = new IconGenerator(context);
        mClusterImageView = new ImageView(context);
        mClusterIconGenerator.setContentView(mClusterImageView);
        mIconGenerator = new IconGenerator(context);
        mImageView = new ImageView(context);
        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(CustomClusterItem item, MarkerOptions markerOptions) {
        markerOptions.title("Covid +").icon(getItemIcon());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }




    @Override
    protected void onBeforeClusterRendered(Cluster<CustomClusterItem> cluster,
                                           MarkerOptions markerOptions) {

        try {
            markerOptions.title("Covid Hotspot: " + cluster.getSize() + " positive users").icon(getClusterItemIcon());
        } catch (Exception e) {
            Log.e("Exception", " Rendering" , e);
        }
            }

    /**
     * Get a descriptor for a single item (i.e., a marker outside a cluster) from their
     * app logo to be used for a marker icon
     *
     * */
    private BitmapDescriptor getItemIcon() {
        mImageView.setImageResource(R.drawable.covid_marker);
        mImageView.setBackgroundColor(Color.TRANSPARENT);
        mImageView.getLayoutParams().height = 70;
        mImageView.getLayoutParams().width = 70;
        mImageView.requestLayout();
        Bitmap icon = mIconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    private BitmapDescriptor getClusterItemIcon() {

        mClusterImageView.setImageResource(R.drawable.logo);
        mClusterImageView.setBackgroundColor(Color.TRANSPARENT);
        mClusterImageView.getLayoutParams().height = 70;
        mClusterImageView.getLayoutParams().width = 70;
        mClusterImageView.requestLayout();
        Bitmap icon = mClusterIconGenerator.makeIcon();

        return BitmapDescriptorFactory.fromBitmap(icon);
    }

}
