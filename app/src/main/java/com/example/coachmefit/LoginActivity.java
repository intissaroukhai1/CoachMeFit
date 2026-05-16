package com.example.coachmefit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
 * LoginActivity permet de connecter un coach ou un membre.
 * L'utilisateur entre son email, son mot de passe et choisit son rôle.
 * Les données sont vérifiées depuis Firebase Realtime Database.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private RadioButton radioCoach, radioMember;
    private Button btnLogin;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        radioCoach = findViewById(R.id.radioCoach);
        radioMember = findViewById(R.id.radioMember);
        btnLogin = findViewById(R.id.btnLogin);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        String selectedRole;

        if (radioCoach.isChecked()) {
            selectedRole = "coach";
        } else if (radioMember.isChecked()) {
            selectedRole = "member";
        } else {
            Toast.makeText(this, "Veuillez choisir un rôle", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean found = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (user != null
                            && email.equals(user.getEmail())
                            && password.equals(user.getPassword())
                            && selectedRole.equals(user.getRole())) {

                        found = true;

                        Toast.makeText(
                                LoginActivity.this,
                                "Bienvenue " + user.getName(),
                                Toast.LENGTH_SHORT
                        ).show();

                        if ("coach".equals(user.getRole())) {

                            getSharedPreferences("CoachSession", MODE_PRIVATE)
                                    .edit()
                                    .putString("name", user.getName())
                                    .putString("email", user.getEmail())
                                    .putString("speciality", user.getSpeciality())
                                    .putString("experience", user.getExperience())
                                    .putString("status", user.getStatus())
                                    .apply();

                            Intent intent = new Intent(LoginActivity.this, CoachHomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else if ("member".equals(user.getRole())) {
                            getSharedPreferences("MemberSession", MODE_PRIVATE)
                                    .edit()
                                    .putString("email", user.getEmail())
                                    .putString("name", user.getName())
                                    .apply();

                            Intent intent = new Intent(LoginActivity.this, MemberHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        break;
                    }
                }

                if (!found) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Email, mot de passe ou rôle incorrect",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        LoginActivity.this,
                        "Erreur Firebase : " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}