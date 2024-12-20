package org.demo.ecommerce.category;

import org.demo.ecommerce.model.Category;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    private CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    void testMapCategoryDtoToCategory() {
        CategoryDto categoryDto = buildCategoryDto();
        Category category = categoryMapper.fromCategoryDto(categoryDto);
        assertThat(category)
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrPropertyWithValue("name", "food")
                .hasFieldOrPropertyWithValue("parentId", 1L);
    }

    private CategoryDto buildCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(2L);
        categoryDto.setName("food");
        categoryDto.setParentId(1L);
        return categoryDto;
    }

}
