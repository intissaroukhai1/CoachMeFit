package com.example.coachmefit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
 * CoachHomeActivity représente l'espace du coach.
 * Cette activité utilise une ListView pour afficher un menu simple.
 * Chaque élément du menu permet au coach d'accéder à une fonctionnalité.
 */
public class CoachHomeActivity extends AppCompatActivity {

    // Déclaration de la ListView
    private ListView listCoachMenu;

    // Tableau contenant les éléments du menu coach
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

        // Liaison entre la variable Java et la ListView XML
        listCoachMenu = findViewById(R.id.listCoachMenu);

        /*
         * ArrayAdapter permet de transformer le tableau coachMenuItems
         * en éléments visibles dans la ListView.
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                coachMenuItems
        );

        // Affectation de l'adapter à la ListView
        listCoachMenu.setAdapter(adapter);

        /*
         * Gestion du clic sur un élément de la liste.
         * position = index de l'élément cliqué.
         */
        listCoachMenu.setOnItemClickListener((parent, view, position, id) -> {

            if (position == 0) {
                // Ajouter un programme
                Intent intent = new Intent(CoachHomeActivity.this, AddProgramActivity.class);
                startActivity(intent);

            } else if (position == 1) {
                // Liste des programmes
                Intent intent = new Intent(CoachHomeActivity.this, CoachProgramsActivity.class);
                startActivity(intent);

            } else if (position == 2) {
                // Demandes des membres
                Intent intent = new Intent(CoachHomeActivity.this, RequestsActivity.class);
                startActivity(intent);

            } else if (position == 3) {
                // Profil coach
                Intent intent = new Intent(CoachHomeActivity.this, CoachProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}