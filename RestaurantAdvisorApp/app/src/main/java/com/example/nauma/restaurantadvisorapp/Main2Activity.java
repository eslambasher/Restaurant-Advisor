package com.example.nauma.restaurantadvisorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";


    // Get all textview from xml
    private TextView restaurant_name;
    private TextView restaurant_note;
    private TextView restaurant_weektime;
    private TextView restaurant_weekendtime;
    private TextView restaurant_numero;
    private TextView restaurant_site;
    private TextView restaurant_adresse;
    private TextView restaurant_description;

    private Button gotomenus;

    private String restaurantId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get restaurant details from mainactivity
        final Restaurant restaurantdetail = (Restaurant) getIntent().getExtras().getParcelable("restaurant");
        restaurantId = restaurantdetail.getId().toString();

        // Get textview with the id
        restaurant_name = (TextView) findViewById(R.id.single_restaurant_name);
        restaurant_note = (TextView) findViewById(R.id.single_restaurant_note);
        restaurant_weektime= (TextView) findViewById(R.id.restaurant_weektime);
        restaurant_weekendtime = (TextView) findViewById(R.id.restaurant_weekendtime);
        restaurant_description = (TextView) findViewById(R.id.restaurant_description);
        restaurant_numero = (TextView) findViewById(R.id.restaurant_number);
        restaurant_site = (TextView) findViewById(R.id.restaurant_site);
        restaurant_adresse = (TextView) findViewById(R.id.restaurant_adresse);

        // settext of the textview
        restaurant_name.setText(restaurantdetail.getName());
        restaurant_note.setText(restaurantdetail.getNote());
        restaurant_weektime.setText(restaurantdetail.getTimeOpen_week());
        restaurant_weekendtime.setText(restaurantdetail.getTimeOpen_weekend());
        restaurant_numero.setText(restaurantdetail.getNum_tel());
        restaurant_site.setText(restaurantdetail.getSite_web());
        restaurant_adresse.setText(restaurantdetail.getLocalisation());
        restaurant_description.setText(restaurantdetail.getDescription());

        // make textview scrollable
        restaurant_adresse.setMovementMethod(new ScrollingMovementMethod());
        restaurant_description.setMovementMethod(new ScrollingMovementMethod());

        // get voir menu button
        gotomenus = (Button) findViewById(R.id.buttongetmenu);

        gotomenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain3Activity(restaurantId);
            }
        });
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

    private void startMain3Activity(String restaurantId) {
        Intent intent = new Intent(this,Main3Activity.class);
        intent.putExtra("restaurantid", restaurantId);

        startActivity(intent);
    }

    public void GoToSignIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
