package saborexpress.model;

/** Contrato mínimo de todo lo que se guarda en un repositorio: tener un identificador de tipo K. (ISP) */
public interface Entidad<K> {
    K getId();
}
