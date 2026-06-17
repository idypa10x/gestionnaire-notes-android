package com.esp.gestionnairenotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class NoteListFragment extends Fragment implements NoteAdapter.OnNoteClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> noteList;
    private View layoutEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recycler_notes);
        layoutEmpty  = view.findViewById(R.id.layout_empty);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_note);

        // Données de test (à remplacer plus tard par la vraie base de données)
        noteList = new ArrayList<>();
        noteList.add(new Note("Réunion design", "Contenu...", "#219653", true, "14 juin 2025"));
        noteList.add(new Note("Bug critique", "Contenu...", "#EB5757", false, "13 juin 2025"));
        noteList.add(new Note("Todo courses", "Contenu...", "#F2C94C", true, "12 juin 2025"));

        // Configurer le RecyclerView
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Afficher état vide ou liste
        updateEmptyState();

        // Bouton + pour ajouter une note
        fab.setOnClickListener(v -> {
            // TODO : naviguer vers l'écran de création de note
        });
    }

    private void updateEmptyState() {
        if (noteList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoteClick(Note note) {
        // TODO : naviguer vers le détail de la note
    }

    @Override
    public void onFavoriteClick(Note note, int position) {
        note.setFavori(!note.isFavori());
        adapter.notifyItemChanged(position);
    }
}