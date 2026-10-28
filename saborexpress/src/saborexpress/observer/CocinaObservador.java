package saborexpress.observer;

import saborexpress.model.EstadoPedido;
import saborexpress.model.Pedido;

public class CocinaObservador implements ObservadorPedido {
    @Override public void actualizar(Pedido p, EstadoPedido anterior, EstadoPedido nuevo) {
        if (nuevo == EstadoPedido.EN_COCINA)
            System.out.printf("   [COCINA %s] Preparar pedido #%d (%d ítems)%n", p.getSede(), p.getId(), p.getItems().size());
        else if (nuevo == EstadoPedido.CANCELADO)
            System.out.printf("   [COCINA %s] Detener pedido #%d: cancelado%n", p.getSede(), p.getId());
    }
}
