package android.example.com.tareaspendientes;

import android.app.Activity;
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
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Variables y elementos de la UI
    private Toolbar toolbar;
    public List<tareasBD> tareas = new ArrayList<>();
    ConexionSQLiteHelper con;
    RecyclerView rv;

    /**
     * Constructor de la Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.my_toolbar);
        rv = findViewById(R.id.rv);
        setSupportActionBar(toolbar);
        con = new ConexionSQLiteHelper(this, "bd_tareas", null, 1);
        this.CargarTareas();
    }

    /**
     * Carga los elementos de la toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Verifica cual de los elementos de la toolbar fue presionado y se acciona el evento segun el elemento
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.completado:
                Intent intent = new Intent(this, TareasCompletadas.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inicia la Activity CrearTarea para generar una nueva tarea
     * @param view
     */
    public void bAgregarArticulo(View view) {
        Intent intent = new Intent(this, CrearTarea.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Recibe el resultado de la activity CrearTarea
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                this.CargarTareas();
            }
            else{
                //Toast.makeText(this,"no devuelve RESULT OK",Toast.LENGTH_SHORT).show();
                System.out.println("No devolvió RESULT_OK");
            }
        }
        catch (Exception e){
            Toast.makeText(this,"hubo un error"+e,Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Carga las tareas pendientes de la base de datos a la UI
     */
    private void CargarTareas(){
        SQLiteDatabase db = con.getReadableDatabase();
        String[] campos = {utilidades.CAMPO_ID, utilidades.CAMPO_TITULO, utilidades.CAMPO_DESCRIPCION, utilidades.CAMPO_FECHAC, utilidades.CAMPO_FECHAF, utilidades.CAMPO_HORAR};
        Cursor cursor = db.rawQuery(" SELECT " + utilidades.CAMPO_ID + ", " + utilidades.CAMPO_TITULO + ", " +
                        "" + utilidades.CAMPO_DESCRIPCION + ", " + utilidades.CAMPO_FECHAC + ", " + utilidades.CAMPO_FECHAF + ", " +
                        "" + utilidades.CAMPO_HORAR + ", "+utilidades.CAMPO_ESTADO+" FROM " + utilidades.TABLA_TAREA + " WHERE "+utilidades.CAMPO_ESTADO+"='false' ORDER BY " + utilidades.CAMPO_FECHAC + " DESC",
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
     * Refresca la lista de tareas
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
