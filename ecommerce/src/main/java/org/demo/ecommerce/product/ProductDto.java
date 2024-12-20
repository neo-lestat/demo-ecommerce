package org.demo.ecommerce.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDto {

    private Long id;
    @NotBlank
    private String name;
    private Double price;
    private Integer stock;
    private String imageUrl;
    @NotNull
    @Min(1)
    private Long categoryId;

}
