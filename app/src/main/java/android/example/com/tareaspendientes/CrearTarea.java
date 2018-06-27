package android.example.com.tareaspendientes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.tareaspendientes.BD.ConexionSQLiteHelper;
import android.example.com.tareaspendientes.BD.tareasBD;
import android.example.com.tareaspendientes.utilidades.utilidades;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrearTarea extends AppCompatActivity {

    //Variables y elementos de la interfaz de usuario
    Toolbar toolbar;
    EditText title, description, dateF, time;
    CheckBox checkBoxCT;
    FloatingActionButton buttonDelete;
    tareasBD tareaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_crear_tarea);
            toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);
            title = findViewById(R.id.tituloF);
            description = findViewById(R.id.descripcionF);
            dateF = findViewById(R.id.fechaF);
            time = findViewById(R.id.horaF);
            checkBoxCT = findViewById(R.id.checkBoxCrearTarea);
            buttonDelete = findViewById(R.id.buttonDelete);
            Intent intent = getIntent();
            if(intent.getExtras()!=null){
                checkBoxCT.setVisibility(View.VISIBLE);
                List<String> item;
                item = intent.getExtras().getStringArrayList("tareaOriginal");
                for(int i=0;i<item.size();i++){
                    System.out.println(item.get(i));
                }
                tareaActual = new tareasBD(Integer.parseInt(item.get(0)),item.get(1),item.get(2),item.get(3),item.get(4),item.get(5),Boolean.valueOf(item.get(6)));
                title.setText(tareaActual.getTitulo());
                description.setText(tareaActual.getDescripcion());
                dateF.setText(tareaActual.getFechaFinalizacion());
                time.setText(tareaActual.getHoraRecordatorio());
                checkBoxCT.setChecked(tareaActual.isEstado());
                if(tareaActual.isEstado()){
                    buttonDelete.setVisibility(View.VISIBLE);
                    title.setEnabled(false);
                    description.setEnabled(false);
                    dateF.setEnabled(false);
                    time.setEnabled(false);
                    checkBoxCT.setEnabled(false);
                }
                else {
                    buttonDelete.setVisibility(View.GONE);
                }
            }
            else{
                checkBoxCT.setVisibility(View.GONE);
                buttonDelete.setVisibility(View.GONE);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Crea el menú de acciones de la toolbar de la aplicación
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        if(tareaActual!=null){
            if(tareaActual.isEstado()){
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.tareas_completadas, menu);
                return super.onCreateOptionsMenu(menu);
            }
            else {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.crear_tarea_actions, menu);
                return super.onCreateOptionsMenu(menu);
            }
        }
        else{
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.crear_tarea_actions, menu);
            return super.onCreateOptionsMenu(menu);
        }
    }

    /**
     * Muestra el fragment para la selección de la fecha
     * @param v
     */
    public void showDatePickerDialog(View v) {
        fechaFragment newFragment = new fechaFragment();
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.date_picker));
    }

    /**
     * Obtiene la fecha ingresada por el usuario en el fragment
     * @param year
     * @param month
     * @param day
     */
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (day_string + "/" + month_string + "/" + year_string);
        dateF.setText(dateMessage);
    }

    /**
     * Muestra el picker para la seleccion de la hora
     * @param v
     */
    public void showTimePickerDialog(View v) {
        horaFragment newFragment = new horaFragment();//Crea una instancia del Fragment del tipo Time
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.time_picker));//Muestra el fragment en pantalla
    }

    /**
     * Obtiene la hora ingresado por el usuario en el fragment
     * @param hourOfDay
     * @param minute
     */
    public void processTimePickerResult(int hourOfDay, int minute) {
        String hour_string = Integer.toString(hourOfDay);//Crea un string en el cual se almacena la hora ingresada
        String minute_string;//Crea un string para guardar los minutos ingresados
        if(minute <10){//Verifica si los minutos ingresados son menores a 10
            minute_string = "0"+Integer.toString(minute);//concatena un cero antes del minuto ingresado y lo guarda
        }
        else{//si es mayor o igual a diez
            minute_string = Integer.toString(minute);//guarda los minutos ingresados
        }
        time.setText(hour_string +":"+ minute_string);//concatena la hora y los minutos ingresados
    }

    /**
     * Verifica cual item de la toolbar fue clickeado y comienza el evento correspondiente
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.completado:
                guardarTarea();
                return true;
            case R.id.descartar:
                cerrarVentana();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo en el cual se ejecuta el query que almacena o modifica las tareas del usuario
     */
    private void guardarTarea(){
        try{
            ConexionSQLiteHelper con = new ConexionSQLiteHelper(this, "bd_tareas", null, 1);
            SQLiteDatabase db = con.getWritableDatabase();
            if(tareaActual==null){//si la tareaActual es NULL, significa que se desea crear una nueva tarea
                if(verificarCampos()){
                    ContentValues values = new ContentValues();
                    values.put(utilidades.CAMPO_TITULO, title.getText().toString());
                    values.put(utilidades.CAMPO_DESCRIPCION, description.getText().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date date = new Date();
                    String fecha = dateFormat.format(date);
                    values.put(utilidades.CAMPO_FECHAC, fecha);
                    values.put(utilidades.CAMPO_FECHAF, dateF.getText().toString());
                    values.put(utilidades.CAMPO_HORAR, time.getText().toString());
                    values.put(utilidades.CAMPO_ESTADO,"false");
                    Long idResultante = db.insert(utilidades.TABLA_TAREA, utilidades.CAMPO_ID, values);
                    db.close();
                    Toast.makeText(this,"Tarea creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
            else{//Si tareaActual es distinto de null, significa que se desea modificar una tarea, la cual es tareaActual
                if(tareaActual.getDescripcion().equals(description.getText().toString()) &&
                        tareaActual.getTitulo().equals(title.getText().toString()) &&
                        tareaActual.getFechaFinalizacion().equals(dateF.getText().toString()) &&
                        tareaActual.getHoraRecordatorio().equals(time.getText().toString()) &&
                        !checkBoxCT.isChecked()){//Verifica que algún campo halla sido modificado
                    Toast.makeText(this,"Debe realizar cambios para editar la tarea", Toast.LENGTH_LONG).show();
                }
                else{//Si algún campo es distinto, se puede modificar
                    if(checkBoxCT.isChecked() && verificarCampos()){//verifica si se desea finalizar la tarea y que ningun campo es vacio, si ambas son TRUE, finaliza la tarea
                        db.execSQL("UPDATE "+utilidades.TABLA_TAREA+" SET "+utilidades.CAMPO_ESTADO+"='true', "+utilidades.CAMPO_FECHAC+"="+utilidades.CAMPO_FECHAF+" WHERE "+utilidades.CAMPO_ID+"="+tareaActual.getId()+"");
                        db.close();
                        Toast.makeText(this,"Tarea finalizada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    else if(!checkBoxCT.isChecked() && verificarCampos()){//verifica que ningun campo es vacio, y edita la tarea
                        db.execSQL("UPDATE "+utilidades.TABLA_TAREA+" SET "+utilidades.CAMPO_TITULO+"='"+title.getText()+"', " +
                                ""+utilidades.CAMPO_DESCRIPCION+"='"+description.getText()+"', "+utilidades.CAMPO_FECHAF+"='"+dateF.getText()+"'," +
                                " "+utilidades.CAMPO_HORAR+"='"+time.getText()+"' WHERE "+utilidades.CAMPO_ID+"="+tareaActual.getId()+"");
                        db.close();
                        Toast.makeText(this,"Tarea actualizada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }

            }
        }
        catch (Exception e){
            System.out.println(e);
            Toast.makeText(this, "Ha ocurrido un error al crear la tarea, por favor intente más tarde",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Verifica que ningún campo de la tarea es vacio
     * @return
     */
    private boolean verificarCampos(){
        if(title.getText().toString().equals("") || description.getText().toString().equals("") || dateF.getText().toString().equals("") || time.getText().toString().equals("")){
            Toast.makeText(this, "Alguno de los campos es inválido, no deben existir campos inválidos", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Finaliza la actividad actual
     */
    private void cerrarVentana(){
        finish();
    }

    /**
     * Elimina la tareaActual
     * @param view
     */
    public void bBorrarTarea(View view) {
        try{
            ConexionSQLiteHelper con = new ConexionSQLiteHelper(this, "bd_tareas", null, 1);
            SQLiteDatabase db = con.getWritableDatabase();
            db.execSQL("DELETE FROM "+utilidades.TABLA_TAREA+" WHERE "+utilidades.CAMPO_ID+"="+tareaActual.getId()+"");
            db.close();
            Toast.makeText(this,"Tarea eliminada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        catch (Exception e){
            System.out.println("Excepcion: "+e);
            Toast.makeText(this, "Ha ocurrido un error al borrar la tarea, por favor intente más tarde",Toast.LENGTH_LONG).show();
        }
    }
}
