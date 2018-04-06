package com.example.nauma.restaurantadvisorapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class SignUp extends AppCompatActivity {

    private UserClass mAuthTask = null;
    private static RestaurantApi restaurantApi;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mNameView;
    private EditText mLastNameView;
    private EditText mUsernameView;
    private EditText mAgeView;
    private EditText mC_passwordView;
    private View mProgressView;
    private View mSignUpFormView;
    private View mLayoutText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mUsernameView = findViewById(R.id.userName);
        mAgeView = findViewById(R.id.age);
        mNameView = findViewById(R.id.name);
        mLastNameView = findViewById(R.id.lastName);
        mC_passwordView = findViewById(R.id.c_password);
        Button mEmailSignInButton = findViewById(R.id.email_sign_up_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignUpFormView = findViewById(R.id.signUp_form);
        mProgressView = findViewById(R.id.signUp_progress);
        mLastNameView = findViewById(R.id.LayoutText);
    }

    private void setErrors() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mLastNameView.setError(null);
        mNameView.setError(null);
        mC_passwordView.setError(null);
        mAgeView.setError(null);
        mUsernameView.setError(null);
    }

    public boolean hasText(EditText editText, View focusView, boolean cancel) {
        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
            return false;
        }
        return true;
    }

    private void attemptLogin() {
        if (mAuthTask != null) return;
        setErrors();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String name = mNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String UserName = mUsernameView.getText().toString();
        String age = mAgeView.getText().toString();
        String c_password = mC_passwordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (!hasText(mUsernameView, focusView, cancel) ) return;
        if (!hasText(mNameView, focusView, cancel) ) return;
        if (!hasText(mLastNameView, focusView, cancel) ) return;
        if (!hasText(mAgeView, focusView, cancel) ) return;
        if (!hasText(mEmailView, focusView, cancel) ) return;
        else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (!hasText(mPasswordView, focusView, cancel) ) return;
        if (!hasText(mC_passwordView, focusView, cancel) ) return;

        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }   if (!isPasswordValid(c_password)) {
            mC_passwordView.setError(getString(R.string.error_invalid_password));
            focusView = mC_passwordView;
            cancel = true;
        }   if (!(password.equals(c_password))) {
            mC_passwordView.setError("Password is not same");
            focusView = mC_passwordView;
            cancel = true;
        }

        if (cancel) focusView.requestFocus();
        else {
            showProgress(true);
            restaurantApi = new ConfigRetrofit().configureRetrofit();
            mAuthTask = new UserClass(email, password, c_password, UserName, name,lastName,age);
            this.signUpFun();
        }
    }

    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9-]+(\\-[A-Za-z0-9])*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9])");
        Matcher m = p.matcher(email);
        return m.find();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    public void onClick(View v) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLayoutText.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mLayoutText.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLayoutText.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }

    private void signUpFun() {
        restaurantApi.signUp(mAuthTask).enqueue(new Callback<UserClass>() {
            @Override
            public void onResponse(Call<UserClass> call, Response<UserClass> response) {
                if(response.isSuccessful()) {
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Action was not successful", Toast.LENGTH_SHORT).show();
                    mAuthTask = null;
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<UserClass> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connected Errors !", Toast.LENGTH_SHORT).show();
                mAuthTask = null;
                showProgress(false);
            }
        });
    }
}

