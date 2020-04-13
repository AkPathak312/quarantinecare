package com.example.quarantineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class myprofile extends AppCompatActivity {
    private TextView txtEmail,txtName,txtlocation,txtaadhar;
    String name,longi,latti,email,aadhar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        txtlocation=findViewById(R.id.txtmyaddress);
        txtName=findViewById(R.id.txtmyname);
        txtEmail=findViewById(R.id.txtmyemail);
        txtaadhar=findViewById(R.id.txtmyaadhar);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
            } else {
                name= extras.getString("NAME");
                email=extras.getString("EMAIL");
                longi=extras.getString("LONG");
                latti=extras.getString("LATT");
                aadhar=extras.getString("AADHAR");
            }
        } else {
        }

        txtaadhar.setText(aadhar);
        txtName.setText(name);
        txtlocation.setText("("+latti+","+longi+")");
        txtEmail.setText(email);

    }
}
