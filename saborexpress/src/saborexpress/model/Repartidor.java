package saborexpress.model;

public class Repartidor implements Entidad<String> {
    private final String id;
    private final String nombre;
    private double sumaCalificaciones;
    private int entregas;

    public Repartidor(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void registrarEntrega(int calificacion) {
        if (calificacion < 1 || calificacion > 5) throw new IllegalArgumentException("Calificación entre 1 y 5");
        sumaCalificaciones += calificacion;
        entregas++;
    }

    public double getCalificacion() { return entregas == 0 ? 0 : sumaCalificaciones / entregas; }
    public int getEntregas() { return entregas; }
    public String getNombre() { return nombre; }
    @Override public String getId() { return id; }
    @Override public String toString() {
        return String.format("%s (calif %.2f, %d entregas)", nombre, getCalificacion(), entregas);
    }
}
