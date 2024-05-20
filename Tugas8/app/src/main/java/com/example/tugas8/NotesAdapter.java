package com.example.tugas8;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Notes> notes;
    private Context context;
    private DBConfig dbConfig;
    private ActivityResultLauncher<Intent> resultLauncher;
    private List<Notes> notesFull;

    public NotesAdapter(List<Notes> notes, Context context, ActivityResultLauncher<Intent> resultLauncher) {
        this.notes = notes;
        this.context = context;
        this.dbConfig = new DBConfig(context);
        this.notesFull = new ArrayList<>(notes);
        this.resultLauncher = resultLauncher;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notes, parent, false);
        return new NotesViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {
        Notes note = notes.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());

        String updatedAt = note.getUpdatedAt();
        if (updatedAt != null){
            holder.tvDate.setText("Updated at " + updatedAt);
        } else {
            holder.tvDate.setText("Created at " + note.getCreatedAt());
        }

        holder.btnDelete.setOnClickListener(v -> {
            dbConfig.deleteNote(note.getId());
            notes.remove(note);
            notifyDataSetChanged();
        });

        holder.cardEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("id", note.getId());
            resultLauncher.launch(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
        this.notesFull = new ArrayList<>(notes);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;
        ImageView btnDelete;
        CardView cardEdit;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_judul);
            tvContent = itemView.findViewById(R.id.tv_konten);
            tvDate = itemView.findViewById(R.id.tv_tanggal);
            btnDelete = itemView.findViewById(R.id.iv_delete);
            cardEdit = itemView.findViewById(R.id.card_edit);
        }
    }
}
