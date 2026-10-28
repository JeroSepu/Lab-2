package saborexpress.repo;

import saborexpress.model.Entidad;

import java.util.*;
import java.util.function.Predicate;

/**
 * Una sola implementación para Cliente, Producto, Repartidor y Pedido (condición 4).
 * Internamente es un HashMap: buscar por id es O(1) promedio (condición 2) y,
 * como la llave del cliente es su documento, no puede haber duplicados (condición 1).
 */
public class Repositorio<K, T extends Entidad<K>> {
    private final Map<K, T> datos = new HashMap<>();

    /** Guarda o reemplaza. */
    public void guardar(T e) { datos.put(e.getId(), e); }

    /** Si ya existía uno con ese id, devuelve el existente y NO guarda el nuevo. */
    public T guardarSiAusente(T e) { return datos.computeIfAbsent(e.getId(), k -> e); }

    /** Nunca retorna null: retorna Optional (condición 7). */
    public Optional<T> buscarPorId(K id) { return Optional.ofNullable(datos.get(id)); }

    public Collection<T> todos() { return Collections.unmodifiableCollection(datos.values()); }
    public int tamano() { return datos.size(); }

    /** Comparator<? super T>: sirve un comparador de T o de cualquier supertipo de T. */
    public List<T> ordenados(Comparator<? super T> criterio) {
        List<T> lista = new ArrayList<>(datos.values());
        lista.sort(criterio);
        return lista;
    }

    public List<T> filtrar(Predicate<? super T> condicion) {
        return datos.values().stream().filter(condicion).toList();
    }
}
