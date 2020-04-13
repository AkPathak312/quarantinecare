package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import static androidx.appcompat.widget.AppCompatDrawableManager.get;

public class Register extends AppCompatActivity {

    Window window;
    private  static final int LOCATION_CODE=1;
    private  EditText uid,email,name,age,location,aadhar;
    private Button submit,getlocation;
    String xuid,xname,xemail,xage,xlocation,xaadhar;
    private DatabaseReference databaseReference;
    Double lattitude,longitude;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    String gotmail="",gotuid="",gotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog=new ProgressDialog(Register.this);
        //Initialising UI
        uid=findViewById(R.id.edtUid);
        email=findViewById(R.id.edtMail);
        name=findViewById(R.id.edtName);
        age=findViewById(R.id.edtAge);
        location=findViewById(R.id.edtLocation);
        aadhar=findViewById(R.id.edtAadhar);
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        auth=FirebaseAuth.getInstance();
       // xuid=
        ///Log.i("TAG",databaseReference.toString());

        //Status Bar Color
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                gotmail= null;
            } else {
                gotmail= extras.getString("EMAIL");
                gotuid=extras.getString("UID");
             //   gotpass=extras.getString("PASS");
            }
        } else {
            gotmail= (String) savedInstanceState.getSerializable("EMAIL");
            gotuid=(String)savedInstanceState.getSerializable("UID");
            //gotpass=(String)savedInstanceState.getSerializable("PASS");
        }

        uid.setText("Please fill in to complete Login Process!");
        email.setText(gotmail);



        //Checking Internet
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if(!connected){
            Toast.makeText(Register.this, "Please enable internet", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(Register.this,ErrorPage.class);
            startActivity(i);
            finish();
        }

        Toast.makeText(Register.this, gotpass+""+gotmail, Toast.LENGTH_SHORT).show();


    }




    //OnCick of Submit button
    public void gotoDashboard(View view) {

        //xuid=uid.getText().toString().trim();
        xname=name.getText().toString().trim();
        xemail=email.getText().toString().trim();
        xage=age.getText().toString().trim();
        xlocation=location.getText().toString().trim();
        xaadhar=aadhar.getText().toString().trim();
        if(TextUtils.isEmpty(xname)||TextUtils.isEmpty(xemail)||TextUtils.isEmpty(xaadhar)||TextUtils.isEmpty(xage)||TextUtils.isEmpty(xlocation)){
            Toast.makeText(Register.this, "All fields are compulsory!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            UserModel userModel=new UserModel(gotuid,xemail,xname,xage,xaadhar,lattitude.toString(),longitude.toString(),"","");
            databaseReference.child(gotuid).setValue(userModel);
            progressDialog.cancel();
            Intent i=new Intent(Register.this,MainActivity.class);
            startActivity(i);
            finish();


        }

//
//        Intent i=new Intent(Register.this,MainActivity.class);
//        startActivity(i);
//        finish();
    }

    public void getLocation(View view) {
        //Checking Location On/Off
        final Context context=getApplicationContext();
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            Toast.makeText(Register.this, "Location Not Enabled", Toast.LENGTH_SHORT).show();
        }else if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_CODE);
        }else{

            get();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_CODE&&grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                get();
            }else{
                Toast.makeText(Register.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }



    private  void get(){
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Register.this).requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LocationServices.getFusedLocationProviderClient(Register.this)
                        .removeLocationUpdates(this);
                if(locationResult!=null && locationResult.getLocations().size()>0){
                    int latestlocationindex=locationResult.getLocations().size()-1;
                    lattitude= locationResult.getLocations().get(latestlocationindex).getLatitude();
                    longitude=locationResult.getLocations().get(latestlocationindex).getLongitude();
                    location.setText("("+lattitude+","+longitude+")");


                }
            }
        }, Looper.getMainLooper());

    }
}
