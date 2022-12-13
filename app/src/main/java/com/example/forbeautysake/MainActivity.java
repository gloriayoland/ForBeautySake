package com.example.forbeautysake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // this is duration of splash screen
    private static int SPLASH_SCREEN = 5000;

    // define variables
    Animation topanim;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // input animation from res> anim folder
        topanim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        // find components by id according to the defined variable
        logo = findViewById(R.id.splashscreen_fbs);
        // set animation for logo
        logo.setAnimation(topanim);

        //define the next page after splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_SCREEN);
    }
}