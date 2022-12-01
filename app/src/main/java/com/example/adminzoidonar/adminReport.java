package com.example.adminzoidonar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adminReport extends AppCompatActivity {

    ImageButton btnHome;
    Button btnNext;
    Spinner eventPickSpinner;
    TextView btnGo, displayTitle, sample;

    TextView totalDonorCount, totalStudents, totalEmployees, totalGuests;

    PieChart pieChart;
    TextView btAbp, btAbn, btOp, btOn, btBp, btBn, btAp, btAn;
    int btAbpTotal = 0, btAbnTotal = 0, btOpTotal = 0, btOnTotal = 0,btBpTotal = 0, btBnTotal = 0, btApTotal = 0, btAnTotal = 0;
    ArrayList<PieEntry> bloodList = new ArrayList<>();

    DatabaseReference eventsFromDB = FirebaseDatabase.getInstance().getReference().child("Events");
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        btnHome = (ImageButton) findViewById(R.id.backButton);
        btnNext = (Button) findViewById(R.id.btnNext);
        eventPickSpinner = (Spinner) findViewById(R.id.eventPickSpinner);
        btnGo = (TextView) findViewById(R.id.btnGo);
        displayTitle = (TextView) findViewById(R.id.displayTitle);
        sample = (TextView) findViewById(R.id.sample);

        totalDonorCount = (TextView) findViewById(R.id.totalDonorCount);
        totalStudents = (TextView) findViewById(R.id.totalStudents);
        totalEmployees = (TextView) findViewById(R.id.totalEmployees);
        totalGuests = (TextView) findViewById(R.id.totalGuests);

        pieChart = (PieChart) findViewById(R.id.pieChart);
        btAbp = (TextView) findViewById(R.id.btAbp);
        btAbn = (TextView) findViewById(R.id.btAbn);
        btOp = (TextView) findViewById(R.id.btOp);
        btOn = (TextView) findViewById(R.id.btOn);
        btBp = (TextView) findViewById(R.id.btBp);
        btBn = (TextView) findViewById(R.id.btBn);
        btAp = (TextView) findViewById(R.id.btAp);
        btAn = (TextView) findViewById(R.id.btAn);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminReport.this, MainActivity.class));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCounter();
                startActivity(new Intent(adminReport.this, adminReport2.class));
            }
        });

        displaySpinnerContent();
        showEventListSpinner();
    }

    public void myCounter() {
        DatabaseReference donorCountByCatReport = FirebaseDatabase.getInstance().getReference().child("Report");

        for (String k : spinnerList) {
            eventsFromDB.child(k).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int students = (int) snapshot.child("STUDENT").getChildrenCount();
                    int employees = (int) snapshot.child("EMPLOYEES").getChildrenCount();
                    int guests = (int) snapshot.child("GUEST").getChildrenCount();

                    donorCountByCatReport.child(k).child("studentCount").setValue(students);
                    donorCountByCatReport.child(k).child("employeeCount").setValue(employees);
                    donorCountByCatReport.child(k).child("guestCount").setValue(guests);

                    int sum = students + employees + guests;
                    donorCountByCatReport.child(k).child("TOTAL").setValue(sum);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            donorCountByCatReport.child(k).child("Qualified").child("BloodType").
                    addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int Abp = (int) snapshot.child("AB+").getChildrenCount();
                    int Abn = (int) snapshot.child("AB-").getChildrenCount();
                    int Op = (int) snapshot.child("O+").getChildrenCount();
                    int On = (int) snapshot.child("O-").getChildrenCount();
                    int Bp = (int) snapshot.child("B+").getChildrenCount();
                    int Bn = (int) snapshot.child("B-").getChildrenCount();
                    int Ap = (int) snapshot.child("A+").getChildrenCount();
                    int An = (int) snapshot.child("A-").getChildrenCount();

                    DatabaseReference newRef = donorCountByCatReport.child(k).child("eachBloodTypeTotalCount");
                    newRef.child("abPositive").setValue(Abp);
                    newRef.child("abNegative").setValue(Abn);
                    newRef.child("oPositive").setValue(Op);
                    newRef.child("oNegative").setValue(On);
                    newRef.child("bPositive").setValue(Bp);
                    newRef.child("bNegative").setValue(Bn);
                    newRef.child("aPositive").setValue(Ap);
                    newRef.child("aNegative").setValue(An);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public void showEventListSpinner() {
        adapter = new ArrayAdapter<String>(adminReport.this, android.R.layout.simple_dropdown_item_1line, spinnerList);

        Query q = eventsFromDB;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    String x = (String) item.getKey();
                    spinnerList.add(x);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        eventPickSpinner.setAdapter(adapter);
    }

    public void displaySpinnerContent() {

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = eventPickSpinner.getSelectedItem().toString();
                DatabaseReference events = eventsFromDB.child(selectedItem);

                displayDataOfSelectedEvent(selectedItem);

                events.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.exists()) {
                            Toast.makeText(adminReport.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            String eventTitle = snapshot.child("eventTitle").getValue(String.class);
                            displayTitle.setText(eventTitle);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });//eventsFromDB
            }
        });//btnGo
    }

    public void displayDataOfSelectedEvent(String basedEvent) {
        DatabaseReference donorCountByCat = FirebaseDatabase.getInstance().getReference().child("Events");
        DatabaseReference donorCountByBT = FirebaseDatabase.getInstance().getReference().child("Report");

        donorCountByCat.child(basedEvent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int students = (int) snapshot.child("STUDENT").getChildrenCount();
                totalStudents.setText(students + "");

                int employees = (int) snapshot.child("EMPLOYEES").getChildrenCount();
                totalEmployees.setText(employees + "");

                int guests = (int) snapshot.child("GUEST").getChildrenCount();
                totalGuests.setText(guests + "");

                int sum = students + employees + guests;
                totalDonorCount.setText(sum + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        donorCountByBT.child(basedEvent).child("Qualified").child("BloodType")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int Abp = (int) snapshot.child("AB+").getChildrenCount();
                int Abn = (int) snapshot.child("AB-").getChildrenCount();
                int Op = (int) snapshot.child("O+").getChildrenCount();
                int On = (int) snapshot.child("O-").getChildrenCount();
                int Bp = (int) snapshot.child("B+").getChildrenCount();
                int Bn = (int) snapshot.child("B-").getChildrenCount();
                int Ap = (int) snapshot.child("A+").getChildrenCount();
                int An = (int) snapshot.child("A-").getChildrenCount();

                addDataPie(Abp, "Ab+"); addDataPie(Abn, "Ab-");
                addDataPie(Op, "O+"); addDataPie(On, "O-");
                addDataPie(Bp, "B+"); addDataPie(Bn, "B-");
                addDataPie(Ap, "A+"); addDataPie(An, "A-");

                PieDataSet pieDataSet = new PieDataSet(bloodList, "Blood Types");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);

                pieChart.setData(pieData);
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText("Blood Types");
                pieChart.animate();

                Legend l = pieChart.getLegend();
                l.setEnabled(false);

                btAbp.setText(Abp+""); btAbn.setText(Abn+"");
                btOp.setText(Op+""); btOn.setText(On+"");
                btBp.setText(Bp+""); btBn.setText(Bn+"");
                btAp.setText(Ap+""); btAn.setText(An+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDataPie(int x, String y) {
        bloodList.add(new PieEntry(x, y));
    }


}