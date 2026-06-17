package com.esp.gestionnairenotes;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NoteFormActivity extends AppCompatActivity {

    private EditText etTitre, etContenu;
    private Button btnAction;
    private String couleur;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        couleur = getIntent().getStringExtra("color");
        mode = getIntent().getStringExtra("mode");

        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        btnAction = findViewById(R.id.btnAction);

        // Appliquer la couleur au fond et au bouton
        int couleurInt = Color.parseColor(couleur);
        findViewById(R.id.layoutPrincipal).setBackgroundColor(couleurInt);
        btnAction.setBackgroundColor(couleurInt);

        // Texte du bouton selon le mode
        if (mode.equals("creation")) {
            btnAction.setText("Créer");
        } else {
            btnAction.setText("Modifier");
        }

        btnAction.setOnClickListener(v -> enregistrerNote());
    }

    private void enregistrerNote() {
        String titre = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        // Vérifier que les champs ne sont pas vides
        if (titre.isEmpty() || contenu.isEmpty()) {
            Toast.makeText(this, "Titre et contenu obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pour l'instant on affiche juste un message de succès
        Toast.makeText(this, "Note enregistrée !", Toast.LENGTH_SHORT).show();
        finish(); // Retourner à la liste
    }
}