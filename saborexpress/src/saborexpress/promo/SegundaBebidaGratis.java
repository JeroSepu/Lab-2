package saborexpress.promo;

import saborexpress.model.Bebida;
import saborexpress.model.ItemPedido;

import java.util.*;

/** 2x1 en bebidas: de cada par, la más barata es gratis. */
public class SegundaBebidaGratis implements Promocion {
    @Override public double descuento(List<ItemPedido> items, double subtotal) {
        List<Double> precios = items.stream()
                .filter(i -> i.getProducto() instanceof Bebida)
                .flatMap(i -> Collections.nCopies(i.getCantidad(), i.getProducto().getPrecio()).stream())
                .sorted(Comparator.reverseOrder())
                .toList();
        double d = 0;
        for (int i = 1; i < precios.size(); i += 2) d += precios.get(i);
        return d;
    }
}
