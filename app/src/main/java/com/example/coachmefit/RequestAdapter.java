package com.example.coachmefit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
 * Adapter pour afficher les demandes des membres dans RecyclerView.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private Context context;
    private List<GoalRequest> requestList;

    public RequestAdapter(Context context, List<GoalRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
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
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView txtRequestProgram, txtRequestGoal, txtRequestStatus;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            txtRequestProgram = itemView.findViewById(R.id.txtRequestProgram);
            txtRequestGoal = itemView.findViewById(R.id.txtRequestGoal);
            txtRequestStatus = itemView.findViewById(R.id.txtRequestStatus);
        }
    }
}