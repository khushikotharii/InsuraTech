package com.example.insuratech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button btn1;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView tv2;

    @Override
    public void onStart() {
        super.onStart();
        // if user logged in already, homepage is opened.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent welcome = new Intent(getApplicationContext(), Register.class);
            startActivity(welcome);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv2 = findViewById(R.id.tv2);
        mAuth = FirebaseAuth.getInstance();
        ed1 = findViewById(R.id.email);
        ed2= findViewById(R.id.password);
        btn1 = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar);

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(MainActivity.this, Register.class);
                startActivity(reg);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = ed1.getText().toString().trim();
                String password = ed2.getText().toString().trim();

                // Define regex patterns for email and password validation
                String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

                // Create Pattern objects for validation
                Pattern emailMatcher = Pattern.compile(emailPattern);
                Pattern passwordMatcher = Pattern.compile(passwordPattern);

                // Reset errors on EditText fields
                ed1.setError(null);
                ed2.setError(null);

                if (TextUtils.isEmpty(email)) {
                    ed1.setError("Enter Email");
                    progressBar.setVisibility(View.GONE);
                    return;
                } else if (!emailMatcher.matcher(email).matches()) {
                    ed1.setError("Invalid Email Format");
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ed2.setError("Enter Password");
                    progressBar.setVisibility(View.GONE);
                    return;
                } else if (!passwordMatcher.matcher(password).matches()) {
                    ed2.setError("Password must contain at least 8 characters, including at least one uppercase letter, one lowercase letter, and one digit");
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    Intent welcome = new Intent(MainActivity.this, homepage.class);
                                    startActivity(welcome);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}