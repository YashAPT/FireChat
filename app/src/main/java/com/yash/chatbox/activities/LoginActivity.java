package com.yash.chatbox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yash.chatbox.R;

import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button loginLogin, loginReg;
    CircleImageView loginImg;
    ProgressBar loginProgg;
    FirebaseAuth mAuth;
    Toolbar loginToolbar;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginLogin = findViewById(R.id.login_login);
        loginReg = findViewById(R.id.login_reg);
        loginImg = findViewById(R.id.image_login);
        loginProgg = findViewById(R.id.login_progg);
        loginToolbar = findViewById(R.id.toolbar_login);

        setSupportActionBar(loginToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginProgg.setVisibility(View.INVISIBLE);
        loginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
            }
        });

        loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Verify all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginProgg.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent homeActivity = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(homeActivity);
                                CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            CustomIntent.customType(LoginActivity.this, "fadein-to-fadeout");
            finish();
        }
    }
}