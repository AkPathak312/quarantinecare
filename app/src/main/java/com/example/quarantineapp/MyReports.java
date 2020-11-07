package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quarantineapp.DataModels.ManageReportModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class MyReports extends AppCompatActivity {

    List<ManageReportModel> manageReportModels=new ArrayList<>();
    ListView listView;
    Context context=this;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        listView=findViewById(R.id.myreport_listview);
        load();

    }
    class CustomAdatpter extends BaseAdapter {

        @Override
        public int getCount() {
            return manageReportModels.size();
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
            convertView=getLayoutInflater().inflate(R.layout.single_manage_report,null);
            TextView uploaddate=convertView.findViewById(R.id.manage_report_time);
            TextView name=convertView.findViewById(R.id.manage_report_name);
            ImageViewZoom imageViewZoom=convertView.findViewById(R.id.manage_report_image);
            uploaddate.setText("Uploaded On: "+manageReportModels.get(position).getDate());
            name.setText("Report Name: "+manageReportModels.get(position).getName());
            Picasso.with(context).load(manageReportModels.get(position).getImage_link()).into(imageViewZoom);
            return convertView;
        }
    }

    public void load(){
        DatabaseReference reference=databaseReference.child("report_uploads").child(GlobalVariables.USER_EMAIL);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ManageReportModel manageReportModel=dataSnapshot.getValue(ManageReportModel.class);
                manageReportModels.add(manageReportModel);
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