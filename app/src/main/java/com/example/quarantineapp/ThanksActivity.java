package com.example.quarantineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThanksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
    }

    public void goback(View view) {
        startActivity(new Intent(ThanksActivity.this,MainActivity.class));
        finish();
    }
}
