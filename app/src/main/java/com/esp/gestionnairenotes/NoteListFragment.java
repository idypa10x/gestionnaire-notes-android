package com.esp.gestionnairenotes;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    private List<Note> noteList;          // source complete (non filtree)
    private View layoutEmpty;

    // ===== AJOUT : etat des filtres =====
    private EditText etSearch;
    private Button btnFavoris;
    private String currentQuery = "";
    private boolean favorisOnly = false;

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

        // AJOUT Personne 5 : vues recherche + favoris
        etSearch   = view.findViewById(R.id.et_search);
        btnFavoris = view.findViewById(R.id.btn_favoris);

        // Donnees de test (a remplacer plus tard par la vraie base de donnees)
        noteList = new ArrayList<>();
        noteList.add(new Note("Réunion design", "Contenu...", "#219653", true, "14 juin 2025"));
        noteList.add(new Note("Bug critique", "Contenu...", "#EB5757", false, "13 juin 2025"));
        noteList.add(new Note("Todo courses", "Contenu...", "#F2C94C", true, "12 juin 2025"));

        // Configurer le RecyclerView
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // ===== AJOUT Personne 5 : recherche par titre en temps reel =====
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                currentQuery = s.toString();
                applyFilters();
            }
        });

        // ===== AJOUT Personne 5 : filtre Favoris (toggle on/off) =====
        btnFavoris.setOnClickListener(v -> {
            favorisOnly = !favorisOnly;
            btnFavoris.setSelected(favorisOnly);
            btnFavoris.setTextColor(favorisOnly ? Color.WHITE : Color.BLACK);
            applyFilters();
        });

        // Affichage initial (liste complete, filtres appliques)
        applyFilters();

        // Bouton + pour ajouter une note
        fab.setOnClickListener(v -> {
            // TODO : naviguer vers l'ecran de creation de note
        });
    }

    // ===== AJOUT Personne 5 : application recherche + favoris =====
    private void applyFilters() {
        List<Note> filtered = NoteFilter.apply(noteList, currentQuery, favorisOnly);
        adapter.updateList(filtered);
        updateEmptyState();
    }

    private void updateEmptyState() {
        boolean empty = adapter.getItemCount() == 0;
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onNoteClick(Note note) {
        // TODO : naviguer vers le detail de la note
    }

    @Override
    public void onFavoriteClick(Note note, int position) {
        note.setFavori(!note.isFavori());
        adapter.notifyItemChanged(position);
        // Optionnel (coherence avec le filtre Favoris) : applyFilters();
    }

    // ===== AJOUT : double clic = (de)favori + mise a jour icone =====
    @Override
    public void onNoteDoubleClick(Note note) {
        note.setFavori(!note.isFavori());

        applyFilters(); // rafraichit l'icone ; si le filtre Favoris est actif, la note (dis)parait
    }
}
