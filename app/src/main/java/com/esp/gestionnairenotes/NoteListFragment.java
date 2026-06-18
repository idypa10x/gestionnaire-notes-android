package com.esp.gestionnairenotes;

import android.content.Intent;
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
    private List<Note> noteList;
    private List<Note> toutesLesNotes;
    private View layoutEmpty;
    private NoteDAO noteDAO;
    private boolean filtreFavorisActif = false;

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

        recyclerView = view.findViewById(R.id.recycler_notes);
        layoutEmpty = view.findViewById(R.id.layout_empty);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_note);
        EditText etRecherche = view.findViewById(R.id.et_recherche);
        Button btnFavoris = view.findViewById(R.id.btn_favoris);

        noteDAO = new NoteDAO(getContext());
        noteDAO.ouvrir();

        chargerNotes();

        // Bouton + pour ajouter une note
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NoteFormActivity.class);
            startActivity(intent);
        });

        // Recherche en temps réel
        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrerNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filtre Favoris
        btnFavoris.setOnClickListener(v -> {
            filtreFavorisActif = !filtreFavorisActif;
            btnFavoris.setBackgroundTintList(
                    filtreFavorisActif
                            ? android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#F2C94C"))
                            : android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE)
            );
            filtrerNotes(etRecherche.getText().toString());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        chargerNotes();
    }

    private void chargerNotes() {
        toutesLesNotes = noteDAO.obtenirToutesLesNotes();
        noteList = new ArrayList<>(toutesLesNotes);
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        updateEmptyState();
    }

    private void filtrerNotes(String recherche) {
        noteList.clear();
        for (Note note : toutesLesNotes) {
            boolean matchRecherche = note.getTitre().toLowerCase()
                    .contains(recherche.toLowerCase());
            boolean matchFavori = !filtreFavorisActif || note.isFavori();
            if (matchRecherche && matchFavori) {
                noteList.add(note);
            }
        }
        adapter.notifyDataSetChanged();
        updateEmptyState();
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
        Intent intent = new Intent(getActivity(), NoteFormActivity.class);
        intent.putExtra("note_id", note.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Note note, int position) {
        noteDAO.basculerFavori(note);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noteDAO.fermer();
    }
}