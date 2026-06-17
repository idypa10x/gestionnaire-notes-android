package com.esp.gestionnairenotes;

import android.content.Intent;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v -> afficherPalette());
    }

    private void afficherPalette() {

        String[] nomsCouleurs = {
                "Vert",
                "Rouge",
                "Bleu",
                "Jaune",
                "Orange",
                "Gris"
        };

        String[] codesCouleurs = {
                "#219653",
                "#EB5757",
                "#2F80ED",
                "#F2C94C",
                "#F2994A",
                "#828282"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir une couleur");

        builder.setItems(nomsCouleurs, (dialog, which) -> {

            String couleurChoisie = codesCouleurs[which];

            Intent intent = new Intent(this, NoteFormActivity.class);
            intent.putExtra("color", couleurChoisie);
            intent.putExtra("mode", "creation");
            startActivity(intent);

        });

        builder.show();
    }
}