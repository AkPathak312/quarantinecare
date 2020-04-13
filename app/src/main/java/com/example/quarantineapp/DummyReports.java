package com.example.quarantineapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DummyReports extends AppCompatActivity {

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_reports);
        builder=new AlertDialog.Builder(this);

    }

    public void retestrequested(View view) {
        builder.setMessage("Medical Officials will contact you shortly for a test!")
        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(DummyReports.this,MainActivity.class);
                startActivity(i);
                
            }
        });
       // builder.setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();


    }
}
