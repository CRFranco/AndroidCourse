package br.com.senior.tp_persistencia.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by cristiano.franco on 09/03/2017.
 */
public class Banco extends SQLiteOpenHelper {

    public Banco(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqld) {

        sqld.execSQL("CREATE TABLE usuarios ("
                + "id_usuarios INTEGER PRIMARY KEY autoincrement,"
                + " usuario varchar(45) NOT NULL ,"
                + " senha varchar(45) NOT NULL,"
                + " nome_completo varchar(45) NOT NULL"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        //TODO
    }
}
