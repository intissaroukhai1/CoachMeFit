package com.example.coachmefit;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * MemberHomeActivity représente l'espace du membre.
 * Elle affiche les programmes depuis Firebase.
 * Elle écoute aussi les changements de statut des demandes
 * et affiche une notification Android si le coach accepte/refuse.
 */
public class MemberHomeActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "coachmefit_status_channel";
    private static final int NOTIFICATION_PERMISSION_CODE = 100;

    private RecyclerView recyclerPrograms;
    private ProgramAdapter adapter;
    private ArrayList<Program> programList;

    private ImageButton btnLogoutMember;
    private Button btnMyRequests;
    private Button btnNotifications;

    private DatabaseReference programsRef;
    private DatabaseReference requestsRef;

    private String memberEmail;

    /*
     * Cette liste évite d'afficher plusieurs fois la même notification.
     */
    private final Set<String> notifiedRequestIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);

        recyclerPrograms = findViewById(R.id.recyclerPrograms);
        btnLogoutMember = findViewById(R.id.btnLogoutMember);
        btnMyRequests = findViewById(R.id.btnMyRequests);

        /*
         * Si tu n'as pas encore ajouté le bouton Notifications dans XML,
         * commente ces 2 lignes.
         */
        // btnNotifications = findViewById(R.id.btnNotifications);

        programList = new ArrayList<>();
        adapter = new ProgramAdapter(this, programList, true);

        recyclerPrograms.setLayoutManager(new LinearLayoutManager(this));
        recyclerPrograms.setAdapter(adapter);

        memberEmail = getSharedPreferences("MemberSession", MODE_PRIVATE)
                .getString("email", "");

        programsRef = FirebaseDatabase.getInstance().getReference("programs");
        requestsRef = FirebaseDatabase.getInstance().getReference("requests");

        createNotificationChannel();
        askNotificationPermission();

        loadPrograms();
        listenToRequestStatusChanges();

        btnLogoutMember.setOnClickListener(v -> logout());

        btnMyRequests.setOnClickListener(v -> {
            Intent intent = new Intent(MemberHomeActivity.this, MyRequestsActivity.class);
            startActivity(intent);
        });

        /*
         * Si tu ajoutes une page Notifications, tu peux activer ça.
         */
        /*
        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(MemberHomeActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });
        */
    }

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
                        MemberHomeActivity.this,
                        "Erreur Firebase : " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    /*
     * Cette méthode écoute les demandes du membre connecté.
     * Si le statut devient Acceptée ou Refusée, on affiche une notification téléphone.
     */
    private void listenToRequestStatusChanges() {
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    GoalRequest request = requestSnapshot.getValue(GoalRequest.class);

                    if (request == null) {
                        continue;
                    }

                    if (request.getMemberEmail() == null || !request.getMemberEmail().equals(memberEmail)) {
                        continue;
                    }

                    String status = request.getStatus();

                    if ("Acceptée".equals(status) || "Refusée".equals(status)) {

                        String requestId = request.getId();

                        if (requestId != null && !notifiedRequestIds.contains(requestId)) {
                            notifiedRequestIds.add(requestId);

                            showStatusNotification(
                                    "Mise à jour de votre demande",
                                    "Votre demande pour \"" + request.getProgramTitle() + "\" est " + status
                            );
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        MemberHomeActivity.this,
                        "Erreur notifications : " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    /*
     * Créer le canal de notification pour Android 8+.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Statut des demandes";
            String description = "Notifications lorsque le coach accepte ou refuse une demande";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /*
     * Demander la permission de notification sur Android 13+.
     */
    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
    }

    /*
     * Afficher la notification sur le téléphone.
     */
    private void showStatusNotification(String title, String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logout)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void logout() {
        getSharedPreferences("MemberSession", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(MemberHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}