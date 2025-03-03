package com.example.spotifydb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class mainActivity extends AppCompatActivity {

    public EditText warning;
    private ListView listView;
    private ArrayList<String> canciones; // Lista donde guardamos los datos
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        canciones = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, canciones);
        listView.setAdapter(adapter);
    }

    public void consultar(View view) {
        canciones.clear(); // Limpiar lista antes de cargar nuevos datos
        DataBase admin = new DataBase(this, "Link", null, 1);
        SQLiteDatabase datab = admin.getReadableDatabase();

        Cursor cursor = datab.rawQuery("SELECT titulo, artista, duracion FROM playlist", null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(0);
                String artista = cursor.getString(1);
                int duracion = cursor.getInt(2);

                String song = titulo + " - " + artista + " (" + (duracion / 60) + "m " + (duracion % 60) + "s)";
                canciones.add(song);
            } while (cursor.moveToNext());
        }

        cursor.close();
        datab.close();

        adapter.notifyDataSetChanged(); // Refrescar ListView con los nuevos datos
    }

    public void borrar(View view){
        DataBase admin = new DataBase(mainActivity.this, "Link", null, 1);
        SQLiteDatabase datab = admin.getReadableDatabase();
        datab.execSQL("Delete from playlist");

        warning = findViewById(R.id.message);
        warning.setText("Playlist borrada");
    }
    public void insertar(View view){

        DataBase admin = new DataBase(mainActivity.this, "Link", null, 1);

        SQLiteDatabase datab = admin.getReadableDatabase();
        ContentValues log = new ContentValues();

        log.put("titulo","BIRDS OF A FEATHER");
        log.put("artista","Billie Eilish");
        log.put("duracion",210);
        log.put("imagen","hmhas");
        datab.insert("playlist",null, log);

        log.put("titulo","THE GREATEST");
        log.put("artista","BIllie Eilish");
        log.put("duracion",293);
        log.put("imagen","hmhas");
        datab.insert("playlist",null, log);

        log.put("titulo","Starfucker");
        log.put("artista","FINNEAS");
        log.put("duracion",217);
        log.put("imagen","fcol");
        datab.insert("playlist",null, log);

        log.put("titulo","Lotus Eater");
        log.put("artista","FINNEAS");
        log.put("duracion",231);
        log.put("imagen","fcol");
        datab.insert("playlist",null, log);

        log.put("titulo","For Cryin' Out Loud!");
        log.put("artista","FINNEAS");
        log.put("duracion",217);
        log.put("imagen","fcol");
        datab.insert("playlist",null, log);

        log.put("titulo","Peaches Etude");
        log.put("artista","FINNEAS");
        log.put("duracion",135);
        log.put("imagen","optimist");
        datab.insert("playlist",null, log);

        log.put("titulo","Only A Lifetime");
        log.put("artista","FINNEAS");
        log.put("duracion",256);
        log.put("imagen","optimist");
        datab.insert("playlist",null, log);

        log.put("titulo","Happier Than Ever");
        log.put("artista","Billie Eilish");
        log.put("duracion",298);
        log.put("imagen","hte");
        datab.insert("playlist",null, log);

        log.put("titulo","Fine Line");
        log.put("artista","Harry Styles");
        log.put("duracion",377);
        log.put("imagen","fineline");
        datab.insert("playlist",null, log);

        log.put("titulo","Golden");
        log.put("artista","Harry Styles");
        log.put("duracion",208);
        log.put("imagen","fineline");
        datab.insert("playlist",null, log);

        log.put("titulo","Falling");
        log.put("artista","Harry Styles");
        log.put("duracion",240);
        log.put("imagen","fineline");
        datab.insert("playlist",null, log);

        log.put("titulo","Bon Jovi");
        log.put("artista","I'll Be There For You");
        log.put("duracion",346);
        log.put("imagen","bonjovi");
        datab.insert("playlist",null, log);

        log.put("titulo","Every Breath You Take");
        log.put("artista","The Police");
        log.put("duracion",253);
        log.put("imagen","police");
        datab.insert("playlist",null, log);

        log.put("titulo","Help!");
        log.put("artista","The Beatles");
        log.put("duracion",139);
        log.put("imagen","help");
        datab.insert("playlist",null, log);

        log.put("titulo","You're Going To Lose That Girl");
        log.put("artista","The Beatles");
        log.put("duracion",138);
        log.put("imagen","help");
        datab.insert("playlist",null, log);

        log.put("titulo","Ticket To Ride");
        log.put("artista","The Beatles");
        log.put("duracion",189);
        log.put("imagen","help");
        datab.insert("playlist",null, log);

        log.put("titulo","A Hard Day's Night");
        log.put("artista","The Beatles");
        log.put("duracion",154);
        log.put("imagen","help");
        datab.insert("playlist",null, log);

        log.put("titulo","Can't Buy Me Love");
        log.put("artista","The Beatles");
        log.put("duracion",131);
        log.put("imagen","help");
        datab.insert("playlist",null, log);

        log.put("titulo","Circles");
        log.put("artista","Post Malone");
        log.put("duracion",215);
        log.put("imagen","hollywood");
        datab.insert("playlist",null, log);

        log.put("titulo","Internet");
        log.put("artista","Post Malone");
        log.put("duracion",123);
        log.put("imagen","hollywood");
        datab.insert("playlist",null, log);

        log.put("titulo","The Emptiness Machine");
        log.put("artista","Linkin Park");
        log.put("duracion",190);
        log.put("imagen","linkinpark");
        datab.insert("playlist",null, log);

        log.put("titulo","Best Song Ever");
        log.put("artista","One Direction");
        log.put("duracion",200);
        log.put("imagen","midnight");
        datab.insert("playlist",null, log);

        warning = findViewById(R.id.message);
        warning.setText("Playlist cargada");

    }
}
