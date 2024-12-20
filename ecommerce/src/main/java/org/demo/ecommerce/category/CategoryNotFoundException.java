package org.demo.ecommerce.category;

import org.demo.ecommerce.commons.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
