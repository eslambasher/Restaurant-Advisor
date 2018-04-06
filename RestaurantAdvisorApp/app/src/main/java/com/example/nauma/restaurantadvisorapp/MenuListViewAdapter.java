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

public class MenuListViewAdapter extends BaseAdapter {

    private Context context;

    private List<Menus> menusList;

    public MenuListViewAdapter(Context context, List menusList)
    {
        this.context = context;
        this.menusList = menusList;
    }

    @Override
    public int getCount() {
        return menusList.size();
    }

    @Override
    public Object getItem(int position) {
        return menusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.menus_row, null);
        }

        Menus menus = menusList.get(position);

        TextView textViewMenusName = (TextView) convertView.findViewById(R.id.menus_name);
        TextView textViewMenusPrice = (TextView) convertView.findViewById(R.id.menus_price);
        TextView textViewMenusDescription = (TextView) convertView.findViewById(R.id.menus_description);

        String price = menus.getPrice() + " €";
        textViewMenusName.setText(menus.getName());
        textViewMenusPrice.setText(price);
        textViewMenusDescription.setText(menus.getDescription());

        return convertView;
    }
}
