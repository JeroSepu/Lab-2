package saborexpress;

import java.time.LocalDateTime;
import java.util.*;
import saborexpress.factory.ProductoFactory;
import saborexpress.factory.PromocionFactory;
import saborexpress.model.*;
import saborexpress.observer.*;
import saborexpress.promo.Promocion;
import saborexpress.repo.Repositorio;
import saborexpress.service.ConsultasService;
import saborexpress.sorting.Comparadores;
import saborexpress.util.Calculos;

public class Main {
    public static void main(String[] args) {
        // Un solo tipo de repositorio, cuatro usos (condición 4)
        Repositorio<String, Cliente> clientes = new Repositorio<>();
        Repositorio<String, Producto> menu = new Repositorio<>();
        Repositorio<String, Repartidor> repartidores = new Repositorio<>();
        Repositorio<Integer, Pedido> pedidos = new Repositorio<>();

        // ---------- 1. Menú (Factory) ----------
        Plato bandeja = ProductoFactory.plato("P1", "Bandeja paisa", 32000);
        Plato pasta = ProductoFactory.plato("P2", "Pasta carbonara", 28000);
        Plato ensalada = ProductoFactory.plato("P3", "Ensalada César", 21000);
        Plato hamburguesa = ProductoFactory.plato("P4", "Hamburguesa", 28000);
        Bebida limonada = ProductoFactory.bebida("B1", "Limonada", 8000, 400);
        Bebida cola = ProductoFactory.bebida("B2", "Gaseosa", 5000, 350);
        Combo comboBurger = ProductoFactory.combo("C1", "Combo Hamburguesa", 10, hamburguesa, cola);
        List.of(bandeja, pasta, ensalada, hamburguesa, limonada, cola, comboBurger).forEach(menu::guardar);

        System.out.println("=== MENÚ ordenado por precio desc y luego nombre ===");
        menu.ordenados(Comparadores.PRECIO_DESC_Y_NOMBRE).forEach(p -> System.out.println("  " + p));

        System.out.println("\n=== MENÚ con criterios combinados dinámicamente (categoría, luego nombre) ===");
        Comparator<Producto> porCategoria = Comparator.comparing(Producto::categoria);
        menu.ordenados(Comparadores.combinar(List.of(porCategoria, Comparadores.POR_NOMBRE)))
            .forEach(p -> System.out.println("  " + p));

        // ---------- 2. Clientes sin duplicados ----------
        Cliente porApp = clientes.guardarSiAusente(new Cliente("1001", "Ana Gómez", Canal.APP));
        Cliente porWhatsapp = clientes.guardarSiAusente(new Cliente("1001", "Ana Gómez", Canal.WHATSAPP));
        Cliente porTelefono = clientes.guardarSiAusente(new Cliente("1001", "Ana G.", Canal.TELEFONO));
        Cliente luis = clientes.guardarSiAusente(new Cliente("2002", "Luis Pérez", Canal.TELEFONO));
        System.out.println("\n=== CLIENTES ===");
        System.out.println("Registros intentados: 4 | Clientes reales: " + clientes.tamano()
                + " | ¿Ana es la misma en los 3 canales? " + (porApp == porWhatsapp && porApp == porTelefono));

        // búsqueda sin null
        System.out.println("Buscar 1001: " + clientes.buscarPorId("1001").map(Cliente::getNombre).orElse("no existe"));
        System.out.println("Buscar 9999: " + clientes.buscarPorId("9999").map(Cliente::getNombre).orElse("no existe"));

        // ---------- 3. Repartidores ----------
        Repartidor r1 = new Repartidor("R1", "Carlos"), r2 = new Repartidor("R2", "Marta"), r3 = new Repartidor("R3", "Pedro");
        for (int c : new int[]{5, 5, 4}) r1.registrarEntrega(c);
        for (int c : new int[]{5, 5, 5, 5}) r2.registrarEntrega(c);
        for (int c : new int[]{3, 4}) r3.registrarEntrega(c);
        List.of(r1, r2, r3).forEach(repartidores::guardar);

        // ---------- 4. Pedidos con promociones (Strategy + Factory) ----------
        Promocion dosPorUno = PromocionFactory.crear("2X1_BEBIDAS");
        Promocion diezPorCiento = PromocionFactory.crear("PORCENTAJE", 10);
        // Promoción NUEVA creada con una lambda, SIN modificar Pedido (condición 8)
        Promocion cincoMilSiPasaDe60 = (items, subtotal) -> subtotal > 60000 ? 5000 : 0;

        Pedido p1 = nuevoPedido(pedidos, 1, porApp, "Sede Rionegro", diezPorCiento);
        p1.agregarItem(bandeja, 2); p1.agregarItem(limonada, 1);
        Pedido p2 = nuevoPedido(pedidos, 2, luis, "Sede Medellín", dosPorUno);
        p2.agregarItem(pasta, 1); p2.agregarItem(limonada, 2); p2.agregarItem(cola, 1);
        Pedido p3 = nuevoPedido(pedidos, 3, porApp, "Sede Rionegro", cincoMilSiPasaDe60);
        p3.agregarItem(comboBurger, 3); p3.agregarItem(pasta, 1);
        Pedido p4 = nuevoPedido(pedidos, 4, luis, "Sede Bello", PromocionFactory.crear("NINGUNA"));
        p4.agregarItem(ensalada, 1);

        System.out.println("\n=== TOTALES CON PROMOCIÓN ===");
        pedidos.ordenados(Comparator.comparing(Pedido::getId)).forEach(p ->
                System.out.printf("  Pedido #%d  subtotal $%,.0f  descuento $%,.0f  total $%,.0f%n",
                        p.getId(), p.subtotal(), p.descuento(), p.total()));

        // ---------- 5. Observer ----------
        System.out.println("\n=== CAMBIOS DE ESTADO (Observer) ===");
        p1.asignarRepartidor(r2);
        p1.agregarObservador((ped, ant, nue) ->   // interesado NUEVO (auditoría) sin tocar Pedido (condición 9)
                System.out.printf("   [AUDITORÍA] #%d %s -> %s%n", ped.getId(), ant, nue));
        p1.cambiarEstado(EstadoPedido.EN_COCINA);
        p1.cambiarEstado(EstadoPedido.LISTO);
        p1.cambiarEstado(EstadoPedido.EN_CAMINO);
        p1.cambiarEstado(EstadoPedido.ENTREGADO);
        p2.cambiarEstado(EstadoPedido.EN_COCINA);
        p3.cambiarEstado(EstadoPedido.CANCELADO);
        p4.cambiarEstado(EstadoPedido.EN_COCINA);
        try { p1.cambiarEstado(EstadoPedido.EN_COCINA); }
        catch (IllegalStateException e) { System.out.println("   Error controlado: " + e.getMessage()); }

        // ---------- 6. Consultas con Streams ----------
        ConsultasService q = new ConsultasService(pedidos, repartidores);
        System.out.println("\n=== CONSULTAS ===");
        System.out.println("Top 3 platos de la semana: " + q.topPlatos(3, LocalDateTime.now().minusDays(7)));
        System.out.println("Facturación por sede: " + q.facturacionPorSede());
        System.out.println("Pedido más caro de Ana: " + q.pedidoMasCaroDe("1001").map(p -> "#" + p.getId() + " $" + (long) p.total()).orElse("ninguno"));
        System.out.println("Pedido más caro de 9999: " + q.pedidoMasCaroDe("9999").map(p -> "#" + p.getId()).orElse("ninguno"));
        System.out.println("Mejores repartidores: " + q.mejoresRepartidores(3));
        System.out.println("Menú de más de 20.000: " + q.nombres(q.filtrarMenu(menu.todos(), p -> p.getPrecio() > 20000)));
        System.out.println("Promedio por categoría: " + q.precioPromedioPorCategoria(menu.todos()));
        System.out.println("Pedidos por estado: " + q.pedidosPorEstado());

        // ---------- 7. Genéricos con wildcards ----------
        System.out.println("\n=== GENÉRICOS CON WILDCARDS ===");
        List<Plato> platos = List.of(bandeja, pasta, ensalada);
        List<Bebida> bebidas = List.of(limonada, cola);
        System.out.println("Suma platos: " + Calculos.sumarPrecios(platos) + " | Suma bebidas: " + Calculos.sumarPrecios(bebidas)
                + " | Suma menú completo: " + Calculos.sumarPrecios(menu.todos()));
        System.out.println("Plato más caro: " + Calculos.masCaro(platos).map(Producto::getNombre).orElse("-"));
        System.out.println("Bebida más cara en lista vacía: " + Calculos.masCaro(new ArrayList<Bebida>()).map(Producto::getNombre).orElse("no hay"));
        List<Producto> todos = new ArrayList<>();
        Calculos.copiar(platos, todos);      // origen List<Plato>, destino List<Producto>
        Calculos.copiar(bebidas, todos);
        System.out.println("Copiados a List<Producto>: " + todos.size());
        System.out.println("Agrupados por categoría: " + Calculos.agrupar(todos, Producto::categoria).keySet());
    }

    /** Crea el pedido, le conecta los observadores estándar y lo guarda. */
    private static Pedido nuevoPedido(Repositorio<Integer, Pedido> repo, int id, Cliente c, String sede, Promocion promo) {
        Pedido p = new Pedido(id, c, sede, promo);
        p.agregarObservador(new CocinaObservador());
        p.agregarObservador(new ClienteObservador());
        p.agregarObservador(new RepartidorObservador());
        repo.guardar(p);
        return p;
    }
}
