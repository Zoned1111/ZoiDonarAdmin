package com.example.adminzoidonar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminLogin extends AppCompatActivity {

    TextInputLayout tilUsername, tilPassword;
    TextInputEditText aUser, aPassword;
    Button btnLogin;

    DatabaseReference dbAccount = FirebaseDatabase.getInstance().getReference().child("adminAccount");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        tilUsername = (TextInputLayout) findViewById(R.id.tilUsername);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        aUser = (TextInputEditText) findViewById(R.id.adminUsername);
        aPassword = (TextInputEditText) findViewById(R.id.adminPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputEmail = aUser.getText().toString().trim();
                final String inputPass = aPassword.getText().toString().trim();

                if (inputEmail.isEmpty() || inputPass.isEmpty()) {
                    Toast.makeText(adminLogin.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        isAdmin();
                    } catch (Exception e) {
                        Toast.makeText(adminLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void isAdmin() {
        try {
            final String enteredUser = aUser.getText().toString().trim();
            final String enteredPass = aPassword.getText().toString().trim();

            Query checkAdmin = dbAccount.orderByChild("username").equalTo(enteredUser);

            checkAdmin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        aUser.setError("Cannot find admin");
                    } else {
                        String passwordFromDB = snapshot.child("admin").child("password").getValue(String.class);

                        if (!enteredPass.equals(passwordFromDB)) {
                            aPassword.setError("Password incorrect.");
                        } else {
                            Intent intent = new Intent(adminLogin.this, adminMenuDashboard.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(adminLogin.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}