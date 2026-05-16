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
 * Cette activité permet au membre de consulter ses demandes
 * et de voir si le coach les a acceptées ou refusées.
 */
public class MyRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerMyRequests;
    private RequestAdapter adapter;
    private ArrayList<GoalRequest> requestList;

    private DatabaseReference requestsRef;
    private String memberEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        recyclerMyRequests = findViewById(R.id.recyclerMyRequests);

        requestList = new ArrayList<>();

        // false = ne pas afficher les boutons Accepter / Refuser côté membre
        adapter = new RequestAdapter(this, requestList, false);

        recyclerMyRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyRequests.setAdapter(adapter);

        memberEmail = getSharedPreferences("MemberSession", MODE_PRIVATE)
                .getString("email", "");

        requestsRef = FirebaseDatabase.getInstance().getReference("requests");

        loadMyRequests();
    }

    private void loadMyRequests() {
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                requestList.clear();

                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    GoalRequest request = requestSnapshot.getValue(GoalRequest.class);

                    if (request != null
                            && request.getMemberEmail() != null
                            && request.getMemberEmail().equals(memberEmail)) {

                        requestList.add(request);
                    }
                }

                adapter.notifyDataSetChanged();

                if (requestList.isEmpty()) {
                    Toast.makeText(MyRequestsActivity.this,
                            "Aucune demande trouvée",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyRequestsActivity.this,
                        "Erreur Firebase : " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}