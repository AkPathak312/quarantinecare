package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private  static final int LOCATION_CODE=1;
    Window window;
    private TextView txtLocation;
    //private ProgressDialog progressDialog;
    Geocoder geocoder;
    List<Address> addresses;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    TextView txtname,txtlastuploaded;
    String[] data;
    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Checking for location permission
        txtLocation=findViewById(R.id.txtlocation);
        geocoder = new Geocoder(this, Locale.getDefault());

        databaseReference=FirebaseDatabase.getInstance().getReference("users/"+uid);
        txtlastuploaded=findViewById(R.id.textView3);
        txtname=findViewById(R.id.textView2);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting details...");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i=(int)dataSnapshot.getChildrenCount();
                data=new String[i];
                Log.i("UID",uid);
                int j=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Log.i("RESULT",ds.getValue().toString());
                    data[j]=ds.getValue().toString();
                    j++;
                }
                Log.i("TAG",data[3]);
                progressDialog.cancel();
                txtname.setText(data[6]);
                if(data[3].equals("NA")){
                    txtlastuploaded.setText("No Picture Uploaded Yet !");
                }else
                    txtlastuploaded.setText("Last Pic Uploaded:"+data[3]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
            // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_CODE);
        }else{
            getLocation();
        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_CODE&&grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else{
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private  void getLocation(){
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                LocationServices.getFusedLocationProviderClient(MainActivity.this)
                        .removeLocationUpdates(this);
                if(locationResult!=null && locationResult.getLocations().size()>0){
                    int latestlocationindex=locationResult.getLocations().size()-1;
                    double lattitude= locationResult.getLocations().get(latestlocationindex).getLatitude();
                    double longitude=locationResult.getLocations().get(latestlocationindex).getLongitude();
                    try {
                        addresses=geocoder.getFromLocation(lattitude,longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String address=addresses.get(0).getAddressLine(0);
                        String area=addresses.get(0).getLocality();
                        String city=addresses.get(0).getAdminArea();
                        String postal=addresses.get(0).getPostalCode();
                    txtLocation.setText("Co-ordinates : ("+lattitude+","+longitude+") \n\n"+address+", "+area+", "+city+", -"+postal);


                }
            }
        }, Looper.getMainLooper());

    }

    public void clickPhoto(View view) {

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
            Toast.makeText(MainActivity.this, "Turn on Location Pls", Toast.LENGTH_SHORT).show();
        }else if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_CODE);
        }else{


            Intent i=new Intent(MainActivity.this,UploadImage.class);
            startActivity(i);
        }
    }







    public void gotoMyProfile(View view) {
        Intent i=new Intent(MainActivity.this,myprofile.class);
        i.putExtra("EMAIL",data[2]);
        i.putExtra("NAME",data[6]);
        i.putExtra("AADHAR",data[0]);
        i.putExtra("LATT",data[4]);
        i.putExtra("LONG",data[5]);
        startActivity(i);
    }


    public void gotomyreports(View view) {
        Intent i=new Intent(MainActivity.this,DummyReports.class);
        startActivity(i);


    }

    public void gotoservices(View view) {
        startActivity(new Intent(MainActivity.this,EmergencyServices.class));

    }

    public void gotoweb(View view) {
        startActivity(new Intent(MainActivity.this,CovidWebView.class));
    }

    public void reportperson(View view) {
        startActivity(new Intent(MainActivity.this, Reportperson.class));

    }
}
