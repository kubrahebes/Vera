package com.example.mac.vera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SpleshActivity extends AppCompatActivity {
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        pref = SpleshActivity.this.getSharedPreferences("MyPref", 0);
        // pref = getSharedPreferences("MyPref", 0); // 0 - for private mode


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getBoolean("IS_LOGIN", false)) {
                    startActivity(new Intent(SpleshActivity.this, MainActivity.class));

                } else {
                    startActivity(new Intent(SpleshActivity.this, LoginActivity.class));

                }
            }
        }, 2000);


    }
}
