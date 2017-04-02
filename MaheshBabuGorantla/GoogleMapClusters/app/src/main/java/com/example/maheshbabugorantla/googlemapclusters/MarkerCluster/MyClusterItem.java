package com.example.maheshbabugorantla.googlemapclusters.MarkerCluster;

import com.google.android.gms.maps.model.LatLng;
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

    public MyClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "Blah";
        mSnippet = "Blah";
    }

    public MyClusterItem(double lat, double lng, String title, String snippet) {
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
