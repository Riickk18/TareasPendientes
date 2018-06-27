package android.example.com.tareaspendientes.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.com.tareaspendientes.utilidades.utilidades;

/**
 *Clase que contiene la conexion con la base de datos
 */
public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    /**
     * Constructor de la clase y establecimiento de la comunicacion con la BD
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Generacion de la base de datos
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(utilidades.CREAR_TABLA_TAREA);
    }

    /**
     * Metodo que actualiza la version de la BD y la borra en el caso de que la version de la bd halla sido modificada
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tarea");
    }
}
