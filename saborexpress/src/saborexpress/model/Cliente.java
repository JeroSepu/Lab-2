package saborexpress.model;

import java.util.Objects;

/** El identificador del cliente ES su documento: dos clientes con el mismo documento son el mismo. */
public class Cliente implements Entidad<String> {
    private final String documento;
    private final String nombre;
    private final Canal canal;

    public Cliente(String documento, String nombre, Canal canal) {
        this.documento = Objects.requireNonNull(documento);
        this.nombre = Objects.requireNonNull(nombre);
        this.canal = Objects.requireNonNull(canal);
    }

    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public Canal getCanal() { return canal; }

    @Override public String getId() { return documento; }

    @Override public boolean equals(Object o) {
        return o instanceof Cliente c && documento.equals(c.documento);
    }
    @Override public int hashCode() { return documento.hashCode(); }
    @Override public String toString() { return nombre + " (" + documento + ", " + canal + ")"; }
}
