package com.example.coachmefit;

/*
 * Cette classe représente une demande envoyée par un membre au coach.
 * Elle sera enregistrée dans Firebase sous le noeud "requests".
 */
public class GoalRequest {

    private String id;
    private String programTitle;
    private String memberGoal;
    private String status;

    // Constructeur vide obligatoire pour Firebase
    public GoalRequest() {
    }

    public GoalRequest(String id, String programTitle, String memberGoal, String status) {
        this.id = id;
        this.programTitle = programTitle;
        this.memberGoal = memberGoal;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public String getMemberGoal() {
        return memberGoal;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public void setMemberGoal(String memberGoal) {
        this.memberGoal = memberGoal;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}