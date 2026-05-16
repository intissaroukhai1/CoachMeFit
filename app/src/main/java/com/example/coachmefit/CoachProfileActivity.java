package com.example.coachmefit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/*
 * CoachProfileActivity affiche les informations simples du coach.
 * Pour le mini-projet, les informations sont statiques.
 */
public class CoachProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile);
    }
}