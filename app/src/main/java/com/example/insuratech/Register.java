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

public class Register extends AppCompatActivity {

    EditText ed3, ed4;
    Button btn2;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        tv3 = findViewById(R.id.tv3);
        ed3 = findViewById(R.id.r_email);
        ed4 = findViewById(R.id.r_password);
        btn2 = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.progressBar);

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(Register.this, MainActivity.class);
                startActivity(log);
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed3.getText().toString().trim();
                String password = ed4.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    ed3.setError("Enter Email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ed4.setError("Enter Password");
                    return;
                }

                // Define regex patterns for email and password validation
                String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

                // Create Pattern objects for validation
                Pattern emailMatcher = Pattern.compile(emailPattern);
                Pattern passwordMatcher = Pattern.compile(passwordPattern);

                // Check email and password format
                if (!emailMatcher.matcher(email).matches()) {
                    ed3.setError("Invalid Email Format");
                    return;
                }

                if (!passwordMatcher.matcher(password).matches()) {
                    ed4.setError("Password must contain at least 8 characters, including at least one uppercase letter, one lowercase letter, and one digit");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
