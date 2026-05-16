package com.example.coachmefit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
 * GoalActivity permet au membre d'écrire son objectif sportif.
 * Cette activité renvoie ensuite l'objectif à ProgramDetailsActivity.
 */
public class GoalActivity extends AppCompatActivity {

    private TextView txtProgramName;
    private EditText editGoal;
    private Button btnSendGoalResult;

    private String programTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        txtProgramName = findViewById(R.id.txtProgramName);
        editGoal = findViewById(R.id.editGoal);
        btnSendGoalResult = findViewById(R.id.btnSendGoalResult);

        // Récupérer le titre du programme envoyé depuis ProgramDetailsActivity
        programTitle = getIntent().getStringExtra("programTitle");

        if (programTitle != null) {
            txtProgramName.setText("Programme : " + programTitle);
        }

        btnSendGoalResult.setOnClickListener(v -> sendGoalBack());
    }

    /*
     * Cette méthode renvoie l'objectif saisi vers ProgramDetailsActivity.
     */
    private void sendGoalBack() {
        String goal = editGoal.getText().toString().trim();

        if (goal.isEmpty()) {
            Toast.makeText(this, "Veuillez écrire votre objectif", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("memberGoal", goal);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}