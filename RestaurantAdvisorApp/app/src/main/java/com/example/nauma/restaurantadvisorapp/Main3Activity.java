package com.example.nauma.restaurantadvisorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main3Activity extends AppCompatActivity {

    private static final String TAG = "Main3Activity";
    private static RestaurantApi restaurantApi;
    private ListView menuslistview;
    private MenuListViewAdapter menusListViewAdapter;
    private List<Menus> allmenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        String restaurantId = getIntent().getStringExtra("restaurantid");
        Log.d("from oncreate", "id : " + restaurantId);

        allmenus = new ArrayList<>();

        this.menuslistview = (ListView) findViewById(R.id.menulistview);

        menusListViewAdapter = new MenuListViewAdapter(getApplicationContext(), allmenus);
        menuslistview.setAdapter(menusListViewAdapter);

        restaurantApi = new ConfigRetrofit().configureRetrofit();
        this.getMenuViaApi(restaurantId);
    }

    private void getMenuViaApi(String Id)
    {
        restaurantApi.getMenusById(Id).enqueue(new Callback<List<Menus>>() {
            @Override
            public void onResponse(Call<List<Menus>> call, Response<List<Menus>> response) {
                List<Menus> listmenus = response.body();
                if (listmenus != null) {
                    for (Menus menus : listmenus) {
                        Log.d("XXXXX", "onResponse menu : " + menus.getName());
                        allmenus.add(menus);
                    }
                    menusListViewAdapter = new MenuListViewAdapter(getApplicationContext(), allmenus);
                    menuslistview.setAdapter(menusListViewAdapter);
                }
                else
                    Log.d("XXXXX", "There is no menus");

            }

            @Override
            public void onFailure(Call<List<Menus>> call, Throwable t) {
                Log.d("XXXXX", "onFailure: " + t.getMessage());
            }
        });
    }
}
