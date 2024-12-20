package org.demo.ecommerce.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {

    private Long id;
    @NotBlank
    private String name;
    private Long parentId;

}
