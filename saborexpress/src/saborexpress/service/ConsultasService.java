package saborexpress.service;

import saborexpress.model.*;
import saborexpress.repo.Repositorio;
import saborexpress.sorting.Comparadores;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Consultas dinámicas con Streams: filtrar, transformar, agrupar y resumir (condición 6). */
public class ConsultasService {
    private final Repositorio<Integer, Pedido> pedidos;
    private final Repositorio<String, Repartidor> repartidores;

    public ConsultasService(Repositorio<Integer, Pedido> pedidos, Repositorio<String, Repartidor> repartidores) {
        this.pedidos = pedidos;
        this.repartidores = repartidores;
    }

    /** Top N platos más vendidos desde una fecha (ej. la semana). filter -> flatMap -> groupingBy -> sorted -> limit */
    public List<Map.Entry<String, Integer>> topPlatos(int n, LocalDateTime desde) {
        return pedidos.todos().stream()
                .filter(p -> p.getEstado() != EstadoPedido.CANCELADO)
                .filter(p -> !p.getFecha().isBefore(desde))
                .flatMap(p -> p.getItems().stream())
                .filter(i -> i.getProducto() instanceof Plato)
                .collect(Collectors.groupingBy(i -> i.getProducto().getNombre(),
                        Collectors.summingInt(ItemPedido::getCantidad)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .toList();
    }

    /** Facturación total por sede (sin contar cancelados). */
    public Map<String, Double> facturacionPorSede() {
        return pedidos.todos().stream()
                .filter(p -> p.getEstado() != EstadoPedido.CANCELADO)
                .collect(Collectors.groupingBy(Pedido::getSede, TreeMap::new,
                        Collectors.summingDouble(Pedido::total)));
    }

    /** Pedido más caro de un cliente. Optional: puede no existir (condición 7). */
    public Optional<Pedido> pedidoMasCaroDe(String documento) {
        return pedidos.todos().stream()
                .filter(p -> p.getCliente().getDocumento().equals(documento))
                .max(Comparator.comparingDouble(Pedido::total));
    }

    /** Mejores repartidores: calificación desc, luego entregas desc. */
    public List<Repartidor> mejoresRepartidores(int n) {
        return repartidores.todos().stream().sorted(Comparadores.MEJORES).limit(n).toList();
    }

    /** Filtro dinámico del menú: el criterio lo decide quien llama. */
    public List<Producto> filtrarMenu(Collection<? extends Producto> menu, Predicate<? super Producto> criterio) {
        return menu.stream().filter(criterio).collect(Collectors.toList());
    }

    /** Transformar: solo los nombres. */
    public List<String> nombres(Collection<? extends Producto> menu) {
        return menu.stream().map(Producto::getNombre).sorted().toList();
    }

    /** Resumir: precio promedio por categoría. */
    public Map<String, Double> precioPromedioPorCategoria(Collection<? extends Producto> menu) {
        return menu.stream().collect(Collectors.groupingBy(Producto::categoria, TreeMap::new,
                Collectors.averagingDouble(Producto::getPrecio)));
    }

    /** Cuántos pedidos hay en cada estado. */
    public Map<EstadoPedido, Long> pedidosPorEstado() {
        return pedidos.todos().stream()
                .collect(Collectors.groupingBy(Pedido::getEstado, TreeMap::new, Collectors.counting()));
    }
}
