package org.demo.ecommerce.product;

import org.demo.ecommerce.commons.EntityNotFoundException;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
