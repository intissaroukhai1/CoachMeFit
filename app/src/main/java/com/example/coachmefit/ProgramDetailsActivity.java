package com.example.coachmefit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 * ProgramDetailsActivity affiche les détails d'un programme sportif.
 * Elle utilise :
 * - les données envoyées par Intent depuis ProgramAdapter
 * - une conversion simple de devise TND vers EUR/USD
 * - un Intent implicite pour partager le programme
 * - un Intent implicite pour ouvrir la localisation
 * - un Intent bidirectionnel pour recevoir l'objectif depuis GoalActivity
 * - Firebase pour enregistrer l'objectif dans le noeud "requests"
 */
public class ProgramDetailsActivity extends AppCompatActivity {

    private TextView txtDetailTitle, txtDetailLevel, txtDetailObjective;
    private TextView txtDetailDuration, txtDetailPrice, txtDetailLocation;
    private TextView txtDetailDescription, txtConversionResult;

    private Button btnConvertPrice, btnShareProgram, btnOpenLocation, btnSendGoal;

    private String title, level, objective, duration, description, location;
    private double priceTnd;

    // Launcher utilisé pour ouvrir GoalActivity et récupérer le résultat
    private ActivityResultLauncher<Intent> goalLauncher;

    // Référence Firebase vers le noeud "requests"
    private DatabaseReference requestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);

        // Initialisation Firebase
        requestsRef = FirebaseDatabase.getInstance().getReference("requests");

        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailLevel = findViewById(R.id.txtDetailLevel);
        txtDetailObjective = findViewById(R.id.txtDetailObjective);
        txtDetailDuration = findViewById(R.id.txtDetailDuration);
        txtDetailPrice = findViewById(R.id.txtDetailPrice);
        txtDetailLocation = findViewById(R.id.txtDetailLocation);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        txtConversionResult = findViewById(R.id.txtConversionResult);

        btnConvertPrice = findViewById(R.id.btnConvertPrice);
        btnShareProgram = findViewById(R.id.btnShareProgram);
        btnOpenLocation = findViewById(R.id.btnOpenLocation);
        btnSendGoal = findViewById(R.id.btnSendGoal);

        // Récupération des données envoyées depuis ProgramAdapter
        title = getIntent().getStringExtra("title");
        level = getIntent().getStringExtra("level");
        objective = getIntent().getStringExtra("objective");
        duration = getIntent().getStringExtra("duration");
        description = getIntent().getStringExtra("description");
        location = getIntent().getStringExtra("location");
        priceTnd = getIntent().getDoubleExtra("priceTnd", 0);

        // Affichage des détails du programme
        txtDetailTitle.setText(title);
        txtDetailLevel.setText("Niveau : " + level);
        txtDetailObjective.setText("Objectif : " + objective);
        txtDetailDuration.setText("Durée : " + duration);
        txtDetailPrice.setText("Prix : " + priceTnd + " TND");
        txtDetailLocation.setText("Localisation : " + location);
        txtDetailDescription.setText("Description : " + description);

        /*
         * Initialisation de l'intent bidirectionnel.
         * Quand GoalActivity renvoie un objectif, on l'enregistre dans Firebase.
         */
        goalLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String goal = result.getData().getStringExtra("memberGoal");

                        if (goal != null) {
                            saveGoalRequest(goal);
                        }
                    }
                }
        );

        btnConvertPrice.setOnClickListener(v -> convertPrice());
        btnShareProgram.setOnClickListener(v -> shareProgram());
        btnOpenLocation.setOnClickListener(v -> openLocation());
        btnSendGoal.setOnClickListener(v -> openGoalActivity());
    }

    /*
     * Conversion simple du prix TND vers EUR et USD.
     */
    private void convertPrice() {
        double priceEuro = priceTnd * 0.30;
        double priceUsd = priceTnd * 0.32;

        String result = "Prix en EUR : " + String.format("%.2f", priceEuro) + " €\n"
                + "Prix en USD : " + String.format("%.2f", priceUsd) + " $";

        txtConversionResult.setText(result);
    }

    /*
     * Intent implicite ACTION_SEND pour partager le programme.
     */
    private void shareProgram() {
        String message = "Programme sportif : " + title + "\n"
                + "Niveau : " + level + "\n"
                + "Objectif : " + objective + "\n"
                + "Durée : " + duration + "\n"
                + "Prix : " + priceTnd + " TND";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(Intent.createChooser(shareIntent, "Partager avec"));
    }

    /*
     * Intent implicite ACTION_VIEW pour ouvrir Google Maps.
     */
    private void openLocation() {
        if (location == null || location.trim().isEmpty()) {
            Toast.makeText(this, "Localisation vide", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(mapIntent);
    }

    /*
     * Ouvre GoalActivity avec goalLauncher pour récupérer le résultat.
     */
    private void openGoalActivity() {
        Intent intent = new Intent(ProgramDetailsActivity.this, GoalActivity.class);
        intent.putExtra("programTitle", title);
        goalLauncher.launch(intent);
    }

    /*
     * Enregistre l'objectif du membre dans Firebase.
     * Le coach pourra ensuite consulter cette demande.
     */
    private void saveGoalRequest(String goal) {

        String requestId = requestsRef.push().getKey();

        if (requestId == null) {
            Toast.makeText(this, "Erreur lors de la création de la demande", Toast.LENGTH_SHORT).show();
            return;
        }

        GoalRequest request = new GoalRequest(
                requestId,
                title,
                goal,
                "En attente"
        );

        requestsRef.child(requestId).setValue(request)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(
                            ProgramDetailsActivity.this,
                            "Objectif envoyé au coach avec succès",
                            Toast.LENGTH_LONG
                    ).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            ProgramDetailsActivity.this,
                            "Erreur Firebase : " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}