package br.com.senior.tp_persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.senior.tp_persistencia.data.Banco;

public class BancoDeDadosActivity extends AppCompatActivity {
    private Banco banco;
    private GridView gridView;
    public static ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banco_de_dados);
        criarBanco();
        listarUsuarios();

    }





    private void criarBanco() {
        banco = new Banco(this, "nomeDoBanco", 1);
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", "ximbinha");
        contentValues.put("senha", "guitarman");
        contentValues.put("nome_completo", "Ximbinha Calypso");
        banco.getWritableDatabase().insert("usuarios", null, contentValues);

    }



    private void listarUsuarios() {
        try {
            Cursor cr = banco.getWritableDatabase().rawQuery("SELECT * FROM usuarios", null);
            if (cr != null) {
                if (cr.moveToFirst()) {
                    do {
                        String usuario = cr.getString(cr.getColumnIndex("usuario"));
                        String nomeCompleto = cr.getString(cr.getColumnIndex("nome_completo"));
                        lista.add(usuario);
                        lista.add(nomeCompleto);
                    } while (cr.moveToNext());
                } else {
                    Toast.makeText(getApplicationContext(), "No Data to show",
                            Toast.LENGTH_LONG).show();
                }
            }
            cr.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        gridView = (GridView) findViewById(R.id.gridView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lista);
        gridView.setAdapter(adapter);

    }


}
