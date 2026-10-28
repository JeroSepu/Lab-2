package saborexpress.util;

import saborexpress.model.Producto;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Métodos genéricos con wildcards: sirven para List<Plato>, List<Bebida>, List<Combo>... (condición 5). */
public final class Calculos {
    private Calculos() {}

    /** ? extends Producto: acepta colecciones de CUALQUIER subtipo de Producto (solo lectura). */
    public static double sumarPrecios(Collection<? extends Producto> productos) {
        double suma = 0;
        for (Producto p : productos) suma += p.getPrecio();
        return suma;
    }

    public static double promedioPrecio(Collection<? extends Producto> productos) {
        return productos.isEmpty() ? 0 : sumarPrecios(productos) / productos.size();
    }

    /** Producto más caro; Optional en vez de null si la colección está vacía (condición 7). */
    public static <T extends Producto> Optional<T> masCaro(Collection<? extends T> productos) {
        T mejor = null;
        for (T p : productos) if (mejor == null || p.getPrecio() > mejor.getPrecio()) mejor = p;
        return Optional.ofNullable(mejor);
    }

    /** ? extends T en el origen (se lee) y ? super T en el destino (se escribe): regla PECS. */
    public static <T> void copiar(Collection<? extends T> origen, Collection<? super T> destino) {
        destino.addAll(origen);
    }

    public static <T, K> Map<K, List<T>> agrupar(Collection<? extends T> datos, Function<? super T, ? extends K> clave) {
        return datos.stream().collect(Collectors.groupingBy(clave, TreeMap::new, Collectors.toList()));
    }
}
