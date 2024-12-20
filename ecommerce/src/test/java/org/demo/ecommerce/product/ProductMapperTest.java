package org.demo.ecommerce.product;


import org.demo.ecommerce.model.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper productMapper = new ProductMapper();

    @Test
    void testMapFromProductDto() {
        ProductDto categoryDto = buildProductCreateDto();
        Product product = productMapper.fromProductDto(categoryDto);
        assertEquals(2L, product.getCategory().getId());
        assertThat(product)
                .hasFieldOrPropertyWithValue("name", "pizza")
                .hasFieldOrPropertyWithValue("stock", 50)
                .hasFieldOrPropertyWithValue("price", 5d)
                .hasFieldOrPropertyWithValue("imageUrl", "url");
    }

    private ProductDto buildProductCreateDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("pizza");
        productDto.setStock(50);
        productDto.setPrice(5d);
        productDto.setImageUrl("url");
        productDto.setCategoryId(2L);
        return productDto;
    }
}
