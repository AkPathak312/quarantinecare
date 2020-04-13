package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadImage extends AppCompatActivity {

    public static final int REQUEST_CODE = 101;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        img=findViewById(R.id.imgphoto);
    }

    public void uploadphoto(View view) {

        askforPermission();





    }

    private void askforPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);

        }else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,102);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==102){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length<0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else {
                Toast.makeText(UploadImage.this, "Please Provide Permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void finishuploading(View view) {
        if(img.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.profile).getConstantState()){
            Toast.makeText(UploadImage.this, "No Picture Clicked", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(UploadImage.this,ThanksActivity.class));

        }
    }
}
