package com.example.maheshbabugorantla.google_places.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.maheshbabugorantla.google_places.CustomViews.RestaurantItem;
import com.example.maheshbabugorantla.google_places.R;

import java.util.ArrayList;

/**
 * DESCRIPTION: RestaurantAdapter class
 * Created by MaheshBabuGorantla
 * First Update On Sep 02, 2017 .
 * Last Update On Sep 02, 2017.
 */
public class RestaurantAdapter extends ArrayAdapter<RestaurantItem> {

    private static class ViewHolder {
        TextView RestaurantRating;
        TextView RestaurantName;
        TextView RestaurantOpenNow;
    }

    public RestaurantAdapter(Context context, ArrayList<RestaurantItem> restaurants) {
        super(context, R.layout.restaurent_item, restaurants);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the restaurant item for this position
        RestaurantItem restaurantItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurent_item, parent, false);
            viewHolder.RestaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
            viewHolder.RestaurantRating = (TextView) convertView.findViewById(R.id.rating);
            viewHolder.RestaurantOpenNow = (TextView) convertView.findViewById(R.id.open_now);

            // Cache the ViewHolder Object to be used later
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Insert the data from the restaurantItem into convertView
        try {
            viewHolder.RestaurantName.setText(restaurantItem.getName());
            viewHolder.RestaurantRating.setText(Double.toString(restaurantItem.getRating()));

            if(restaurantItem.getOpenNow()) {
                viewHolder.RestaurantOpenNow.setText("OPEN");
                viewHolder.RestaurantOpenNow.setTextColor(Color.parseColor("#cddc39"));
            }
            else {
                viewHolder.RestaurantOpenNow.setText("CLOSED");
                viewHolder.RestaurantOpenNow.setTextColor(Color.parseColor("#c62828"));
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
