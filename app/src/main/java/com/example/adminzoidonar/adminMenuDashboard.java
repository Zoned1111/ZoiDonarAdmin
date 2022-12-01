package com.example.adminzoidonar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminMenuDashboard extends AppCompatActivity {

    BottomNavigationView navMenu;
    adminAddEvent aeFragment = new adminAddEvent();
    adminSms smsFragment = new adminSms();
    adminLogin adminLogin = new adminLogin();
    activateEvent aesFragment = new activateEvent();
    TextView adminIntro, btnLO;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Switch");
    Query stat = ref.child("eventStatus");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_dashboard);
        displayIntro();

        adminIntro = (TextView) findViewById(R.id.adminIntro);
        navMenu = (BottomNavigationView) findViewById(R.id.navMenu);

        btnLO = (TextView) findViewById(R.id.btnLO);
        btnLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminMenuDashboard.this, adminLogin.class));
                adminLogin.aPassword.setText("");
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, aesFragment).commit();
        startNav();
    }

    public void startNav(){
        navMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navEventStatus:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, aesFragment).commit();
                        return true;
                    case R.id.navSms:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, smsFragment).commit();
                        return true;
                    case R.id.navAddEvent:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, aeFragment).commit();
                        return true;
                    case R.id.navReport:
                        startActivity(new Intent(adminMenuDashboard.this, adminReport.class));
                        return true;
                    case R.id.navDatabase:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://console.firebase.google.com/u/3/project/zoidonar-7f9e5/database/zoidonar-7f9e5-default-rtdb/data")));
                        return true;
                }
                return false;
            }
        });
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, 
                                           @NonNull String[] permissions, 
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0]
                == PackageManager.PERMISSION_GRANTED) {
            smsFragment.sendMessage();
        }else{
            Toast.makeText(adminMenuDashboard.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayIntro(){

        stat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String status = snapshot.getValue(String.class);

                if(!status.equals("true")){
                    adminIntro.setTextColor(Color.RED);
                }else{
                    adminIntro.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    
}