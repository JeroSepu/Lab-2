package saborexpress.observer;

import saborexpress.model.EstadoPedido;
import saborexpress.model.Pedido;

public class RepartidorObservador implements ObservadorPedido {
    @Override public void actualizar(Pedido p, EstadoPedido anterior, EstadoPedido nuevo) {
        if (nuevo != EstadoPedido.LISTO && nuevo != EstadoPedido.EN_CAMINO) return;
        // Optional.ifPresent: si no hay repartidor asignado simplemente no pasa nada (sin null)
        p.getRepartidor().ifPresent(r ->
                System.out.printf("   [REPARTIDOR %s] Pedido #%d está %s%n", r.getNombre(), p.getId(), nuevo));
    }
}
