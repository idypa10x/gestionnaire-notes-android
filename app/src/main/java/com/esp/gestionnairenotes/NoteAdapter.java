package com.esp.gestionnairenotes;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onFavoriteClick(Note note, int position);
        void onNoteDoubleClick(Note note);   // AJOUT Personne 5 : double clic = favori
    }

    private List<Note> noteList;
    private OnNoteClickListener listener;

    public NoteAdapter(List<Note> noteList, OnNoteClickListener listener) {
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitre());
        holder.tvDate.setText(note.getDate());

        String colorHex = note.getCouleur();
        if (colorHex != null && !colorHex.isEmpty()) {
            try {
                int color = Color.parseColor(colorHex);
                GradientDrawable bg = new GradientDrawable();
                bg.setColor(color);
                bg.setCornerRadius(24f);
                holder.cardRoot.setBackground(bg);
                holder.tvTitle.setTextColor(Color.WHITE);
                holder.tvDate.setTextColor(0xCCFFFFFF);
            } catch (IllegalArgumentException e) {
                holder.cardRoot.setBackgroundResource(R.drawable.card_default_bg);
                holder.tvTitle.setTextColor(Color.parseColor("#333333"));
                holder.tvDate.setTextColor(Color.parseColor("#999999"));
            }
        }

        holder.ivFavorite.setImageResource(
                note.isFavori() ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
        );

        // ===== AJOUT Personne 5 : clic simple + double clic sur la carte =====
        // Double clic = (de)favori. Clic simple = ouverture (comportement existant).
        // On route les deux via GestureDetector (un setOnClickListener classique
        // entrerait en conflit avec la detection du double clic).
        GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        listener.onNoteClick(note);
                        return true;
                    }
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        listener.onNoteDoubleClick(note);
                        return true;
                    }
                });
        holder.itemView.setOnTouchListener((v, event) -> {
            boolean handled = gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return handled;
        });
        // ===== fin ajout Personne 5 =====

        // Conserve (Personne 2) : clic sur l'etoile bascule aussi le favori.
        holder.ivFavorite.setOnClickListener(v ->
                listener.onFavoriteClick(note, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateList(List<Note> newList) {
        noteList = newList;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        View cardRoot;
        TextView tvTitle, tvDate;
        ImageView ivFavorite;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoot   = itemView.findViewById(R.id.card_root);
            tvTitle    = itemView.findViewById(R.id.tv_note_title);
            tvDate     = itemView.findViewById(R.id.tv_note_date);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
        }
    }
}
