package saborexpress.observer;

import saborexpress.model.EstadoPedido;
import saborexpress.model.Pedido;

/** OBSERVER: cualquier interesado en los cambios de estado implementa esta interfaz (condición 9). */
@FunctionalInterface
public interface ObservadorPedido {
    void actualizar(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo);
}
