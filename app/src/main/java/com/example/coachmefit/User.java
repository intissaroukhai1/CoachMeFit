package com.example.coachmefit;

/*
 * Classe modèle pour représenter un utilisateur.
 */
public class User {

    private String email;
    private String password;
    private String role;
    private String name;
    private String speciality;
    private String experience;
    private String status;

    public User() {
    }

    public User(String email, String password, String role, String name,
                String speciality, String experience, String status) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.name = name;
        this.speciality = speciality;
        this.experience = experience;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getExperience() {
        return experience;
    }

    public String getStatus() {
        return status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}