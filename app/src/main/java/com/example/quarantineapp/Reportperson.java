package com.example.quarantineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Repo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Reportperson extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText name,mobile,aadhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportperson);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        name=findViewById(R.id.report_name);
        mobile=findViewById(R.id.report_mobile);
        aadhar=findViewById(R.id.report_aadhar);
    }

    public void gotothanks(View view) {
        if(name.getText().toString().equals("")||mobile.getText().toString().equals("")){
            Toast.makeText(this, "One or more empty Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Map map=new HashMap();
        map.put("name",name.getText().toString());
        map.put("mobile",mobile.getText().toString());
        map.put("aadhar",aadhar.getText().toString());
        map.put("status","Scheduled");
        Date date=new Date();
        String stamp=date.getTime()+"";
        Log.d("TAG",stamp);
        databaseReference.child("report").child(GlobalVariables.USER_EMAIL).child(stamp).updateChildren(map);
        startActivity(new Intent(Reportperson.this,ReportedActivity.class));
        finish();
    }

    public void gotoreportedpersons(View view) {
        startActivity(new Intent(Reportperson.this,ReportedActivity.class));
    }
}
