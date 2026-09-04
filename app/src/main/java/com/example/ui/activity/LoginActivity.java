package com.example.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                        this,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // OWNER LOGIN
//            if (email.equalsIgnoreCase("admin")
//                    && password.equals("admin")) {
//
//                Toast.makeText(
//                        this,
//                        "Welcome Owner",
//                        Toast.LENGTH_SHORT
//                ).show();
//
//                Intent intent = new Intent(
//                        LoginActivity.this,
//                        MainActivity.class
//                );
//
//                // Send user role
//                intent.putExtra("USER_ROLE", "OWNER");
//
//                startActivity(intent);
//                finish();
//
//            }
            else if (email.equalsIgnoreCase("admin")
                    && password.equals("admin")) {

                Intent intent = new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );

                intent.putExtra("USER_ROLE", "OWNER");

                startActivity(intent);
                finish();
            }

            // DELIVERY PERSON LOGIN
            else if (email.equalsIgnoreCase("delivery")
                    && password.equals("delivery")) {

                Intent intent = new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );

                intent.putExtra("USER_ROLE", "DELIVERY_PERSON");

                startActivity(intent);
                finish();
            }

            // INVALID LOGIN
            else {

                Toast.makeText(
                        this,
                        "Invalid email or password",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Password reset instructions sent to registered admin email", Toast.LENGTH_LONG).show());

        tvRegister.setOnClickListener(v ->
                Toast.makeText(this, "Please contact administrator for staff registration", Toast.LENGTH_LONG).show());
//        Intent intent = new Intent(LoginActivity.this, RegistartionActivity.class);
//        startActivity(intent);
//        finish();

    }
}
