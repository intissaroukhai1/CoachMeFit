package com.example.coachmefit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/*
 * Adapter pour afficher les demandes.
 * Côté coach : showActions = true → boutons Accepter / Refuser.
 * Côté membre : showActions = false → voir seulement le statut.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private Context context;
    private List<GoalRequest> requestList;
    private boolean showActions;

    public RequestAdapter(Context context, List<GoalRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
        this.showActions = true;
    }

    public RequestAdapter(Context context, List<GoalRequest> requestList, boolean showActions) {
        this.context = context;
        this.requestList = requestList;
        this.showActions = showActions;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        GoalRequest request = requestList.get(position);

        holder.txtRequestProgram.setText("Programme : " + request.getProgramTitle());
        holder.txtRequestGoal.setText("Objectif : " + request.getMemberGoal());
        holder.txtRequestStatus.setText("Statut : " + request.getStatus());

        if (showActions && "En attente".equals(request.getStatus())) {
            holder.btnAcceptRequest.setVisibility(View.VISIBLE);
            holder.btnRejectRequest.setVisibility(View.VISIBLE);

            holder.btnAcceptRequest.setOnClickListener(v -> {
                updateRequestStatus(request.getId(), "Acceptée");
            });

            holder.btnRejectRequest.setOnClickListener(v -> {
                updateRequestStatus(request.getId(), "Refusée");
            });

        } else {
            holder.btnAcceptRequest.setVisibility(View.GONE);
            holder.btnRejectRequest.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    /*
     * Mise à jour du statut de la demande + création d'une notification.
     */
    private void updateRequestStatus(String requestId, String newStatus) {
        if (requestId == null) {
            Toast.makeText(context, "ID de demande introuvable", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference requestRef = FirebaseDatabase
                .getInstance()
                .getReference("requests")
                .child(requestId);

        requestRef.child("status").setValue(newStatus)
                .addOnSuccessListener(unused -> {

                    for (GoalRequest request : requestList) {
                        if (requestId.equals(request.getId())) {
                            createNotificationForMember(request, newStatus);
                            break;
                        }
                    }

                    Toast.makeText(context, "Statut mis à jour : " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
    /*
     * Créer une notification pour le membre après acceptation/refus.
     */
    private void createNotificationForMember(GoalRequest request, String newStatus) {

        if (request.getMemberEmail() == null || request.getMemberEmail().isEmpty()) {
            Toast.makeText(context, "Email du membre introuvable", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference notificationsRef = FirebaseDatabase
                .getInstance()
                .getReference("notifications");

        String notificationId = notificationsRef.push().getKey();

        if (notificationId == null) {
            Toast.makeText(context, "Erreur notification", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = "Votre demande pour le programme \""
                + request.getProgramTitle()
                + "\" a été "
                + newStatus;

        NotificationItem notification = new NotificationItem(
                notificationId,
                request.getMemberEmail(),
                message,
                "Non lue",
                request.getId()
        );

        notificationsRef.child(notificationId).setValue(notification);
    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView txtRequestProgram, txtRequestGoal, txtRequestStatus;
        Button btnAcceptRequest, btnRejectRequest;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRequestProgram = itemView.findViewById(R.id.txtRequestProgram);
            txtRequestGoal = itemView.findViewById(R.id.txtRequestGoal);
            txtRequestStatus = itemView.findViewById(R.id.txtRequestStatus);

            btnAcceptRequest = itemView.findViewById(R.id.btnAcceptRequest);
            btnRejectRequest = itemView.findViewById(R.id.btnRejectRequest);
        }
    }
}