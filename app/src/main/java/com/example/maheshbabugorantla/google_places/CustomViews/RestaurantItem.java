package com.example.maheshbabugorantla.google_places.CustomViews;

import android.support.annotation.NonNull;

/**
 * DESCRIPTION: RestaurantItem class
 * Created by MaheshBabuGorantla
 * First Update On Sep 02, 2017 .
 * Last Update On Sep 02, 2017.
 */
public class RestaurantItem implements Comparable<RestaurantItem> {

    private double rating;
    private String name;
    private boolean open_now;
    private String place_id;
    private float distanceBetween;
    private boolean searchSetting;

    public RestaurantItem(double rating,@NonNull String name, boolean open_now, String place_id, float distanceBetween, boolean searchSetting) {
        this.rating = rating;
        this.name = name;
        this.open_now = open_now;
        this.place_id = place_id;
        this.distanceBetween = distanceBetween;
        this.searchSetting = searchSetting;
    }

    public double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public boolean getOpenNow() {
        return open_now;
    }

    public String getPlaceId() { return place_id; }

    public float getDistanceBetween() { return distanceBetween; }

    @Override
    public int compareTo(@NonNull RestaurantItem o) {

            if ((!this.open_now && !o.getOpenNow()) || (this.open_now && o.getOpenNow())) {

                if (!searchSetting) { // Order by Rating

                    if (this.rating > o.getRating()) {
                        return -1;
                    } else if (this.rating < o.getRating()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }

                else { // Order by the distance of the restaurant from the user's current location
                    if (this.distanceBetween < o.getDistanceBetween()) {
                        return -1;
                    } else if (this.rating > o.getRating()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
            else if (!this.open_now) {
                return 1;
            } else {
                return -1;
            }
    }
    @Override
    public String toString() {
        return "\nRestaurant: " + this.name + ", Rating: " + this.rating + ", Open/Closed: " + this.open_now + "\n";
    }
}
