package com.example.spotifydb;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView warning;
    private ListView listView;
    private ArrayList<String> canciones;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        warning = findViewById(R.id.message);
        canciones = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.item, canciones);
        listView.setAdapter(adapter);

        ejecutarUnaVez(this);
        consultar();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSong = canciones.get(position);
            showDeleteDialog(selectedSong, position);
        });
    }

    private void showDeleteDialog(String song, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Canción")
                .setMessage("¿Estás seguro de que quieres eliminar \"" + song + "\"?")
                .setPositiveButton("Sí", (dialog, which) -> BorrarCancion(position))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void BorrarCancion(int position) {
        String song = canciones.get(position);
        String title = song.split(" - ")[0];

        try (SQLiteDatabase datab = new DataBase(this, "Link", null, 1).getWritableDatabase()) {
            datab.delete("playlist", "titulo = ?", new String[]{title});
        }

        warning.setText("Cancion eliminada");
        consultar();
        warning.postDelayed(() -> warning.setText(""), 2000);

        Toast.makeText(this, "Canción eliminada correctamente", Toast.LENGTH_SHORT).show();
    }

    public void consultar() {
        canciones.clear();

        try (SQLiteDatabase datab = new DataBase(this, "Link", null, 1).getReadableDatabase();
             Cursor cursor = datab.rawQuery("SELECT titulo, artista, duracion FROM playlist", null)) {

            if (cursor.moveToFirst()) {
                do {
                    String titulo = cursor.getString(0);
                    String artista = cursor.getString(1);
                    int duracion = cursor.getInt(2);
                    canciones.add(titulo + " - " + artista + " (" + (duracion / 60) + "m " + (duracion % 60) + "s)");
                } while (cursor.moveToNext());
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void borrarTodos(View view) {
        try (SQLiteDatabase datab = new DataBase(this, "Link", null, 1).getWritableDatabase()) {
            datab.execSQL("DELETE FROM playlist");
        }

        consultar();
        warning.setText("Playlist borrada");
        warning.postDelayed(() -> warning.setText(""), 2000);
    }

    public void insertar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingresar Contenido");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        EditText editTextTitulo = dialogView.findViewById(R.id.editTextTitulo);
        EditText editTextContenido = dialogView.findViewById(R.id.editTextContenido);
        EditText editTextDuracion = dialogView.findViewById(R.id.editTextDuracion);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String titulo = editTextTitulo.getText().toString();
            String contenido = editTextContenido.getText().toString();
            String duracion = editTextDuracion.getText().toString();

            if (titulo.isEmpty() || contenido.isEmpty() || duracion.isEmpty()) {
                warning.setText("Complete todos los campos");
            } else {
                try (SQLiteDatabase datab = new DataBase(this, "Link", null, 1).getWritableDatabase()) {
                    ContentValues values = new ContentValues();
                    values.put("titulo", titulo);
                    values.put("artista", contenido);
                    values.put("duracion", duracion);
                    values.put("imagen", "fcol");
                    datab.insert("playlist", null, values);
                }
                warning.setText("Cancion agregada");
            }

            consultar();
            warning.postDelayed(() -> warning.setText(""), 2000);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void ejecutarUnaVez(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        if (!prefs.getBoolean("metodoEjecutado", false)) {

            try (SQLiteDatabase datab = new DataBase(this, "Link", null, 1).getWritableDatabase()) {
                String[][] cancionesIniciales = {
                        {"BIRDS OF A FEATHER", "Billie Eilish", "210", "hmhas"},
                        {"THE GREATEST", "Billie Eilish", "293", "hmhas"},
                        {"Starfucker", "FINNEAS", "217", "fcol"},
                        {"Lotus Eater", "FINNEAS", "231", "fcol"},
                        {"Peaches Etude", "FINNEAS", "135", "optimist"},
                        {"Only A Lifetime", "FINNEAS", "256", "optimist"},
                        {"Happier Than Ever", "Billie Eilish", "298", "hte"},
                        {"Fine Line", "Harry Styles", "377", "fineline"},
                        {"Golden", "Harry Styles", "208", "fineline"},
                        {"Falling", "Harry Styles", "240", "fineline"}
                };
                for (String[] song : cancionesIniciales) {
                    ContentValues values = new ContentValues();
                    values.put("titulo", song[0]);
                    values.put("artista", song[1]);
                    values.put("duracion", Integer.parseInt(song[2]));
                    values.put("imagen", song[3]);
                    datab.insert("playlist", null, values);
                }
            }

            warning.setText("Playlist cargada");
            consultar();
            warning.postDelayed(() -> warning.setText(""), 2000);
            prefs.edit().putBoolean("metodoEjecutado", true).apply();
        }
    }
}
