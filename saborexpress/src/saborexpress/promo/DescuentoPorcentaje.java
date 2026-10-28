package saborexpress.promo;

import saborexpress.model.ItemPedido;
import java.util.List;

public class DescuentoPorcentaje implements Promocion {
    private final double porcentaje;
    public DescuentoPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }
    @Override public double descuento(List<ItemPedido> items, double subtotal) {
        return subtotal * porcentaje / 100.0;
    }
}
