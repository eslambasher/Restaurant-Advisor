package com.example.nauma.restaurantadvisorapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nauma on 30/03/2018.
 */

public class RestoListViewAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Restaurant> restaurantList;
    private List<Restaurant> restaurantFilterList;
    private ValueFilter valueFilter;

    public RestoListViewAdapter(Context context, List restaurantList)
    {
        this.context = context;
        this.restaurantList = restaurantList;
        restaurantFilterList = restaurantList;
    }

    @Override
    public int getCount() {
        return restaurantList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.restaurant_row, null);
        }

        Restaurant restaurant = restaurantList.get(position);

        TextView textViewRestaurantName = (TextView) convertView.findViewById(R.id.restaurant_name);
        TextView textViewRestaurantId = (TextView) convertView.findViewById(R.id.restaurant_id);
        TextView textViewRestaurantNote = (TextView) convertView.findViewById(R.id.restaurant_note);

        textViewRestaurantName.setText(restaurant.getName());
        textViewRestaurantId.setText(restaurant.getId());
        textViewRestaurantNote.setText(restaurant.getNote());
        textViewRestaurantNote.setTextColor(Color.parseColor("#FFFFFF"));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null)
            valueFilter = new ValueFilter();
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Restaurant> filterList = new ArrayList<Restaurant>();
                for(int i = 0; i < restaurantFilterList.size(); i++) {
                    if ((restaurantFilterList.get(i).getName().toUpperCase())
                        .contains(constraint.toString().toUpperCase())) {

                        Restaurant restaurant = new Restaurant(restaurantFilterList.get(i)
                                .getName(), restaurantFilterList.get(i)
                                .getNote(), restaurantFilterList.get(i)
                                .getId());

                        filterList.add(restaurant);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {
                results.count = restaurantFilterList.size();
                results.values = restaurantFilterList;
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            restaurantList = (ArrayList<Restaurant>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
