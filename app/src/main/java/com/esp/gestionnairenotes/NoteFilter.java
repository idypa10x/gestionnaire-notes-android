package com.esp.gestionnairenotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Logique de filtrage des notes : recherche par titre + filtre favoris.
 * Sans dependance Android -> testable et reutilisable.
 * Responsabilite : Personne 5 (fonctionnalites intelligentes).
 */
public final class NoteFilter {

    private NoteFilter() { }

    public static List<Note> apply(List<Note> source, String query, boolean favorisOnly) {
        List<Note> result = new ArrayList<>();
        if (source == null) {
            return result;
        }
        String q = (query == null) ? "" : query.trim().toLowerCase(Locale.getDefault());

        for (Note note : source) {
            boolean matchTitre = q.isEmpty()
                    || (note.getTitre() != null
                    && note.getTitre().toLowerCase(Locale.getDefault()).contains(q));
            boolean matchFavori = !favorisOnly || note.isFavori();
            if (matchTitre && matchFavori) {
                result.add(note);
            }
        }
        return result;
    }
}
