package org.demo.ecommerce.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class PurchaseOrderCreateDto {

    @NotBlank
    private String seatLetter;
    @NotNull
    @Min(1)
    private Short seatNumber;

    private String email;

    private Set<ProductDto> products;


}
