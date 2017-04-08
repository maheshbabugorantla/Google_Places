package com.example.maheshbabugorantla.googlemapclusters.MarkerCluster;

import com.example.maheshbabugorantla.googlemapclusters.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

/**
 * DESCRIPTION: MyClusterItem class
 *              This class is used to make the marker and be able to turn into a Marker Cluster
 * Created by MaheshBabuGorantla
 * First Update On Mar 22, 2017 .
 * Last Update On Mar 22, 2017.
 */

public class MyClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private BitmapDescriptor mMarkerIcon;

    private final int mCustomIcon;

    public MyClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "Blah";
        mSnippet = "Blah";
//        mMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED); // Default Icon Marker if not specified
        mCustomIcon = R.drawable.thumbnail_marker;
    }

    public MyClusterItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mCustomIcon = R.drawable.thumbnail_marker;
    }

    // This is a constructor which can be used to make a Cluster Item (Marker) using MarkerOptions
    public MyClusterItem(MarkerOptions markerOptions) {
        this.mPosition = markerOptions.getPosition();
        this.mTitle = markerOptions.getTitle();
        this.mSnippet = markerOptions.getSnippet();
        this.mMarkerIcon = markerOptions.getIcon();
        this.mCustomIcon = R.drawable.thumbnail_marker;
    }

/*    // This Constructor allows me to set
    public MyClusterItem(double lat, double lng, String title, String snippet, BitmapDescriptor markerIcon) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
//        mMarkerIcon = markerIcon;
    }*/

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

    public BitmapDescriptor getMarkerIcon() {
        return mMarkerIcon;
    }

    /**
     * TO DO List next Time
     * Make a function called setIcon(); to allow the user to set the icon for the google Marker
     * */

}
