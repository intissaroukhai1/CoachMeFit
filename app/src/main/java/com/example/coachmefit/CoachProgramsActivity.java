package com.example.coachmefit;

import android.os.Bundle;
import android.widget.Toast;

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

/*
 * CoachProgramsActivity affiche tous les programmes ajoutés par le coach.
 * Les programmes sont récupérés depuis Firebase Realtime Database.
 */
public class CoachProgramsActivity extends AppCompatActivity {

    private RecyclerView recyclerCoachPrograms;
    private ProgramAdapter adapter;
    private ArrayList<Program> programList;

    private DatabaseReference programsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_programs);

        recyclerCoachPrograms = findViewById(R.id.recyclerCoachPrograms);

        programList = new ArrayList<>();
        adapter = new ProgramAdapter(this, programList);

        recyclerCoachPrograms.setLayoutManager(new LinearLayoutManager(this));
        recyclerCoachPrograms.setAdapter(adapter);

        programsRef = FirebaseDatabase.getInstance().getReference("programs");

        loadPrograms();
    }

    /*
     * Lire les programmes depuis Firebase.
     */
    private void loadPrograms() {
        programsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                programList.clear();

                for (DataSnapshot programSnapshot : snapshot.getChildren()) {
                    Program program = programSnapshot.getValue(Program.class);

                    if (program != null) {
                        programList.add(program);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        CoachProgramsActivity.this,
                        "Erreur Firebase : " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}