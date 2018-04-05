package com.example.nauma.restaurantadvisorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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

    private Retrofit retrofit;

    private ListView menuslistview;
    private MenuListViewAdapter menusListViewAdapter;
    private List<Menus> allmenus;
    private TextView menumessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        String restaurantId = getIntent().getStringExtra("restaurantid");
        Log.d("from oncreate", "id : " + restaurantId);

        allmenus = new ArrayList<>();

        this.menuslistview = (ListView) findViewById(R.id.menulistview);
        this.menumessage = (TextView) findViewById(R.id.nomenu);

        menusListViewAdapter = new MenuListViewAdapter(getApplicationContext(), allmenus);
        menuslistview.setAdapter(menusListViewAdapter);

        this.configureRetrofit();
        this.getMenuViaApi(restaurantId);
    }

    private void configureRetrofit()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.62.2:8000/") //http://192.168.0.24:8000/ //http://172.16.30.50:8000/
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restaurantApi = retrofit.create(RestaurantApi.class);
    }

    private void getMenuViaApi(String Id)
    {
        if (menumessage.getVisibility() == View.VISIBLE)
            menumessage.setVisibility(View.GONE);
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
                else {
                    menumessage.setText("No menus found.");
                    menumessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<Menus>> call, Throwable t) {
                Log.d("XXXXX", "onFailure: " + t.getMessage());
                menumessage.setText("Couldn't found any menus.");
                menumessage.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "There was internal Error on ths Server cannot get menus", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
