package com.example.adminzoidonar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context c;
    List<String> mdataDate;

    public MyAdapter(Context c, List<String> mdataDate) {
        this.c = c;
        this.mdataDate = mdataDate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.item, viewgroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        holder.itemEventDate.setText(mdataDate.get(i));

        ref.child("Events").child(mdataDate.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("eventTitle").getValue(String.class);
                holder.itemEventName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("Report").child(mdataDate.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = snapshot.child("TOTAL").getValue(Integer.class);
                holder.itemEventCount.setText(String.format("%d", count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mdataDate.size();
    }
}
