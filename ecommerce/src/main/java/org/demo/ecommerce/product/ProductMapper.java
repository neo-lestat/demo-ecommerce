package org.demo.ecommerce.product;

import org.demo.ecommerce.model.Category;
import org.demo.ecommerce.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product fromProductDto(ProductDto productDto) {
        Category category = new Category();
        category.setId(productDto.getCategoryId());
        Product product = new Product();
        product.setCategory(category);
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setImageUrl(productDto.getImageUrl());
        return product;
    }
}
