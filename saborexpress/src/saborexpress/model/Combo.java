package saborexpress.model;

import java.util.List;

/** Combo = varios productos con un porcentaje de descuento. Su precio se calcula. */
public class Combo extends Producto {
    private final List<Producto> items;
    private final double descuentoPct;

    public Combo(String id, String nombre, double descuentoPct, List<? extends Producto> items) {
        super(id, nombre, 0);
        this.items = List.copyOf(items);
        this.descuentoPct = descuentoPct;
    }

    public List<Producto> getItems() { return items; }

    @Override public double getPrecio() {
        double suma = items.stream().mapToDouble(Producto::getPrecio).sum();
        return suma * (1 - descuentoPct / 100.0);
    }
    @Override public String categoria() { return "COMBO"; }
}
