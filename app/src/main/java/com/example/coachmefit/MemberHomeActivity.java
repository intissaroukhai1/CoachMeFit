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
 * MemberHomeActivity représente l'espace du membre.
 * Elle affiche les programmes sportifs depuis Firebase
 * dans un RecyclerView.
 */
public class MemberHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerPrograms;
    private ProgramAdapter adapter;
    private ArrayList<Program> programList;

    private DatabaseReference programsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);

        recyclerPrograms = findViewById(R.id.recyclerPrograms);

        programList = new ArrayList<>();

        adapter = new ProgramAdapter(this, programList);

        recyclerPrograms.setLayoutManager(new LinearLayoutManager(this));
        recyclerPrograms.setAdapter(adapter);

        // Référence vers le noeud "programs" dans Firebase
        programsRef = FirebaseDatabase.getInstance().getReference("programs");

        loadPrograms();
    }

    /*
     * Cette méthode récupère tous les programmes depuis Firebase
     * et les affiche dans le RecyclerView.
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
                Toast.makeText(MemberHomeActivity.this,
                        "Erreur Firebase : " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}