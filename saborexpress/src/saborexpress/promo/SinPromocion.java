package saborexpress.promo;

import saborexpress.model.ItemPedido;
import java.util.List;

public class SinPromocion implements Promocion {
    @Override public double descuento(List<ItemPedido> items, double subtotal) { return 0; }
}
