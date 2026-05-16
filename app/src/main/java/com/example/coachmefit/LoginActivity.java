package com.example.coachmefit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/*
 * LoginActivity est la première page de l'application.
 * Elle permet de choisir le rôle : Coach ou Membre.
 * Chaque bouton utilise un Intent explicite pour ouvrir une autre Activity.
 */
public class LoginActivity extends AppCompatActivity {

    private Button btnCoach, btnMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCoach = findViewById(R.id.btnCoach);
        btnMember = findViewById(R.id.btnMember);

        btnCoach.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CoachHomeActivity.class);
            startActivity(intent);
        });

        btnMember.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MemberHomeActivity.class);
            startActivity(intent);
        });
    }
}