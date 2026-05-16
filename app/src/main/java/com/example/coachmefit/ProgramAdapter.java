package com.example.coachmefit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
 * ProgramAdapter permet d'afficher la liste des programmes
 * dans un RecyclerView.
 */
public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder> {

    private Context context;
    private List<Program> programList;

    public ProgramAdapter(Context context, List<Program> programList) {
        this.context = context;
        this.programList = programList;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_program, parent, false);
        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        Program program = programList.get(position);

        holder.txtProgramTitle.setText(program.getTitle());
        holder.txtProgramLevel.setText("Niveau : " + program.getLevel());
        holder.txtProgramDuration.setText("Durée : " + program.getDuration());
        holder.txtProgramPrice.setText("Prix : " + program.getPriceTnd() + " TND");

        /*
         * Quand le membre clique sur un programme,
         * on ouvre ProgramDetailsActivity avec les données du programme.
         */
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProgramDetailsActivity.class);

            intent.putExtra("title", program.getTitle());
            intent.putExtra("level", program.getLevel());
            intent.putExtra("objective", program.getObjective());
            intent.putExtra("duration", program.getDuration());
            intent.putExtra("priceTnd", program.getPriceTnd());
            intent.putExtra("description", program.getDescription());
            intent.putExtra("location", program.getLocation());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    public static class ProgramViewHolder extends RecyclerView.ViewHolder {

        TextView txtProgramTitle, txtProgramLevel, txtProgramDuration, txtProgramPrice;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProgramTitle = itemView.findViewById(R.id.txtProgramTitle);
            txtProgramLevel = itemView.findViewById(R.id.txtProgramLevel);
            txtProgramDuration = itemView.findViewById(R.id.txtProgramDuration);
            txtProgramPrice = itemView.findViewById(R.id.txtProgramPrice);
        }
    }
}