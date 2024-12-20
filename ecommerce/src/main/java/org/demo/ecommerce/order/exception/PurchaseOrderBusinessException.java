package org.demo.ecommerce.order.exception;

public class PurchaseOrderBusinessException extends RuntimeException {
    public PurchaseOrderBusinessException(String message) {
        super(message);
    }
}
