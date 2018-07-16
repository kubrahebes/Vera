package com.example.mac.vera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SpleshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        Intent intent = new Intent(SpleshActivity.this, YeniNotEkleActivity.class);

        startActivity(intent);
    }
}
