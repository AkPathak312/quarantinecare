package com.example.quarantineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText email,password;
    private Button signup;
    Window window;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup=findViewById(R.id.buttonSignUp);
        email=findViewById(R.id.editTextEmail);
        password=findViewById(R.id.editTextPassword);
        auth=FirebaseAuth.getInstance();
        //mAuth2=FirebaseAuth.getInstance();
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        progressDialog=new ProgressDialog(SignUp.this);




    }

    public void gotoRegistration(View view) {
        final String mail,pass;
        mail=email.getText().toString();
        pass=password.getText().toString();

        if(TextUtils.isEmpty(mail.trim()) ||TextUtils.isEmpty(pass.trim())){
            Toast.makeText(SignUp.this, "One or more fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }else if(pass.length()<6){
            Toast.makeText(SignUp.this, "Password should be more than 6 digits!", Toast.LENGTH_SHORT).show();
        }

        else {

            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            auth.createUserWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                            }
                            else {

                                Toast.makeText(SignUp.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        }
                    });

            auth.signInWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.cancel();
                                String gotuid=auth.getCurrentUser().getUid();
                                Toast.makeText(SignUp.this,"Logged In", Toast.LENGTH_SHORT).show();

                                Intent i=new Intent(SignUp.this,Register.class);
                                // i.putExtra("UID",Uid);
                                i.putExtra("EMAIL",mail);
                                i.putExtra("UID",gotuid);
                                startActivity(i);
                                finish();

                            }
                            else {
                                progressDialog.cancel();
                                Toast.makeText(SignUp.this, "Login Failed!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("ERROR",e.getLocalizedMessage());
                }
            });




        }
    }
}
