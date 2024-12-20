package org.demo.ecommerce.category;

import org.demo.ecommerce.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category fromCategoryDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        if (categoryDto.getParentId() != null && categoryDto.getParentId() > 0) {
            category.setParentId(categoryDto.getParentId());
        }
        return category;
    }
}
