package com.example.coachmefit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 * AddProgramActivity permet au coach d'ajouter un programme sportif.
 * Les données saisies sont enregistrées dans Firebase Realtime Database.
 */
public class AddProgramActivity extends AppCompatActivity {

    private EditText editTitle, editLevel, editObjective, editDuration;
    private EditText editPrice, editLocation, editDescription;
    private Button btnSaveProgram;

    private DatabaseReference programsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);

        editTitle = findViewById(R.id.editTitle);
        editLevel = findViewById(R.id.editLevel);
        editObjective = findViewById(R.id.editObjective);
        editDuration = findViewById(R.id.editDuration);
        editPrice = findViewById(R.id.editPrice);
        editLocation = findViewById(R.id.editLocation);
        editDescription = findViewById(R.id.editDescription);
        btnSaveProgram = findViewById(R.id.btnSaveProgram);

        // Référence vers le noeud "programs" dans Firebase
        programsRef = FirebaseDatabase.getInstance().getReference("programs");

        btnSaveProgram.setOnClickListener(v -> saveProgram());
    }

    /*
     * Cette méthode récupère les valeurs du formulaire,
     * vérifie les champs obligatoires,
     * puis enregistre le programme dans Firebase.
     */
    private void saveProgram() {
        String title = editTitle.getText().toString().trim();
        String level = editLevel.getText().toString().trim();
        String objective = editObjective.getText().toString().trim();
        String duration = editDuration.getText().toString().trim();
        String priceText = editPrice.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (title.isEmpty() || level.isEmpty() || objective.isEmpty()
                || duration.isEmpty() || priceText.isEmpty()
                || location.isEmpty() || description.isEmpty()) {

            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        double priceTnd;

        try {
            priceTnd = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Prix invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Générer un ID unique pour le programme
        String programId = programsRef.push().getKey();

        if (programId == null) {
            Toast.makeText(this, "Erreur lors de la génération de l'ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer un objet Program
        Program program = new Program(
                programId,
                title,
                level,
                objective,
                duration,
                priceTnd,
                description,
                location
        );

        // Enregistrer dans Firebase
        programsRef.child(programId).setValue(program)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Programme ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}