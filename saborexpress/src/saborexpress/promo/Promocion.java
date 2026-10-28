package saborexpress.promo;

import saborexpress.model.ItemPedido;

import java.util.List;

/**
 * STRATEGY: cada promoción es una estrategia intercambiable que calcula cuánto se descuenta.
 * Agregar una nueva = crear una clase (o una lambda) sin tocar Pedido (condición 8).
 */
@FunctionalInterface
public interface Promocion {
    double descuento(List<ItemPedido> items, double subtotal);
}
