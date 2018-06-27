package android.example.com.tareaspendientes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.com.tareaspendientes.BD.ConexionSQLiteHelper;
import android.example.com.tareaspendientes.BD.tareasBD;
import android.example.com.tareaspendientes.utilidades.utilidades;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de manejar los recyclerView y agregar nuevos elementos
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    //Variables y elementos de la UI
    List<tareasBD> tareas;
    int idTarea;

    /**
     * Constructor
     * @param persons
     */
    RVAdapter(List<tareasBD> persons){//constructor que recibe la lista de productos
        this.tareas = persons;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plantilla_tarjetas, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    /**
     * Asigna los datos al cardView
     * @param personViewHolder
     * @param i
     */
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {//asigna los valores al cardview producto a producto
        personViewHolder.id = tareas.get(i).getId();
        personViewHolder.titulo.setText(tareas.get(i).getTitulo());
        personViewHolder.descripcion.setText("Descripción: "+tareas.get(i).getDescripcion()+"");
        personViewHolder.fechaC.setText("Fecha de creación: "+tareas.get(i).getFechaCreacion());
        personViewHolder.fechaF.setText("Fecha de culminación: "+tareas.get(i).getFechaFinalizacion());
        personViewHolder.horaR.setText("Hora de recordatorio: "+tareas.get(i).getHoraRecordatorio());
        personViewHolder.check.setChecked(tareas.get(i).isEstado());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public int getItemCount() {//cuenta cuantas tareas tiene la lista
        return tareas.size();
    }

    //ViewHolder
    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView titulo;
        TextView descripcion;
        TextView fechaC;
        TextView fechaF;
        TextView horaR;
        CheckBox check;
        int id;
        ConexionSQLiteHelper con;

        /**
         * Instancia los elementos de la UI
         * @param itemView
         */
        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardview);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            descripcion = (TextView) itemView.findViewById(R.id.descripcion);
            fechaC = (TextView) itemView.findViewById(R.id.fechaCreacion);
            fechaF = (TextView) itemView.findViewById(R.id.fechaFinalizacion);
            fechaC = (TextView) itemView.findViewById(R.id.fechaCreacion);
            horaR = (TextView) itemView.findViewById(R.id.estado);
            check = (CheckBox) itemView.findViewById(R.id.checkBox);
            itemView.setOnClickListener(this);
        }

        /**
         * Obtiene el elemento cardView al cual se le realizo el tap
         * @param v
         */
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent editar = new Intent( context, CrearTarea.class);
            Bundle bundle = new Bundle();
            List<String> item = new ArrayList<>();
            con = new ConexionSQLiteHelper(context, "bd_tareas", null, 1);
            SQLiteDatabase db = con.getReadableDatabase();
            Cursor cursor = db.rawQuery(" SELECT "+utilidades.CAMPO_ID+", "+ utilidades.CAMPO_TITULO +", " +
                            "" + utilidades.CAMPO_DESCRIPCION + ", "+utilidades.CAMPO_FECHAC+", " + utilidades.CAMPO_FECHAF + ", " +
                            "" + utilidades.CAMPO_HORAR + ", "+utilidades.CAMPO_ESTADO+" FROM " + utilidades.TABLA_TAREA + " WHERE "+utilidades.CAMPO_ID+"="+id+"",
                    null);

            while (cursor.moveToNext()) {
                item.add(cursor.getString(0));
                item.add(cursor.getString(1));
                item.add(cursor.getString(2));
                item.add(cursor.getString(3));
                item.add(cursor.getString(4));
                item.add(cursor.getString(5));
                item.add(cursor.getString(6));
            }
            bundle.putSerializable("tareaOriginal", (Serializable) item);
            editar.putExtras(bundle);
            context.startActivity(editar);
        }


    }

}
