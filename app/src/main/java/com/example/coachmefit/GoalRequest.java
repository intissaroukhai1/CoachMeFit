package com.example.coachmefit;

/*
 * Cette classe représente une demande envoyée par un membre au coach.
 * Elle contient aussi l'email du membre pour filtrer ses propres demandes.
 */
public class GoalRequest {

    private String id;
    private String programTitle;
    private String memberGoal;
    private String status;
    private String memberEmail;

    public GoalRequest() {
    }

    public GoalRequest(String id, String programTitle, String memberGoal, String status, String memberEmail) {
        this.id = id;
        this.programTitle = programTitle;
        this.memberGoal = memberGoal;
        this.status = status;
        this.memberEmail = memberEmail;
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

    public String getMemberEmail() {
        return memberEmail;
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

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
}