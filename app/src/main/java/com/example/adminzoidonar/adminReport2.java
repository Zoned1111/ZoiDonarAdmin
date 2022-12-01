package com.example.adminzoidonar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminReport2 extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView list;
    Button btnRefresh;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report2);

        btnBack = (ImageButton) findViewById(R.id.backButton);

        list = (RecyclerView) findViewById(R.id.list);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminReport2.this, adminReport.class));
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEventList();
            }
        });

    }//end of onCreate()

    private void displayEventList() {
        List<String> dateOfEvent = new ArrayList<>();
        MyAdapter adapter;
        list.setLayoutManager(new LinearLayoutManager(adminReport2.this));
        adapter = new MyAdapter(adminReport2.this, dateOfEvent);
        list.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String k;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    k = (String) snap.getKey();
                    dateOfEvent.add(k);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}//end of Activity