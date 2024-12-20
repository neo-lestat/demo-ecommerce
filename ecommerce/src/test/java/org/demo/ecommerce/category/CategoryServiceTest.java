package org.demo.ecommerce.category;


import org.demo.ecommerce.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testFindByIdReturnsCategory() {
        Optional<Category> categoryOptional = Optional.of(new Category());
        when(categoryRepository.findById(anyLong())).thenReturn(categoryOptional);
        Category category = categoryService.findById(1L);
        assertEquals(categoryOptional.get(), category);
    }

    @Test
    void testFindByIdThrowsCategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenThrow(new CategoryNotFoundException("Category not found with 1"));
        CategoryNotFoundException thrown = Assertions.assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.findById(1L);
        });
        Assertions.assertEquals("Category not found with 1", thrown.getMessage());
    }

    @Test
    void testGetCategories() {
        Category category = new Category();
        Page<Category> categoryPage = new PageImpl<>(Collections.singletonList(category));
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(categoryPage);
        Pageable paging = PageRequest.of(0, 1);
        Page<Category> result = categoryService.getCategories(paging);
        assertEquals(categoryPage, result);
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        Category categorySaved = categoryService.create(category);
        assertEquals(category, categorySaved);
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setId(1L);
        Optional<Category> categoryOptional = Optional.of(category);
        when(categoryRepository.findById(anyLong())).thenReturn(categoryOptional);
        Category categoryUpdated = categoryService.update(category);
        assertEquals(category, categoryUpdated);
    }
}
