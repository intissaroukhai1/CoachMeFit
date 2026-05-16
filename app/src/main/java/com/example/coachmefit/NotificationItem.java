package com.example.coachmefit;

/*
 * Cette classe représente une notification envoyée au membre
 * après la modification du statut par le coach.
 */
public class NotificationItem {

    private String id;
    private String memberEmail;
    private String message;
    private String status;
    private String requestId;

    public NotificationItem() {
    }

    public NotificationItem(String id, String memberEmail, String message, String status, String requestId) {
        this.id = id;
        this.memberEmail = memberEmail;
        this.message = message;
        this.status = status;
        this.requestId = requestId;
    }

    public String getId() {
        return id;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}