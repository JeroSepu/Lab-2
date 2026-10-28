package saborexpress.promo;

import saborexpress.model.ItemPedido;
import java.util.List;

/** Si el subtotal llega al mínimo, se descuenta un valor fijo. */
public class DescuentoPorMontoMinimo implements Promocion {
    private final double minimo;
    private final double descuentoFijo;
    public DescuentoPorMontoMinimo(double minimo, double descuentoFijo) {
        this.minimo = minimo;
        this.descuentoFijo = descuentoFijo;
    }
    @Override public double descuento(List<ItemPedido> items, double subtotal) {
        return subtotal >= minimo ? descuentoFijo : 0;
    }
}
