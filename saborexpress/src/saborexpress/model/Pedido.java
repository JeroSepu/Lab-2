package saborexpress.model;

import saborexpress.observer.ObservadorPedido;
import saborexpress.promo.Promocion;

import java.time.LocalDateTime;
import java.util.*;

/**
 * El pedido NO sabe qué promociones existen ni quién debe enterarse de los cambios:
 * solo conoce las abstracciones Promocion y ObservadorPedido (OCP + DIP).
 */
public class Pedido implements Entidad<Integer> {
    private final int id;
    private final Cliente cliente;
    private final String sede;
    private final LocalDateTime fecha;
    private final List<ItemPedido> items = new ArrayList<>();
    private final List<ObservadorPedido> observadores = new ArrayList<>();
    private Promocion promocion;
    private EstadoPedido estado = EstadoPedido.CREADO;
    private Repartidor repartidor; // nunca se expone como null: ver getRepartidor()

    public Pedido(int id, Cliente cliente, String sede, Promocion promocion) {
        this.id = id;
        this.cliente = Objects.requireNonNull(cliente);
        this.sede = Objects.requireNonNull(sede);
        this.promocion = Objects.requireNonNull(promocion);
        this.fecha = LocalDateTime.now();
    }

    // ---- contenido y total ----
    public void agregarItem(Producto p, int cantidad) { items.add(new ItemPedido(p, cantidad)); }
    public double subtotal() { return items.stream().mapToDouble(ItemPedido::subtotal).sum(); }
    public double descuento() { return promocion.descuento(items, subtotal()); }
    public double total() { return subtotal() - descuento(); }
    public void setPromocion(Promocion p) { this.promocion = Objects.requireNonNull(p); }

    // ---- Observer ----
    public void agregarObservador(ObservadorPedido o) { observadores.add(o); }

    public void cambiarEstado(EstadoPedido nuevo) {
        if (estado == EstadoPedido.ENTREGADO || estado == EstadoPedido.CANCELADO)
            throw new IllegalStateException("El pedido #" + id + " ya está cerrado (" + estado + ")");
        EstadoPedido anterior = estado;
        estado = nuevo;
        observadores.forEach(o -> o.actualizar(this, anterior, nuevo));
    }

    public void asignarRepartidor(Repartidor r) { this.repartidor = r; }
    public Optional<Repartidor> getRepartidor() { return Optional.ofNullable(repartidor); }

    @Override public Integer getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public String getSede() { return sede; }
    public LocalDateTime getFecha() { return fecha; }
    public EstadoPedido getEstado() { return estado; }
    public List<ItemPedido> getItems() { return Collections.unmodifiableList(items); }
}
