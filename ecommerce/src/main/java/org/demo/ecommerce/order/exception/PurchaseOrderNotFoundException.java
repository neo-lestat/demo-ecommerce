package org.demo.ecommerce.order.exception;

import org.demo.ecommerce.commons.EntityNotFoundException;

public class PurchaseOrderNotFoundException extends EntityNotFoundException {
    public PurchaseOrderNotFoundException(String message) {
        super(message);
    }
}
