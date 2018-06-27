package android.example.com.tareaspendientes.utilidades;

/**
 * Contiene las constantes que estan relacionadas con las columnas de la tabla de la BD
 */
public class utilidades {

    //Constantes
    public static final String TABLA_TAREA = "tarea";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_DESCRIPCION = "descripcion";
    public static final String CAMPO_FECHAC = "fechaC";
    public static final String CAMPO_FECHAF = "fechaF";
    public static final String CAMPO_HORAR = "horaR";
    public static final String CAMPO_ESTADO = "estado";

    //Query que genera la tabla en la base de datos
    public static final String CREAR_TABLA_TAREA = "CREATE TABLE "+ TABLA_TAREA +" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ""+CAMPO_TITULO+" TEXT, "+CAMPO_DESCRIPCION+" TEXT, "+CAMPO_FECHAC+" TEXT, "+CAMPO_FECHAF+" TEXT, "+CAMPO_HORAR+" TEXT, "+CAMPO_ESTADO+" TEXT)";
}
