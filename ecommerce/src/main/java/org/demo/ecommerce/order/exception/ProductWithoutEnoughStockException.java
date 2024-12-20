package org.demo.ecommerce.order.exception;


public class ProductWithoutEnoughStockException extends PurchaseOrderBusinessException {
    public ProductWithoutEnoughStockException(String message) {
        super(message);
    }
}
