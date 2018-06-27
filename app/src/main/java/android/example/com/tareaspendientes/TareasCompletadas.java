package android.example.com.tareaspendientes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.tareaspendientes.BD.ConexionSQLiteHelper;
import android.example.com.tareaspendientes.BD.tareasBD;
import android.example.com.tareaspendientes.utilidades.utilidades;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TareasCompletadas extends AppCompatActivity {

    //Variables y elementos de la UI
    private Toolbar toolbar;
    public List<tareasBD> tareas = new ArrayList<>();
    ConexionSQLiteHelper con;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas_completadas);
        toolbar = findViewById(R.id.my_toolbar);
        rv = findViewById(R.id.rv);
        setSupportActionBar(toolbar);
        con = new ConexionSQLiteHelper(this, "bd_tareas", null, 1);
        this.CargarTareas();
    }

    /**
     * Inicializa los elementos de la toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tareas_completadas, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Evalua cual elemento de la toolbar fue presionado
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.descartar:
                cerrarVentana();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * finaliza la actividad
     */
    private void cerrarVentana(){
        finish();
    }

    /**
     * Carga las tareas finalizadas desde la base de datos hacia la UI
     */
    private void CargarTareas(){
        SQLiteDatabase db = con.getReadableDatabase();
        String[] campos = {utilidades.CAMPO_ID, utilidades.CAMPO_TITULO, utilidades.CAMPO_DESCRIPCION, utilidades.CAMPO_FECHAC, utilidades.CAMPO_FECHAF, utilidades.CAMPO_HORAR};
        Cursor cursor = db.rawQuery(" SELECT " + utilidades.CAMPO_ID + ", " + utilidades.CAMPO_TITULO + ", " +
                        "" + utilidades.CAMPO_DESCRIPCION + ", " + utilidades.CAMPO_FECHAC + ", " + utilidades.CAMPO_FECHAF + ", " +
                        "" + utilidades.CAMPO_HORAR + ", "+utilidades.CAMPO_ESTADO+" FROM " + utilidades.TABLA_TAREA + " WHERE "+utilidades.CAMPO_ESTADO+"='true' ORDER BY " + utilidades.CAMPO_FECHAC + " DESC",
                null);

        while (cursor.moveToNext()) {
            tareasBD tarea = new tareasBD(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5),Boolean.valueOf(cursor.getString(6)));
            tareas.add(tarea);

        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(tareas);
        rv.setAdapter(adapter);
    }

    /**
     * Refresca la UI
     */
    @Override
    protected void onResume() {
        super.onResume();
        tareas.clear();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(tareas);
        rv.setAdapter(adapter);
        this.CargarTareas();
    }
}
