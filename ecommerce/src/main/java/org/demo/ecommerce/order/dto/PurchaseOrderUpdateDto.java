package org.demo.ecommerce.order.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class PurchaseOrderUpdateDto {

    @NotNull
    private Long id;
    @NotBlank
    private String email;
    @NotNull
    private Set<ProductDto> products;


}
