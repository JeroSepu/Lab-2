 SaborExpress. – Sistema de Gestión de Pedidos
Laboratorio 2 

Participantes
- Jeronimo (completar con apellido)

Colecciones 
- `Repositorio` usa `HashMap<K,T>`: buscar por id es O(1) promedio (condición 2).
- La llave del cliente es el documento, así que no hay duplicados aunque vengan por app, WhatsApp o teléfono (condición 1). `guardarSiAusente` devuelve el cliente que ya existía.
- `Pedido` usa `ArrayList` para ítems y observadores; `Combo` usa `List.copyOf` (inmutable); los resultados agrupados usan `TreeMap` para salida ordenada.

Ordenamiento 
- `Producto` implementa `Comparable` (orden natural por nombre).
- `Comparadores` define criterios con `Comparator.comparing...reversed().thenComparing(...)`, por ejemplo precio descendente y luego nombre.
- `Comparadores.combinar(...)` une cualquier lista de criterios en tiempo de ejecución (condición 3).

Genéricos con wildcards
- `Repositorio<K, T extends Entidad<K>>` evita repetir lógica para 4 entidades (condición 4).
- `Calculos.sumarPrecios(Collection<? extends Producto>)` sirve para platos, bebidas o combos (condición 5).
- `Calculos.copiar(Collection<? extends T>, Collection<? super T>)` aplica PECS.
- `Repositorio.ordenados(Comparator<? super T>)` y `filtrar(Predicate<? super T>)`.

Programación funcional 
- `ConsultasService`: `filter`, `map`, `flatMap`, `groupingBy`, `summingInt/Double`, `averagingDouble`, `counting`, `sorted`, `limit`, `max` (condición 6).
- `Optional` en lugar de `null` en búsquedas, `masCaro`, `pedidoMasCaroDe` y `Pedido.getRepartidor()` (condición 7).
- Interfaces funcionales (`Promocion`, `ObservadorPedido`) que se pueden crear con lambdas.

*SOLID
-cada clase tiene una sola razón de cambio (`Repositorio` guarda, `ConsultasService` consulta, `Comparadores` ordena, `Pedido` maneja su estado y total).
-nuevas promociones o nuevos observadores se agregan sin modificar `Pedido` (condiciones 8 y 9).
-`Plato`, `Bebida` y `Combo` se usan en cualquier lugar donde se espera `Producto`.
-interfaces pequeñas (`Entidad`, `Promocion`, `ObservadorPedido`).
-`Pedido` depende de abstracciones (`Promocion`, `ObservadorPedido`), no de clases concretas.

Bonus – patrones
- Strategy `Promocion` + `DescuentoPorcentaje`, `DescuentoPorMontoMinimo`, `SegundaBebidaGratis`, `SinPromocion`. En `Main` se crea una promoción nueva con una lambda.
- Observer `Pedido.cambiarEstado` avisa a todos los `ObservadorPedido` (cocina, cliente, repartidor). En `Main` se agrega un observador de auditoría con una lambda sin tocar `Pedido`.
- Factory `ProductoFactory` (plato, bebida, combo) y `PromocionFactory.crear("PORCENTAJE", 10)`.

 Decisiones
- El documento es el id del cliente: es la forma más simple y segura de impedir duplicados.
- Ningún método de búsqueda devuelve `null`; todo es `Optional`.
- El total se calcula como `subtotal - promocion.descuento(...)`, así `Pedido` no tiene `if-else` por promoción.
- El precio de un `Combo` se calcula a partir de sus productos, para que siempre esté actualizado.
