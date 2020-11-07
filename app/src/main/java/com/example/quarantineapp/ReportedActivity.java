package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quarantineapp.DataModels.ReportModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class ReportedActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;
    Button dashboard;
    List<ReportModel> reportModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported);
        dashboard=findViewById(R.id.report_back);
        listView=findViewById(R.id.report_list);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        loadData();
    }

    public void gotodash(View view) {
        startActivity(new Intent(ReportedActivity.this,MainActivity.class));
        finish();
    }

    class CustomAdatpter extends BaseAdapter {

        @Override
        public int getCount() {
            return reportModels.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.single_reportedperson_layout,null);
            TextView name=convertView.findViewById(R.id.report_name);
            TextView mobile=convertView.findViewById(R.id.report_mobile);
            TextView aadhar=convertView.findViewById(R.id.report_aadhar);
            Button status=convertView.findViewById(R.id.report_button);
            name.setText(reportModels.get(position).getName());
            mobile.setText(reportModels.get(position).getMobile());
            aadhar.setText(reportModels.get(position).getAadhar());
            if(reportModels.get(position).getStatus().equals("positive")){
                status.setText("Tested Positive");
                status.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            if(reportModels.get(position).getStatus().equals("negative")){
                status.setText("Tested Negative");
                status.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            return convertView;
        }
    }

    public void loadData(){
        DatabaseReference reference=databaseReference.child("report").child(GlobalVariables.USER_EMAIL);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ReportModel reportModel=dataSnapshot.getValue(ReportModel.class);
                reportModels.add(reportModel);
                CustomAdatpter customAdatpter=new CustomAdatpter();
                listView.setAdapter(customAdatpter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}