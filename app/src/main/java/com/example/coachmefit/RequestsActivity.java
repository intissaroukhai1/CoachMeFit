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
 * RequestsActivity affiche les objectifs envoyés par les membres.
 * Les données sont lues depuis Firebase dans le noeud "requests".
 */
public class RequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerRequests;
    private RequestAdapter adapter;
    private ArrayList<GoalRequest> requestList;

    private DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        recyclerRequests = findViewById(R.id.recyclerRequests);

        requestList = new ArrayList<>();
        adapter = new RequestAdapter(this, requestList);

        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerRequests.setAdapter(adapter);

        requestsRef = FirebaseDatabase.getInstance().getReference("requests");

        loadRequests();
    }

    /*
     * Lire les demandes depuis Firebase et les afficher dans RecyclerView.
     */
    private void loadRequests() {
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();

                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    GoalRequest request = requestSnapshot.getValue(GoalRequest.class);

                    if (request != null) {
                        requestList.add(request);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        RequestsActivity.this,
                        "Erreur Firebase : " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}