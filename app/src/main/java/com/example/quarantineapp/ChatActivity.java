package com.example.quarantineapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    MessageApapter messageApapter;
    RecyclerView recyclerView;
    Button send;
    EditText edtmessage;
    List<String> messageList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView=findViewById(R.id.studentchat_recycler_view);
        messageApapter=new MessageApapter(messageList,this);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(messageApapter);
        edtmessage=findViewById(R.id.student_chat_edt_text);
        send=findViewById(R.id.student_btn_chat_send);
        loadMessage();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    public void loadMessage(){
        messageList.add("Hey, this is Quarantine Care. Please enter the number as according to know the result: \n1. Covid Stats\n2. Avoiding Covid\n3. Covid Recent News");

        messageApapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageList.size()-1);

    }

    public void sendMessage(){
        if(edtmessage.getText().toString().equals("")){
            return;
        }
        if(edtmessage.getText().toString().trim().equals("1")){
           messageList.add("1");
            messageApapter.notifyDataSetChanged();
            loadCovidData();
            edtmessage.setText("");
        }
        else if(edtmessage.getText().toString().trim().equals("2")){
            messageList.add("Tips to Avoid Covid:\n" +
                    "1. When cooking and preparing food, limit the amount of salt and high-sodium condiments (e.g. soy sauce and fish sauce)." +
                    "\n2. Limit your daily salt intake to less than 5 g (approximately 1 teaspoon), and use iodized salt." +
                    "\n3. Avoid foods (e.g. snacks) that are high in salt and sugar." +
                    "\n4. Limit your intake of soft drinks or sodas and other drinks that are high in sugar (e.g. fruit juices, fruit juice concentrates and syrups, flavoured milks and yogurt drinks)." +
                    "5. Choose fresh fruits instead of sweet snacks such as cookies, cakes and chocolate.");
            messageApapter.notifyDataSetChanged();
            edtmessage.setText("");

        }else if(edtmessage.getText().toString().trim().equals("3")){
            messageList.add("3");
            messageApapter.notifyDataSetChanged();
            loadnews();
            edtmessage.setText("");
        }else if(edtmessage.getText().toString().trim().equals("more")){
            messageList.add("more");
            messageApapter.notifyDataSetChanged();
            loadnews();
            edtmessage.setText("");
        }
        else {
            Toast.makeText(this, "Invalid code, We will upgrade soon", Toast.LENGTH_SHORT).show();
            edtmessage.setText("");
            return;
        }

    }

    private void loadnews() {
        final int j=a+5;
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://newsapi.org/v2/everything?q=COVID&from=2020-10-21&sortBy=publishedAt&apiKey=900c0b1246024e8090447f2e297ecc33&pageSize=100&page=1")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        String str= null;
                        try {
                            str = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject jsonObject=new JSONObject(str);
                            JSONArray jsonArray=jsonObject.getJSONArray("articles");
                            for(int i=a;i<j;i++){
                                JSONObject jo=jsonArray.getJSONObject(i);
                                messageList.add(jo.getString("title")+"\n Link: "+jo.getString("url") );
                                messageApapter.notifyDataSetChanged();
                            }
                            a=a+5;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void loadCovidData(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://corona.lmao.ninja/v2/countries/india?yesterday=true&strict=true&query")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(ChatActivity.this, "Error connecting to API", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String str=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });

                try {
                    JSONObject jsonObject=new JSONObject(str);
                    int totalcases=jsonObject.getInt("cases");
                    Log.d("CASES",totalcases+"");
                    int todaycase=jsonObject.getInt("todayCases");
                    int recovered2=jsonObject.getInt("todayRecovered");
                    int recovered=jsonObject.getInt("recovered");
                    int death=jsonObject.getInt("todayDeaths");
                    messageList.add("As of now there are total of "+totalcases+" of which "+recovered+" are recovered.\nToday Cases: "+todaycase+
                            "\nToday recovered: "+recovered2+
                            "\nToday Deaths:"+ death);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageApapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}