package saborexpress.observer;

import saborexpress.model.EstadoPedido;
import saborexpress.model.Pedido;

public class ClienteObservador implements ObservadorPedido {
    @Override public void actualizar(Pedido p, EstadoPedido anterior, EstadoPedido nuevo) {
        System.out.printf("   [CLIENTE %s] Tu pedido #%d pasó de %s a %s%n",
                p.getCliente().getNombre(), p.getId(), anterior, nuevo);
    }
}
