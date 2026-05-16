package com.example.coachmefit;

/*
 * Classe modèle représentant un programme sportif.
 * Firebase a besoin d'un constructeur vide + getters/setters.
 */
public class Program {

    private String id;
    private String title;
    private String level;
    private String objective;
    private String duration;
    private double priceTnd;
    private String description;
    private String location;

    public Program() {
    }

    public Program(String id, String title, String level, String objective,
                   String duration, double priceTnd, String description, String location) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.objective = objective;
        this.duration = duration;
        this.priceTnd = priceTnd;
        this.description = description;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public String getObjective() {
        return objective;
    }

    public String getDuration() {
        return duration;
    }

    public double getPriceTnd() {
        return priceTnd;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setPriceTnd(double priceTnd) {
        this.priceTnd = priceTnd;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}