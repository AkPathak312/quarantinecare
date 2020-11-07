package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quarantineapp.DataModels.UploadImageModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class ThanksActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    List<UploadImageModel> uploadImageModels=new ArrayList<>();
    Context context=this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        listView=findViewById(R.id.upload_image_list);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        loadImages();
    }

    public void goback(View view) {
        startActivity(new Intent(ThanksActivity.this,MainActivity.class));
        finish();
    }

    public void loadImages(){
        DatabaseReference reference=databaseReference.child("image_uploads").child(GlobalVariables.USER_EMAIL);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UploadImageModel uploadImageModel=dataSnapshot.getValue(UploadImageModel.class);
                uploadImageModels.add(uploadImageModel);
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


    class CustomAdatpter extends BaseAdapter {

        @Override
        public int getCount() {
            return uploadImageModels.size();
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
            convertView=getLayoutInflater().inflate(R.layout.single_image_upload,null);
            TextView uploaddate=convertView.findViewById(R.id.uploaded_on);
            TextView location=convertView.findViewById(R.id.upload_location);
            ImageViewZoom imageViewZoom=convertView.findViewById(R.id.upload_picture);
            uploaddate.setText(uploadImageModels.get(position).getDate());
            location.setText(uploadImageModels.get(position).getLocation());
            Picasso.with(context).load(uploadImageModels.get(position).getImage_link()).into(imageViewZoom);
            return convertView;
        }
    }
}
