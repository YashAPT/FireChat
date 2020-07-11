package com.yash.chatbox.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yash.chatbox.R;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class RegisterActivity extends AppCompatActivity {

    EditText regEmail, regPassword, regName, regConfPass;
    Button regLogin, regReg;
    ImageView regImg;
    ProgressBar regProgg;
    Toolbar regToolbar;

    FirebaseAuth auth;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;

    Task<Uri> download;

    static int REQUEST_CODE = 1;
    static int PReqcode = 1;
    Uri pickedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_pass);
        regLogin = findViewById(R.id.reg_login);
        regReg = findViewById(R.id.reg_reg);
        regImg = findViewById(R.id.reg_img);
        regName = findViewById(R.id.reg_name);
        regConfPass = findViewById(R.id.reg_confirm_pass);
        regProgg = findViewById(R.id.reg_progress);
        regToolbar = findViewById(R.id.toolbar_reg);

        setSupportActionBar(regToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        regProgg.setVisibility(View.INVISIBLE);

        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                //finish();
                CustomIntent.customType(RegisterActivity.this, "bottom-to-up");
            }
        });


        regReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String txtemail = regEmail.getText().toString();
                final String txtpassword = regPassword.getText().toString();
                final String txtpassword2 = regConfPass.getText().toString();
                final String txtusername = regName.getText().toString();

                if (txtemail.isEmpty() || txtusername.isEmpty() || txtpassword.isEmpty()
                        || !txtpassword.equals(txtpassword2)) {
                    Toast.makeText(RegisterActivity.this, "Verify All Fields", Toast.LENGTH_SHORT).show();
                    regProgg.setVisibility(View.INVISIBLE);
                } else {

                    regProgg.setVisibility(View.VISIBLE);
                    registerNow(txtusername, txtemail, txtpassword);
                    Toast.makeText(RegisterActivity.this, "register successful", Toast.LENGTH_SHORT).show();

                }


            }
        });



    }


    private void registerNow(final String username, final String email, final String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(userId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageUrl", "default");
                            hashMap.put("email", email);
                            hashMap.put("password", password);


                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        CustomIntent.customType(RegisterActivity.this, "fadein-to-fadeout");
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            pickedImgUri = data.getData();
            regImg.setImageURI(pickedImgUri);
        }
    }

}