package com.example.adminzoidonar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminSms extends Fragment {

    TextInputLayout tilEventDate, tilEventTitle, tilEventTime, tilEventAddress, tilInstruction;
    TextInputEditText eventTitle, eventDate, eventTime, eventAddress, eventInstruction;
    Button btnSend;


    DatabaseReference phoneNumbersFromDB = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_sms, container, false);


        tilEventDate = view.findViewById(R.id.tilEventDate);
        tilEventTitle = view.findViewById(R.id.tilEventTitle);
        tilEventTime = view.findViewById(R.id.tilEventTime);
        tilEventAddress = view.findViewById(R.id.tilEventAddress);
        tilInstruction = view.findViewById(R.id.tilInstruction);
        eventDate = view.findViewById(R.id.eventDate);
        eventTitle = view.findViewById(R.id.eventTitle);
        eventTime = view.findViewById(R.id.eventTime);
        eventAddress = view.findViewById(R.id.eventAddress);
        eventInstruction = view.findViewById(R.id.eventInstruction);
        btnSend = view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {

                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });
        return view;
    }

    public void sendMessage() {
        String eventtitle = eventTitle.getText().toString().trim();
        String eventdate = eventDate.getText().toString().trim();
        String eventtime = eventTime.getText().toString().toUpperCase().trim();
        String eventaddress = eventAddress.getText().toString().trim();
        String eventinstruction = eventInstruction.getText().toString().trim();
        if (eventtitle.isEmpty() || eventdate.isEmpty() || eventtime.isEmpty() || eventaddress.isEmpty() || eventinstruction.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
        } else {
            phoneNumbersFromDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot phoneN : snapshot.getChildren()) {
                        String p = "0" + phoneN.child("mobile").getValue(String.class);

                        try {
                            SmsManager smsM = SmsManager.getDefault();
                            smsM.sendTextMessage(p,
                                    null,
                                    "Event Title : " + eventtitle.toUpperCase() + "\n" +
                                            "Date : " + eventdate.toUpperCase() + "\n" +
                                            "Time : " + eventtime + "\n" +
                                            "Venue : " + eventaddress.toUpperCase() + "\n\n" +
                                            "Instructions\n" + eventinstruction,
                                    null,
                                    null);


                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(getActivity(), "SMS Successful.\nSending may take a minute", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }


}