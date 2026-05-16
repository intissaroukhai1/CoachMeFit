package com.example.coachmefit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

/*
 * CoachHomeActivity représente l'espace du coach.
 * Elle utilise une ListView pour afficher les actions disponibles.
 * Elle contient aussi un bouton de déconnexion.
 */
public class CoachHomeActivity extends AppCompatActivity {

    private ListView listCoachMenu;
    private Button btnLogout;

    private String[] coachMenuItems = {
            "Ajouter un programme",
            "Liste des programmes",
            "Demandes des membres",
            "Profil coach"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_home);

        listCoachMenu = findViewById(R.id.listCoachMenu);
        btnLogout = findViewById(R.id.btnLogout);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_coach_menu,
                coachMenuItems
        );

        listCoachMenu.setAdapter(adapter);

        listCoachMenu.setOnItemClickListener((parent, view, position, id) -> {

            if (position == 0) {
                startActivity(new Intent(CoachHomeActivity.this, AddProgramActivity.class));

            } else if (position == 1) {
                startActivity(new Intent(CoachHomeActivity.this, CoachProgramsActivity.class));

            } else if (position == 2) {
                startActivity(new Intent(CoachHomeActivity.this, RequestsActivity.class));

            } else if (position == 3) {
                startActivity(new Intent(CoachHomeActivity.this, CoachProfileActivity.class));
            }
        });

        btnLogout.setOnClickListener(v -> logout());
    }

    /*
     * Déconnexion du coach.
     * On supprime les informations de session et on retourne vers LoginActivity.
     */
    private void logout() {
        getSharedPreferences("CoachSession", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(CoachHomeActivity.this, LoginActivity.class);

        // Supprime les anciennes activités de la pile
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }
}