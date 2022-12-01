package com.example.adminzoidonar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminAddEvent extends Fragment {

    TextInputLayout tilEventDate, tilEventTitle;
    TextInputEditText eventTitle, eventDate;
    Button btnSave;

    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://zoidonar-7f9e5-default-rtdb.firebaseio.com/");
    DatabaseReference aSwitch = FirebaseDatabase.getInstance().getReference().child("Switch");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_add_event, container, false);

        tilEventDate = (TextInputLayout) view.findViewById(R.id.tilEventDate);
        tilEventTitle = (TextInputLayout) view.findViewById(R.id.tilEventTitle);
        eventDate = (TextInputEditText) view.findViewById(R.id.eventDate);
        eventTitle = (TextInputEditText) view.findViewById(R.id.eventTitle);
        btnSave = (Button) view.findViewById(R.id.btnSave);


        Query stat = aSwitch.child("eventStatus");
        stat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);

                if (status.equals("true")) {
                    eventTitle.setEnabled(false);
                    eventDate.setEnabled(false);
                } else {
                    eventTitle.setEnabled(true);
                    eventDate.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String eventtitle = eventTitle.getText().toString().toUpperCase().trim();
                final String eventdate = eventDate.getText().toString().trim();


                if (eventtitle.isEmpty() || eventdate.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("Events").child(eventdate).child("eventTitle").setValue(eventtitle);
                    db.child("Events").child(eventdate).child("eventDate").setValue(eventdate);

                    String dbDateEvent = eventDate.getText().toString().trim();
                    aSwitch.child("eventStatusBaseDate").setValue(dbDateEvent);

                    Toast.makeText(getActivity(), "Event Saved.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }
}