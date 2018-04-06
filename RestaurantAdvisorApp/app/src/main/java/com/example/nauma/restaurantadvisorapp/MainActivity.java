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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class
MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static RestaurantApi restaurantApi;

    private Retrofit retrofit;

    private EditText searchInput;

    // restaurant listview && sort
    private ListView restolist;
    private RestoListViewAdapter restoListViewAdapter;
    private List<Restaurant> restaurants;

    // add restaurant buttons && dialog
    private Dialog addrestodialog;
    private Button add_restobutton;
    private Button closeadd_restobutton;
    private Button confimadd_restobutton;

    // edittext for add restaurant
    private EditText name;
    private EditText timeOpen_week;
    private EditText timeOpen_weekend;
    private EditText num_tel;
    private EditText site_web;
    private EditText localisation;
    private EditText description;

    // progressBar && error message
    private ProgressBar progressrestaurant;
    private TextView restauranterror;
    private TextView tryagainrestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurants = new ArrayList<>();

        this.restolist = (ListView) findViewById(R.id.restolistview);
        this.searchInput = (EditText) findViewById(R.id.searchInput);
        this.add_restobutton = (Button) findViewById(R.id.add_restaurantbutton);

        // sort selector
        Spinner restaurant_spinner = findViewById(R.id.restaurant_spinner);

        restaurantApi = new ConfigRetrofit().configureRetrofit("");
        this.getSorted(restaurant_spinner);
        //progressBar && error
        progressrestaurant = (ProgressBar) findViewById(R.id.progressgetrestaurant);
        restauranterror = (TextView) findViewById(R.id.getrestauranterror);
        tryagainrestaurant = (TextView) findViewById(R.id.tryagain);

        // get restaurant again if there error
        tryagainrestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.getRestaurantViaApi();
            }
        });

        // Create a dialog for add restaurant
        addrestodialog = new Dialog(this);
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

        // show dialog
        add_restobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addrestodialog.show();
            }
        });

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
                    MainActivity.this.addRestaurantViaApi(add_restaurant);

                }
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

        final Toast filtertoast = Toast.makeText(getApplicationContext(), "Aucun resultat trouvé", Toast.LENGTH_SHORT);
        filtertoast.setGravity(0, 0, 0);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.restoListViewAdapter.getFilter().filter(s);
                if (restoListViewAdapter != null) {
                    if (MainActivity.this.restoListViewAdapter.getCount() <= 0)
                        filtertoast.show();
                    else
                        filtertoast.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        restolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = restaurants.get(position);
                startMain2Activity(restaurant);
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
                Toast.makeText(this, "Please complete all fileds !", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void getSorted(final Spinner spinner)
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String Itemvalue = spinner.getSelectedItem().toString();
               String value = spinner.getSelectedItem().toString().replace(" (Desc)", "");

               //clear listview
               restaurants.clear();
               restoListViewAdapter = null;
               restolist.setAdapter(null);

               switch (Itemvalue) {
                   case "none" :
                       MainActivity.this.getRestaurantViaApi();
                       break;
                   case "visite (Desc)" :
                       MainActivity.this.getSortedRestaurants(value);
                       break;
                   case "note (Desc)" :
                       MainActivity.this.getSortedRestaurants(value);
                       break;
                   case "price (Desc)" :
                       MainActivity.this.getSortedRestaurants(value);
                       break;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getRestaurantViaApi()
    {
        progressrestaurant.setVisibility(View.VISIBLE);
        restauranterror.setVisibility(View.GONE);
        tryagainrestaurant.setVisibility(View.GONE);
        restaurantApi.getRestaurants().enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> restaurantList = response.body();
                if(restaurantList != null) {
                    for(Restaurant restaurant: restaurantList){
                        restaurants.add(restaurant);
                    }

                    restoListViewAdapter = new RestoListViewAdapter(getApplicationContext(), restaurants);
                    restolist.setAdapter(restoListViewAdapter);
                }
                else if (restaurants == null) {
                    progressrestaurant.setVisibility(View.GONE);
                    restauranterror.setText("No restaurants found.");
                    restauranterror.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "No resturants found.", Toast.LENGTH_SHORT).show();
                }
                if (progressrestaurant.getVisibility() == View.VISIBLE)
                    progressrestaurant.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                progressrestaurant.setVisibility(View.GONE);
                restauranterror.setVisibility(View.VISIBLE);
                restauranterror.setText("Couldn't found any restaurant.");
                tryagainrestaurant.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "There was internal Error on ths Server cannot get restaurants", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Sort function
    private void getSortedRestaurants(String SortedType)
    {
        progressrestaurant.setVisibility(View.VISIBLE);
        restauranterror.setVisibility(View.GONE);
        tryagainrestaurant.setVisibility(View.GONE);
        restaurantApi.getSortedRestaurants(SortedType).enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                List<Restaurant> restaurantList1 = response.body();
                if(restaurantList1 != null) {
                    for(Restaurant restaurant: restaurantList1){
                        restaurants.add(restaurant);
                    }
                    restoListViewAdapter = new RestoListViewAdapter(getApplicationContext(), restaurants);
                    restolist.setAdapter(restoListViewAdapter);
                }
                else if (restaurants == null) {
                    progressrestaurant.setVisibility(View.GONE);
                    restauranterror.setText("No restaurants foundw.");
                    restauranterror.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "No resturants found sorted.", Toast.LENGTH_SHORT).show();
                }
                if (progressrestaurant.getVisibility() == View.VISIBLE)
                    progressrestaurant.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                progressrestaurant.setVisibility(View.GONE);
                restauranterror.setVisibility(View.VISIBLE);
                restauranterror.setText("Couldn't found any restaurant.");
                tryagainrestaurant.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "There was internal Error on ths Server cannot get restaurants", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRestaurantViaApi(final Restaurant restaurant) {
        restaurantApi.addRestaurants(restaurant).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if(response.code() == 201) {
                    // Update listview
                    MainActivity.this.getRestaurantViaApi();
                    restoListViewAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Restaurant was added successfully", Toast.LENGTH_SHORT).show();
                    MainActivity.this.addrestodialog.dismiss();
                }
                else {
                    try {
                        JSONObject getErrors = new JSONObject(response.errorBody().string());
                        if(getErrors.getString("errors") != null) {
                            getErrors = getErrors.getJSONObject("errors");
                            String errorname = getErrors.getString("name").replace("[", "").replace("\"", "").replace("]", "");
                            Toast.makeText(getApplicationContext(), errorname, Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Internal error: Cannot add the restaurant.", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Internal error: Cannot add the restaurant.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
