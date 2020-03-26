package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Buyers.MainActivity;

import static java.lang.Thread.sleep;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash_screen);


        Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        finish();
    }
}
