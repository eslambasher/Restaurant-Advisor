package com.example.nauma.restaurantadvisorapp;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";
    private static RestaurantApi restaurantApi;

    private Retrofit retrofit;

    // Get all textview from xml
    private TextView restaurant_name;
    private TextView restaurant_note;
    private TextView restaurant_weektime;
    private TextView restaurant_weekendtime;
    private TextView restaurant_numero;
    private TextView restaurant_site;
    private TextView restaurant_adresse;
    private TextView restaurant_description;

    // get menus button
    private Button gotomenus;

    // get comments listview
    private ListView commentslistview;
    private CommentsListViewAdapter commentsListViewAdapter;
    private List<Comments> allComments;
    private TextView commentmessage;
    private TextView totalcomments;
    private Dialog addcommentdialog;
    private TextView addcomment;
    private Button confirmaddcomment;
    private Button closecommentdialog;

    private String restaurantId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ScrollView scroll = (ScrollView) findViewById(R.id.restaurantscroll);

        // force scrollview to the top
        scroll.smoothScrollTo(0,0);
        scroll.scrollTo(0,0);

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
        // enable child scroll
        restaurant_description.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // make textview scrollable
        restaurant_adresse.setMovementMethod(new ScrollingMovementMethod());
        restaurant_description.setMovementMethod(new ScrollingMovementMethod());

        // get menu
        gotomenus = (Button) findViewById(R.id.buttongetmenu);
        gotomenus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain3Activity(restaurantId);
            }
        });

        // get comment listview
        commentslistview = (ListView) findViewById(R.id.commentslistview);
        commentslistview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        allComments = new ArrayList<>();
        totalcomments = (TextView) findViewById(R.id.totalcomments);
        commentmessage = (TextView) findViewById(R.id.nocomment);

        addcommentdialog = new Dialog(this);
        addcommentdialog.setContentView(R.layout.add_comment);
        addcommentdialog.setTitle("Ajouter un commentaire");
        addcomment = findViewById(R.id.opencommentdialog);
        confirmaddcomment = addcommentdialog.findViewById(R.id.addcommentbutton);
        closecommentdialog = addcommentdialog.findViewById(R.id.cancelcommentdialog);

        // show add comment dialog
        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcommentdialog.show();
            }
        });

        // close comment dialog
        closecommentdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcommentdialog.dismiss();
            }
        });

        this.configureRetrofit();
        this.getSingleRestaurant(restaurantId);
        this.getCommentsViaApi(restaurantId);
    }

    private void startMain3Activity(String restaurantId) {
        Intent intent = new Intent(this,Main3Activity.class);
        intent.putExtra("restaurantid", restaurantId);

        startActivity(intent);
    }

    private void configureRetrofit()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.14.61:8000/") //http://192.168.0.24:8000/ //http://172.16.30.50:8000/
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restaurantApi = retrofit.create(RestaurantApi.class);
    }

    private void getSingleRestaurant(final String restaurantId) {
        restaurantApi.getSingleRestaurants(restaurantId).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                Restaurant restaurant = response.body();
            Log.d(TAG, "onResponse: restaurant visite : " + restaurant.getVisite());
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getCommentsViaApi(String restaurantId) {
        if (commentmessage.getVisibility() == View.VISIBLE)
            commentmessage.setVisibility(View.GONE);
        restaurantApi.getCommentById(restaurantId).enqueue(new Callback<List<Comments>>() {
            @Override
            public void onResponse(Call<List<Comments>> call, Response<List<Comments>> response) {
                List<Comments> listcomments = response.body();
                if (listcomments != null)
                {
                    for (Comments comments : listcomments)
                    {
                        allComments.add(comments);
                    }
                    commentsListViewAdapter = new CommentsListViewAdapter(getApplicationContext(), allComments);
                    commentslistview.setAdapter(commentsListViewAdapter);
                    if (String.valueOf(allComments.size()).equals("0")) {
                        commentmessage.setText("No comment found.");
                        commentmessage.setVisibility(View.VISIBLE);
                    }
                    totalcomments.setText(String.valueOf(allComments.size()));
                }
                else
                {
                    commentmessage.setText("No comment found.");
                    commentmessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Comments>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                commentmessage.setText("Couldn't found any comment.");
                commentmessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addCommentViaApi(final Restaurant restaurant) {
        restaurantApi.addRestaurants(restaurant).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if(response.code() == 201) {
                    // Update listview
                    allComments.clear();
                    Main2Activity.this.getCommentsViaApi(restaurantId);
                    commentsListViewAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Restaurant was added successfully", Toast.LENGTH_SHORT).show();
                    Main2Activity.this.addcommentdialog.dismiss();
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
