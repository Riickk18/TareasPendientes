package android.example.com.tareaspendientes.BD;

/**
 * Clase Tarea, la cual modela las tareas almacenadas en la bd
 */
public class tareasBD {

    //Variables
    private int id;
    private String titulo;
    private String descripcion;
    private String fechaCreacion;
    private String fechaFinalizacion;
    private String horaRecordatorio;
    private boolean estado;

    /**
     * Contructor
     * @param id
     * @param titulo
     * @param descripcion
     * @param fechaCreacion
     * @param fechaFinalizacion
     * @param horaRecordatorio
     * @param estado
     */
    public tareasBD(int id, String titulo, String descripcion, String fechaCreacion, String fechaFinalizacion, String horaRecordatorio, boolean estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.horaRecordatorio = horaRecordatorio;
        this.estado = estado;
    }

    //Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(String fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getHoraRecordatorio() {
        return horaRecordatorio;
    }

    public void setHoraRecordatorio(String horaRecordatorio) {
        this.horaRecordatorio = horaRecordatorio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
