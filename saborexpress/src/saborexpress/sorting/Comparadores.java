package saborexpress.sorting;

import saborexpress.model.Producto;
import saborexpress.model.Repartidor;

import java.util.Comparator;
import java.util.List;

/** Criterios de ordenamiento reutilizables y combinables (condición 3). */
public final class Comparadores {
    private Comparadores() {}

    // ---- Productos ----
    public static final Comparator<Producto> POR_NOMBRE = Comparator.comparing(Producto::getNombre);
    public static final Comparator<Producto> POR_PRECIO_ASC = Comparator.comparingDouble(Producto::getPrecio);
    public static final Comparator<Producto> POR_PRECIO_DESC = POR_PRECIO_ASC.reversed();
    /** Precio descendente y, si empatan, por nombre. */
    public static final Comparator<Producto> PRECIO_DESC_Y_NOMBRE = POR_PRECIO_DESC.thenComparing(POR_NOMBRE);

    // ---- Repartidores ----
    public static final Comparator<Repartidor> POR_CALIFICACION_DESC =
            Comparator.comparingDouble(Repartidor::getCalificacion).reversed();
    public static final Comparator<Repartidor> POR_ENTREGAS_DESC =
            Comparator.comparingInt(Repartidor::getEntregas).reversed();
    public static final Comparator<Repartidor> MEJORES =
            POR_CALIFICACION_DESC.thenComparing(POR_ENTREGAS_DESC).thenComparing(Repartidor::getNombre);

    /** Combina cualquier lista de criterios en uno solo, en el orden dado. */
    public static <T> Comparator<T> combinar(List<? extends Comparator<? super T>> criterios) {
        Comparator<T> resultado = (a, b) -> 0;
        for (Comparator<? super T> c : criterios) resultado = resultado.thenComparing(c);
        return resultado;
    }
}
