package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.BaseColumns;
import android.view.View;
import android.widget.TableLayout;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class Listado extends AppCompatActivity {

    private TableLayout tblistado;
    private String[] cabecera = {"Id","Nombre","Apellido"};

    private DynamicTable creaTabla;
    private ArrayList<String[]> datos = new ArrayList<>();

    private FeedReaderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        // Toolbar (ID NUEVO: toolbarListado)
        MaterialToolbar toolbar = findViewById(R.id.toolbarListado);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Referencias de UI (ID NUEVO: tblistado)
        tblistado = findViewById(R.id.tblistado);

        // Inicializa DBHelper ANTES de consultar datos
        dbHelper = new FeedReaderDBHelper(this);

        // DynamicTable como en clase
        creaTabla = new DynamicTable(tblistado, getApplicationContext());
        creaTabla.setCabecera(cabecera);

        // Traer datos de SQLite
        TraerDatos();

        // Pintar cabecera y filas
        creaTabla.setDatos(datos);
        creaTabla.crearCabecera();
        creaTabla.crearFilas();
    }

    private void TraerDatos(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };
        String sortOrder = FeedReaderContract.FeedEntry.column2 + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            while (cursor.moveToNext()){
                String[] fila = new String[3];
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
                fila[0] = String.valueOf(itemId);
                fila[1] = nombre;
                fila[2] = apellido;
                datos.add(fila);
            }
            cursor.close();
        }
        db.close();
    }

    // Método como en clase
    public void Regresar(View vista){
        Intent registro = new Intent(this, MainActivity.class);
        startActivity(registro);
        // Si prefieren solo cerrar y volver atrás, podrían usar finish();
    }
}
