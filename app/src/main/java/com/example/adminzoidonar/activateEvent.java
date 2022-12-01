
package com.example.adminzoidonar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activateEvent extends Fragment {

    SwitchCompat eventStatusChecker;
    TextView hiddenText;

    DatabaseReference aSwitch = FirebaseDatabase.getInstance().getReference().child("Switch");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activate_event, container, false);

        eventStatusChecker = view.findViewById(R.id.eventStatusChecker);
        hiddenText = view.findViewById(R.id.hiddenText);


        eventStatus();

        return view;
    }

    public void eventStatus() {

        Query stat = aSwitch.child("eventStatus");

        stat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);

                if (status.equals("true")) {
                    eventStatusChecker.setChecked(true);
                    hiddenText.setText("live");
                    hiddenText.setTextColor(Color.GREEN);
                } else {
                    eventStatusChecker.setChecked(false);
                    hiddenText.setText("offline");
                    hiddenText.setTextColor(Color.RED);
                }
                eventStatusChecker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!eventStatusChecker.isChecked()) {

                            aSwitch.child("eventStatus").setValue("false");

                            hiddenText.setText("offline");
                            hiddenText.setTextColor(Color.RED);

                            Toast.makeText(getActivity(), "EVENT FINISHED!", Toast.LENGTH_SHORT).show();


                        } else {
                            aSwitch.child("eventStatus").setValue("true");

                            hiddenText.setText("live");
                            hiddenText.setTextColor(Color.GREEN);

                            Toast.makeText(getActivity(), "EVENT IS NOW LIVE!", Toast.LENGTH_SHORT).show();


                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}