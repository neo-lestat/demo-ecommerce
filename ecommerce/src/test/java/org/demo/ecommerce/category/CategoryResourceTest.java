package org.demo.ecommerce.category;

import org.demo.ecommerce.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryResource.class)
class CategoryResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CategoryMapper categoryMapper;

    @MockitoBean
    private CategoryService categoryService;


    @Test
    void testGetById() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("food");
        given(categoryService.findById(eq(1L))).willReturn(category);
        mvc.perform(get("/api/categories/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(category.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(category.getName())));
    }

    @Test
    void testGetByIdThrowsNotFound() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("food");
        given(categoryService.findById(eq(1L))).willThrow(new CategoryNotFoundException("Category not found with 1"));
        mvc.perform(get("/api/categories/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Category not found with 1")));
    }


    @Test
    void testGetAll() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("food");
        Page<Category> categoryPage = new PageImpl<>(List.of(category), PageRequest.of(0, 1), 1);
        given(categoryService.getCategories(any(Pageable.class))).willReturn(categoryPage);
        mvc.perform(get("/api/categories/all?page=0&size=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.pageSize", equalTo(1)))
                .andExpect(jsonPath("$.pageNumber", equalTo(0)));

    }

    @Test
    void testCreate() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("food");
        given(categoryMapper.fromCategoryDto(any(CategoryDto.class))).willReturn(category);
        given(categoryService.create(any(Category.class))).willReturn(category);
        mvc.perform(post("/api/categories").content(categoryDtoAsJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(category.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(category.getName())));
    }

    @Test
    void testUpdate() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("food");
        given(categoryMapper.fromCategoryDto(any(CategoryDto.class))).willReturn(category);
        given(categoryService.update(any(Category.class))).willReturn(category);
        mvc.perform(put("/api/categories").content(categoryDtoAsJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(category.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(category.getName())));
    }

    private String categoryDtoAsJson() {
        return """ 
               {
                   "name": "food"
               }
               """;
    }

}
