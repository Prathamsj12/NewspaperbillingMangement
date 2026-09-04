package com.example.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtEmail;
    private EditText edtMobile;
    private EditText edtPassword;
    private EditText edtConfirmPassword;

    private AppCompatButton btnCreateAccount;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registartion);

        // -----------------------------------------
        // INITIALIZE VIEWS
        // -----------------------------------------

        edtName = findViewById(R.id.edtFullName);

        edtEmail = findViewById(R.id.edtEmail);

        edtMobile = findViewById(R.id.edtPhoneNumber);

        edtPassword = findViewById(R.id.edtPassword);


        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        txtLogin = findViewById(R.id.txtLogin);


        // -----------------------------------------
        // REGISTER BUTTON
        // -----------------------------------------

        btnCreateAccount.setOnClickListener(
                v -> registerUser()
        );


        // -----------------------------------------
        // GO TO LOGIN
        // -----------------------------------------

        txtLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegistrationActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            finish();
        });
    }


    // =================================================
    // REGISTER USER - VALIDATION ONLY
    // =================================================

    private void registerUser() {

        String name =
                edtName.getText()
                        .toString()
                        .trim();

        String email =
                edtEmail.getText()
                        .toString()
                        .trim();

        String mobile =
                edtMobile.getText()
                        .toString()
                        .trim();

        String password =
                edtPassword.getText()
                        .toString()
                        .trim();

        String confirmPassword =
                edtConfirmPassword.getText()
                        .toString()
                        .trim();


        // -----------------------------------------
        // NAME VALIDATION
        // -----------------------------------------

        if (TextUtils.isEmpty(name)) {

            edtName.setError("Enter name");
            edtName.requestFocus();

            return;
        }


        // -----------------------------------------
        // EMAIL VALIDATION
        // -----------------------------------------

        if (TextUtils.isEmpty(email)) {

            edtEmail.setError("Enter email");
            edtEmail.requestFocus();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            edtEmail.setError("Enter valid email");
            edtEmail.requestFocus();

            return;
        }


        // -----------------------------------------
        // MOBILE VALIDATION
        // -----------------------------------------

        if (TextUtils.isEmpty(mobile)) {

            edtMobile.setError("Enter mobile number");
            edtMobile.requestFocus();

            return;
        }

        if (mobile.length() != 10) {

            edtMobile.setError(
                    "Enter valid 10 digit mobile number"
            );

            edtMobile.requestFocus();

            return;
        }


        // -----------------------------------------
        // PASSWORD VALIDATION
        // -----------------------------------------

        if (TextUtils.isEmpty(password)) {

            edtPassword.setError("Enter password");
            edtPassword.requestFocus();

            return;
        }

        if (password.length() < 6) {

            edtPassword.setError(
                    "Password must contain at least 6 characters"
            );

            edtPassword.requestFocus();

            return;
        }


        // -----------------------------------------
        // CONFIRM PASSWORD VALIDATION
        // -----------------------------------------

        if (TextUtils.isEmpty(confirmPassword)) {

            edtConfirmPassword.setError(
                    "Confirm password"
            );

            edtConfirmPassword.requestFocus();

            return;
        }

        if (!password.equals(confirmPassword)) {

            edtConfirmPassword.setError(
                    "Passwords do not match"
            );

            edtConfirmPassword.requestFocus();

            return;
        }


        // -----------------------------------------
        // VALIDATION SUCCESS
        // -----------------------------------------

        Toast.makeText(
                RegistrationActivity.this,
                "Registration Successful",
                Toast.LENGTH_SHORT
        ).show();


        // -----------------------------------------
        // OPEN LOGIN
        // -----------------------------------------

        Intent intent = new Intent(
                RegistrationActivity.this,
                LoginActivity.class
        );

        startActivity(intent);

        finish();
    }
}
