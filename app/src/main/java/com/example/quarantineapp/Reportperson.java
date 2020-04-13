package com.example.quarantineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.core.Repo;

public class Reportperson extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportperson);
    }

    public void gotothanks(View view) {
        startActivity(new Intent(Reportperson.this,ThanksActivity.class));
        finish();
    }
}
