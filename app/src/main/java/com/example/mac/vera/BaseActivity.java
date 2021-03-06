package com.example.mac.vera;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mac.vera.models.Kullanicilar;
import com.google.gson.Gson;


/**
 * Created by User on 27.02.2018.
 */

public class BaseActivity extends AppCompatActivity {
    SharedPreferences preferences;
    public static BaseActivity instance;

    public static BaseActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public Kullanicilar getUser() {

        return new Gson().fromJson(preferences.getString("savedUser",  new Gson().toJson(new Kullanicilar())), Kullanicilar.class);

    }

    public void saveUser(Kullanicilar user) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("savedUser", new Gson().toJson(user));
        editor.commit();
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }
}
