package com.example.maheshbabugorantla.googlemapclusters.MarkerCluster;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

/**
 * DESCRIPTION: MyClusterRenderer class
 *              This is a custom Google Maps Cluster Renderer
 * Created by MaheshBabuGorantla
 * First Update On Apr 08, 2017 .
 * Last Update On Apr 08, 2017.
 */

public class MyClusterRenderer extends DefaultClusterRenderer<MyClusterItem> {

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MyClusterItem> clusterManager) {
        super(context, map, clusterManager);
        //clusterManager.setRenderer(this);
    }

    /**
     *  Called before the marker of a ClusterItem is added to the map.
     * */
    @Override
    protected void onBeforeClusterItemRendered(MyClusterItem item, MarkerOptions markerOptions) {

        if(item.getMarkerIcon() != null) {
            // Here I am retrieving the Image that I want to insert in place of a Default Marker
            markerOptions.icon(item.getMarkerIcon());
        }
        markerOptions.visible(true);
    }

/*    *//**
     *  Called before the marker for a Cluster is added to the map
     *  The default implementation draws a circle with a rough count of the number of items
     * *//*
    @Override
    protected void onBeforeClusterRendered(Cluster<MyClusterItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }*/

    /**
     * Determine when the cluster should be rendered as a cluster or individual markers
     * */
    @Override
    protected boolean shouldRenderAsCluster(Cluster<MyClusterItem> cluster) {
        // This means that always Render Clusters
        return cluster.getSize() > 1;
    }
}
