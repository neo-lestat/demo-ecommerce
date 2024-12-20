package org.demo.ecommerce.security;

public class ApiTokenNotValid extends RuntimeException {

    public ApiTokenNotValid(String message) {
        super(message);
    }

}

