package com.example.myapplication;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.BaseColumns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;

    private FeedReaderDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtid=findViewById(R.id.txtid);
        txtnombre=findViewById(R.id.txtnombre);
        txtapellido=findViewById(R.id.txtapellido);
        dbHelper = new FeedReaderDBHelper(this);
    }

    public void Listar(View vista){
        Intent listar = new Intent(this,Listado.class);
        startActivity(listar);
    }

    public void Guardar(View Vista){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());

        long newRowID = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);

        Toast.makeText(getApplicationContext(), "se guardo el registro con clave: "+ newRowID,Toast.LENGTH_LONG).show();
        db.close();
    }

    public void Buscar(View vista){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = {txtid.getText().toString()};

        String sortOrder = FeedReaderContract.FeedEntry.column2 + " ASC";
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while(cursor.moveToNext()){
            String nombre=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            txtnombre.setText(nombre+"");
            String apellido=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));
            txtapellido.setText(apellido+"");
        }
        db.close();
    }

    public void Eliminar(View vista){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";

        String[] selectionArgs = {txtid.getText().toString() };

        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nameTable, selection, selectionArgs);
        db.close();
        Toast.makeText(getApplicationContext(), "se elimino "+ deletedRows+" registro(s)", Toast.LENGTH_LONG).show();
    }

    public void Actualizar(View vista){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        int count = db.update(FeedReaderContract.FeedEntry.nameTable, values, selection, selectionArgs);
        Toast.makeText(getApplicationContext(), "se actualiza "+ count+" registrado(s)",Toast.LENGTH_LONG).show();
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
