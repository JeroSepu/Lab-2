package saborexpress.model;

public class Plato extends Producto {
    public Plato(String id, String nombre, double precio) { super(id, nombre, precio); }
    @Override public String categoria() { return "PLATO"; }
}
