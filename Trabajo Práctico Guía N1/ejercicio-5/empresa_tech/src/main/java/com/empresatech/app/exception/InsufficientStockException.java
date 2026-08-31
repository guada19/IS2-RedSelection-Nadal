package com.empresatech.app.exception;

/**
 * Excepción de negocio para señalar que la operación solicitada excede el
 * stock disponible del producto.
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
