package com.example.quarantineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN=2000;

    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo,slogan;
    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.buttom_animation);
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        image=findViewById(R.id.imageView);
        logo=findViewById(R.id.textView);
        slogan=findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                 //   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, SignUp.class);
                    startActivity(intent);
                    finish();
                    // User is signed out
                   // Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        },SPLASH_SCREEN);
    }
}
