package org.demo.ecommerce.commons;

import org.demo.ecommerce.category.CategoryNotFoundException;
import org.demo.ecommerce.order.exception.ProductWithoutEnoughStockException;
import org.demo.ecommerce.order.exception.PurchaseOrderBusinessException;
import org.demo.ecommerce.order.exception.PurchaseOrderNotFoundException;
import org.demo.ecommerce.order.exception.UpdateStockException;
import org.demo.ecommerce.product.ProductNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleDataIntegrity(DataIntegrityViolationException ex) {
        //we could log the exception
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler({CategoryNotFoundException.class, PurchaseOrderNotFoundException.class, ProductNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler({ProductWithoutEnoughStockException.class, UpdateStockException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handlePurchaseOrderBusinessException(PurchaseOrderBusinessException ex) {
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    public record ErrorMessage(int statusCode, String message) { }

}
