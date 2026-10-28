package saborexpress.model;

import java.util.Objects;

/** Clase base de Plato, Bebida y Combo. Cualquiera se puede usar donde se pida un Producto (LSP). */
public abstract class Producto implements Entidad<String>, Comparable<Producto> {
    private final String id;
    private final String nombre;
    private final double precio;

    protected Producto(String id, String nombre, double precio) {
        if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo");
        this.id = Objects.requireNonNull(id);
        this.nombre = Objects.requireNonNull(nombre);
        this.precio = precio;
    }

    public abstract String categoria();

    @Override public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    /** Orden natural: por nombre. */
    @Override public int compareTo(Producto o) { return nombre.compareTo(o.nombre); }
    @Override public String toString() { return String.format("%s [%s] $%,.0f", nombre, categoria(), getPrecio()); }
}
