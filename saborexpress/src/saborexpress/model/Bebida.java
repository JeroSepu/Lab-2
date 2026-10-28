package saborexpress.model;

public class Bebida extends Producto {
    private final int mililitros;

    public Bebida(String id, String nombre, double precio, int mililitros) {
        super(id, nombre, precio);
        this.mililitros = mililitros;
    }
    public int getMililitros() { return mililitros; }
    @Override public String categoria() { return "BEBIDA"; }
}
