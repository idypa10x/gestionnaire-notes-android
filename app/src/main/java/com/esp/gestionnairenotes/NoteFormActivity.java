package com.esp.gestionnairenotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteFormActivity extends AppCompatActivity {

    public static final String EXTRA_COLOR   = "note_color";
    public static final String EXTRA_NOTE_ID = "note_id";

    private EditText     etTitle;
    private EditText     etContent;
    private Button       btnSave;
    private LinearLayout noteCard;

    private String selectedColor;
    private Note   existingNote;
    private NoteDAO noteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        noteDAO = new NoteDAO(this);
        noteDAO.ouvrir();
        initViews();
        readIntentExtras();
        applyColorToCard();
        configureModeUI();
    }

    private void initViews() {
        etTitle   = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSave   = findViewById(R.id.btnSave);
        noteCard  = findViewById(R.id.noteCard);
        btnSave.setOnClickListener(v -> onSaveClicked());
    }

    private void readIntentExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            int noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1);
            existingNote = noteDAO.getNoteById(noteId);
            if (existingNote != null) {
                selectedColor = existingNote.getCouleur();
            } else {
                Toast.makeText(this, "Note introuvable", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            existingNote  = null;
            selectedColor = intent.getStringExtra(EXTRA_COLOR);
            if (selectedColor == null || selectedColor.isEmpty()) {
                selectedColor = "#219653";
            }
        }
    }

    private void configureModeUI() {
        if (existingNote != null) {
            etTitle.setText(existingNote.getTitre());
            etContent.setText(existingNote.getContenu());
            btnSave.setText("Modifier");
        } else {
            btnSave.setText("Créer");
        }
    }

    private void applyColorToCard() {
        try {
            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.RECTANGLE);
            bg.setCornerRadius(12 * getResources().getDisplayMetrics().density);
            bg.setColor(Color.parseColor(selectedColor));
            noteCard.setBackground(bg);
        } catch (IllegalArgumentException e) {
            noteCard.setBackgroundColor(Color.parseColor("#219653"));
        }
    }

    private void onSaveClicked() {
        String titre   = etTitle.getText().toString().trim();
        String contenu = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(titre)) {
            etTitle.setError("Le titre ne peut pas être vide");
            etTitle.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contenu)) {
            etContent.setError("Le contenu ne peut pas être vide");
            etContent.requestFocus();
            return;
        }

        if (existingNote != null) {
            existingNote.setTitre(titre);
            existingNote.setContenu(contenu);
            existingNote.setCouleur(selectedColor);
            noteDAO.modifierNote(existingNote);
            Toast.makeText(this, "Note modifiée !", Toast.LENGTH_SHORT).show();
        } else {
            String date = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH).format(new Date());
            Note note = new Note(titre, contenu, selectedColor, false, date);
            noteDAO.creerNote(note);
            Toast.makeText(this, "Note créée !", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        noteDAO.fermer();
        finish();
    }
}