package com.example.coachmefit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*
 * CoachProfileActivity affiche les informations dynamiques du coach connecté.
 */
public class CoachProfileActivity extends AppCompatActivity {

    private TextView txtCoachName, txtCoachTitle;
    private TextView txtCoachSpeciality, txtCoachExperience, txtCoachEmail, txtCoachStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile);

        txtCoachName = findViewById(R.id.txtCoachName);
        txtCoachTitle = findViewById(R.id.txtCoachTitle);
        txtCoachSpeciality = findViewById(R.id.txtCoachSpeciality);
        txtCoachExperience = findViewById(R.id.txtCoachExperience);
        txtCoachEmail = findViewById(R.id.txtCoachEmail);
        txtCoachStatus = findViewById(R.id.txtCoachStatus);

        SharedPreferences prefs = getSharedPreferences("CoachSession", MODE_PRIVATE);

        String name = prefs.getString("name", "Coach");
        String email = prefs.getString("email", "coach@coachmefit.com");
        String speciality = prefs.getString("speciality", "Fitness");
        String experience = prefs.getString("experience", "Non renseignée");
        String status = prefs.getString("status", "Disponible");

        txtCoachName.setText(name);
        txtCoachTitle.setText("Coach Fitness Professionnel");
        txtCoachSpeciality.setText(speciality);
        txtCoachExperience.setText(experience);
        txtCoachEmail.setText(email);
        txtCoachStatus.setText(status);
    }
}