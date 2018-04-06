package com.example.nauma.restaurantadvisorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        String restaurantId = getIntent().getStringExtra("restaurantid");
        Log.d("from oncreate", "id : " + restaurantId);

        allmenus = new ArrayList<>();

        this.menuslistview = (ListView) findViewById(R.id.menulistview);

        menusListViewAdapter = new MenuListViewAdapter(getApplicationContext(), allmenus);
        menuslistview.setAdapter(menusListViewAdapter);

        restaurantApi = new ConfigRetrofit().configureRetrofit("");
        this.getMenuViaApi(restaurantId);
        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!saved_values.getString("token", "").isEmpty()) {
            ckeckTocken();
        }
    }

    private void ckeckTocken() {
        Button button = (Button) findViewById(R.id.SignUpOrSignIn);
        button.setText("Log out");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = saved_values.edit();
                edit.clear();
                edit.apply();
                finish();
                startActivity(getIntent());
            }
        });
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
    public void GoToSignIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
