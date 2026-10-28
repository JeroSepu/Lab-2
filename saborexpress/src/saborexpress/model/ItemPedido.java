package saborexpress.model;

public class ItemPedido {
    private final Producto producto;
    private final int cantidad;

    public ItemPedido(Producto producto, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser positiva");
        this.producto = producto;
        this.cantidad = cantidad;
    }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double subtotal() { return producto.getPrecio() * cantidad; }
}
