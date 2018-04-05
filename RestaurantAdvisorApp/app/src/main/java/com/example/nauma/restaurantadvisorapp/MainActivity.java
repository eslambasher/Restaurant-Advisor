package com.example.nauma.restaurantadvisorapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static RestaurantApi restaurantApi;

    private Retrofit retrofit;

    private ListView restolist;
    private EditText searchInput;
    private SearchView searchView;
    private Button add_restobutton;
    private Button closeadd_restobutton;
    private Button confimadd_restobutton;
    private RestoListViewAdapter restoListViewAdapter;

    // edittext for add restaurant
    private EditText name;
    private EditText timeOpen_week;
    private EditText timeOpen_weekend;
    private EditText num_tel;
    private EditText site_web;
    private EditText localisation;
    private EditText description;

    private List<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurants = new ArrayList<>();

        this.restolist = (ListView) findViewById(R.id.restolistview);
        this.searchInput = (EditText) findViewById(R.id.searchInput);
        this.add_restobutton = (Button) findViewById(R.id.add_restaurantbutton);

        // Create a dialog for add restaurant
        final Dialog addrestodialog = new Dialog(this);
        addrestodialog.setContentView(R.layout.restaurant_register);
        addrestodialog.setTitle("Add restaurant");

        //get editext add restaurant values
        name = addrestodialog.findViewById(R.id.add_restaurant_name);
        num_tel = addrestodialog.findViewById(R.id.add_restaurant_numero);
        site_web = addrestodialog.findViewById(R.id.add_restaurant_site);
        description = addrestodialog.findViewById(R.id.add_restaurant_description);
        localisation = addrestodialog.findViewById(R.id.add_restaurant_adresse);
        timeOpen_week = addrestodialog.findViewById(R.id.add_restaurant_timeopenweek);
        timeOpen_weekend = addrestodialog.findViewById(R.id.add_restaurant_timeopenweekend);

        // confirm add restaurant
        this.confimadd_restobutton = (Button) addrestodialog.findViewById(R.id.confirmrestaurant);
        confimadd_restobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // verify values if null return Toast message
                boolean fieldsOK = validate(new EditText[] { name,
                        num_tel, site_web, description,
                        localisation, timeOpen_week, timeOpen_weekend
                });

                if (fieldsOK) {
                    Restaurant add_restaurant = new Restaurant();
                    add_restaurant.setName(name.getText().toString().trim());
                    add_restaurant.setNum_tel(num_tel.getText().toString().trim());
                    add_restaurant.setNote("0.0");
                    add_restaurant.setSite_web(site_web.getText().toString().trim());
                    add_restaurant.setDescription(description.getText().toString().trim());
                    add_restaurant.setLocalisation(localisation.getText().toString().trim());
                    add_restaurant.setTimeOpen_week(timeOpen_week.getText().toString().trim());
                    add_restaurant.setTimeOpen_weekend(timeOpen_weekend.getText().toString().trim());

                    // add to database
                    Log.d("XXXX", "getr name : " + add_restaurant.getName());
                    Toast.makeText(getApplicationContext(), "name : " + add_restaurant.getNote(), Toast.LENGTH_SHORT).show();

                    MainActivity.this.addRestaurantViaApi(add_restaurant);
                }
            }
        });
        // show dialog
        add_restobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrestodialog.show();
            }
        });

        // close dialog
        this.closeadd_restobutton = (Button) addrestodialog.findViewById(R.id.canceladdrestaurant);
        closeadd_restobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrestodialog.dismiss();
            }
        });

        restoListViewAdapter = new RestoListViewAdapter(getApplicationContext(), restaurants);
        restolist.setAdapter(restoListViewAdapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (restoListViewAdapter != null) {
                    MainActivity.this.restoListViewAdapter.getFilter().filter(s);
                    Log.d("filter", "filter available");

                } else {
                    Log.d("filter", "no filter available");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (restoListViewAdapter == null)
                    Toast.makeText(getApplicationContext(), "Aucun resultat trouvé !", Toast.LENGTH_SHORT).show();
            }
        });

       this.configureRetrofit();
       this.getRestaurantViaApi();

        restolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant1 = restaurants.get(position);
                startMain2Activity(restaurant1);
            }
        });
    }

    private void startMain2Activity(Restaurant restaurant) {
        Intent intent = new Intent(this,Main2Activity.class);
        intent.putExtra("restaurant", restaurant);

        startActivity(intent);
    }

    private boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                Log.d("XXXXX", "Please complete all fileds !");
                Toast.makeText(this, "Please complete all fileds !", Toast.LENGTH_SHORT).show();

                return false;
            }
        }
        return true;
    }

    private void configureRetrofit()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.14.24:8000/") //http://192.168.0.24:8000/
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restaurantApi = retrofit.create(RestaurantApi.class);
    }

    private void getRestaurantViaApi()
    {
        restaurantApi.getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                Log.d(TAG, "onResponse:");
                List<Restaurant> restaurantList = response.body();
                if(restaurantList != null) {
                    for(Restaurant restaurant: restaurantList){
                        restaurants.add(restaurant);
                    }
                    restoListViewAdapter = new RestoListViewAdapter(getApplicationContext(), restaurants);
                    restolist.setAdapter(restoListViewAdapter);
                }
                else {
                  Log.d(TAG, "onResponse: resturants is empty: " + response.body().toString());
                    Toast.makeText(getApplicationContext(), "No resturants found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e(TAG, "onFailure:" + t.getMessage());
                Toast.makeText(getApplicationContext(), "There was internel Error on ths Server cannot get restaurants", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void addRestaurantViaApi(final Restaurant restaurant) {
       restaurantApi.addRestaurants(restaurant).enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, Response<String> response) {
               if(response.body() != null) {
                   Log.d(TAG, "The restaurant was added");
                   Log.d(TAG, "Onresponse: " + response.body().toString());
                   Toast.makeText(getApplicationContext(), "The restaurant was added", Toast.LENGTH_SHORT).show();

               }
               else {
                   Log.d(TAG, "Cannot add the restaurant");
                   Log.d(TAG, "Onresponse: " + restaurant.getName());
                   Toast.makeText(getApplicationContext(), "Cannot add the restaurant", Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {
               Toast.makeText(getApplicationContext(), "There was internel Error on ths Server cannot get restaurant", Toast.LENGTH_LONG).show();

           }
       });
    }

}
