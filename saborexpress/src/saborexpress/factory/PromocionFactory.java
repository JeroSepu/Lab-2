package saborexpress.factory;

import saborexpress.promo.*;

/** FACTORY: marketing pide una promoción por nombre y valor; aquí se decide qué clase concreta crear. */
public final class PromocionFactory {
    private PromocionFactory() {}

    public static Promocion crear(String tipo, double... valores) {
        return switch (tipo.toUpperCase()) {
            case "PORCENTAJE" -> new DescuentoPorcentaje(valores[0]);
            case "MONTO_MINIMO" -> new DescuentoPorMontoMinimo(valores[0], valores[1]);
            case "2X1_BEBIDAS" -> new SegundaBebidaGratis();
            case "NINGUNA" -> new SinPromocion();
            default -> throw new IllegalArgumentException("Promoción desconocida: " + tipo);
        };
    }
}
