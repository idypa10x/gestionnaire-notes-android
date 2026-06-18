package com.esp.gestionnairenotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private SQLiteDatabase db;
    private final NoteDatabase noteDatabase;

    public NoteDAO(Context context) {
        noteDatabase = new NoteDatabase(context);
    }

    public void ouvrir() {
        db = noteDatabase.getWritableDatabase();
    }

    public void fermer() {
        noteDatabase.close();
    }

    // Créer une note
    public long creerNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.COL_TITRE, note.getTitre());
        values.put(NoteDatabase.COL_CONTENU, note.getContenu());
        values.put(NoteDatabase.COL_COULEUR, note.getCouleur());
        values.put(NoteDatabase.COL_FAVORI, note.isFavori() ? 1 : 0);
        values.put(NoteDatabase.COL_DATE, note.getDate());
        return db.insert(NoteDatabase.TABLE_NOTES, null, values);
    }

    // Lire toutes les notes
    public List<Note> obtenirToutesLesNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = db.query(
                NoteDatabase.TABLE_NOTES,
                null, null, null, null, null,
                NoteDatabase.COL_ID + " DESC"
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Note note = new Note();
                        note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COL_ID)));
                        note.setTitre(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_TITRE)));
                        note.setContenu(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_CONTENU)));
                        note.setCouleur(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_COULEUR)));
                        note.setFavori(cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COL_FAVORI)) == 1);
                        note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_DATE)));
                        notes.add(note);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return notes;
    }
    // Récupérer une note par son id
    public Note getNoteById(int id) {
        Note note = null;
        Cursor cursor = db.query(
                NoteDatabase.TABLE_NOTES,
                null,
                NoteDatabase.COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    note = new Note();
                    note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COL_ID)));
                    note.setTitre(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_TITRE)));
                    note.setContenu(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_CONTENU)));
                    note.setCouleur(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_COULEUR)));
                    note.setFavori(cursor.getInt(cursor.getColumnIndexOrThrow(NoteDatabase.COL_FAVORI)) == 1);
                    note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabase.COL_DATE)));
                }
            } finally {
                cursor.close();
            }
        }
        return note;
    }

    // Modifier une note
    public int modifierNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.COL_TITRE, note.getTitre());
        values.put(NoteDatabase.COL_CONTENU, note.getContenu());
        values.put(NoteDatabase.COL_COULEUR, note.getCouleur());
        values.put(NoteDatabase.COL_FAVORI, note.isFavori() ? 1 : 0);
        values.put(NoteDatabase.COL_DATE, note.getDate());
        return db.update(
                NoteDatabase.TABLE_NOTES,
                values,
                NoteDatabase.COL_ID + " = ?",
                new String[]{String.valueOf(note.getId())}
        );
    }

    // Basculer favori
    public void basculerFavori(Note note) {
        note.setFavori(!note.isFavori());
        modifierNote(note);
    }

    // Supprimer une note
    public void supprimerNote(int id) {
        db.delete(
                NoteDatabase.TABLE_NOTES,
                NoteDatabase.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }


}
