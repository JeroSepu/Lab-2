package saborexpress.factory;

import saborexpress.model.*;

import java.util.List;

/** FACTORY: centraliza la creación de productos; el resto del código no usa "new Plato(...)" directamente. */
public final class ProductoFactory {
    private ProductoFactory() {}

    public static Plato plato(String id, String nombre, double precio) { return new Plato(id, nombre, precio); }

    public static Bebida bebida(String id, String nombre, double precio, int ml) {
        return new Bebida(id, nombre, precio, ml);
    }

    public static Combo combo(String id, String nombre, double descuentoPct, Producto... items) {
        return new Combo(id, nombre, descuentoPct, List.of(items));
    }
}
