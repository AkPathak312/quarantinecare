package com.example.quarantineapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadReports extends AppCompatActivity {

    DatabaseReference databaseReference;
    String file_path;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_reports);
        editText=findViewById(R.id.manage_edt_name);
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    public void uploadreport(View view) {
        if(editText.getText().toString().equals("")){
            Toast.makeText(this, "Empty Name", Toast.LENGTH_SHORT).show();
            return;
        }
        filePicker();
    }
    private  void filePicker(){
        Intent opengallery=new Intent(Intent.ACTION_PICK);
        opengallery.setType("image/*");
        startActivityForResult(opengallery,102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==102&&resultCode== Activity.RESULT_OK){
            String filepath=getPath(data.getData(),this);
            Log.d("FILE PATH",filepath);
            file_path=filepath;
            uploadChatImage(file_path);
        }
    }
    public String getPath(Uri uri, Activity activity){
        Cursor cursor=activity.getContentResolver().query(uri,null,null,null,null);
        if(cursor==null){
            return uri.getPath();
        }
        else {
            cursor.moveToFirst();
            int id=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }

    public void uploadChatImage(String path){
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",new File(path).getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(path)))
                .build();
        Request request = new Request.Builder()
                .url("http://api.indiamills.thecodebucket.com/token/chat_image")
                .method("POST", body)
                .addHeader("x-access-token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtb2JpbGUiOiI3NzYzMDkwNDc4IiwiaWF0IjoxNTkzNTg5NTAwfQ.poEdH_XxmchWYyaY04DK-wZJ6mk_A-s6t7fgHCvKRT0")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Log.e("TAG",e.getLocalizedMessage());
                        Toast.makeText(UploadReports.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String str=response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(str);
                            Log.e("Success",jsonObject.getString("image_url"));
                            // sendimageMessage(jsonObject.getString("image_url"));
                            Date date=new Date();
                            DateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Map map=new HashMap();
                            map.put("date",df.format(date));
                            map.put("name",editText.getText().toString());
                            map.put("image_link","http://api.indiamills.thecodebucket.com/"+jsonObject.getString("image_url").substring(2));
                            databaseReference.child("report_uploads").child(GlobalVariables.USER_EMAIL).child(date.getTime()+"").updateChildren(map);
                            Toast.makeText(UploadReports.this, "Done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UploadReports.this,MyReports.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}